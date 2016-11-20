package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.app.Constants;
import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.state.DestinationState;
import nl.gijspeters.pubint.graph.state.OriginState;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.structure.Leg;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 18-10-16.
 */
public class GraphFactoryTest {

    private Leg testLeg;
    private Anchor originAnchor;
    private Anchor destinationAnchor;
    private GraphFactory gf = new GraphFactory(new DateManipulator());

    @Before
    public void setUp() throws Exception {
        OTPHandler.graphDir = Constants.OTP_DIR;
        testLeg = MorphiaHandler.getInstance().getTestLeg();
        originAnchor = testLeg.getOrigin();
        destinationAnchor = testLeg.getDestination();
        System.out.println(originAnchor.toString());
    }

    @Test
    public void getOriginCone() throws Exception {
        Cone<OriginState> cone = gf.makeOriginCone(originAnchor, 7200);
        assertTrue(cone.getStates().size() > 2000);
    }

    @Test
    public void getDestinationCone() throws Exception {
        Cone<DestinationState> cone = gf.makeDestinationCone(destinationAnchor, 7200);
        assertTrue(cone.getStates().size() > 2000);
    }

}