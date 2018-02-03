package nl.gijspeters.pubint.graph.factory;

import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.graph.state.*;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Ride;
import nl.gijspeters.pubint.otpentry.OTPEntry;
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
    private final OTPEntry otp;

    private AnchorManipulator manipulator;

    private TraversableFactory tf = new TraversableFactory();

    public GraphFactory() {
        this(OTPHandler.getInstance(), new EmptyManipulator());
    }

    public GraphFactory(AnchorManipulator manipulator) {
        this(OTPHandler.getInstance(), manipulator);
    }

    public GraphFactory(OTPEntry otp) {
        this(otp, new EmptyManipulator());
    }

    /**
     * Default constructor
     *
     * @param otp         OTPEntry to use for this GraphFactory
     * @param manipulator AnchorManipulator to premanipulate Anchors
     */
    public GraphFactory(OTPEntry otp, AnchorManipulator manipulator) {
        this.otp = otp;
        setManipulator(manipulator);
    }

    /**
     * Converts an OTP graph to a graph.BasicGraph object
     * @param graphId A String identifier to use for the resulting Graph
     * @return A BasicGraph containing all OTP edges and vertices
     * @throws Exception
     */
    public synchronized BasicGraph getCompleteGraph(String graphId) throws Exception {
        HashSet<Edge> edges = new HashSet<>();
        HashSet<Vertex> vertices = new HashSet<>();
        org.opentripplanner.routing.graph.Graph otpgraph = otp.getGraph();
        Collection<org.opentripplanner.routing.graph.Edge> otpedges = otpgraph.getEdges();
        int ei = 0;
        int vi = 0;
        for (org.opentripplanner.routing.graph.Edge e : otpedges) {
            Edge ed = tf.makeEdge(e);
            edges.add(ed);
            ei++;
        }
        Collection<org.opentripplanner.routing.graph.Vertex> otpvertices = otpgraph.getVertices();
        for (org.opentripplanner.routing.graph.Vertex v : otpvertices) {
            vertices.add(tf.makeVertex(v));
            vi++;
        }
        logger.info(String.format("Graph created |V|=%d, |E|=%d", vertices.size(), edges.size()));
        return new BasicGraph(graphId, edges, vertices);
    }

    /**
     * Auxilliary method for retrieving a ShortestPathTree from an OTP instance.
     * @param anchor The Anchor to use as origin or destination
     * @param maxTimeSeconds The maximum time available for this ShortestPathTree
     * @param mode The RouteMode, either FROM_ORIGIN or TO_DESTINATION
     * @return An OTP ShortestPathTree
     */
    public synchronized ShortestPathTree makeSPT(Anchor anchor, int maxTimeSeconds, OTPEntry.RouteMode mode) {
        getManipulator().manipulate(anchor);
        try {
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
        ShortestPathTree spt = makeSPT(anchor, maxTimeSeconds, OTPEntry.RouteMode.FROM_ORIGIN);
        return makeOriginCone(anchor, spt, maxTimeSeconds);
    }

    public Cone<OriginState> makeOriginCone(Anchor anchor, ShortestPathTree spt, int maxTimeSeconds) {
        // Create empty Cone
        Cone<OriginState> cone = new Cone<>(maxTimeSeconds, spt.getOptions().walkSpeed);
        Set<Edge> edges = new HashSet<>();
        Map<org.opentripplanner.routing.graph.Vertex, OriginState> vertices = new HashMap<>();
        // Create RideBuilder
        RideBuilder rb = new RideBuilder();
        // Iterate OTP states
        for (org.opentripplanner.routing.core.State s : spt.getAllStates()) {
            if (s.getBackEdge() != null && s.getBackEdge().getGeometry() != null) {
                if (s.getBackTrip() == null) {
                    // If the state is on a StreetEdge, create an OriginUndirected State
                    org.opentripplanner.routing.graph.Edge backEdge = s.getBackEdge();
                    org.opentripplanner.routing.graph.Vertex backFromV = backEdge.getFromVertex();
                    GraphPath fromPath = spt.getPath(backFromV, false);
                    long minTraversalTime = s.getAbsTimeDeltaSeconds() * 1000;
                    Date earliestDeparture = new Date(fromPath.getEndTime() * 1000);
                    Date earliestArrival = new Date(earliestDeparture.getTime() + minTraversalTime);
                    Edge e = tf.makeEdge(backEdge);
                    edges.add(e);
                    OriginState<Edge> state = new OriginTraversedUndirectedState(e, earliestDeparture, earliestArrival);
                    vertices.put(s.getVertex(), state);
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
                    GraphPath boardPath = spt.getPath(boardVertex, false);
                    if (boardPath != null) {

                        // Add OriginTransitState to the Cone
                        Date earliestDeparture = new Date(boardPath.getEndTime() * 1000);
                        if (earliestDeparture.getTime() <= otpride.getDeparture().getTime()) {
                            OriginTraversedState<Ride> s = new OriginTransitState(otpride.getRide(),
                                    earliestDeparture, otpride.getDeparture(), otpride.getArrival());
                            cone.getStates().add(s);
                        }
                    }
                }
            }
        }
        for (org.opentripplanner.routing.graph.Vertex v : vertices.keySet()) {
            for (org.opentripplanner.routing.graph.Edge e : v.getOutgoingStreetEdges()) {
                Edge edge = tf.makeEdge(e);
                if (!edges.contains(edge)) {

                    long minTraversalTime = (long) (e.getDistance() / cone.getWalkSpeed());
                    Date earliestDeparture = vertices.get(v).getEarliestDeparture();
                    Date earliestArrival = new Date(earliestDeparture.getTime() + minTraversalTime * 1000);
                    OriginState<Edge> s;
                    if (earliestArrival.getTime() <= anchor.getDate().getTime() + cone.getDuration() * 1000) {
                        s = new OriginTraversedUndirectedState(edge, earliestDeparture, earliestArrival);
                    } else {
                        s = new OriginHalfwayState(edge, earliestDeparture);
                    }
                    cone.getStates().add(s);
                }
            }
        }
        return cone;
    }

    public Cone<DestinationState> makeDestinationCone(Anchor anchor, ShortestPathTree spt, int maxTimeSeconds) {

        // Create empty Cone
        Cone<DestinationState> cone = new Cone<>(maxTimeSeconds, spt.getOptions().walkSpeed);

        Set<Edge> edges = new HashSet<>();
        Map<org.opentripplanner.routing.graph.Vertex, DestinationState> vertices = new HashMap<>();
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
                    DestinationTraversedState<Edge> state = new DestinationTraversedUndirectedState(e, latestDeparture, latestArrival);
                    cone.getStates().add(state);
                    edges.add(e);
                    vertices.put(s.getVertex(), state);
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
                            DestinationTraversedState<Ride> s = new DestinationTransitState(otpride.getRide(), otpride.getDeparture(), otpride.getArrival(), latestArrival);
                            cone.getStates().add(s);
                        }
                    }
                }
            }
        }
        for (org.opentripplanner.routing.graph.Vertex v : vertices.keySet()) {
            for (org.opentripplanner.routing.graph.Edge e : v.getOutgoingStreetEdges()) {
                Edge edge = tf.makeEdge(e);
                if (!edges.contains(edge)) {
                    long minTraversalTime = (long) (e.getDistance() / cone.getWalkSpeed());
                    Date latestArrival = vertices.get(v).getLatestArrival();
                    Date latestDeparture = new Date(latestArrival.getTime() - minTraversalTime * 1000);
                    DestinationState<Edge> s;
                    if (latestDeparture.getTime() >= anchor.getDate().getTime() - cone.getDuration() * 1000) {
                        s = new DestinationTraversedUndirectedState(edge, latestDeparture, latestArrival);
                    } else {
                        s = new DestinationHalfwayState(edge, latestArrival);
                    }
                    cone.getStates().add(s);
                }
            }
        }
        return cone;
    }

    public Cone<DestinationState> makeDestinationCone(Anchor anchor, int maxTimeSeconds) {
        // Get ShortestPath Tree
        ShortestPathTree spt = makeSPT(anchor, maxTimeSeconds, OTPEntry.RouteMode.TO_DESTINATION);
        return makeDestinationCone(anchor, spt, maxTimeSeconds);
    }

    /**
     * Create a Prism from a leg. This first creates two Cones, and then matches all States
     * @param originCone The OriginState Cone to use
     * @param destinationCone The DestinationState Cone to use
     * @return A Prism
     */
    public Prism getPrism(Cone<OriginState> originCone, Cone<DestinationState> destinationCone) {
        // Counters
        int markovstates = 0;
        int brownianstates = 0;
        int uturnstates = 0;

        // Create a StateMatcher
        StateMatcher sm = new StateMatcher();

        // Create empty Prism and a StateFactory
        Prism prism = new Prism(originCone.getWalkSpeed());

        // Populate StateMatcher
        for (DestinationState ds : destinationCone.getStates()) {
            sm.add(ds);
        }
        // Iterate all OriginStates
        for (OriginState os : originCone.getStates()) {

            // Match States
            PrismState ps = sm.matchStates(os);
            if (ps != null) {
                prism.getStates().add(ps);
                if (ps instanceof MarkovState) {
                    markovstates++;
                } else if (ps instanceof BrownianTraversedState) {
                    brownianstates++;
                } else if (ps instanceof BrownianUturnState) {
                    uturnstates++;
                }
            }

        }
        int total = brownianstates + markovstates + uturnstates;
        String s = String.format("States in Prism: %d ( %d B; %s U; %d M )", total, brownianstates, uturnstates, markovstates);
        logger.info(s);
        return prism;
    }

    public Prism getPrism(Leg leg) {
        // Create Cones
        int deltaTime = (int) leg.getDeltaTime() / 1000;
        Cone<OriginState> originCone = makeOriginCone(leg.getOrigin(), deltaTime);
        Cone<DestinationState> destinationCone = makeDestinationCone(leg.getDestination(), deltaTime);
        return getPrism(originCone, destinationCone);
    }

    public Set<org.opentripplanner.routing.core.State> makeTestTripStates(Anchor anchor) {
        ShortestPathTree spt = makeSPT(anchor, 7200, OTPEntry.RouteMode.FROM_ORIGIN);
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
