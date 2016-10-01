package dbhandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.*;

/**
 * Created by gijspeters on 02-10-16.
 */
public class DBConnectionTest {

    private DBConnection c;
    @Before
    public void setUp() throws Exception {
        c = new DBConnection();
    }

    @After
    public void tearDown() throws Exception {
        c.close();
    }

    @Test
    public void select() throws Exception {
        String sql = "SELECT id FROM testje LIMIT 1";
        ResultSet rs = c.select(sql);
        rs.next();
        assertEquals(rs.getInt("id"), 1);
    }

    @Test
    public void execute() throws Exception {
        String sql = "UPDATE stations_ns SET korte_naam = 'Abcoude' WHERE id = 5";
        c.execute(sql, true);
        sql = "SELECT korte_naam FROM stations_ns WHERE id = 5";
        ResultSet rs = c.select(sql);
        rs.next();
        assertEquals(rs.getString("korte_naam"), "Abcoude");
    }


    @Test
    public void commit() throws Exception {
        c.commit();
    }

}