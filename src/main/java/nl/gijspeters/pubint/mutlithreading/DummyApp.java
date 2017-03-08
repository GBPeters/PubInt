package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.graph.factory.DateManipulator;
import nl.gijspeters.pubint.graph.factory.GraphFactory;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPEntry;
import nl.gijspeters.pubint.structure.Leg;

/**
 * Created by gijspeters on 01-03-17.
 */
public class DummyApp {

    public static void main(String[] args) {
        TaskManager tm = new TaskManager(2);
        OTPEntry.otpDir = "/Users/gijspeters/otp";
        Leg testLeg = MorphiaHandler.getInstance().getTestLeg();
        GraphFactory gf = new GraphFactory(new DateManipulator());
        tm.addTask(new CreatePrismTask(tm, testLeg, gf));
        tm.addTask(new CreatePrismTask(tm, testLeg, gf));
        tm.addTask(new CreatePrismTask(tm, testLeg, gf));
        tm.addTask(new CreatePrismTask(tm, testLeg, gf));
        tm.addTask(new CreatePrismTask(tm, testLeg, gf));
        tm.start();
    }

}
