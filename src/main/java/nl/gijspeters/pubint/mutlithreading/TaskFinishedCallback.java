package nl.gijspeters.pubint.mutlithreading;

/**
 * Created by gijspeters on 01-03-17.
 */
public interface TaskFinishedCallback {

    void onFinished(Task t);

    void onError(Task t, Exception e);
}
