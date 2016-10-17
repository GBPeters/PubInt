package nl.gijspeters.pubint.graph;

import org.mongodb.morphia.annotations.Entity;

/**
 * Created by gijspeters on 17-10-16.
 */
@Entity("edge")
public class LinkEdge extends Edge {

    public LinkEdge() {
        super();
    }

    public LinkEdge(int edgeId, Vertex fromVertex, Vertex toVertex) {
        super(edgeId, fromVertex, toVertex);
    }

    public TYPE getEdgeType() {
        return TYPE.LINK;
    }
}
