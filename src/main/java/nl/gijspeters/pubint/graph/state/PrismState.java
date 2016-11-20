package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.PrismTimes;
import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 20-11-16.
 */
public interface PrismState<T extends Traversable> extends OriginState<T>, DestinationState<T>, PrismTimes {
}
