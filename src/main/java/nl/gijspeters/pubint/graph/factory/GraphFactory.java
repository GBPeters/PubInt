package nl.gijspeters.pubint.graph.factory;

import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.graph.state.*;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Ride;
import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.otpentry.OTPRide;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.structure.Leg;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.edgetype.PatternHop;
import org.opentripplanner.routing.spt.GraphPath;
import org.opentripplanner.routing.spt.ShortestPathTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by gijspeters on 17-10-16.
 *
 * The GraphFactory contains functionality for building Graphs, Cubes, Cones and Prisms from other sources
 */
public class GraphFactory {

    private static Logger logger = LoggerFactory.getLogger(GraphFactory.class);

    private AnchorManipulator manipulator;

    private TraversableFactory tf = new TraversableFactory();

    /**
     * Default Constructor
     */
    public GraphFactory() {
        setManipulator(new EmptyManipulator());
    }

    /**
     * Constructor setting this GraphFactory's AnchorManipulator.
     *
     * @param manipulator An AnchorManipulator. Each Anchor used in calculations will be manipulated first through
     *                    this AnchorManipulator
     */
    public GraphFactory(AnchorManipulator manipulator) {
        this.setManipulator(manipulator);
    }

    /**
     * Converts an OTP graph to a graph.BasicGraph object
     * @param graphId A String identifier to use for the resulting Graph
     * @return A BasicGraph containing all OTP edges and vertices
     * @throws Exception
     */
    public BasicGraph getCompleteGraph(String graphId) throws Exception {
        HashSet<Edge> edges = new HashSet<>();
        HashSet<Vertex> vertices = new HashSet<>();
        org.opentripplanner.routing.graph.Graph otpgraph = OTPHandler.getInstance().getGraph();
        Collection<org.opentripplanner.routing.graph.Edge> otpedges = otpgraph.getEdges();
        for (org.opentripplanner.routing.graph.Edge e : otpedges) {
            edges.add(tf.makeEdge(e));
        }
        Collection<org.opentripplanner.routing.graph.Vertex> otpvertices = otpgraph.getVertices();
        for (org.opentripplanner.routing.graph.Vertex v : otpvertices) {
            vertices.add(tf.makeVertex(v));
        }
        return new BasicGraph(graphId, edges, vertices);
    }

