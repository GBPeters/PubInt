package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.traversable.Traversable;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 17-10-16.
 * Class representing a basic graph, which just contains vertices and traversables
 *
 */
@Entity("graph")
public class BasicGraph implements NavigableGraph<Traversable> {

    @Id
    private String id;
    @Reference
    private HashMap<String, Vertex> vertices = new HashMap<>();
    private Set<Traversable> traversables = new HashSet<>();

    /**
     * Empty constructor
     */
    public BasicGraph() {
    }

    /**
     * Constructor. Vertices will be extracted from Traversables.
     *
     * @param id           A String identifier
     * @param traversables A collection of Traversables
     */
    public BasicGraph(String id, Collection<? extends Traversable> traversables) {
        this(id, traversables, new HashSet<>());
    }

    /**
     * Constructor. Vertices will also be extracted from Traversables.
     * @param id A String identifier
     * @param traversables A collection of Traversables
     * @param vertices An additional collection of Vertices, that will be saved with the Vertices extracted from
     *                 the Traversables
     */
    public BasicGraph(String id, Collection<? extends Traversable> traversables, Collection<Vertex> vertices) {
        this.id = id;
        for (Traversable t : traversables) {
            vertices.add(t.getFromVertex());
            vertices.add(t.getToVertex());
        }
        getTraversables().addAll(traversables);
        for (Vertex v : vertices) {
            this.vertices.put(v.getVertexLabel(), v);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public Collection<Traversable> getTraversables() {
        return traversables;
    }

    public Set<Vertex> getVertices() {
        return new HashSet<>(vertices.values());
    }

    /**
     * Get all outgoing Traversables for Vertex v, for which v is the fromVertex
     *
     * @param v A Vertex
     * @return A Set with outgoing Traversables
     */
    public Set<Traversable> getOutgoingTraversables(Vertex v) {
        HashSet<Traversable> outgoing = new HashSet<>();
        for (Traversable t : getTraversables()) {
            if (v.equals(t.getFromVertex())) {
                outgoing.add(t);
            }
        }
        return outgoing;
    }

    /**
     * Get all incoming Traversables for Vertex v, for which v is the toVertex
     *
     * @param v A Vertex
     * @return A Set with incoming Traversables
     */
    public Set<Traversable> getIncomingTraversables(Vertex v) {
        HashSet<Traversable> incoming = new HashSet<>();
        for (Traversable t : getTraversables()) {
            if (v.equals(t.getToVertex())) {
                incoming.add(t);
            }
        }
        return incoming;
    }

    /**
     * Get all connected (outgoing + incoming) Traversables from Vertex v, for which v is either fromVertex, toVertex
     * or both.
     *
     * @param v A Vertex
     * @return A Set with connected Traversables
     */
    public Set<Traversable> getConnectedTraversables(Vertex v) {
        HashSet<Traversable> connected = new HashSet<>();
        connected.addAll(getOutgoingTraversables(v));
        connected.addAll(getIncomingTraversables(v));
        return connected;
    }

}
