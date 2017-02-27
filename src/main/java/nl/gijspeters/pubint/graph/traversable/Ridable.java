package nl.gijspeters.pubint.graph.traversable;

import java.util.Date;

/**
 * Created by gijspeters on 19-11-16.
 */
public interface Ridable extends Traversable {

    Trip getTrip();

    Date getDeparture();

    Date getArrival();

    long getRideTime();

}
