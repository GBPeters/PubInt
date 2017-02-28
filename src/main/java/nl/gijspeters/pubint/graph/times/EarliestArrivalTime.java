package nl.gijspeters.pubint.graph.times;

import java.util.Date;

/**
 * Created by gijspeters on 20-11-16.
 *
 * Should be implemented for a State that has an earliest arrival time
 */
public interface EarliestArrivalTime {

    Date getEarliestArrival();

}
