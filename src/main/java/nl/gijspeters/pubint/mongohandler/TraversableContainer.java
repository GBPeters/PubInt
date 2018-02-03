package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.traversable.Traversable;
import org.mongodb.morphia.annotations.Reference;

/**
 * Created by gijspeters on 12-04-17.
 */
public class TraversableContainer {

    @Reference
    private Traversable traversable;

    private double p;

    public TraversableContainer() {
    }

    public TraversableContainer(Traversable traversable, double p) {
        this.traversable = traversable;
        this.p = p;
    }

    public Traversable getTraversable() {
        return traversable;
    }

    public void setTraversable(Traversable traversable) {
        this.traversable = traversable;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }
}
