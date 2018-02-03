package nl.gijspeters.pubint.graph.times;

/**
 * Created by gijspeters on 07-04-17.
 */
public class DepartureTimeComparator extends TimeComparator<EarliestDepartureTime> {
    @Override
    public int compare(EarliestDepartureTime o1, EarliestDepartureTime o2) {
        return compareDates(o1.getEarliestDeparture(), o2.getEarliestDeparture());
    }
}
