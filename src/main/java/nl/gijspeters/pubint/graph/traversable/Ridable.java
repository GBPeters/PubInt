package nl.gijspeters.pubint.graph.traversable;

import java.util.Date;

/**
 * Created by gijspeters on 19-11-16.
 *
 * Basic Traversable extension for a transit Traversable, containing Trip information, departure time and arrival time.
 */
public interface Ridable extends Traversable {

    Trip getTrip();

    Date getDeparture();

    Date getArrival();

    long getRideTime();

}
