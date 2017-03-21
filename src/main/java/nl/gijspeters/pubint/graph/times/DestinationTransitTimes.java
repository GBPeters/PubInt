package nl.gijspeters.pubint.graph.times;

/**
 * Created by gijspeters on 20-11-16.
 *
 * Transit states derived from shortest paths to a destination should implement this.
 */
public interface DestinationTransitTimes extends DestinationTraversedTimes, TransitTimes {
}
