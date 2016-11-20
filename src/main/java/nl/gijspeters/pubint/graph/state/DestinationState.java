package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.DestinationTimes;
import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 20-11-16.
 */
public interface DestinationState<T extends Traversable> extends State<T>, DestinationTimes {
}