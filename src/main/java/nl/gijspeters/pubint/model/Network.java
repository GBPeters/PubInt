package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.structure.Leg;

/**
 * Created by gijspeters on 04-04-17.
 */
public abstract class Network extends ModelResultGraph {

    public Network(Leg leg) {
        super(leg);
    }

    public abstract void addTransect(Transect transect, long deltaTimeSeconds);

    public ModelResultGraph getResultGraph() {
        return new ModelResultGraph(this);
    }

}
