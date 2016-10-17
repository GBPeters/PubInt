package nl.gijspeters.pubint.structure;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.twitter.Tweet;
import nl.gijspeters.pubint.twitter.TwitterUser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by gijspeters on 17-10-16.
 */
public class TrajectoryTest {

    private Trajectory t;

    @Before
    public void setUp() throws Exception {
        Agent agent = new Agent();
        TwitterUser user1 = new TwitterUser("A", agent);
        TwitterUser user2 = new TwitterUser("B", agent);
        Date date1 = new Date(1000);
        Date date2 = new Date(2000);
        Anchor a1 = new Tweet(new Coordinate(1, 2), date1, user1, "", 123);
        Anchor a2 = new Tweet(new Coordinate(2, 1), date2, user2, "", 456);
        t = new Trajectory();
        t.add(a1);
        t.add(a2);
    }

    @Test
    public void first() throws Exception {
        assertEquals("A", t.first().getUser().getName());
    }

    @Test
    public void last() throws Exception {
        assertEquals("B", t.last().getUser().getName());
    }

    @Test
    public void add() throws Exception {
        Date date = new Date(500);
        User user = t.first().getUser();
        Tweet tweet = new Tweet(new Coordinate(1, 0), date, user, "", 789);
        t.add(tweet);
        assertEquals(500, t.first().getDate().getTime());
        Agent newa = new Agent();
        User newu = new TwitterUser("C", newa);
        Date date2 = new Date(700);
        tweet = new Tweet(new Coordinate(0, 1), date2, newu, "", 1000);
        try {
            t.add(tweet);
            fail();
        } catch (IllegalStateException e) {

        }
    }

    @Test
    public void addAll() throws Exception {
        Collection<Anchor> c = new ArrayList<Anchor>();
        Agent agent = t.getAgent();
        TwitterUser user1 = new TwitterUser("A", agent);
        TwitterUser user2 = new TwitterUser("B", agent);
        Date date1 = new Date(4000);
        Date date2 = new Date(3000);
        Anchor a1 = new Tweet(new Coordinate(1, 2), date1, user1, "", 123);
        Anchor a2 = new Tweet(new Coordinate(2, 1), date2, user2, "", 456);
        c.add(a1);
        c.add(a2);
        t.addAll(c);
        assertEquals(1000, t.first().getDate().getTime());
        assertEquals(4000, t.last().getDate().getTime());
        user1.setAgent(new Agent());
        try {
            t.addAll(c);
            fail();
        } catch (IllegalStateException e) {

        }
    }

}