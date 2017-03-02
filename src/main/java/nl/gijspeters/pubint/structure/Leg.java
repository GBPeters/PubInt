package nl.gijspeters.pubint.structure;

import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.mongohandler.PrismContainer;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by gijspeters on 17-10-16.
 */
@Entity("leg")
public class Leg {

    @Id
    private ObjectId objectId;
    @Reference
    private Anchor origin;
    @Reference
    private Anchor destination;
    private PrismContainer prism;

    public Leg() {
    }

    public Leg(Anchor origin, Anchor destination) {
        this.origin = origin;
        setDestination(destination);
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public Anchor getOrigin() {
        return origin;
    }

    public Anchor getDestination() {
        return destination;
    }

    public void setOrigin(Anchor origin) {
        if (origin.getUser().getAgent() != destination.getUser().getAgent() ||
                !destination.getDate().after(origin.getDate())) {
            throw new IllegalStateException("Non-equal agents");
        }
        this.origin = origin;
    }

    public void setDestination(Anchor destination) {
        if (origin.getUser().getAgent() != destination.getUser().getAgent() ||
                !destination.getDate().after(origin.getDate())) {
            throw new IllegalStateException("Non-equal agents");
        }
        this.destination = destination;
    }

    public long getDeltaTime() {
        return destination.getDate().getTime() - origin.getDate().getTime();
    }

    public Agent getAgent() {
        return origin.getUser().getAgent();
    }

    public String toString() {
        return "Leg <" + origin.toString() + " to " + destination.toString() + ">";
    }

    public Prism getPrism() {
        return prism.getPrism();
    }

    public void setPrism(Prism prism) {
        this.prism = new PrismContainer(prism);
    }
}


