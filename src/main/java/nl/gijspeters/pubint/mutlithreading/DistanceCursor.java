package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.structure.Leg;
import org.mongodb.morphia.query.Query;

public class DistanceCursor extends QueryTaskCursor<Leg> {

    public DistanceCursor(Query<Leg> query) {
        super(query);
    }

    @Override
    protected Task createTask(Leg leg, TaskFinishedCallback callback) {
        return new DistanceTask(leg, callback);
    }
}
