package nl.gijspeters.pubint.mutlithreading;

import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.graph.factory.GraphFactory;
import nl.gijspeters.pubint.graph.state.DestinationState;
import nl.gijspeters.pubint.graph.state.OriginState;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPEntry;
import nl.gijspeters.pubint.structure.Leg;
import org.opentripplanner.routing.spt.ShortestPathTree;

/**
 * Created by gijspeters on 01-03-17.
 */
public class CreatePrismTask extends Task {

    private Leg leg;
    private GraphFactory gf;

    public CreatePrismTask(TaskFinishedCallback callback, Leg leg, GraphFactory factory) {
        super(callback);
        this.leg = leg;
        gf = factory;
    }

    @Override
    protected void executeTask() throws Exception {
        int maxTime = (int) (leg.getDeltaTime() / 1000);
        ShortestPathTree spt = gf.makeSPT(leg.getOrigin(), maxTime, OTPEntry.RouteMode.FROM_ORIGIN);
        Cone<OriginState> originCone = gf.makeOriginCone(leg.getOrigin(), spt, maxTime);
        spt = gf.makeSPT(leg.getDestination(), maxTime, OTPEntry.RouteMode.TO_DESTINATION);
        Cone<DestinationState> destinationCone = gf.makeDestinationCone(leg.getDestination(), spt, maxTime);
        Prism p = gf.getPrism(originCone, destinationCone);
        leg.setPrism(p);
        MorphiaHandler.getInstance().saveLeg(leg);
    }

    public String toString() {
        return "CreatePrismTask <" + leg.toString() + ">";
    }
}
