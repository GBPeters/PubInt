package twitter;

import org.junit.Before;
import org.junit.Test;
import structure.SourceType;

import static org.junit.Assert.assertEquals;

/**
 * Created by gijspeters on 03-10-16.
 */
public class TwitterUserTest {

    private TwitterUser user;

    @Before
    public void setUp() throws Exception {
        user = new TwitterUser("Gijs", "123");
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
    public void getId() throws Exception {
        assertEquals("123", user.getId());
    }

}