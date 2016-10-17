package nl.gijspeters.pubint.graph;

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

    public enum TYPE {STREET, TRANSIT, LINK}

    public Edge() {

    }

    public Edge(int edgeId, Vertex fromVertex, Vertex toVertex) {
        this.edgeId = edgeId;
        setFromVertex(fromVertex);
        setToVertex(toVertex);
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

    public abstract TYPE getEdgeType();


}
