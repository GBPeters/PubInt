package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by gijspeters on 18-03-17.
 */
public abstract class DestinationUndirectedState extends UndirectedState implements DestinationState<Edge> {


    private Date latestArrival;

    public DestinationUndirectedState(Edge edge, Date latestArrival) {
        super(edge);
        setLatestArrival(latestArrival);
    }

    public DestinationUndirectedState() {

    }

    @Override
    public Date getLatestArrival() {
        return latestArrival;
    }

    public void setLatestArrival(Date latestArrival) {
        this.latestArrival = latestArrival;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 33)
                .append(getTraversable())
                .append(getLatestArrival().getTime())
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
                && getLatestArrival().equals(c.getLatestArrival());
    }

}
