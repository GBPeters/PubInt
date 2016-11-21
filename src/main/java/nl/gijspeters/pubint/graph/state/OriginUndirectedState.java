package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 20-10-16.
 */
@Entity("state")
public class OriginUndirectedState extends UndirectedState implements OriginState<Edge> {

    private Date earliestArrival;
    private Date earliestDeparture;

    public OriginUndirectedState() {

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 33)
                .append(getTraversable())
                .append(earliestArrival.getTime())
                .append(earliestDeparture.getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof OriginUndirectedState)) {
            return false;
        }
        OriginUndirectedState c = (OriginUndirectedState) o;
        return getTraversable().equals(c.getTraversable())
                && getEarliestArrival().equals(c.getEarliestArrival())
                && getEarliestDeparture().equals(c.getEarliestDeparture());
    }

    public OriginUndirectedState(Edge edge, Date earliestArrival, Date earliestDeparture) {
        super(edge);
        this.earliestArrival = earliestArrival;
        this.earliestDeparture = earliestDeparture;
    }

    @Override
    public Date getEarliestArrival() {
        return earliestArrival;
    }

    public void setEarliestArrival(Date earliestArrival) {
        this.earliestArrival = earliestArrival;
    }

    @Override
    public Date getEarliestDeparture() {
        return earliestDeparture;
    }

    public void setEarliestDeparture(Date earliestDeparture) {
        this.earliestDeparture = earliestDeparture;
    }

    @Override
    public long getMinimalTraversalTime() {
        return getEarliestArrival().getTime() - getEarliestDeparture().getTime();
    }

    @Override
    public boolean matches(DestinationState<Edge> destinationState) {
        if (destinationState instanceof OriginUndirectedState) {
            return getTraversable().equals(destinationState.getTraversable())
                    && earliestDeparture.getTime() <= destinationState.getLatestDeparture().getTime()
                    && earliestArrival.getTime() <= destinationState.getLatestArrival().getTime();
        }
        return false;
    }
}
