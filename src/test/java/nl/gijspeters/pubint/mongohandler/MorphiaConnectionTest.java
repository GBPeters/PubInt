package nl.gijspeters.pubint.mongohandler;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.structure.Agent;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.twitter.Tweet;
import nl.gijspeters.pubint.twitter.TwitterUser;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by gijspeters on 16-10-16.
 */
public class MorphiaConnectionTest {
    MorphiaConnection con;

    @Before
    public void setUp() throws Exception {
        con = new MorphiaConnection();
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

}