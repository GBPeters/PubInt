package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Ride;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Created by gijspeters on 18-10-16.
 *
 * PrismState derived from matching TransitStates. The Semi-Markov model for visit probabilities should be
 * used on these States.
 */
@Entity("state")
public class MarkovState extends TransitState implements PrismState<Ride> {

    private Date latestArrival;
    private Date earliestDepature;

    public MarkovState() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(15, 31)
                .append(getTraversable())
                .append(getEarliestArrival())
                .append(getLatestArrival())
                .append(getEarliestDeparture())
                .append(getLatestDeparture())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof MarkovState)) {
            return false;
        }
        MarkovState m = (MarkovState) o;
        return getTraversable().equals(m.getTraversable())
                && getEarliestArrival().equals(m.getEarliestArrival())
                && getLatestArrival().equals(m.getLatestArrival())
                && getEarliestDeparture().equals(m.getEarliestDeparture())
                && getLatestDeparture().equals(m.getLatestDeparture());
    }

    public MarkovState(Ride ride, Date earliestDepature, Date latestDeparture, Date earliestArrival, Date latestArrival) {
        super(ride, latestDeparture, earliestArrival);
        this.earliestDepature = earliestDepature;
        this.latestArrival = latestArrival;
    }

    @Override
    public long getMinimalTraversalTime() {
        return getEarliestDeparture().getTime() - getLatestArrival().getTime();
    }

    @Override
    public Date getEarliestDeparture() {
        return earliestDepature;
    }

    @Override
    public Date getLatestArrival() {
        return latestArrival;
    }

    public void setLatestArrival(Date latestArrival) {
        this.latestArrival = latestArrival;
    }

    public void setEarliestDepature(Date earliestDepature) {
        this.earliestDepature = earliestDepature;
    }

    @Override
    public double getVisitProbability(Date originDate, Date destinationDate, Date currentDate, double dispersion,
                                      double transition) {
        long t = currentDate.getTime() / 1000;
        long timax = getLatestDeparture().getTime() / 1000;
        long tjmin = getEarliestArrival().getTime() / 1000;
        if (t < timax || t > tjmin) {
            return 0;
        }
        long tO = originDate.getTime() / 1000;
        long tD = destinationDate.getTime() / 1000;
        long timin = getEarliestDeparture().getTime() / 1000;
        long tjmax = getLatestArrival().getTime() / 1000;
        double eimin = -Math.exp(-transition * (timin - tO));
        double eimax = -Math.exp(-transition * (timax - tO));
        double ejmin = -Math.exp(-transition * (tD - tjmax));
        double ejmax = -Math.exp(-transition * (tD - tjmin));
        double pOi = eimin - eimax;
        double pjD = ejmin - ejmax;
        return pOi * pjD;
    }
}
