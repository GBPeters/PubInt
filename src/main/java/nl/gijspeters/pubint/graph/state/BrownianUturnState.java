package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 19-03-17.
 */
@Entity("state")
public class BrownianUturnState extends BrownianState {

    public BrownianUturnState() {
        super();
    }

    public BrownianUturnState(Edge edge, Date earliestDeparture, Date latestArrival) {
        super(edge, earliestDeparture, latestArrival);
    }

    @Override
    public double getVisitProbability(Date originDate, Date destinationDate, Date currentDate) {
        return 0;
    }
}
