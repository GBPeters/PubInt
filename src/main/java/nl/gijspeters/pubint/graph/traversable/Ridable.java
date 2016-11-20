package nl.gijspeters.pubint.graph.traversable;

import org.onebusaway.gtfs.model.AgencyAndId;

import java.util.Date;

/**
 * Created by gijspeters on 19-11-16.
 */
public interface Ridable extends Traversable {

    AgencyAndId getTrip();

    Date getDeparture();

    Date getArrival();

    long getRideTime();

}
