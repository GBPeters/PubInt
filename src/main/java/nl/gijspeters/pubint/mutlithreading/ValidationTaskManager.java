package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.validation.ValidatedExample;

import java.util.HashSet;
import java.util.Set;

public class ValidationTaskManager extends TaskManager {

    public Set<ValidatedExample> getValidations() {
        return validations;
    }

    private Set<ValidatedExample> validations = new HashSet<>();

    public ValidationTaskManager(TaskCursor cursor, int maxConcurrency) {
        super(cursor, maxConcurrency);
    }


    protected synchronized void finishTask(Task t) {
        if (t != null) {
            ValidateTask vt = (ValidateTask) t;
            validations.addAll(vt.getValidations());
        }
        super.finishTask(t);
    }

}
