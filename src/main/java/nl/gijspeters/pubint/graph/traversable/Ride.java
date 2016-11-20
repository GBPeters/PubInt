package nl.gijspeters.pubint.graph.traversable;

import java.util.Collection;

/**
 * Created by gijspeters on 02-11-16.
 */
public class Ride extends GenericRide<Hop> {

    public Ride() {
        super();
    }

    public Ride(Hop h) {
        super(h);
    }

    public Ride(Collection<? extends Hop> hops) {
        super(hops);
    }

}
