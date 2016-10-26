package nl.gijspeters.pubint.graph;

/**
 * Created by gijspeters on 20-10-16.
 */
public interface TraversableState extends OriginState, DestinationState {

    long getMinimalTraversalTime();

}
