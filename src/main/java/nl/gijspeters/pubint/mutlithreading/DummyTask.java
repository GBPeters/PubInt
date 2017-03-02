package nl.gijspeters.pubint.mutlithreading;

/**
 * Created by gijspeters on 01-03-17.
 */
public class DummyTask extends Task {

    int sleepSeconds;

    public DummyTask(TaskFinishedCallback callback, int sleepSeconds) {
        super(callback);
        this.sleepSeconds = sleepSeconds;
    }

    @Override
    protected void executeTask() throws Exception {
        Thread.sleep(sleepSeconds * 1000);
        System.out.println(String.format("Sleeped %d seconds!", sleepSeconds));
    }
}
