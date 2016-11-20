package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.traversable.Traversable;

import java.util.Collection;
import java.util.Set;

/**
 * Created by gijspeters on 18-10-16.
 */
public interface Graph<T extends Traversable> {

    Collection<T> getTraversables();

    Set<Vertex> getVertices();

    Set<T> getOutgoingTraversables(Vertex v);

    Set<T> getIncomingTraversables(Vertex v);

    Set<T> getConnectedTraversables(Vertex v);

}
