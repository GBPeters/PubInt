package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by gijspeters on 20-11-16.
 */
@Entity("state")
public abstract class UndirectedState extends AbstractState implements State<Edge> {

    @Reference
    private Edge edge;

    public UndirectedState() {
    }

    public UndirectedState(Edge edge) {
        this.edge = edge;
    }

    public Edge getTraversable() {
        return edge;
    }

}
