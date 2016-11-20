package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.state.State;
import nl.gijspeters.pubint.graph.traversable.Traversable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 18-10-16.
 */

public class Cube<T extends State> implements Graph<Traversable> {

    private double walkSpeed;

    protected Set<T> states = new HashSet<>();

    public Cube() {

    }

    public Cube(Collection<T> states, double walkSpeed) {
        this.states.addAll(states);
        this.walkSpeed = walkSpeed;
    }

    public Cube(double walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    private Set<Traversable> createTraversableSet() {
        HashSet<Traversable> traversables = new HashSet<>();
        for (State s : states) {
            traversables.add(s.getTraversable());
        }
        return traversables;
    }

    public Set<T> getStates() {
        return states;
    }

    @Override
    public Collection<Traversable> getTraversables() {
        Set<Traversable> traversables = createTraversableSet();
        return traversables;
    }

    @Override
    public Set<Vertex> getVertices() {
        return new BasicGraph("temp", createTraversableSet()).getVertices();
    }

    @Override
    public Set<Traversable> getOutgoingTraversables(Vertex v) {
        return new BasicGraph("temp", createTraversableSet()).getOutgoingTraversables(v);
    }

    @Override
    public Set<Traversable> getIncomingTraversables(Vertex v) {
        return new BasicGraph("temp", createTraversableSet()).getIncomingTraversables(v);
    }

    @Override
    public Set<Traversable> getConnectedTraversables(Vertex v) {
        return new BasicGraph("temp", createTraversableSet()).getConnectedTraversables(v);
    }

    public Set<T> getTraversingStates(Traversable t) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (t.equals(s.getTraversable())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<T> getOutgoingStates(Vertex v) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (v.equals(s.getTraversable().getFromVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<T> getIncomingStates(Vertex v) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (v.equals(s.getTraversable().getToVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    public Set<T> getConnectedStates(Vertex v) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (v.equals(s.getTraversable().getFromVertex()) ||
                    v.equals(s.getTraversable().getToVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    public double getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(double walkSpeed) {
        this.walkSpeed = walkSpeed;
    }
}
