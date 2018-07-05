package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.graph.state.PrismState;
import nl.gijspeters.pubint.structure.Leg;

import java.util.Date;

public class TransectBuilder {

    private final Leg leg;

    private final ModelConfig config;

    public TransectBuilder(Leg leg, ModelConfig modelConfig) {
        this.leg = leg;
        this.config = modelConfig;
    }

    public Leg getLeg() {
        return leg;
    }

    public ModelConfig getConfig() {
        return config;
    }

    public Transect buildTransect(Date t) {
        Transect transect = new Transect(leg, t);
        for (PrismState state : leg.getPrism().getStates()) {
            double p = state.getVisitProbability(leg.getOrigin().getDate(), leg.getDestination().getDate(), t,
                    config.getDispersion(), config.getTransition(), config.getTransitWeight());
            if (p > 0) {
                transect.add(state.getTraversable(), p);
            }
        }
        return transect;
    }

}
