package nl.gijspeters.pubint.graph;

import java.util.Date;

/**
 * Created by gijspeters on 20-10-16.
 */
public interface DestinationState extends State, TransitDeparture {

    Date getLatestDeparture();

}
