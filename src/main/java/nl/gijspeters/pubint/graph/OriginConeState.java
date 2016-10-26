package nl.gijspeters.pubint.graph;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 20-10-16.
 */
@Entity("state")
public class OriginConeState extends ConeState implements OriginState {

    private Date earliestArrival;
    private Date earliestDeparture;

    public OriginConeState() {

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 33)
                .append(getEdge())
                .append(earliestArrival.getTime())
                .append(earliestDeparture.getTime())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof OriginConeState)) {
            return false;
        }
        OriginConeState c = (OriginConeState) o;
        return getEdge().equals(c.getEdge())
                && getEarliestArrival().equals(c.getEarliestArrival())
                && getEarliestDeparture().equals(c.getEarliestDeparture());
    }

    public OriginConeState(Edge edge, Date earliestArrival, Date earliestDeparture) {
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
}
