package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 20-10-16.
 *
 * Elementary description of a State: It traverses a Traversable, and for this a minimal time is required
 */
public interface State<T extends Traversable> {

    T getTraversable();

}
