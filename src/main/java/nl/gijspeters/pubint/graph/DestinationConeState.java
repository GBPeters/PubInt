package nl.gijspeters.pubint.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by gijspeters on 20-10-16.
 */
public class DestinationConeState extends ConeState implements DestinationState {

    private Date latestDeparture;
    private Date latestArrival;

    public DestinationConeState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 33)
                .append(getEdge())
                .append(latestDeparture.getTime())
                .append(latestArrival.getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof DestinationConeState)) {
            return false;
        }
        DestinationConeState c = (DestinationConeState) o;
        return getEdge().equals(c.getEdge())
                && getLatestArrival().equals(c.getLatestArrival())
                && getLatestDeparture().equals(c.getLatestDeparture());
    }

    public DestinationConeState(Edge edge, Date latestArrival, Date latestDeparture) {
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
}
