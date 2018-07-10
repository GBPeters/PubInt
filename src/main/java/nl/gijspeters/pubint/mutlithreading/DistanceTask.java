package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.structure.Leg;

public class DistanceTask extends Task {

    Leg l;

    public DistanceTask(Leg leg, TaskFinishedCallback callback) {
        super(callback);
        l = leg;
    }

    @Override
    protected void executeTask() throws Exception {
        double d = l.getOrigin().getCoord().distance(l.getDestination().getCoord());
        l.setDistance(d);
        MorphiaHandler.getInstance().getDs().save(l);
    }
}
