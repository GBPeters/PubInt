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
    public double getVisitProbability(Date originDate, Date destinationDate, Date currentDate, double dispersion,
                                      double transtition) {
        long tO = originDate.getTime();
        long tD = destinationDate.getTime();
        long t = currentDate.getTime();
        long timin = getEarliestDeparture().getTime();
        long timax = getLatestArrival().getTime();
        assert tO <= t && t <= tD && tO <= timin && timax <= tD;
        double bimin = 0;
        double bimax = 0;
        double dtO = 1 / (Math.sqrt(2 * Math.PI) * dispersion * (t - tO));
        double dtD = 1 / (Math.sqrt(2 * Math.PI) * dispersion * (tD - t));
        if (t >= timin) {
            double eimin = -Math.pow(timin - tO, 2) / (2 * Math.pow(dispersion, 2) * Math.pow(t - tO, 2));
            bimin = dtO * Math.exp(eimin);
        }
        if (t <= timax) {
            double eimax = -Math.pow(tD - timax, 2) / (2 * Math.pow(dispersion, 2) * Math.pow(tD - t, 2));
            bimax = dtD * Math.exp(eimax);
        }
        return bimin * bimax;
    }
}
