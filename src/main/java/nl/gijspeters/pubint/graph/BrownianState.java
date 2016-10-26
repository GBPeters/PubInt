package nl.gijspeters.pubint.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 */
@Entity("state")
public class BrownianState extends PrismState {

    private long minimalTraversalTime;

    public BrownianState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 31)
                .append(getEdge())
                .append(getEarliestArrival())
                .append(getLatestDeparture())
                .append(minimalTraversalTime)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof BrownianState)) {
            return false;
        }
        BrownianState b = (BrownianState) o;
        return getEdge().equals(b.getEdge())
                && getEarliestArrival().equals(b.getEarliestArrival())
                && getLatestDeparture().equals(b.getLatestDeparture())
                && getMinimalTraversalTime() == b.getMinimalTraversalTime();
    }

    public BrownianState(Edge edge, Date earliestArrival, Date latestDeparture, long minimalTraversalTime) {
        super(edge, earliestArrival, latestDeparture);
        setMinimalTraversalTime(minimalTraversalTime);
    }

    @Override
    public long getMinimalTraversalTime() {
        return minimalTraversalTime;
    }

    public void setMinimalTraversalTime(long t) {
        this.minimalTraversalTime = t;
    }

    @Override
    public Date getEarliestDeparture() {
        return new Date(getEarliestArrival().getTime() + minimalTraversalTime);
    }

    @Override
    public Date getLatestArrival() {
        return new Date(getLatestDeparture().getTime() - minimalTraversalTime);
    }
}
