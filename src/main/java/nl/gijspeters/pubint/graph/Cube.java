package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.state.State;
import nl.gijspeters.pubint.graph.traversable.Traversable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 18-10-16.
 *
 * Class representing a network-time cube,
 * which is a set of states representing a (possible) trajectory in space and time.
 */

public class Cube<T extends State> implements NavigableGraph<Traversable> {

    private double walkSpeed;

    protected Set<T> states = new HashSet<>();

    public Cube() {

    }

    /**
     * Constructor that populates this Cube with a Set of States
     *
     * @param states    A Set of T objects
     * @param walkSpeed The walking speed to use in this Cube
     */
    public Cube(Collection<T> states, double walkSpeed) {
        this.states.addAll(states);
        this.walkSpeed = walkSpeed;
    }

    /**
     * Constructor for empty Cube
     * @param walkSpeed The walking speed to use in this Cube
     */
    public Cube(double walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    /**
     * Auxilliary method that creates a set of unique Traversables from the states in this Cube.
     * @return A Set of Traversables
     */
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

    /**
     * Get all outgoing Traversables for Vertex v, for which v is the fromVertex
     *
     * @param v A Vertex
     * @return A Set with outgoing Traversables
     */
    @Override
    public Set<Traversable> getOutgoingTraversables(Vertex v) {
        return new BasicGraph("temp", createTraversableSet()).getOutgoingTraversables(v);
    }

    /**
     * Get all incoming Traversables for Vertex v, for which v is the toVertex
     *
     * @param v A Vertex
     * @return A Set with incoming Traversables
     */
    @Override
    public Set<Traversable> getIncomingTraversables(Vertex v) {
        return new BasicGraph("temp", createTraversableSet()).getIncomingTraversables(v);
    }

    /**
     * Get all connected (outgoing + incoming) Traversables from Vertex v, for which v is either fromVertex, toVertex
     * or both.
     *
     * @param v A Vertex
     * @return A Set with connected Traversables
     */
    @Override
    public Set<Traversable> getConnectedTraversables(Vertex v) {
        return new BasicGraph("temp", createTraversableSet()).getConnectedTraversables(v);
    }

    /**
     * Get all states that traverse Traversable t
     * @param t A Traversable
     * @return A Set of T objects
     */
    public Set<T> getTraversingStates(Traversable t) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (t.equals(s.getTraversable())) {
                states.add(s);
            }
        }
        return states;
    }

    /**
     * Get all States going out from Vertex v, for which Vertex v is the State's Traversable fromVertex
     * @param v A Vertex
     * @return A Set of States
     */
    public Set<T> getOutgoingStates(Vertex v) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (v.equals(s.getTraversable().getFromVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    /**
     * Get all States coming in to Vertex v, for which Vertex v is the State's Traversable toVertex
     * @param v A Vertex
     * @return A Set of States
     */
    public Set<T> getIncomingStates(Vertex v) {
        HashSet<T> states = new HashSet<>();
        for (T s : this.states) {
            if (v.equals(s.getTraversable().getToVertex())) {
                states.add(s);
            }
        }
        return states;
    }

    /**
     * Get all States going out from or going in to Vertex v,
     * for which Vertex v is the State's Traversable fromVertex, toVertex, or both
     * @param v A Vertex
     * @return A Set of States
     */
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
