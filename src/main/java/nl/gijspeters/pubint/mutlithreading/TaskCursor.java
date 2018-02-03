package nl.gijspeters.pubint.mutlithreading;

/**
 * Created by gijspeters on 11-04-17.
 */
public interface TaskCursor {

    Task getNextTask(TaskFinishedCallback callback);

    int tasksLeft();

    boolean hasTasksLeft();

}
