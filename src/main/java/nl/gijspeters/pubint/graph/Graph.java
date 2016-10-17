package nl.gijspeters.pubint.graph;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 17-10-16.
 */
@Entity("graph")
public class Graph {

    @Id
    private ObjectId objectId;
    @Reference
    private HashSet<Edge> edges = new HashSet<Edge>();
    @Reference
    private HashSet<Vertex> vertices = new HashSet<Vertex>();

    public Graph() {
    }

    public Graph(Collection<Edge> edges) {
        this(edges, new HashSet<Vertex>());
    }

    public Graph(Collection<Edge> edges, Collection<Vertex> vertices) {
        for (Edge e : edges) {
            this.vertices.add(e.getFromVertex());
            this.vertices.add(e.getToVertex());
        }
        this.edges.addAll(edges);
        this.vertices.addAll(vertices);
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public Edge[] getEdges() {
        return edges.toArray(new Edge[edges.size()]);
    }

    public Vertex[] getVertices() {
        return vertices.toArray(new Vertex[vertices.size()]);
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
