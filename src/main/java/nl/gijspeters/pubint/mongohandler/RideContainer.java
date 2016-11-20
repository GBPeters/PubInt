package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.traversable.Ride;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by gijspeters on 19-11-16.
 */
@Entity("ride")
public class RideContainer {

    @Id
    private Ride ride;

    public RideContainer() {
    }

    public RideContainer(Ride ride) {
        this.ride = ride;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
