package nl.gijspeters.pubint.graph;

import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 */
@Entity("state")
public class MarkovState extends State {

    public MarkovState() {
        super();
    }

    public MarkovState(Edge edge, Date earliestArrival, Date latestArrival, Date earliestDeparture, Date latestDeparture) {
        super(edge, earliestArrival, latestArrival, earliestDeparture, latestDeparture);
    }

    @Override
    public long getMinimalTraversalTime() {
        return getEarliestDeparture().getTime() - getLatestArrival().getTime();
    }

}
