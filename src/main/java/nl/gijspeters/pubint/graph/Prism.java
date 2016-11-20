package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.state.PrismState;
import nl.gijspeters.pubint.structure.Leg;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Collection;

/**
 * Created by gijspeters on 18-10-16.
 */
@Entity("prism")
public class Prism extends Cube<PrismState> {

    @Id
    private ObjectId objectId;
    @Reference
    private Leg leg;

    public Prism() {
        super();
    }

    public Prism(Leg leg, double walkSpeed) {
        super(walkSpeed);
        this.setLeg(leg);
    }

    public Prism(Leg leg, double walkSpeed, Collection<PrismState> states) {
        super(states, walkSpeed);
        this.setLeg(leg);
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public Leg getLeg() {
        return leg;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }
}
