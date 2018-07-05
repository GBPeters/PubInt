package nl.gijspeters.pubint.mutlithreading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gijspeters on 01-03-17.
 */
public class TaskManager implements TaskFinishedCallback {

    private TaskCursor cursor;
    private int processed = 0;
    private int completed = 0;
    private int errors = 0;

    private List<Task> runningTasks = new ArrayList<>();
    private int maxConcurrency;

    private boolean finished = false;

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

    public void join() {
        while (!finished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTask() {
        while (cursor.hasTasksLeft()) {
            Task t = cursor.getNextTask(this);
            if (t != null) {
                runningTasks.add(t);
                new Thread(t).start();
                return;
            }
            errors++;
            processed++;
        }
        System.out.println(String.format("Almost finished, %d left. %d completed, %d errors.", runningTasks.size(),
                completed, errors));
        finished = runningTasks.isEmpty();
    }

    protected synchronized void finishTask(Task t) {
        runningTasks.remove(t);
        processed++;
        completed++;
        String s = String.format("%s finished. %d tasks completed, %d errors.", t, completed, errors);
        System.out.println(s);
        if (runningTasks.size() < maxConcurrency) {
            loadTask();
        }

    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void onFinished(Task t) {
        finishTask(t);
    }

    @Override
    public void onError(Task t, Exception e) {
        processed++;
        errors++;
        String s;
        if (t == null) {
            s = String.format("%s on Task Init", e.getClass().getSimpleName());
        } else {
            s = String.format("%s on %s", e.getClass().getSimpleName(), t);
        }
        System.out.println(s);
        System.out.println("Closing task.");
        finishTask(t);
    }
}
