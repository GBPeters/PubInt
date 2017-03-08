package nl.gijspeters.pubint.graph.factory;

import nl.gijspeters.pubint.config.Constants;
import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.graph.state.DestinationState;
import nl.gijspeters.pubint.graph.state.OriginState;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPEntry;
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
        OTPEntry.otpDir = Constants.OTP_DIR;
        testLeg = MorphiaHandler.getInstance().getTestLeg();
        originAnchor = testLeg.getOrigin();
        destinationAnchor = testLeg.getDestination();
    }

    @Test
    public void getOriginCone() throws Exception {
        System.out.println("From " + originAnchor.toString());
        Cone<OriginState> cone = gf.makeOriginCone(originAnchor, 7200);
        assertTrue(cone.getStates().size() > 2000);
    }

    @Test
    public void getDestinationCone() throws Exception {
        System.out.println("To " + destinationAnchor.toString());
        Cone<DestinationState> cone = gf.makeDestinationCone(destinationAnchor, 7200);
        assertTrue(cone.getStates().size() > 2000);
    }

    @Test
    public void getPrism() throws Exception {

        System.out.println("From " + originAnchor.toString() + " to " + destinationAnchor.toString());

        Prism prism = gf.getPrism(testLeg);
        assertTrue(prism.getStates().size() > 1000);
    }

}