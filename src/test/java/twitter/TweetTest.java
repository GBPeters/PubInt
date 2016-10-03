package twitter;

import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Before;
import org.junit.Test;
import structure.Agent;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by gijspeters on 03-10-16.
 */
public class TweetTest {

    Tweet tweet;

    @Before
    public void setUp() throws Exception {
        TwitterUser user = new TwitterUser("Gijs", "123");
        Agent agent = new Agent(456);
        agent.add(user);
        Coordinate coord = new Coordinate(4.889, 52.37);
        Date date = new Date(1449316800000L);
        tweet = new Tweet(coord, date, agent, user, "bla");
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals("bla", tweet.getMessage());
    }

    @Test
    public void getLocation() throws Exception {
        assertEquals(4.889, tweet.getLocation().x, 0);
    }

    @Test
    public void getDate() throws Exception {
        assertEquals(1449316800000L, tweet.getDate().getTime());
    }

    @Test
    public void getAgent() throws Exception {
        assertEquals(456, tweet.getAgent().getAgentId());
    }

    @Test
    public void getUser() throws Exception {
        assertEquals("Gijs", tweet.getUser().getName());
    }

    @Test
    public void constructorTest() throws Exception {
        TwitterUser user = new TwitterUser("Gijs", "123");
        Agent agent = new Agent(456);
        Coordinate coord = new Coordinate(4.889, 52.37);
        Date date = new Date(1449316800000L);

        try {
            Tweet tweet = new Tweet(coord, date, agent, user, "bla");

        } catch (AssertionError e) {
            return;
        }
        fail();
    }

}