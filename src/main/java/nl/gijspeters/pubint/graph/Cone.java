package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.graph.state.State;
import nl.gijspeters.pubint.structure.Anchor;

import java.util.Collection;

/**
 * Created by gijspeters on 18-10-16.
 */
public class Cone<T extends State> extends Cube {

    private Anchor anchor;
    private long duration;

    public Cone(Anchor anchor, long duration, double walkSpeed) {
        super(walkSpeed);
        this.setAnchor(anchor);
        this.setDuration(duration);
    }

    public Cone(Anchor anchor, long duration, double walkSpeed, Collection<T> states) {
        super(states, walkSpeed);
        this.setAnchor(anchor);
        this.setDuration(duration);
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
