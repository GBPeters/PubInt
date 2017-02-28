package nl.gijspeters.pubint.graph.traversable;

import nl.gijspeters.pubint.config.Constants;
import nl.gijspeters.pubint.graph.factory.DateManipulator;
import nl.gijspeters.pubint.graph.factory.GraphFactory;
import nl.gijspeters.pubint.graph.factory.TraversableFactory;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.structure.Anchor;
import org.junit.Before;
import org.junit.Test;
import org.opentripplanner.routing.core.State;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 03-11-16.
 */
public class RideTest {

    private Hop hop1 = null;
    private Hop hop2 = null;
    private Hop wrongHop = null;

    private Ride ride = new Ride();

    @Before
    public void setUp() throws Exception {
        OTPHandler.graphDir = Constants.OTP_DIR;
        Anchor anchor = MorphiaHandler.getInstance().getTestLeg().getOrigin();
        GraphFactory gf = new GraphFactory(new DateManipulator());
        TraversableFactory tf = new TraversableFactory();
        Set<State> states = gf.makeTestTripStates(anchor);
        Iterator<State> iter = states.iterator();
        while (iter.hasNext() && hop2 == null) {
            State s = iter.next();
            if (s.getPreviousTrip() != null) {
                for (State s2 : states) {
                    if (s2.getBackTrip().equals(s.getPreviousTrip()) && !s.equals(s2)) {
                        hop1 = tf.makeHop(tf.makeOTPHop(s));
                        hop2 = tf.makeHop(tf.makeOTPHop(s2));
                        break;
                    }
                }
            }
        }
        iter.next();
        wrongHop = tf.makeHop(tf.makeOTPHop(iter.next()));
    }

    @Test
    public void add() throws Exception {
        ride.add(hop1);
        ride.add(hop2);
        ride.clear();
        ride.add(hop2);
        ride.add(hop1);
        assertEquals(2, ride.size());
        assertTrue(ride.contains(hop1));
        assertTrue(ride.contains(hop2));
    }

    @Test
    public void addWrong() throws Exception {
        try {
            ride.add(hop1);
            ride.add(wrongHop);
            throw new Exception();
        } catch (AssertionError e) {
            return;
        }
    }

    @Test
    public void getTrip() throws Exception {
        ride.add(hop1);
        ride.add(hop2);
        assertEquals(hop1.getTrip(), ride.getTrip());
        assertEquals(hop2.getTrip(), ride.getTrip());
    }

    @Test
    public void getSubRides() throws Exception {
        ride.add(hop1);
        ride.add(hop2);
        Set subRides = ride.getSubRides();
        assertEquals(3, subRides.size());
        assertTrue(subRides.contains(new Ride(hop1)));
        assertTrue(subRides.contains(new Ride(hop2)));
        assertTrue(subRides.contains(ride));
    }
}