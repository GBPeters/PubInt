package nl.gijspeters.pubint.twitter;

import nl.gijspeters.pubint.structure.Agent;
import nl.gijspeters.pubint.structure.SourceType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gijspeters on 03-10-16.
 */
public class TwitterUserTest {

    private TwitterUser user;

    @Before
    public void setUp() throws Exception {
        Agent agent = new Agent();
        user = new TwitterUser("Gijs", agent, 123);
    }

    @Test
    public void getSourceType() throws Exception {
        assertEquals(SourceType.TWITTER, user.getSourceType());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("Gijs", user.getName());
    }

    @Test
    public void getTwitterId() throws Exception {
        assertEquals(123, user.getTwitterId());
    }

}