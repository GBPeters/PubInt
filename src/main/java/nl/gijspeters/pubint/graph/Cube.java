package nl.gijspeters.pubint.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 18-10-16.
 */

public class Cube<T extends State> implements Graph {

    protected HashSet<T> states = new HashSet<>();

    public Cube() {

    }

    public Cube(Collection<T> states) {
        this.states.addAll(states);
    }

    private Set<Edge> createEdgeSet() {
        HashSet<Edge> edges = new HashSet<>();
        for (State s : states) {
            edges.add(s.getEdge());
        }
        return edges;
    }

    public Set<T> getStates() {
        return states;
    }

    @Override
    public Edge[] getEdges() {
        Set<Edge> edges = createEdgeSet();
        return edges.toArray(new Edge[edges.size()]);
    }

    @Override
    public Vertex[] getVertices() {
        return new BasicGraph("temp", createEdgeSet()).getVertices();
    }

    @Override
    public Set<Edge> getOutgoingEdges(Vertex v) {
        return new BasicGraph("temp", createEdgeSet()).getOutgoingEdges(v);
    }

    @Override
    public Set<Edge> getIncomingEdges(Vertex v) {
        return new BasicGraph("temp", createEdgeSet()).getIncomingEdges(v);
    }

    @Override
    public Set<Edge> getConnectedEdges(Vertex v) {
        return new BasicGraph("temp", createEdgeSet()).getConnectedEdges(v);
    }

    public Set<T> getTraversingStates(Edge e) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (e.equals(s.getEdge())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<T> getOutgoingStates(Vertex v) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (v.equals(s.getEdge().getFromVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<T> getIncomingStates(Vertex v) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (v.equals(s.getEdge().getToVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<T> getConnectedStates(Vertex v) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (v.equals(s.getEdge().getFromVertex()) ||
                    v.equals(s.getEdge().getToVertex())) {
                states.add(s);
            }
        }
        return states;
    }

}
