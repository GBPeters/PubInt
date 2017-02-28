package nl.gijspeters.pubint.graph.factory;

import org.opentripplanner.routing.core.State;

import java.util.Comparator;

/**
 * Created by gijspeters on 19-11-16.
 *
 * Compares OTP states by time
 */
public class StateTimeComparator implements Comparator<State> {

    @Override
    public int compare(State o1, State o2) {
        if (o1.getTimeInMillis() > o2.getTimeInMillis()) {
            return 1;
        } else if (o1.getTimeInMillis() < o2.getTimeInMillis()) {
            return -1;
        } else {
            return 0;
        }
    }
}
