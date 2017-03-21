package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 20-10-16.
 * <p>
 * UndirectedState calculated fom an origin
 */
@Entity("state")
public class OriginTraversedUndirectedState extends OriginUndirectedState implements OriginTraversedState<Edge> {

    private Date earliestArrival;

    public OriginTraversedUndirectedState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 33)
                .append(super.hashCode())
                .append(getEarliestDeparture().getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof OriginTraversedUndirectedState)) {
            return false;
        }
        OriginTraversedUndirectedState c = (OriginTraversedUndirectedState) o;
        return getTraversable().equals(c.getTraversable())
                && getEarliestArrival().equals(c.getEarliestArrival())
                && getEarliestDeparture().equals(c.getEarliestDeparture());
    }

    public OriginTraversedUndirectedState(Edge edge, Date earliestDeparture, Date earliestArrival) {
        super(edge, earliestDeparture);
        this.earliestArrival = earliestArrival;
    }

    @Override
    public Date getEarliestArrival() {
        return earliestArrival;
    }

    public void setEarliestArrival(Date earliestArrival) {
        this.earliestArrival = earliestArrival;
    }

    @Override
    public long getMinimalTraversalTime() {
        return getEarliestArrival().getTime() - getEarliestDeparture().getTime();
    }

}
