package nl.gijspeters.pubint.structure;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.twitter.Tweet;
import nl.gijspeters.pubint.twitter.TwitterUser;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by gijspeters on 17-10-16.
 */
public class LegTest {

    private Leg leg;

    @Before
    public void setUp() throws Exception {
        Agent agent = new Agent();
        User user = new TwitterUser("A", agent);
        Date d1 = new Date(1000);
        Date d2 = new Date(2000);
        Anchor a1 = new Tweet(new Coordinate(1, 0), d1, user, "", 123);
        Anchor a2 = new Tweet(new Coordinate(0, 1), d2, user, "", 123);
        leg = new Leg(a1, a2);

    }

    @Test
    public void getTimeDelta() {
        assertEquals(1000, leg.getDeltaTime());
    }

    @Test
    public void setOrigin() throws Exception {
        Agent agent = new Agent();
        User user = new TwitterUser("A", agent);
        Anchor origin = leg.getOrigin();
        origin.setUser(user);
        try {
            leg.setOrigin(origin);
            fail();
        } catch (IllegalStateException e) {

        }
        origin.setUser(leg.getDestination().getUser());
        leg.setOrigin(origin);
        origin.setDate(new Date(3000));
        try {
            leg.setOrigin(origin);
            fail();
        } catch (IllegalStateException e) {

        }

    }

}