package nl.gijspeters.pubint.graph;

import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 */
@Entity("state")
public abstract class PrismState extends ConeState implements OriginState, DestinationState, TraversableState {

    private Date earliestArrival;
    private Date latestDeparture;

    public PrismState() {
        super();
    }

    public PrismState(Edge edge, Date earliestArrival, Date latestDeparture) {
        super(edge);
    }

    @Override
    public Date getLatestDeparture() {
        return latestDeparture;
    }

    public void setLatestDeparture(Date latestDeparture) {
        this.latestDeparture = latestDeparture;
    }

    @Override
    public Date getEarliestArrival() {
        return earliestArrival;
    }

    public void setEarliestArrival(Date earliestArrival) {
        this.earliestArrival = earliestArrival;
    }

}
