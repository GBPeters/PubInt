package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.structure.Leg;

/**
 * Created by gijspeters on 04-04-17.
 */
public class VisitTimeNetwork extends Network {

    public VisitTimeNetwork(Leg leg) {
        super(leg);
    }

    @Override
    public void addTransect(Transect transect, long deltaTimeSeconds) {
        for (Traversable t : transect.keySet()) {
            add(t, get(t) * deltaTimeSeconds);
        }
    }
}
