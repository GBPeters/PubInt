package nl.gijspeters.pubint.graph.traversable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 02-11-16.
 *
 * Specific implementation of a Ride as an ordered set of Hop
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

    /**
     * Get all Rides possible on the trajectory of Hops contained in this Ride
     *
     * @return A Set of Rides
     */
    public Set<Ride> getSubRides() {
        Set<Ride> subRides = new HashSet<>();
        for (Hop th : this) {
            Ride hr = new Ride(this.headSet(th, true));
            for (Hop fh : hr) {
                Ride sr = new Ride(hr.tailSet(fh, true));
                subRides.add(sr);
            }
        }
        return subRides;
    }
}
