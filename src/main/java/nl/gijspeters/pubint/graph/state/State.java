package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 20-10-16.
 */
public interface State<T extends Traversable> {

    T getTraversable();

    long getMinimalTraversalTime();

}
