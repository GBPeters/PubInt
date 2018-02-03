package nl.gijspeters.pubint.graph.times;

/**
 * Created by gijspeters on 07-04-17.
 */
public class ArrivalTimeComparator extends TimeComparator<LatestArrivalTime> {

    @Override
    public int compare(LatestArrivalTime o1, LatestArrivalTime o2) {
        return compareDates(o1.getLatestArrival(), o2.getLatestArrival());
    }
}
