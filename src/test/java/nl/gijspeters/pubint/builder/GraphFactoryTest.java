package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.app.Constants;
import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.state.OriginState;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.structure.Anchor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 18-10-16.
 */
public class GraphFactoryTest {

    private Anchor anchor;

    @Before
    public void setUp() throws Exception {
        OTPHandler.graphDir = Constants.OTP_DIR;
        anchor = MorphiaHandler.getInstance().getTestLeg().getOrigin();
        System.out.println(anchor.toString());
    }

    @Test
    public void getCone() throws Exception {
        GraphFactory gb = new GraphFactory(new DateManipulator());
        Cone<OriginState> cone = gb.makeOriginCone(anchor, 7200);
        assertTrue(cone.getStates().size() > 20);
    }

}