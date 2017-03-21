package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.DestinationTraversedTimes;
import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 20-11-16.
 * <p>
 * Interface for State calculated from a destination that fully traverses its Traversable
 */
public interface DestinationTraversedState<T extends Traversable> extends DestinationState<T>, TraversedState<T>,
        DestinationTraversedTimes {

}
