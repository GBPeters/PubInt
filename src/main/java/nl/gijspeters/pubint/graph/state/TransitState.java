package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.TransitTimes;
import nl.gijspeters.pubint.graph.traversable.Ride;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

/**
 * Created by gijspeters on 20-11-16.
 */
@Entity("state")
public class TransitState extends AbstractState implements State<Ride>, TransitTimes {

    @Reference
    private Ride ride;
    private Date latestDeparture;
    private Date earliestArrival;

    private TransitType type;

    public TransitState() {
        super();
    }

    public TransitState(Ride ride, Date latestDeparture, Date earliestArrival) {
        this();
        this.ride = ride;
        this.latestDeparture = latestDeparture;
        this.earliestArrival = earliestArrival;
    }

    @Override
    public Date getEarliestArrival() {
        return earliestArrival;
    }

    @Override
    public Ride getTraversable() {
        return ride;
    }

    @Override
    public long getMinimalTraversalTime() {
        return earliestArrival.getTime() - latestDeparture.getTime();
    }

    @Override
    public Date getLatestDeparture() {
        return latestDeparture;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public void setLatestDeparture(Date latestDeparture) {
        this.latestDeparture = latestDeparture;
    }

    public void setEarliestArrival(Date earliestArrival) {
        this.earliestArrival = earliestArrival;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(89, 98)
                .append(ride)
                .append(earliestArrival)
                .append(latestDeparture)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(this.getClass())) {
            TransitState s = (TransitState) o;
            return ride.equals(s.getTraversable())
                    && latestDeparture.equals(s.getLatestDeparture())
                    && earliestArrival.equals(s.getEarliestArrival());
        }
        return false;
    }

    public TransitType getType() {
        return type;
    }

    public void setType(TransitType type) {
        this.type = type;
    }
}
