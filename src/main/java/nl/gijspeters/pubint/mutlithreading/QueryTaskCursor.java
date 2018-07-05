package nl.gijspeters.pubint.mutlithreading;

import org.mongodb.morphia.query.Query;

import java.util.Iterator;

/**
 * Created by gijspeters on 11-04-17.
 */
public abstract class QueryTaskCursor<T> implements TaskCursor {

    protected Iterator<T> iterator;

    public QueryTaskCursor(Query<T> query) {
        iterator = query.iterator();
    }

    @Override
    public Task getNextTask(TaskFinishedCallback callback) {
        try {
            return createTask(iterator.next(), callback);
        } catch (RuntimeException e) {
            callback.onError(null, e);
            return null;
        }
    }

    protected abstract Task createTask(T t, TaskFinishedCallback callback);

    @Override
    public boolean hasTasksLeft() {
        return iterator.hasNext();
    }
}
