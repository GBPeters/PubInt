package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 *
 * PrismState derived from matching UnidirectedStates. The Brownian Bridges model for visit probabilities should be
 * used on these States.
 */
@Entity("state")
public class BrownianState extends UndirectedState implements PrismState<Edge> {

    private long minimalTraversalTime;

    private Date earliestDeparture;
    private Date latestArrival;

    public BrownianState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 31)
                .append(getTraversable())
                .append(getEarliestDeparture())
                .append(getLatestArrival())
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
        return getTraversable().equals(b.getTraversable())
                && getEarliestArrival().equals(b.getEarliestArrival())
                && getLatestDeparture().equals(b.getLatestDeparture())
                && getMinimalTraversalTime() == b.getMinimalTraversalTime();
    }

    public BrownianState(Edge edge, Date earliestDeparture, Date latestArrival, long minimalTraversalTime) {
        super(edge);
        setMinimalTraversalTime(minimalTraversalTime);
        setEarliestDeparture(earliestDeparture);
        setLatestArrival(latestArrival);
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
        return earliestDeparture;
    }

    public void setEarliestDeparture(Date earliestDeparture) {
        this.earliestDeparture = earliestDeparture;
    }

    @Override
    public Date getLatestArrival() {
        return latestArrival;
    }

    public void setLatestArrival(Date latestArrival) {
        this.latestArrival = latestArrival;
    }

    @Override
    public Date getEarliestArrival() {
        return new Date(earliestDeparture.getTime() + minimalTraversalTime);
    }

    @Override
    public Date getLatestDeparture() {
        return new Date(latestArrival.getTime() - minimalTraversalTime);
    }
}
