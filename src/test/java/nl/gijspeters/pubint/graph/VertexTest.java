package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gijspeters on 16-10-16.
 */
public class VertexTest {

    Vertex vertex;

    @Before
    public void setUp() throws Exception {
        vertex = new Vertex("bla", new Coordinate(10, 5));
    }

    @Test
    public void getCoord() throws Exception {
        assertEquals(10, vertex.getCoord().x, 0);
    }

    @Test
    public void setCoord() throws Exception {
        vertex.setCoord(new Coordinate(5, 10));
        assertEquals(5, vertex.getCoord().x, 0);
    }

}