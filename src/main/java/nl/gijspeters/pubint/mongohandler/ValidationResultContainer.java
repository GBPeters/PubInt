package nl.gijspeters.pubint.mongohandler;


import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.model.ModelConfig;
import nl.gijspeters.pubint.model.Transect;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.validation.ValidationLeg;
import nl.gijspeters.pubint.validation.ValidationResult;
import org.bson.types.ObjectId;
import org.geotools.feature.SchemaException;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.HashSet;
import java.util.Set;

@Entity("validationresults")
public class ValidationResultContainer {

    @Id
    private ObjectId oid = new ObjectId();

    @Embedded
    private Set<TraversableContainer> tcontainers = new HashSet<>();

    @Reference
    private ValidationLeg leg;

    @Reference
    private Anchor anchor;

    @Reference
    private BasicEdge anchorEdge;

    private double anchorProbability;

    private ModelConfig config;

    public ValidationResultContainer(ValidationResult result) {
        leg = result.getLeg();
        Transect transect = result.getTransect();
        for (Traversable t : transect.keySet()) {
            tcontainers.add(new TraversableContainer(t, transect.get(t)));
        }
        anchor = result.getAnchor();
        anchorEdge = result.getAnchorEdge();
        anchorProbability = result.getAnchorProbability();
        config = result.getConfig();
    }

    public ObjectId getOid() {
        return oid;
    }

    public ValidationResult getValidationResult() {
        Transect transect = new Transect(leg, anchor.getDate());
        for (TraversableContainer t : tcontainers) {
            transect.put(t.getTraversable(), t.getP());
        }
        try {
            return new ValidationResult(leg, anchor, transect, anchorEdge, anchorProbability, config);
        } catch (SchemaException e) {
            e.printStackTrace();
            return null;
        }
    }

}
