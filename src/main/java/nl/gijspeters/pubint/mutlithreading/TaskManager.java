package nl.gijspeters.pubint.mutlithreading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gijspeters on 01-03-17.
 */
public class TaskManager implements TaskFinishedCallback {

    private static Logger logger = LoggerFactory.getLogger(TaskManager.class);
    private TaskCursor cursor;

    private List<Task> runningTasks = new ArrayList<>();
    private int maxConcurrency;

    public TaskManager(TaskCursor cursor, int maxConcurrency) {
        this.cursor = cursor;
        this.maxConcurrency = maxConcurrency;
    }

    public void start() {
        int i = 0;
        while (i < maxConcurrency && cursor.hasTasksLeft()) {
            loadTask();
            i++;
        }
    }

    private void loadTask() {
        if (cursor.hasTasksLeft()) {
            Task t = cursor.getNextTask(this);
            runningTasks.add(t);
            new Thread(t).start();
        }
    }

    private synchronized void finishTask(Task t) {
        runningTasks.remove(t);
        String s = String.format("%s finished. %d tasks left.", t.toString(), cursor.tasksLeft() + runningTasks.size());
        logger.info(s);
        if (runningTasks.size() < maxConcurrency) {
            loadTask();
        }
    }

    @Override
    public void onFinished(Task t) {
        finishTask(t);
    }

    @Override
    public void onError(Task t, Exception e) {
        String s = String.format("%s on %s", e.getClass().getSimpleName(), t.toString());
        logger.error(s);
        e.printStackTrace();
        logger.info("Closing task.");
        finishTask(t);
    }
}
