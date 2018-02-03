package nl.gijspeters.pubint.model;

import static nl.gijspeters.pubint.config.Constants.*;

/**
 * Created by gijspeters on 03-04-17.
 */
public class ModelConfig {

    private final double dispersion;
    private final double transition;
    private final int intervalSeconds;

    public ModelConfig() {
        dispersion = DISPERSION;
        transition = TRANSITION;
        intervalSeconds = INTERVAL_SECONDS;
    }

    public ModelConfig(double dispersion, double transition, int intervalSeconds) {
        this.dispersion = dispersion;
        this.transition = transition;
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
}
