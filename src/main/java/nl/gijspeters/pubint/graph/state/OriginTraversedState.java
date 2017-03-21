package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.OriginTraversedTimes;
import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 20-11-16.
 * <p>
 * Interface for a State calculated from an origin that fully traverses its Traversable
 */
public interface OriginTraversedState<T extends Traversable> extends OriginState<T>, TraversedState<T>,
        OriginTraversedTimes {

}
