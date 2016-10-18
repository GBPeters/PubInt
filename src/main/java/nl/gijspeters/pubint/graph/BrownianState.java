package nl.gijspeters.pubint.graph;

import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 */
@Entity("state")
public class BrownianState extends State {

    private long minimalTraversalTime;

    public BrownianState() {
        super();
    }

    public BrownianState(Edge edge, Date earliestArrival, Date latestArrival, Date earliestDeparture, Date latestDeparture, long minimalTraversalTime) {
        super(edge, earliestArrival, latestArrival, earliestDeparture, latestDeparture);
        setMinimalTraversalTime(minimalTraversalTime);
    }

    @Override
    public long getMinimalTraversalTime() {
        return minimalTraversalTime;
    }

    public void setMinimalTraversalTime(long t) {
        this.minimalTraversalTime = t;
    }
}
