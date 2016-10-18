package nl.gijspeters.pubint.graph;

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
public class BasicGraph implements Graph {

    @Id
    private String id;
    @Reference
    private HashSet<Edge> edges = new HashSet<Edge>();
    @Reference
    private HashMap<String, Vertex> vertices = new HashMap<String, Vertex>();

    public BasicGraph() {
    }

    public BasicGraph(String id, Collection<Edge> edges) {
        this(id, edges, new HashSet<Vertex>());
    }

    public BasicGraph(String id, Collection<Edge> edges, Collection<Vertex> vertices) {
        this.id = id;
        for (Edge e : edges) {
            vertices.add(e.getFromVertex());
            vertices.add(e.getToVertex());
        }
        this.edges.addAll(edges);
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

    public Edge[] getEdges() {
        return edges.toArray(new Edge[edges.size()]);
    }

    public Vertex[] getVertices() {
        return vertices.values().toArray(new Vertex[vertices.size()]);
    }

    public Set<Edge> getOutgoingEdges(Vertex v) {
        HashSet<Edge> outgoing = new HashSet<Edge>();
        for (Edge e : edges) {
            if (v.equals(e.getFromVertex())) {
                outgoing.add(e);
            }
        }
        return outgoing;
    }

    public Set<Edge> getIncomingEdges(Vertex v) {
        HashSet<Edge> incoming = new HashSet<Edge>();
        for (Edge e : edges) {
            if (v.equals(e.getToVertex())) {
                incoming.add(e);
            }
        }
        return incoming;
    }

    public Set<Edge> getConnectedEdges(Vertex v) {
        HashSet<Edge> connected = new HashSet<Edge>();
        connected.addAll(getOutgoingEdges(v));
        connected.addAll(getIncomingEdges(v));
        return connected;
    }

}
