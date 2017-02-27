package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.graph.state.PrismState;
import nl.gijspeters.pubint.structure.Leg;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Set;

/**
 * Created by gijspeters on 23-02-17.
 */
@Entity("prism")
public class PrismContainer {

    @Id
    private ObjectId oid;
    private Set<PrismState> states;
    private Leg leg;
    private double walkSpeed;

    public PrismContainer() {
    }

    public PrismContainer(Prism prism) {
        setPrism(prism);
    }

    public Prism getPrism() {
        return new Prism(leg, walkSpeed, states);
    }

    public void setPrism(Prism prism) {
        states = prism.getStates();
        leg = prism.getLeg();
        walkSpeed = prism.getWalkSpeed();
    }

    public ObjectId getOid() {
        return oid;
    }

}
