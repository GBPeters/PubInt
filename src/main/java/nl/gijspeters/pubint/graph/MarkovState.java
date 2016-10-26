package nl.gijspeters.pubint.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 */
@Entity("state")
public class MarkovState extends PrismState {

    private Date latestArrival;
    private Date earliestDepature;

    public MarkovState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(15, 31)
                .append(getEdge())
                .append(getEarliestArrival())
                .append(getLatestArrival())
                .append(getEarliestDeparture())
                .append(getLatestDeparture())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof MarkovState)) {
            return false;
        }
        MarkovState m = (MarkovState) o;
        return getEdge().equals(m.getEdge())
                && getEarliestArrival().equals(m.getEarliestArrival())
                && getLatestArrival().equals(m.getLatestArrival())
                && getEarliestDeparture().equals(m.getEarliestDeparture())
                && getLatestDeparture().equals(m.getLatestDeparture());
    }

    public MarkovState(Edge edge, Date earliestArrival, Date latestArrival, Date earliestDeparture, Date latestDeparture) {
        super(edge, earliestArrival, latestDeparture);
    }

    @Override
    public long getMinimalTraversalTime() {
        return getEarliestDeparture().getTime() - getLatestArrival().getTime();
    }

    @Override
    public Date getEarliestDeparture() {
        return earliestDepature;
    }

    @Override
    public Date getLatestArrival() {
        return latestArrival;
    }

    public void setLatestArrival(Date latestArrival) {
        this.latestArrival = latestArrival;
    }

    public void setEarliestDepature(Date earliestDepature) {
        this.earliestDepature = earliestDepature;
    }
}
