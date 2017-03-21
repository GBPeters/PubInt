package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.EarliestDepartureTime;
import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 18-03-17.
 *
 * Interface for a State calculated from an origin
 */
public interface OriginState<T extends Traversable> extends ConeState<T>, EarliestDepartureTime {

}
