package nl.gijspeters.pubint.mongohandler;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.structure.Agent;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.structure.Trajectory;
import nl.gijspeters.pubint.twitter.Tweet;
import nl.gijspeters.pubint.twitter.TwitterUser;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 16-10-16.
 */
public class MorphiaHandlerTest {
    MorphiaHandler con;

    @Before
    public void setUp() throws Exception {
        con = MorphiaHandler.getInstance();
    }

    @Test
    public void testSaveQueryRemove() {
        Agent agent = new Agent();
        TwitterUser user = new TwitterUser("Gijs", agent);
        Coordinate coord = new Coordinate(4.889, 52.37);
        Date date = new Date(1449316800000L);
        Tweet tweet = new Tweet(coord, date, user, "bla", 888);
        con.getDs().save(agent);
        con.getDs().save(user);
        con.getDs().save(tweet);
        ObjectId oid = tweet.getObjectId();
        Anchor anchor = con.getDs().get(Anchor.class, oid);
        assertEquals("Gijs", anchor.getUser().getName());
        assertEquals(4.889, anchor.getCoord().x, 0);
        con.getDs().delete(anchor);
        con.getDs().delete(agent);
        con.getDs().delete(user);
    }

    @Test
    public void getTrajectory() throws Exception {
        TwitterUser user = con.getDs().createQuery(TwitterUser.class).filter("twitterName = ", "AngelaAnna").get();
        Trajectory t = con.getTrajectory(user.getAgent());
        assertEquals(8, t.size());
        assertTrue(t.first().getDate().getTime() < t.last().getDate().getTime());
        SortedSet<Trajectory> ts = t.splitTrajectory(3600000);
        for (Trajectory tt : ts) {
            System.out.println(tt.getStartTime().toString() + " - " + tt.getEndTime().toString());
        }
    }

}