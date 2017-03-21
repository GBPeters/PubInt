package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.LatestArrivalTime;
import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 18-03-17.
 *
 * Interface for State calculated from a destination
 */
public interface DestinationState<T extends Traversable> extends ConeState<T>, LatestArrivalTime {

}
