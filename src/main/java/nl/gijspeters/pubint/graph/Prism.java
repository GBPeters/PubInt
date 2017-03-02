package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.state.PrismState;

import java.util.Collection;

/**
 * Created by gijspeters on 18-10-16.
 *
 * A class representing a network-time prism between an origin and a destination, represented by a Leg.
 * All vertices in this prism can be reached from the origin, and from there the destination can be reached in time.
 * States are derived form optimal paths from the origin to the destination through the Vertices in a Prism.
 *
 * A Prism can be seen as the spatio-temporal intersection between two Cones
 */
public class Prism extends Cube<PrismState> {

    public Prism() {
        super();
    }

    public Prism(double walkSpeed) {
        super(walkSpeed);
    }

    public Prism(double walkSpeed, Collection<PrismState> states) {
        super(states, walkSpeed);
    }

}
