package nl.gijspeters.pubint.graph.traversable;

import nl.gijspeters.pubint.graph.Vertex;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by gijspeters on 17-10-16.
 *
 * This class represents a specific edge on a graph, i.e. a fixed connection between two vertices.
 * This could be a street, a piece of railway track, or a link between two station platforms, as retrieved from the
 * original OSM and GTFS input.
 */
@Entity("edge")
public abstract class Edge implements Traversable {

    @Id
    private int edgeId;
    @Reference
    private Vertex fromVertex;
    @Reference
    private Vertex toVertex;
    private boolean streetEdge;

    public Edge() {

    }

    public Edge(int edgeId, Vertex fromVertex, Vertex toVertex) {
        this(edgeId, fromVertex, toVertex, false);
    }

    public Edge(int edgeId, Vertex fromVertex, Vertex toVertex, boolean streetEdge) {
        this.edgeId = edgeId;
        setFromVertex(fromVertex);
        setToVertex(toVertex);
        setStreetEdge(streetEdge);
    }

    public int getEdgeId() {
        return edgeId;
    }

    public Vertex getFromVertex() {
        return fromVertex;
    }

    public Vertex getToVertex() {
        return toVertex;
    }

    public void setFromVertex(Vertex fromVertex) {
        this.fromVertex = fromVertex;
    }

    public void setToVertex(Vertex toVertex) {
        this.toVertex = toVertex;
    }

    public boolean isStreetEdge() {
        return streetEdge;
    }

    public void setStreetEdge(boolean streetEdge) {
        this.streetEdge = streetEdge;
    }

    @Override
    public int hashCode() {
        return edgeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) o;
        return getEdgeId() == e.getEdgeId();
    }

    public String toString() {
        return "Edge " + String.valueOf(getEdgeId()) + " from <" + getFromVertex().getVertexLabel() + "> to <" + getToVertex().getVertexLabel() + ">";
    }

}
