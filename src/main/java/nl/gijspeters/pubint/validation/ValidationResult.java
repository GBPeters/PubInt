package nl.gijspeters.pubint.validation;

import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.model.ModelConfig;
import nl.gijspeters.pubint.model.Transect;
import nl.gijspeters.pubint.structure.Anchor;
import org.geotools.feature.SchemaException;

public class ValidationResult {

    private ValidationLeg leg;
    private Anchor anchor;
    private Transect transect;
    private BasicEdge anchorEdge;
    private double anchorProbability;
    private ModelConfig config;

    public BasicEdge getAnchorEdge() {
        return anchorEdge;
    }

    public double getAnchorProbability() {
        return anchorProbability;
    }

    public ValidationResult(ValidationLeg leg, Anchor anchor, Transect transect, BasicEdge anchorEdge, double anchorProbability, ModelConfig config) throws SchemaException {
        this.leg = leg;
        this.anchor = anchor;
        this.transect = transect;
        this.anchorEdge = anchorEdge;
        this.anchorProbability = anchorProbability;
        this.config = config;
    }

    public ValidationLeg getLeg() {
        return leg;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public Transect getTransect() {
        return transect;
    }

    public ModelConfig getConfig() {
        return config;
    }
}



