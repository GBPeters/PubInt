package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.graph.state.PrismState;
import org.mongodb.morphia.annotations.Embedded;

import java.util.Set;

/**
 * Created by gijspeters on 23-02-17.
 */
@Embedded
public class PrismContainer {

    private Set<PrismState> states;
    private double walkSpeed;

    public PrismContainer() {
    }

    public PrismContainer(Prism prism) {
        setPrism(prism);
    }

    public Prism getPrism() {
        return new Prism(walkSpeed, states);
    }

    public void setPrism(Prism prism) {
        states = prism.getStates();
        walkSpeed = prism.getWalkSpeed();
    }

}
