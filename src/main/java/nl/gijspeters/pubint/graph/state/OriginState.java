package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.times.OriginTimes;
import nl.gijspeters.pubint.graph.traversable.Traversable;

/**
 * Created by gijspeters on 20-11-16.
 */
public interface OriginState<T extends Traversable> extends State<T>, OriginTimes {

    boolean matches(DestinationState destinationState);

}
