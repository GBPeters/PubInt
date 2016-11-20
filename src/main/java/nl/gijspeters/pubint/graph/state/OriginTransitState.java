package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Ride;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 24-10-16.
 */
@Entity("state")
public class OriginTransitState extends TransitState implements OriginState<Ride> {

    private Date earliestDeparture;

    public OriginTransitState() {
        super();
    }

    public OriginTransitState(Ride ride, Date earliestDeparture, Date latestDeparture, Date earliestArrival) {
        super(ride, latestDeparture, earliestArrival);
        this.earliestDeparture = earliestDeparture;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 35)
                .append(super.hashCode())
                .append(earliestDeparture.getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof OriginTransitState)) {
            return false;
        }
        OriginTransitState c = (OriginTransitState) o;
        return super.equals(c)
                && getEarliestDeparture().equals(c.getEarliestDeparture());
    }

    @Override
    public Date getEarliestDeparture() {
        return earliestDeparture;
    }

    public void setEarliestDeparture(Date earliestDeparture) {
        this.earliestDeparture = earliestDeparture;
    }

    public String toString() {
        return "State on " + getTraversable().toString() + " (" + getEarliestArrival().toString() + " - " + getEarliestDeparture().toString() + " : " + getEarliestDeparture().toString() + ")";
    }
}
