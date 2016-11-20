package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Ride;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 25-10-16.
 */
@Entity("state")
public class DestinationTransitState extends TransitState implements DestinationState<Ride> {

    private Date latestArrival;

    public DestinationTransitState() {
        super();
    }

    public DestinationTransitState(Ride ride, Date latestDeparture, Date earliestArrival, Date latestArrival) {
        super(ride, latestDeparture, earliestArrival);
        this.setLatestArrival(latestArrival);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 35)
                .append(super.hashCode())
                .append(latestArrival.getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof DestinationTransitState)) {
            return false;
        }
        DestinationTransitState c = (DestinationTransitState) o;
        return super.equals(c)
                && getLatestArrival().equals(c.getLatestArrival());
    }

    @Override
    public Date getLatestArrival() {
        return latestArrival;
    }

    public void setLatestArrival(Date latestArrival) {
        this.latestArrival = latestArrival;
    }
}
