package nl.gijspeters.pubint.validation;

import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.model.Transect;
import nl.gijspeters.pubint.structure.Anchor;
import org.geotools.feature.SchemaException;

public class ValidationResult {

    private ValidationLeg leg;
    private Anchor anchor;
    private Transect transect;
    private BasicEdge anchorEdge;
    private double anchorProbability;

    public BasicEdge getAnchorEdge() {
        return anchorEdge;
    }

    public double getAnchorProbability() {
        return anchorProbability;
    }

    public ValidationResult(ValidationLeg leg, Anchor anchor, Transect transect, BasicEdge anchorEdge, double anchorProbability) throws SchemaException {
        this.leg = leg;
        this.anchor = anchor;
        this.transect = transect;
        this.anchorEdge = anchorEdge;
        this.anchorProbability = anchorProbability;
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
}



