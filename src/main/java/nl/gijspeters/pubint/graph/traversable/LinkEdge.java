package nl.gijspeters.pubint.graph.traversable;

import nl.gijspeters.pubint.graph.Vertex;
import org.mongodb.morphia.annotations.Entity;

/**
 * Created by gijspeters on 17-10-16.
 *
 * An Edge without a geometry
 */
@Entity("edge")
public class LinkEdge extends Edge {

    public LinkEdge() {
        super();
    }

    public LinkEdge(int edgeId, Vertex fromVertex, Vertex toVertex) {
        super(edgeId, fromVertex, toVertex);
    }

    public boolean equals(Object o) {
        return o != null && o instanceof LinkEdge && super.equals(o);
    }

    @Override
    public Edge reverse() {
        return new LinkEdge(-getEdgeId(), getToVertex(), getFromVertex());
    }

}
