package nl.gijspeters.pubint.graph.traversable;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import nl.gijspeters.pubint.graph.Vertex;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 17-10-16.
 */
public class EdgeTest {

    BasicEdge basicEdge;
    LineString line;

    @Before
    public void setUp() throws Exception {
        Vertex fromVertex = new Vertex("from", new Coordinate(10, 5));
        Vertex toVertex = new Vertex("to", new Coordinate(5, 10));
        Coordinate[] coords = {new Coordinate(10, 5), new Coordinate(5, 10)};
        GeometryFactory geomf = new GeometryFactory(new PrecisionModel(), 4326);
        this.line = geomf.createLineString(coords);
        this.basicEdge = new BasicEdge(123, fromVertex, toVertex, this.line, false);
    }

    @Test
    public void getEdgeId() throws Exception {
        assertEquals(123, this.basicEdge.getEdgeId());

    }

    @Test
    public void getFromVertex() throws Exception {
        assertEquals("from", this.basicEdge.getFromVertex().getVertexLabel());
    }

    @Test
    public void getToVertex() throws Exception {
        assertEquals("to", this.basicEdge.getToVertex().getVertexLabel());

    }

    @Test
    public void getGeometry() throws Exception {
        assertTrue(this.line.equalsExact(this.basicEdge.getGeometry()));

    }

    @Test
    public void setFromVertex() throws Exception {
        Vertex v = new Vertex("from2", new Coordinate(4, 8));
        this.basicEdge.setFromVertex(v);
        assertEquals("from2", this.basicEdge.getFromVertex().getVertexLabel());

    }

    @Test
    public void setToVertex() throws Exception {
        Vertex v = new Vertex("to2", new Coordinate(8, 4));
        this.basicEdge.setToVertex(v);
        assertEquals("to2", this.basicEdge.getToVertex().getVertexLabel());

    }

    @Test
    public void setGeometry() throws Exception {
        Coordinate[] coords = {new Coordinate(4, 8), new Coordinate(8, 4)};
        GeometryFactory geomf = new GeometryFactory(new PrecisionModel(), 4326);
        LineString line = geomf.createLineString(coords);
        this.basicEdge.setGeometry(line);
        assertTrue(line.equalsExact(this.basicEdge.getGeometry()));
    }

}