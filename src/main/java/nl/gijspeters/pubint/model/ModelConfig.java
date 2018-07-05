package nl.gijspeters.pubint.model;

import static nl.gijspeters.pubint.config.Constants.*;

/**
 * Created by gijspeters on 03-04-17.
 */
public class ModelConfig {

    private double dispersion;
    private double transition;
    private double transitWeight;
    private int intervalSeconds;

    public ModelConfig() {
        this(DISPERSION, TRANSITION, TRANSIT_WEIGHT, INTERVAL_SECONDS);
    }

    public ModelConfig(double dispersion, double transition, int intervalSeconds) {
        this(dispersion, transition, TRANSIT_WEIGHT, intervalSeconds);
    }

    public ModelConfig(double dispersion, double transition, double transitWeight, int intervalSeconds) {
        this.dispersion = dispersion;
        this.transition = transition;
        this.transitWeight = transitWeight;
        this.intervalSeconds = intervalSeconds;
    }

    public double getDispersion() {
        return dispersion;
    }

    public double getTransition() {
        return transition;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public double getTransitWeight() {
        return transitWeight;
    }
}
