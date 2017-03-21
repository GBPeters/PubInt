package nl.gijspeters.pubint.graph.times;

/**
 * Created by gijspeters on 20-11-16.
 *
 * Transit states derived from shortest paths from an origin should implement this.
 */
public interface OriginTransitTimes extends OriginTraversedTimes, TransitTimes {
}
