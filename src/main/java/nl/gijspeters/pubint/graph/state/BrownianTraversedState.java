package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.DestinationTraversedTimes;
import nl.gijspeters.pubint.graph.times.OriginTraversedTimes;
import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 * <p>
 * PrismState derived from matching UndirectedStates. The Brownian Bridges model for visit probabilities should be
 * used on these States.
 */
@Entity("state")
public class BrownianTraversedState extends BrownianState implements TraversedState<Edge>,
        OriginTraversedTimes, DestinationTraversedTimes {

    private long minimalTraversalTime;

    public BrownianTraversedState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 3)
                .append(super.hashCode())
                .append(minimalTraversalTime)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof BrownianTraversedState)) {
            return false;
        }
        BrownianTraversedState b = (BrownianTraversedState) o;
        return super.equals(b)
                && getMinimalTraversalTime() == b.getMinimalTraversalTime();
    }

    @Override
    public double getVisitProbability(Date originDate, Date destinationDate, Date currentDate,
                                      double dispersion, double transition) {
        long tO = originDate.getTime();
        long tD = destinationDate.getTime();
        long t = currentDate.getTime();
        long timin = getEarliestDeparture().getTime();
        long tjmax = getLatestArrival().getTime();
        assert tO <= t && t <= tD && tO <= timin && tjmax <= tD;
        long timax = getLatestDeparture().getTime();
        double bimin = 0;
        double bimax = 0;
        double bjmax = 0;
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
        if (t <= tjmax) {
            double ejmax = -Math.pow(tD - tjmax, 2) / (2 * Math.pow(dispersion, 2) * Math.pow(tD - t, 2));
            bjmax = dtD * Math.exp(ejmax);
        }
        double pii = bimin * bimax;
        double pij = bimin * bjmax;
        return pii + pij;
    }

    public BrownianTraversedState(Edge edge, Date earliestDeparture, Date latestArrival, long minimalTraversalTime) {
        super(edge, earliestDeparture, latestArrival);
        setMinimalTraversalTime(minimalTraversalTime);
    }

    @Override
    public long getMinimalTraversalTime() {
        return minimalTraversalTime;
    }

    public void setMinimalTraversalTime(long t) {
        this.minimalTraversalTime = t;
    }

    @Override
    public Date getEarliestArrival() {
        return new Date(getEarliestDeparture().getTime() + minimalTraversalTime);
    }

    @Override
    public Date getLatestDeparture() {
        return new Date(getLatestArrival().getTime() - minimalTraversalTime);
    }
}
