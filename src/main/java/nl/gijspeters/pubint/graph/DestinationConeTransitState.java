package nl.gijspeters.pubint.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by gijspeters on 25-10-16.
 */
public class DestinationConeTransitState extends DestinationConeState implements DestinationTransitState {

    private Date earliestDeparture;

    public DestinationConeTransitState() {
        super();
    }

    public DestinationConeTransitState(Edge edge, Date latestArrival, Date earliestDeparture, Date latestDeparture) {
        super(edge, latestArrival, latestDeparture);
        this.setEarliestDeparture(earliestDeparture);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 35)
                .append(super.hashCode())
                .append(earliestDeparture.getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof DestinationConeTransitState)) {
            return false;
        }
        DestinationConeTransitState c = (DestinationConeTransitState) o;
        return super.equals(c)
                && getEarliestDeparture().equals(c.getEarliestDeparture());
    }

    @Override
    public Date getEarliestDeparture() {
        return earliestDeparture;
    }

    public void setEarliestDeparture(Date earliestDeparture) {
        this.earliestDeparture = earliestDeparture;
    }
}
