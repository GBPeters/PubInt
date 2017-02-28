package nl.gijspeters.pubint.graph.factory;

import nl.gijspeters.pubint.config.Constants;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.otpentry.OTPRide;
import nl.gijspeters.pubint.structure.Anchor;
import org.junit.Before;
import org.junit.Test;
import org.opentripplanner.routing.core.State;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 03-11-16.
 */
public class RideBuilderTest {

    GraphFactory gf = new GraphFactory(new DateManipulator());
    RideBuilder builder = new RideBuilder();

    @Before
    public void setUp() throws Exception {
        OTPHandler.graphDir = Constants.OTP_DIR;
        Anchor anchor = MorphiaHandler.getInstance().getTestLeg().getOrigin();
        Set<State> states = gf.makeTestTripStates(anchor);
        builder.addAll(states);
    }

    @Test
    public void createRides() throws Exception {
        Set<OTPRide> sets = builder.createRides();
        assertTrue(sets.size() < builder.size());
        assertTrue(sets.size() > 0);
    }

}