package nl.gijspeters.pubint.graph.times;

import java.util.Date;

/**
 * Created by gijspeters on 20-11-16.
 *
 * Should be implemented by a State that has a latest arrival time
 */
public interface LatestArrivalTime {

    Date getLatestArrival();

}
