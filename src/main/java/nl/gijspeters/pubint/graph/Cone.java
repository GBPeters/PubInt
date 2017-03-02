package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.state.State;

import java.util.Collection;

/**
 * Created by gijspeters on 18-10-16.
 *
 * A Cone is a network-time structure for which all vertices are reachable from a given origin (or to a given destination)
 * within the set amount of time. The States are derived from optimal paths leading from or to these vertices.
 */
public class Cone<T extends State> extends Cube<T> {

    private long duration;

    public Cone(long duration, double walkSpeed) {
        super(walkSpeed);
        this.setDuration(duration);
    }

    public Cone(long duration, double walkSpeed, Collection<T> states) {
        super(states, walkSpeed);
        this.setDuration(duration);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
