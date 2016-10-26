package nl.gijspeters.pubint.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by gijspeters on 17-10-16.
 */
@Entity("edge")
public abstract class Edge {

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
        return new HashCodeBuilder(5, 79)
                .append(edgeId)
                .toHashCode();
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


}
