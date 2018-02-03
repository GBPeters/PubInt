package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.traversable.Traversable;

import java.util.Collection;
import java.util.Set;

/**
 * Created by gijspeters on 18-10-16.
 *
 * Basic interface for representing a Graph, which contains Vertices (points, nodes) and Traversables (edges, links)
 */
public interface Graph<T extends Traversable> {

    Collection<T> getTraversables();

    Set<Vertex> getVertices();

}
