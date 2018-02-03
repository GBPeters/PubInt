package nl.gijspeters.pubint.mutlithreading;

import org.mongodb.morphia.query.Query;

import java.util.Iterator;

/**
 * Created by gijspeters on 11-04-17.
 */
public abstract class QueryTaskCursor<T> implements TaskCursor {

    protected int tasksLeft;
    protected Iterator<T> iterator;

    public QueryTaskCursor(Query<T> query) {
        Query q = query.cloneQuery();
        Iterator counter = q.iterator();
        while (counter.hasNext()) {
            counter.next();
            tasksLeft++;
        }
        iterator = query.iterator();
    }

    @Override
    public Task getNextTask(TaskFinishedCallback callback) {
        tasksLeft--;
        return createTask(iterator.next(), callback);
    }

    protected abstract Task createTask(T t, TaskFinishedCallback callback);

    @Override
    public int tasksLeft() {
        return tasksLeft;
    }

    @Override
    public boolean hasTasksLeft() {
        return tasksLeft > 0;
    }
}
