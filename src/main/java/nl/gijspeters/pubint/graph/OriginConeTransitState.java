package nl.gijspeters.pubint.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 24-10-16.
 */
@Entity("state")
public class OriginConeTransitState extends OriginConeState implements OriginTransitState {

    private Date latestArrival;

    public OriginConeTransitState() {
        super();
    }

    public OriginConeTransitState(Edge edge, Date earliestArrival, Date latestArrival, Date earliestDeparture) {
        super(edge, earliestArrival, earliestDeparture);
        this.latestArrival = latestArrival;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 35)
                .append(super.hashCode())
                .append(latestArrival.getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof OriginConeTransitState)) {
            return false;
        }
        OriginConeTransitState c = (OriginConeTransitState) o;
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
