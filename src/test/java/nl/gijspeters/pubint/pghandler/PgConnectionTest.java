package nl.gijspeters.pubint.pghandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by gijspeters on 02-10-16.
 */
public class PgConnectionTest {

    private PgConnection c;
    @Before
    public void setUp() throws Exception {
        c = new PgConnection();
    }

    @After
    public void tearDown() throws Exception {
        c.close();
    }

    @Test
    public void select() throws Exception {
        String sql = "SELECT id FROM scraped_tweets LIMIT 1";
        ResultSet rs = c.select(sql);
        rs.next();
        assertEquals(33, rs.getInt("id"));
    }

    @Test
    public void execute() throws Exception {
        String sql = "UPDATE scraped_tweets SET tweet_name = 'AngelaAnna' WHERE id = 1";
        c.execute(sql, true);
        sql = "SELECT tweet_name FROM scraped_tweets WHERE id = 1";
        ResultSet rs = c.select(sql);
        rs.next();
        assertEquals("AngelaAnna", rs.getString("tweet_name"));
    }


    @Test
    public void commit() throws Exception {
        c.commit();
    }

}