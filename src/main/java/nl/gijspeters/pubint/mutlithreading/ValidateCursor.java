package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.validation.ValidationLeg;
import nl.gijspeters.pubint.validation.ValidationResultBuilder;
import org.mongodb.morphia.query.Query;

/**
 * Created by gijspeters on 11-04-17.
 */
public class ValidateCursor extends QueryTaskCursor<ValidationLeg> {

    private ValidationResultBuilder builder;

    public ValidateCursor(Query<ValidationLeg> query, ValidationResultBuilder builder) {
        super(query);
        this.builder = builder;
    }

    @Override
    protected Task createTask(ValidationLeg leg, TaskFinishedCallback callback) {
        return new ValidateTask(callback, leg, builder);
    }
}
