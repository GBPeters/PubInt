package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.state.PrismState;
import nl.gijspeters.pubint.structure.Leg;

import java.util.Collection;

/**
 * Created by gijspeters on 18-10-16.
 */
public class Prism extends Cube<PrismState> {

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

    public Leg getLeg() {
        return leg;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }
}
