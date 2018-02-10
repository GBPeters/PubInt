package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.structure.Leg;

/**
 * Created by gijspeters on 04-04-17.
 */
public abstract class Network extends ModelResultGraph<Traversable> {

    public Network(Leg leg) {
        super(leg);
    }

    public abstract void addTransect(Transect transect, long deltaTimeSeconds);

    public ModelResultGraph<Traversable> getResultGraph() {
        return new ModelResultGraph<>(this);
    }

}
