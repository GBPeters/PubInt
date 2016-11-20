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
 */
@Entity("graph")
public class BasicGraph implements Graph<Traversable> {

    @Id
    private String id;
    @Reference
    private HashMap<String, Vertex> vertices = new HashMap<>();
    private Set<Traversable> traversables = new HashSet<>();

    public BasicGraph() {
    }

    public BasicGraph(String id, Collection<? extends Traversable> traversables) {
        this(id, traversables, new HashSet<>());
    }

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

    public Set<Traversable> getOutgoingTraversables(Vertex v) {
        HashSet<Traversable> outgoing = new HashSet<>();
        for (Traversable t : getTraversables()) {
            if (v.equals(t.getFromVertex())) {
                outgoing.add(t);
            }
        }
        return outgoing;
    }

    public Set<Traversable> getIncomingTraversables(Vertex v) {
        HashSet<Traversable> incoming = new HashSet<>();
        for (Traversable t : getTraversables()) {
            if (v.equals(t.getToVertex())) {
                incoming.add(t);
            }
        }
        return incoming;
    }

    public Set<Traversable> getConnectedTraversables(Vertex v) {
        HashSet<Traversable> connected = new HashSet<>();
        connected.addAll(getOutgoingTraversables(v));
        connected.addAll(getIncomingTraversables(v));
        return connected;
    }

}
