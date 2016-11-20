package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.graph.state.OriginState;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Hop;
import nl.gijspeters.pubint.graph.traversable.LinkEdge;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.otpentry.OTPHop;
import nl.gijspeters.pubint.structure.Anchor;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.edgetype.AreaEdge;
import org.opentripplanner.routing.edgetype.PatternHop;
import org.opentripplanner.routing.edgetype.StreetEdge;
import org.opentripplanner.routing.edgetype.StreetTransitLink;
import org.opentripplanner.routing.spt.ShortestPathTree;

import java.util.*;

/**
 * Created by gijspeters on 17-10-16.
 */
public class GraphFactory {

    public static final Set<Class> STREET_EDGE_CLASSES = new HashSet<>(Arrays.asList(new Class[]{StreetEdge.class, StreetTransitLink.class, AreaEdge.class}));

    private AnchorManipulator manipulator;

    public GraphFactory() {
        setManipulator(new EmptyManipulator());
    }

    public GraphFactory(AnchorManipulator manipulator) {
        this.setManipulator(manipulator);
    }

    public Vertex makeVertex(org.opentripplanner.routing.graph.Vertex v) {
        return new Vertex(v.getLabel(), v.getCoordinate());
    }

    public Edge makeEdge(org.opentripplanner.routing.graph.Edge e) {
        Edge edge;
        Vertex fromV = makeVertex(e.getFromVertex());
        Vertex toV = makeVertex(e.getToVertex());
        if (e.getGeometry() == null) {
            edge = new LinkEdge(e.getId(), fromV, toV);
        } else {
            boolean streetEdge = STREET_EDGE_CLASSES.contains(e.getClass());
            edge = new BasicEdge(e.getId(), fromV, toV, e.getGeometry(), streetEdge);
        }
        return edge;
    }

    public Hop makeHop(OTPHop h) {
        return new Hop(h.getTrip(), h.getDeparture(), h.getArrival(), h.getEdge());
    }

    public OTPHop makeOTPHop(State s) {
        assert s.getBackEdge() instanceof PatternHop;
        Date departure = new Date(s.getBackState().getTimeInMillis());
        Date arrival = new Date(s.getTimeInMillis());
        return new OTPHop(s.getBackTrip().getId(), departure, arrival, makeEdge(s.getBackEdge()), (PatternHop) s.getBackEdge());
    }

    public BasicGraph getCompleteGraph(String graphId) throws Exception {
        HashSet<Edge> edges = new HashSet<>();
        HashSet<Vertex> vertices = new HashSet<>();
        org.opentripplanner.routing.graph.Graph otpgraph = OTPHandler.getInstance().getGraph();
        Collection<org.opentripplanner.routing.graph.Edge> otpedges = otpgraph.getEdges();
        for (org.opentripplanner.routing.graph.Edge e : otpedges) {
            edges.add(makeEdge(e));
        }
        Collection<org.opentripplanner.routing.graph.Vertex> otpvertices = otpgraph.getVertices();
        for (org.opentripplanner.routing.graph.Vertex v : otpvertices) {
            vertices.add(makeVertex(v));
        }
        return new BasicGraph(graphId, edges, vertices);
    }

    private ShortestPathTree makeSPT(Anchor anchor, long maxTimeMillis, OTPHandler.RouteMode mode) {
        getManipulator().manipulate(anchor);
        try {
            OTPHandler otp = OTPHandler.getInstance();
            ShortestPathTree spt = otp.getShortestPathTree(anchor.getCoord(), anchor.getDate(), maxTimeMillis, mode);
            return spt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cone<OriginState> makeOriginCone(Anchor anchor, long maxTimeMillis) {
        Cone<OriginState> cone = null;
        //ShortestPathTree spt = makeSPT(anchor, maxTimeMillis, OTPHandler.RouteMode.FROM_ORIGIN);
        //Cone<OriginState> cone = new Cone<>(anchor, maxTimeMillis, spt.getOptions().walkSpeed);
        /*if (spt == null) {
            return cone;
        }
        long count = 0;
        Set<org.opentripplanner.routing.graph.Edge> edgeids = new HashSet<>();
        for (org.opentripplanner.routing.core.State s : spt.getAllStates()) {
            if (s.getBackEdge() != null && s.getBackEdge().getGeometry() != null) {
                if (s.getBackTrip() == null) {
                    edgeids.add(s.getBackEdge());
                    org.opentripplanner.routing.graph.Edge backEdge = s.getBackEdge();
                    org.opentripplanner.routing.graph.Vertex backFromV = backEdge.getFromVertex();
                    org.opentripplanner.routing.graph.Vertex backToV = backEdge.getToVertex();
                    GraphPath fromPath = spt.getPath(backFromV, true);
                    GraphPath toPath = spt.getPath(backToV, true);
                    Edge e = makeEdge(backEdge);
                    Date earliestArrival = new Date(fromPath.getEndTime() * 1000);
                    Date earliestDeparture = new Date(toPath.getEndTime() * 1000);
                    OriginState state = new OriginUndirectedState(e, earliestArrival, earliestDeparture);
                    cone.getStates().add(state);
                    count++;
                } else {
                    edgeids.add(s.getBackEdge());
                    //patterns.add(s.getLastPattern());
                    org.opentripplanner.routing.graph.Edge edge = null;
                    Edge stateEdge = makeEdge(s.getBackEdge());
                    Date latestArrival = new Date(s.getBackState().getTimeInMillis());
                    Date earliestDeparture = new Date(s.getTimeInMillis());
                    for (org.opentripplanner.routing.graph.Edge e : s.getBackEdge().getFromVertex().getIncoming()) {
                        if (e instanceof TransitBoardAlight) {
                            edge = e;
                            break;
                        }
                    }
                    Date earliestArrival;
                    if (edge == null) {
                        edge = s.getBackState().getBackEdge();
                        earliestArrival = new Date(spt.getPath(edge.getFromVertex(), true).getEndTime() * 1000);
                    } else {
                        earliestArrival = new Date(spt.getPath(edge.getFromVertex().getIncoming().iterator().next().getFromVertex(), true).getEndTime() * 1000);
                    }
                    OriginState state = new OriginTransitState(stateEdge, earliestArrival, latestArrival, earliestDeparture);
                    System.out.println(state);
                    assert earliestArrival.getTime() <= latestArrival.getTime();
                    assert latestArrival.getTime() <= earliestDeparture.getTime();
                    cone.getStates().add(state);

                    count++;
                }
            }
        }
        System.out.println("Edges handled: " + String.valueOf(edgeids.size()));
        System.out.println("Processed: " + String.valueOf(count));
        System.out.println("States in Cone: " + String.valueOf(cone.getStates().size()));*/
        return cone;
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
