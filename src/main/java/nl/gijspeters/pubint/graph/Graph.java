package nl.gijspeters.pubint.graph;

import java.util.Set;

/**
 * Created by gijspeters on 18-10-16.
 */
public interface Graph {

    Edge[] getEdges();

    Vertex[] getVertices();

    Set<Edge> getOutgoingEdges(Vertex v);

    Set<Edge> getIncomingEdges(Vertex v);

    Set<Edge> getConnectedEdges(Vertex v);

}
