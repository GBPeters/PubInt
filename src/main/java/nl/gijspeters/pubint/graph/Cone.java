package nl.gijspeters.pubint.graph;

import nl.gijspeters.pubint.structure.Anchor;

import java.util.Collection;

/**
 * Created by gijspeters on 18-10-16.
 */
public class Cone extends Cube {

    public enum ConeType {ORIGIN, DESTINATION}

    private Anchor anchor;
    private long duration;
    public final ConeType type;

    public Cone(Anchor anchor, long duration, ConeType type) {
        super();
        this.setAnchor(anchor);
        this.setDuration(duration);
        this.type = type;
    }

    public Cone(Anchor anchor, long duration, ConeType type, Collection<State> states) {
        super(states);
        this.setAnchor(anchor);
        this.setDuration(duration);
        this.type = type;
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
