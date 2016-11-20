package nl.gijspeters.pubint.graph.state;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by gijspeters on 20-10-16.
 */
@Entity("state")
public abstract class AbstractState {

    @Id
    private ObjectId objectId;

    public AbstractState() {

    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public abstract int hashCode();

    public abstract boolean equals(Object o);

}
