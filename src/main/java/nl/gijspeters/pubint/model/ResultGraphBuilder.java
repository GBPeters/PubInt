package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.graph.state.PrismState;
import nl.gijspeters.pubint.graph.times.DepartureTimeComparator;
import nl.gijspeters.pubint.structure.Leg;

import java.util.*;

/**
 * Created by gijspeters on 04-04-17.
 */
public class ResultGraphBuilder {

    private final Leg leg;
    private ModelConfig config = new ModelConfig();

    private Set<Transect> transects = new HashSet<>();

    public ResultGraphBuilder(ModelConfig config, Leg leg) {
        this.leg = leg;
        this.config = config;
    }

    public ModelConfig getConfig() {
        return config;
    }

    public boolean add(Date t) {
        Transect transect = buildTransect(t);
        return transects.add(transect);
    }

    public void clear() {
        transects.clear();
    }

    public Transect buildTransect(Date t) {
        Transect transect = new Transect(leg, t);
        for (PrismState state : leg.getPrism().getStates()) {
            double p = state.getVisitProbability(leg.getOrigin().getDate(), leg.getDestination().getDate(), t,
                    config.getDispersion(), config.getTransition());
            if (p > 0) {
                transect.add(state.getTraversable(), p);
            }
        }
        return transect;
    }

    public void addAll() {
        clear();
        SortedSet<PrismState> stateQueue = new TreeSet<>(new DepartureTimeComparator());
        Set<PrismState> currentStates = new HashSet<>();
        stateQueue.addAll(leg.getPrism().getStates());
        for (long time = leg.getOrigin().getDate().getTime(); time <= leg.getDestination().getDate().getTime();
             time += config.getIntervalSeconds()) {
            Date t = new Date(time);
            Set<PrismState> switchStates = new HashSet<>();
            for (PrismState state : stateQueue) {
                if (state.getEarliestDeparture().getTime() < time) {
                    switchStates.add(state);
                } else {
                    break;
                }
            }
            stateQueue.removeAll(switchStates);
            currentStates.addAll(switchStates);
            switchStates.clear();
            Transect transect = new Transect(leg, t);
            for (PrismState state : currentStates) {
                if (time < state.getLatestArrival().getTime()) {
                    double p = state.getVisitProbability(leg.getOrigin().getDate(), leg.getDestination().getDate(), t,
                            config.getDispersion(), config.getTransition());
                    transect.add(state.getTraversable(), p);
                } else {
                    switchStates.add(state);
                }
            }
            currentStates.removeAll(switchStates);
            transects.add(transect);
        }
    }

    public Network buildNetwork(Network network) {
        for (Transect transect : transects) {
            network.addTransect(transect.getNormalisedTransect(), config.getIntervalSeconds());
        }
        return network;
    }

    public ProbabilityNetwork buildProbabilityNetwork() {
        ProbabilityNetwork network = new ProbabilityNetwork(leg);
        return (ProbabilityNetwork) buildNetwork(network);
    }

    public VisitTimeNetwork buildVisitTimeNetwork() {
        VisitTimeNetwork network = new VisitTimeNetwork(leg);
        return (VisitTimeNetwork) buildNetwork(network);
    }

}
