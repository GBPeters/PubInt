package nl.gijspeters.pubint.mutlithreading;

/**
 * Created by gijspeters on 01-03-17.
 */
public abstract class Task implements Runnable {

    TaskFinishedCallback callback;

    public Task(TaskFinishedCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        boolean success = false;
        try {
            executeTask();
            success = true;
        } catch (Exception e) {
            callback.onError(this, e);
        }
        if (success) {
            callback.onFinished(this);
        }
    }

    protected abstract void executeTask() throws Exception;

    public String toString() {
        return "Task <" + this.getClass().getName() + ">";
    }

}
