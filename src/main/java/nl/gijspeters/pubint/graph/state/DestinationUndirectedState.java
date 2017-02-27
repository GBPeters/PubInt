package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 20-10-16.
 */
@Entity("state")
public class DestinationUndirectedState extends UndirectedState implements DestinationState<Edge> {

    private Date latestDeparture;
    private Date latestArrival;

    public DestinationUndirectedState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 33)
                .append(getTraversable())
                .append(latestDeparture.getTime())
                .append(latestArrival.getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof DestinationUndirectedState)) {
            return false;
        }
        DestinationUndirectedState c = (DestinationUndirectedState) o;
        return getTraversable().equals(c.getTraversable())
                && getLatestArrival().equals(c.getLatestArrival())
                && getLatestDeparture().equals(c.getLatestDeparture());
    }

    public DestinationUndirectedState(Edge edge, Date latestArrival, Date latestDeparture) {
        super(edge);
        this.setLatestDeparture(latestDeparture);
        this.setLatestArrival(latestArrival);
    }

    @Override
    public Date getLatestDeparture() {
        return latestDeparture;
    }

    public void setLatestDeparture(Date latestDeparture) {
        this.latestDeparture = latestDeparture;
    }

    @Override
    public Date getLatestArrival() {
        return latestArrival;
    }

    public void setLatestArrival(Date latestArrival) {
        this.latestArrival = latestArrival;
    }

    @Override
    public long getMinimalTraversalTime() {
        return getLatestArrival().getTime() - getLatestDeparture().getTime();
    }

    @Override
    public boolean matches(OriginState originState) {
        if (originState instanceof OriginUndirectedState) {
            return getTraversable().equals(originState.getTraversable())
                    && originState.getEarliestDeparture().getTime() <= getLatestDeparture().getTime()
                    && originState.getEarliestArrival().getTime() <= getLatestArrival().getTime();
        }
        return false;
    }
}
