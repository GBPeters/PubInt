package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 18-03-17.
 */
public interface TraversedState<T extends Traversable> extends State<T> {

    long getMinimalTraversalTime();

}
