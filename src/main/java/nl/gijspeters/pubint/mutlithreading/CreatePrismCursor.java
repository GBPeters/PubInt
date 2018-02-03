package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.graph.factory.GraphFactory;
import nl.gijspeters.pubint.structure.Leg;
import org.mongodb.morphia.query.Query;

import java.util.List;

/**
 * Created by gijspeters on 11-04-17.
 */
public class CreatePrismCursor extends QueryTaskCursor<Leg> {

    private int otpCounter = 0;
    private List<GraphFactory> factories;

    public CreatePrismCursor(Query<Leg> query, List<GraphFactory> factories) {
        super(query);
        this.factories = factories;
    }

    @Override
    protected Task createTask(Leg leg, TaskFinishedCallback callback) {
        Task task = new CreatePrismTask(callback, leg, factories.get(otpCounter));
        otpCounter++;
        if (otpCounter >= factories.size()) {
            otpCounter = 0;
        }
        return task;
    }

}
