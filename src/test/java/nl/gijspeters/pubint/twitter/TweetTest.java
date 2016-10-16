package nl.gijspeters.pubint.twitter;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.structure.Agent;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by gijspeters on 03-10-16.
 */
public class TweetTest {

    Tweet tweet;

    @Before
    public void setUp() throws Exception {
        Agent agent = new Agent();
        TwitterUser user = new TwitterUser("Gijs", agent);
        Coordinate coord = new Coordinate(4.889, 52.37);
        Date date = new Date(1449316800000L);
        tweet = new Tweet(coord, date, user, "bla", 999);
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals("bla", tweet.getMessage());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(4.889, tweet.getCoord().x, 0);
    }

    @Test
    public void getDate() throws Exception {
        assertEquals(1449316800000L, tweet.getDate().getTime());
    }

    @Test
    public void getAgent() throws Exception {
    }

    @Test
    public void getUser() throws Exception {
        assertEquals("Gijs", tweet.getUser().getName());
    }

}