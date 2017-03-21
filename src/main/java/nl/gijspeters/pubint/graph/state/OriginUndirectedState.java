package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-03-17.
 */
@Entity("state")
public abstract class OriginUndirectedState extends UndirectedState implements OriginState<Edge> {


    private Date earliestDeparture;

    public OriginUndirectedState(Edge edge, Date earliestDeparture) {
        super(edge);
        setEarliestDeparture(earliestDeparture);
    }

    public OriginUndirectedState() {
        super();
    }


    @Override
    public Date getEarliestDeparture() {
        return earliestDeparture;
    }

    public void setEarliestDeparture(Date earliestDeparture) {
        this.earliestDeparture = earliestDeparture;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 33)
                .append(getTraversable())
                .append(getEarliestDeparture().getTime())
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
                && getEarliestDeparture().equals(c.getEarliestDeparture());
    }
}
