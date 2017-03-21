package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 20-10-16.
 * <p>
 * Undirected State calculated to a destination
 */
@Entity("state")
public class DestinationTraversedUndirectedState extends DestinationUndirectedState implements DestinationTraversedState<Edge> {

    private Date latestDeparture;

    public DestinationTraversedUndirectedState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 33)
                .append(getTraversable())
                .append(latestDeparture.getTime())
                .append(getLatestArrival().getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof DestinationTraversedUndirectedState)) {
            return false;
        }
        DestinationTraversedUndirectedState c = (DestinationTraversedUndirectedState) o;
        return getTraversable().equals(c.getTraversable())
                && getLatestArrival().equals(c.getLatestArrival())
                && getLatestDeparture().equals(c.getLatestDeparture());
    }

    public DestinationTraversedUndirectedState(Edge edge, Date latestDeparture, Date latestArrival) {
        super(edge, latestArrival);
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
    public long getMinimalTraversalTime() {
        return getLatestArrival().getTime() - getLatestDeparture().getTime();
    }

}
