package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.graph.state.*;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Ride;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.otpentry.OTPRide;
import nl.gijspeters.pubint.structure.Anchor;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.edgetype.PatternHop;
import org.opentripplanner.routing.spt.GraphPath;
import org.opentripplanner.routing.spt.ShortestPathTree;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 17-10-16.
 */
public class GraphFactory {

    private AnchorManipulator manipulator;

    private TraversableFactory tf = new TraversableFactory();

    public GraphFactory() {
        setManipulator(new EmptyManipulator());
    }

    public GraphFactory(AnchorManipulator manipulator) {
        this.setManipulator(manipulator);
    }

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

    private ShortestPathTree makeSPT(Anchor anchor, int maxTimeSeconds, OTPHandler.RouteMode mode) {
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

    public Cone<OriginState> makeOriginCone(Anchor anchor, int maxTimeSeconds) {
        ShortestPathTree spt = makeSPT(anchor, maxTimeSeconds, OTPHandler.RouteMode.FROM_ORIGIN);
        Cone<OriginState> cone = new Cone<>(anchor, maxTimeSeconds, spt.getOptions().walkSpeed);
        if (spt == null) {
            return cone;
        }
        long count = 0;
        Set<org.opentripplanner.routing.graph.Edge> edgeids = new HashSet<>();
        RideBuilder rb = new RideBuilder();
        int nhops = 0;
        int ntstates = 0;
        for (org.opentripplanner.routing.core.State s : spt.getAllStates()) {
            if (s.getBackEdge() != null && s.getBackEdge().getGeometry() != null) {
                if (s.getBackTrip() == null) {
                    edgeids.add(s.getBackEdge());
                    org.opentripplanner.routing.graph.Edge backEdge = s.getBackEdge();
                    org.opentripplanner.routing.graph.Vertex backFromV = backEdge.getFromVertex();
                    org.opentripplanner.routing.graph.Vertex backToV = backEdge.getToVertex();
                    GraphPath fromPath = spt.getPath(backFromV, true);
                    GraphPath toPath = spt.getPath(backToV, true);
                    Edge e = tf.makeEdge(backEdge);
                    Date earliestArrival = new Date(fromPath.getEndTime() * 1000);
                    Date earliestDeparture = new Date(toPath.getEndTime() * 1000);
                    OriginState<Edge> state = new OriginUndirectedState(e, earliestDeparture, earliestArrival);
                    cone.getStates().add(state);
                    count++;
                } else if (s.getBackEdge() != null && s.getBackEdge() instanceof PatternHop) {
                    edgeids.add(s.getBackEdge());
                    rb.add(s);

                    count++;
                }
            }
        }
        for (OTPRide otpsuperride : rb.createRides()) {
            for (OTPRide otpride : otpsuperride.getSubRides()) {
                org.opentripplanner.routing.graph.Vertex boardVertex = otpride.getBoardVertex();
                if (boardVertex != null) {
                    GraphPath boardPath = spt.getPath(boardVertex, true);
                    if (boardPath != null) {
                        Date earliestDeparture = new Date(boardPath.getEndTime() * 1000);
                        if (earliestDeparture.getTime() <= otpride.getDeparture().getTime()) {
                            OriginState<Ride> s = new OriginTransitState(otpride.getRide(), earliestDeparture, otpride.getDeparture(), otpride.getArrival());
                            cone.getStates().add(s);
                            nhops += otpride.size();
                            ntstates++;
                        }
                    }
                }
            }
        }
        System.out.println("Edges handled: " + String.valueOf(edgeids.size()));
        System.out.println("Processed: " + String.valueOf(count));
        System.out.println("States in Cone: " + String.valueOf(cone.getStates().size()));
        System.out.println(String.valueOf(nhops) + " Hops in " + String.valueOf(ntstates) + " TransitStates");
        return cone;
    }

    public Cone<DestinationState> makeDestinationCone(Anchor anchor, int maxTimeSeconds) {
        ShortestPathTree spt = makeSPT(anchor, maxTimeSeconds, OTPHandler.RouteMode.TO_DESTINATION);
        Cone<DestinationState> cone = new Cone<>(anchor, maxTimeSeconds, spt.getOptions().walkSpeed);
        if (spt == null) {
            return cone;
        }
        long count = 0;
        Set<org.opentripplanner.routing.graph.Edge> edgeids = new HashSet<>();
        RideBuilder rb = new RideBuilder();
        int nhops = 0;
        int ntstates = 0;
        for (org.opentripplanner.routing.core.State s : spt.getAllStates()) {
            if (s.getBackEdge() != null && s.getBackEdge().getGeometry() != null) {
                if (s.getBackTrip() == null) {
                    edgeids.add(s.getBackEdge());
                    org.opentripplanner.routing.graph.Edge backEdge = s.getBackEdge();
                    org.opentripplanner.routing.graph.Vertex backFromV = backEdge.getFromVertex();
                    org.opentripplanner.routing.graph.Vertex backToV = backEdge.getToVertex();
                    GraphPath fromPath = spt.getPath(backFromV, true);
                    GraphPath toPath = spt.getPath(backToV, true);
                    Edge e = tf.makeEdge(backEdge);
                    Date latestDeparture = new Date(fromPath.getEndTime() * 1000);
                    Date latestArrival = new Date(toPath.getEndTime() * 1000);
                    DestinationState<Edge> state = new DestinationUndirectedState(e, latestDeparture, latestArrival);
                    cone.getStates().add(state);
                    count++;
                } else if (s.getBackEdge() != null && s.getBackEdge() instanceof PatternHop) {
                    edgeids.add(s.getBackEdge());
                    rb.add(s);

                    count++;
                }
            }
        }
        for (OTPRide otpsuperride : rb.createRides()) {
            for (OTPRide otpride : otpsuperride.getSubRides()) {
                org.opentripplanner.routing.graph.Vertex alightVertex = otpride.getAlightVertex();
                if (alightVertex != null) {
                    GraphPath alightPath = spt.getPath(alightVertex, true);
                    if (alightPath != null) {
                        Date latestArrival = new Date(alightPath.getEndTime() * 1000);
                        if (latestArrival.getTime() >= otpride.getArrival().getTime()) {
                            DestinationState<Ride> s = new DestinationTransitState(otpride.getRide(), otpride.getDeparture(), otpride.getArrival(), latestArrival);
                            cone.getStates().add(s);
                            nhops += otpride.size();
                            ntstates++;
                        }
                    }
                }
            }
        }
        System.out.println("Edges handled: " + String.valueOf(edgeids.size()));
        System.out.println("Processed: " + String.valueOf(count));
        System.out.println("States in Cone: " + String.valueOf(cone.getStates().size()));
        System.out.println(String.valueOf(nhops) + " Hops in " + String.valueOf(ntstates) + " TransitStates");
        return cone;
    }

    /*public Prism getPrism(Leg leg) {
        int deltaTime = (int) leg.getDeltaTime() / 1000;
        Cone<OriginState> originCone = makeOriginCone(leg.getOrigin(), deltaTime);
        Cone<DestinationState> destinationCone = makeDestinationCone(leg.getDestination(), deltaTime);
        Prism prism = new Prism(leg, originCone.getWalkSpeed());
        return prism
    }*/

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
