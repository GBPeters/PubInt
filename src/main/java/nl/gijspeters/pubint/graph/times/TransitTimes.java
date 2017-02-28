package nl.gijspeters.pubint.graph.times;

/**
 * Created by gijspeters on 20-11-16.
 *
 * Should be implemented by State representing traversal over the public transport network.
 * Transit states have a fixed (latest) departure time, and a fixed (latest) arrival time.
 *
 */
public interface TransitTimes extends LatestDepartureTime, EarliestArrivalTime {
}
