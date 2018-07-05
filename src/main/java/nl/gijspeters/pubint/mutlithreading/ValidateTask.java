package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.model.ModelResultGraph;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.validation.ValidatedExample;
import nl.gijspeters.pubint.validation.ValidationLeg;
import nl.gijspeters.pubint.validation.ValidationResult;
import nl.gijspeters.pubint.validation.ValidationResultBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 10-04-17.
 */
public class ValidateTask extends Task {

    private ValidationResultBuilder builder;
    private ValidationLeg leg;
    private Set<ValidatedExample> validations = new HashSet<>();

    public ValidateTask(TaskFinishedCallback callback, ValidationLeg leg, ValidationResultBuilder builder) {
        super(callback);
        this.leg = leg;
        this.builder = builder;
    }

    @Override
    protected void executeTask() throws Exception {
        Set<ValidationResult> results = builder.buildResult(leg);
        for (ValidationResult r : results) {
            MorphiaHandler.getInstance().saveValidationResult(r);
            ModelResultGraph<Edge> edgeProbs = r.getTransect().getEdgeProbabilities();
            for (Edge edge : edgeProbs.keySet()) {
                if (edge instanceof BasicEdge) {
                    BasicEdge be = (BasicEdge) edge;
                    validations.add(new ValidatedExample(be, be.equals(r.getAnchorEdge()), edgeProbs.get(edge)));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ValidateTask <" + leg.toString() + ">";
    }

    public Set<ValidatedExample> getValidations() {
        return validations;
    }
}
