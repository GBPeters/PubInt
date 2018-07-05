package nl.gijspeters.pubint.model;

import java.util.HashSet;
import java.util.Set;

import static nl.gijspeters.pubint.config.Constants.*;

public class ConfigGrid {

    private Set<ModelConfig> configs = new HashSet<>();

    public ConfigGrid(Set<Double> dispersions, Set<Double> transitions, Set<Double> transitWeights,
                      Set<Integer> intervals) {
        fillConfigs(dispersions, transitions, transitWeights, intervals);
    }

    public ConfigGrid(Set<Double> dispersions, Set<Double> transitions, Set<Double> transitWeights) {
        Set<Integer> intervals = new HashSet<>();
        intervals.add(INTERVAL_SECONDS);
        fillConfigs(dispersions, transitions, transitWeights, intervals);
    }

    public ConfigGrid(Set<Double> dispersions, Set<Double> transitions) {
        Set<Integer> intervals = new HashSet<>();
        intervals.add(INTERVAL_SECONDS);
        Set<Double> transitWeights = new HashSet<>();
        transitWeights.add(TRANSIT_WEIGHT);
        fillConfigs(dispersions, transitions, transitWeights, intervals);
    }

    public ConfigGrid() {
        this(GRID_DISPERIONS, GRID_TRANSITIONS, GRID_TRANSIT_WEIGHTS);
    }

    private void fillConfigs(Set<Double> dispersions, Set<Double> transitions, Set<Double> transitWeights,
                             Set<Integer> intervals) {
        for (double dispersion : dispersions) {
            for (double transition : transitions) {
                for (double transitWeight : transitWeights) {
                    for (int interval : intervals) {
                        configs.add(new ModelConfig(dispersion, transition, transitWeight, interval));
                    }
                }
            }
        }
    }

    public Set<ModelConfig> getConfigs() {
        return new HashSet<>(configs);
    }

}
