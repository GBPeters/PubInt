package nl.gijspeters.pubint.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 18-10-16.
 */

public class Cube implements Graph {

    private HashSet<State> states = new HashSet<>();

    public Cube() {

    }

    public Cube(Collection<State> states) {
        this.states.addAll(states);
    }

    private Set<Edge> createEdgeSet() {
        HashSet<Edge> edges = new HashSet<>();
        for (State s : states) {
            edges.add(s.getEdge());
        }
        return edges;
    }

    public Set<State> getStates() {
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

    public Set<State> getTraversingStates(Edge e) {
        HashSet<State> states = new HashSet<>();
        for (State s : this.states) {
            if (e.equals(s.getEdge())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<State> getOutgoingStates(Vertex v) {
        HashSet<State> states = new HashSet<>();
        for (State s : this.states) {
            if (v.equals(s.getEdge().getFromVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<State> getIncomingStates(Vertex v) {
        HashSet<State> states = new HashSet<>();
        for (State s : this.states) {
            if (v.equals(s.getEdge().getToVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<State> getConnectedStates(Vertex v) {
        HashSet<State> states = new HashSet<>();
        for (State s : this.states) {
            if (v.equals(s.getEdge().getFromVertex()) ||
                    v.equals(s.getEdge().getToVertex())) {
                states.add(s);
            }
        }
        return states;
    }

}
