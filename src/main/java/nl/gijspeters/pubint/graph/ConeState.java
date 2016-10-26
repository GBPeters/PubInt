package nl.gijspeters.pubint.graph;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by gijspeters on 20-10-16.
 */
@Entity("state")
public abstract class ConeState implements State {

    @Id
    private ObjectId objectId;
    @Reference
    private Edge edge;


    public ConeState() {

    }

    public ConeState(Edge edge) {
        this.setEdge(edge);
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public abstract int hashCode();

    public abstract boolean equals(Object o);

}
