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
            add(t, transect.get(t));
        }
    }

    @Override
    public Double add(Traversable t, double p) {
        return (p >= getOrDefault(t, 0.0)) ? put(t, p) : get(t);
    }

    @Override
    public ModelResultGraph<Traversable> getResultGraph() {
        ModelResultGraph<Traversable> resultGraph = new ModelResultGraph<>(getLeg());
        for (Traversable t : keySet()) {
//            System.out.println(get(t));
            resultGraph.add(t, get(t));
        }
        return resultGraph;
    }
}
