package nl.gijspeters.pubint.mutlithreading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by gijspeters on 01-03-17.
 */
public class TaskManager implements TaskFinishedCallback {

    private static Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private class TasksAlreadyStartedException extends RuntimeException {

        public TasksAlreadyStartedException() {
            super();
        }

    }

    private boolean started = false;
    private Queue<Task> taskQueue = new LinkedList<>();
    private List<Task> runningTasks = new ArrayList<>();
    private int maxConcurrency;

    public TaskManager(int maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }

    public boolean addTask(Task t) {
        if (!started) {
            return taskQueue.add(t);
        } else {
            throw new TasksAlreadyStartedException();
        }
    }

    public void start() {
        started = true;
        int i = 0;
        while (i < maxConcurrency && taskQueue.size() > 0) {
            loadTask();
            i++;
        }

    }

    private void loadTask() {
        if (taskQueue.size() > 0) {
            Task t = taskQueue.poll();
            runningTasks.add(t);
            new Thread(t).start();
        }
    }

    private synchronized void finishTask(Task t) {
        runningTasks.remove(t);
        String s = String.format("%s finished. %d tasks left.", t.toString(), taskQueue.size() + runningTasks.size());
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
