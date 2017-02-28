package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.PrismTimes;
import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 20-11-16.
 *
 * Interface for a State in a Prism, that requires that all times are provided.
 */
public interface PrismState<T extends Traversable> extends State<T>, PrismTimes {
}
