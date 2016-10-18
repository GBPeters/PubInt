package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.otpentry.OTPHandler;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by gijspeters on 17-10-16.
 */
public class GraphBuilder {

    public GraphBuilder() {

    }

    public Vertex buildVertex(org.opentripplanner.routing.graph.Vertex v) {
        return new Vertex(v.getLabel(), new Coordinate(v.getLon(), v.getLat()));
    }

    public Edge buildEdge(org.opentripplanner.routing.graph.Edge e) {
        Edge edge;
        Vertex fromV = buildVertex(e.getFromVertex());
        Vertex toV = buildVertex(e.getToVertex());
        if (e.getGeometry() == null) {
            edge = new LinkEdge(e.getId(), fromV, toV);
        } else {
            edge = new BasicEdge(e.getId(), fromV, toV, e.getGeometry());
        }
        return edge;
    }

    public BasicGraph getGraph(String graphId) throws Exception {
        HashSet<Edge> edges = new HashSet<Edge>();
        HashSet<Vertex> vertices = new HashSet<Vertex>();
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
}
