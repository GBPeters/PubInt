package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.structure.Leg;

/**
 * Created by gijspeters on 04-04-17.
 */
public class ProbabilityNetwork extends Network {

    public ProbabilityNetwork(Leg leg) {
        super(leg);
    }

    @Override
    public void addTransect(Transect transect, long deltaTimeSeconds) {
        for (Traversable t : transect.keySet()) {
            double notVisitP = Math.log(1 - transect.get(t)) * deltaTimeSeconds;
            add(t, notVisitP);
        }
    }

    @Override
    public ModelResultGraph getResultGraph() {
        ModelResultGraph resultGraph = new ModelResultGraph(getLeg());
        for (Traversable t : keySet()) {
            resultGraph.add(t, 1 - Math.exp(get(t)));
        }
        return resultGraph;
    }
}
