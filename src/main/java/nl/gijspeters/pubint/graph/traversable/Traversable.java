package nl.gijspeters.pubint.graph.traversable;

import nl.gijspeters.pubint.graph.Vertex;

/**
 * Created by gijspeters on 20-11-16.
 *
 * Most simple representation of a connection between to vertices. This does not contain any information on geometry or
 * intermediate edges.
 */
public interface Traversable {

    Vertex getFromVertex();

    Vertex getToVertex();

}
