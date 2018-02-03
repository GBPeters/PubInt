package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.model.ModelConfig;
import nl.gijspeters.pubint.structure.Leg;
import org.mongodb.morphia.query.Query;

/**
 * Created by gijspeters on 11-04-17.
 */
public class CreateNetworkCursor extends QueryTaskCursor<Leg> {

    private ModelConfig config;

    public CreateNetworkCursor(Query<Leg> query, ModelConfig config) {
        super(query);
        this.config = config;
    }

    @Override
    protected Task createTask(Leg leg, TaskFinishedCallback callback) {
        return new CreateNetworkTask(callback, leg, config);
    }
}
