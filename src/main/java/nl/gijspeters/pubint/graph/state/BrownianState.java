package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-03-17.
 */
@Entity("state")
public abstract class BrownianState extends UndirectedState implements PrismState<Edge> {

    private Date earliestDeparture;
    private Date latestArrival;

    public BrownianState() {
        super();
    }

    public BrownianState(Edge edge, Date earliestDeparture, Date latestArrival) {
        super(edge);
        setEarliestDeparture(earliestDeparture);
        setLatestArrival(latestArrival);
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
    public int hashCode() {
        return new HashCodeBuilder(13, 31)
                .append(getTraversable())
                .append(getEarliestDeparture())
                .append(getLatestArrival())
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
                && getEarliestDeparture() == b.getEarliestDeparture()
                && getLatestArrival() == b.getLatestArrival();
    }

}
