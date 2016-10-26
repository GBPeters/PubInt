package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.graph.*;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.structure.Anchor;
import org.opentripplanner.routing.edgetype.AreaEdge;
import org.opentripplanner.routing.edgetype.StreetEdge;
import org.opentripplanner.routing.edgetype.StreetTransitLink;
import org.opentripplanner.routing.spt.GraphPath;
import org.opentripplanner.routing.spt.ShortestPathTree;

import java.util.*;

/**
 * Created by gijspeters on 17-10-16.
 */
public class GraphBuilder {

    public static final Set<Class> STREET_EDGE_CLASSES = new HashSet<>(Arrays.asList(new Class[]{StreetEdge.class, StreetTransitLink.class, AreaEdge.class}));

    private AnchorManipulator manipulator;

    public GraphBuilder() {
        setManipulator(new EmptyManipulator());
    }

    public GraphBuilder(AnchorManipulator manipulator) {
        this.setManipulator(manipulator);
    }

    public Vertex buildVertex(org.opentripplanner.routing.graph.Vertex v) {
        return new Vertex(v.getLabel(), v.getCoordinate());
    }

    public Edge buildEdge(org.opentripplanner.routing.graph.Edge e) {
        Edge edge;
        Vertex fromV = buildVertex(e.getFromVertex());
        Vertex toV = buildVertex(e.getToVertex());
        if (e.getGeometry() == null) {
            edge = new LinkEdge(e.getId(), fromV, toV);
        } else {
            boolean streetEdge = STREET_EDGE_CLASSES.contains(e.getClass());
            edge = new BasicEdge(e.getId(), fromV, toV, e.getGeometry(), streetEdge);
        }
        return edge;
    }

    public BasicGraph getGraph(String graphId) throws Exception {
        HashSet<Edge> edges = new HashSet<>();
        HashSet<Vertex> vertices = new HashSet<>();
        org.opentripplanner.routing.graph.Graph otpgraph = OTPHandler.getInstance().getGraph();
        Collection<org.opentripplanner.routing.graph.Edge> otpedges = otpgraph.getEdges();
        for (org.opentripplanner.routing.graph.Edge e : otpedges) {
            edges.add(buildEdge(e));
        }
        Collection<org.opentripplanner.routing.graph.Vertex> otpvertices = otpgraph.getVertices();
        for (org.opentripplanner.routing.graph.Vertex v : otpvertices) {
            vertices.add(buildVertex(v));
        }
        return new BasicGraph(graphId, edges, vertices);
    }

    private ShortestPathTree getSPT(Anchor anchor, long maxTimeMillis, OTPHandler.RouteMode mode) {
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

    public Cone<OriginState> getOriginCone(Anchor anchor, long maxTimeMillis) {
        ShortestPathTree spt = getSPT(anchor, maxTimeMillis, OTPHandler.RouteMode.FROM_ORIGIN);
        Cone<OriginState> cone = new Cone<>(anchor, maxTimeMillis);
        if (spt == null) {
            return cone;
        }
        long count = 0;
        Set<org.opentripplanner.routing.graph.Edge> edgeids = new HashSet<>();
        double walkSpeed = spt.getOptions().walkSpeed;
        for (org.opentripplanner.routing.core.State s : spt.getAllStates()) {
            if (s.getBackEdge() != null && s.getBackEdge().getGeometry() != null && s.getBackTrip() == null) {
                edgeids.add(s.getBackEdge());
                org.opentripplanner.routing.graph.Edge backEdge = s.getBackEdge();
                org.opentripplanner.routing.graph.Vertex backFromV = backEdge.getFromVertex();
                org.opentripplanner.routing.graph.Vertex backToV = backEdge.getToVertex();
                GraphPath fromPath = spt.getPath(backFromV, true);
                GraphPath toPath = spt.getPath(backToV, true);
                Edge e = buildEdge(backEdge);
                Date earliestArrival = new Date(fromPath.getEndTime() * 1000);
                Date earliestDeparture = new Date(toPath.getEndTime() * 1000);
                OriginState state = new OriginConeState(e, earliestArrival, earliestDeparture);
                cone.getStates().add(state);
                count++;

            }
        }
        System.out.println("Edges handled: " + String.valueOf(edgeids.size()));
        System.out.println("Processed: " + String.valueOf(count));
        System.out.println("States in Cone: " + String.valueOf(cone.getStates().size()));
        return cone;
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
