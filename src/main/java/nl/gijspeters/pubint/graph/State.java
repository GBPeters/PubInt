package nl.gijspeters.pubint.graph;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 */
@Entity("state")
public abstract class State {

    @Id
    private ObjectId objectId;
    @Reference
    private Edge edge;
    private Date earliestArrival;
    private Date latestArrival;
    private Date earliestDeparture;
    private Date latestDeparture;

    public State() {

    }

    public State(Edge edge, Date earliestArrival, Date latestArrival, Date earliestDeparture, Date latestDeparture) {
        this.setEdge(edge);
        this.setEarliestArrival(earliestArrival);
        this.setLatestArrival(latestArrival);
        this.setEarliestDeparture(earliestDeparture);
        this.setLatestDeparture(latestDeparture);
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Date getLatestArrival() {
        return latestArrival;
    }

    public void setLatestArrival(Date latestArrival) {
        this.latestArrival = latestArrival;
    }

    public Date getEarliestDeparture() {
        return earliestDeparture;
    }

    public void setEarliestDeparture(Date earliestDeparture) {
        this.earliestDeparture = earliestDeparture;
    }

    public Date getEarliestArrival() {
        return earliestArrival;
    }

    public void setEarliestArrival(Date earliestArrival) {
        this.earliestArrival = earliestArrival;
    }

    public Date getLatestDeparture() {
        return latestDeparture;
    }

    public void setLatestDeparture(Date latestDeparture) {
        this.latestDeparture = latestDeparture;
    }

    public abstract long getMinimalTraversalTime();

}
