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

    /**
     * Get all outgoing Traversables for Vertex v, for which v is the fromVertex
     *
     * @param v A Vertex
     * @return A Set with outgoing Traversables
     */
    Set<T> getOutgoingTraversables(Vertex v);

    /**
     * Get all incoming Traversables for Vertex v, for which v is the toVertex
     *
     * @param v A Vertex
     * @return A Set with incoming Traversables
     */
    Set<T> getIncomingTraversables(Vertex v);

    /**
     * Get all connected (outgoing + incoming) Traversables from Vertex v, for which v is either fromVertex, toVertex
     * or both.
     *
     * @param v A Vertex
     * @return A Set with connected Traversables
     */
    Set<T> getConnectedTraversables(Vertex v);

}