    /**
     * Auxilliary method for retrieving a ShortestPathTree from an OTP instance.
     * @param anchor The Anchor to use as origin or destination
     * @param maxTimeSeconds The maximum time available for this ShortestPathTree
     * @param mode The RouteMode, either FROM_ORIGIN or TO_DESTINATION
     * @return An OTP ShortestPathTree
     */
    public synchronized ShortestPathTree makeSPT(Anchor anchor, int maxTimeSeconds, OTPHandler.RouteMode mode) {
        getManipulator().manipulate(anchor);
        try {
            OTPHandler otp = OTPHandler.getInstance();
            ShortestPathTree spt = otp.getShortestPathTree(anchor.getCoord(), anchor.getDate(), maxTimeSeconds, mode);
            return spt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create an Cone calculated from a specified origin and duration
     * @param anchor The Anchor to use as origin
     * @param maxTimeSeconds The maximum duration for this Cone
     * @return A Cone containing OriginStates
     */
    public Cone<OriginState> makeOriginCone(Anchor anchor, int maxTimeSeconds) {
        // Get ShortestPath Tree
        ShortestPathTree spt = makeSPT(anchor, maxTimeSeconds, OTPHandler.RouteMode.FROM_ORIGIN);
        return makeOriginCone(anchor, spt, maxTimeSeconds);
    }

    public Cone<OriginState> makeOriginCone(Anchor anchor, ShortestPathTree spt, int maxTimeSeconds) {
        // Create empty Cone
        Cone<OriginState> cone = new Cone<>(anchor, maxTimeSeconds, spt.getOptions().walkSpeed);

        // Create RideBuilder
        RideBuilder rb = new RideBuilder();

        // Iterate OTP states
        for (org.opentripplanner.routing.core.State s : spt.getAllStates()) {
            if (s.getBackEdge() != null && s.getBackEdge().getGeometry() != null) {
                if (s.getBackTrip() == null) {
                    // If the state is on a StreetEdge, create an OriginUndirected State
                    org.opentripplanner.routing.graph.Edge backEdge = s.getBackEdge();
                    org.opentripplanner.routing.graph.Vertex backFromV = backEdge.getFromVertex();
                    GraphPath fromPath = spt.getPath(backFromV, true);
                    long minTraversalTime = s.getAbsTimeDeltaSeconds() * 1000;
                    Date earliestDeparture = new Date(fromPath.getEndTime() * 1000);
                    Date earliestArrival = new Date(earliestDeparture.getTime() + minTraversalTime);
                    Edge e = tf.makeEdge(backEdge);
                    OriginState<Edge> state = new OriginUndirectedState(e, earliestDeparture, earliestArrival);
                    cone.getStates().add(state);
                } else if (s.getBackEdge() != null && s.getBackEdge() instanceof PatternHop) {
                    // If the state is on a transit edge, add it to the RideBuilder
                    rb.add(s);
                }
            }
        }

        // Iterate OTP Rides
        for (OTPRide otpsuperride : rb.createRides()) {

            // For each Ride, get all subrides
            for (OTPRide otpride : otpsuperride.getSubRides()) {
                org.opentripplanner.routing.graph.Vertex boardVertex = otpride.getBoardVertex();

                //Check if the boardVertex is reachable from other sources
                if (boardVertex != null) {
                    GraphPath boardPath = spt.getPath(boardVertex, true);
                    if (boardPath != null) {

                        // Add OriginTransitState to the Cone
                        Date earliestDeparture = new Date(boardPath.getEndTime() * 1000);
                        if (earliestDeparture.getTime() <= otpride.getDeparture().getTime()) {
                            OriginState<Ride> s = new OriginTransitState(otpride.getRide(), earliestDeparture, otpride.getDeparture(), otpride.getArrival());
                            cone.getStates().add(s);
                        }
                    }
                }
            }
        }
        return cone;
    }

    public Cone<DestinationState> makeDestinationCone(Anchor anchor, ShortestPathTree spt, int maxTimeSeconds) {
        // Create empty Cone
        Cone<DestinationState> cone = new Cone<>(anchor, maxTimeSeconds, spt.getOptions().walkSpeed);

        // Create RideBuilder
        RideBuilder rb = new RideBuilder(new TraversableFactory(TraversableFactory.MODE.DESTINATION));

        // Iterate OTP states
        for (org.opentripplanner.routing.core.State s : spt.getAllStates()) {
            if (s.getBackEdge() != null && s.getBackEdge().getGeometry() != null) {
                if (s.getBackTrip() == null) {
                    // If the state is on a StreetEdge, create an DestinationUndirected State
                    org.opentripplanner.routing.graph.Edge backEdge = s.getBackEdge();
                    org.opentripplanner.routing.graph.Vertex backToV = backEdge.getToVertex();
                    GraphPath toPath = spt.getPath(backToV, true);
                    Edge e = tf.makeEdge(backEdge);
                    long minTraversalTime = s.getAbsTimeDeltaSeconds() * 1000;
                    Date latestArrival = new Date(toPath.getStartTime() * 1000);
                    Date latestDeparture = new Date(latestArrival.getTime() - minTraversalTime);
                    DestinationState<Edge> state = new DestinationUndirectedState(e, latestDeparture, latestArrival);
                    cone.getStates().add(state);
                } else if (s.getBackEdge() != null && s.getBackEdge() instanceof PatternHop) {
                    // If the state is on a transit edge, add it to the RideBuilder
                    rb.add(s);
                }
            }
        }

        // Iterate OTP Rides
        for (OTPRide otpsuperride : rb.createRides()) {

            // For each Ride, get all subrides
            for (OTPRide otpride : otpsuperride.getSubRides()) {
                org.opentripplanner.routing.graph.Vertex alightVertex = otpride.getAlightVertex();

                //Check if the alightVertex is reachable from other sources
                if (alightVertex != null) {
                    GraphPath alightPath = spt.getPath(alightVertex, true);
                    if (alightPath != null) {

                        // Add OriginTransitState to the Cone
                        Date latestArrival = new Date(alightPath.getEndTime() * 1000);
                        if (latestArrival.getTime() >= otpride.getArrival().getTime()) {
                            DestinationState<Ride> s = new DestinationTransitState(otpride.getRide(), otpride.getDeparture(), otpride.getArrival(), latestArrival);
                            cone.getStates().add(s);
                        }
                    }
                }
            }
        }
        return cone;
    }

    public Cone<DestinationState> makeDestinationCone(Anchor anchor, int maxTimeSeconds) {
        // Get ShortestPath Tree
        ShortestPathTree spt = makeSPT(anchor, maxTimeSeconds, OTPHandler.RouteMode.TO_DESTINATION);
        return makeDestinationCone(anchor, spt, maxTimeSeconds);
    }

    /**
     * Create a Prism from a leg. This first creates two Cones, and then matches all States
     * @param leg The leg to create the Prism for
     * @return A Prism
     */
    public Prism getPrism(Leg leg, Cone<OriginState> originCone, Cone<DestinationState> destinationCone) {
        // Counters
        int markovstates = 0;
        int brownianstates = 0;

        // Create a HashMap with DestinationStates hashed on the Traversable
        Map<Traversable, DestinationState> destinationMap = new HashMap<>();

        // Create empty Prism and a StateFactory
        Prism prism = new Prism(leg, originCone.getWalkSpeed());
        StateFactory sf = new StateFactory();

        // Populate HashMap
        for (DestinationState ds : destinationCone.getStates()) {
            destinationMap.put(ds.getTraversable(), ds);
        }

        // Iterate all OriginStates
        for (OriginState os : originCone.getStates()) {

            // Find DestinationState with same Traversable in HashMap
            Traversable t = os.getTraversable();
            DestinationState ds = destinationMap.get(t);
            if (ds != null) {

                // If the two states match, create a new PrismState and add it to the Prism
                if (os.matches(ds)) {
                    PrismState s = sf.matchStates(os, ds);
                    prism.getStates().add(s);
                    if (s instanceof BrownianState) {
                        brownianstates++;
                    } else {
                        markovstates++;
                    }
                }
            }
        }
        int total = brownianstates + markovstates;
        String s = String.format("States in Prism: %d ( %d B; %d M )", total, brownianstates, markovstates);
        logger.info(s);
        return prism;
    }

    public Prism getPrism(Leg leg) {
        // Create Cones
        int deltaTime = (int) leg.getDeltaTime() / 1000;
        Cone<OriginState> originCone = makeOriginCone(leg.getOrigin(), deltaTime);
        Cone<DestinationState> destinationCone = makeDestinationCone(leg.getDestination(), deltaTime);
        return getPrism(leg, originCone, destinationCone);
    }

    public Set<org.opentripplanner.routing.core.State> makeTestTripStates(Anchor anchor) {
        ShortestPathTree spt = makeSPT(anchor, 7200, OTPHandler.RouteMode.FROM_ORIGIN);
        Set<State> states = new HashSet<>();
        for (State s : spt.getAllStates()) {
            try {
                if (s.getBackEdge() instanceof PatternHop) {
                    states.add(s);
                }
            } catch (NullPointerException e) {
            }
        }
        return states;
    }

    public void printStateTrace(ShortestPathTree spt, org.opentripplanner.routing.graph.Vertex v) {

    }

    public AnchorManipulator getManipulator() {
        return manipulator;
    }

    public void setManipulator(AnchorManipulator manipulator) {
        this.manipulator = manipulator;
    }

}
