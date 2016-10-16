package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 17-10-16.
 */
public class EdgeTest {

    Edge edge;
    LineString line;

    @Before
    public void setUp() throws Exception {
        Vertex fromVertex = new Vertex("from", new Coordinate(10, 5));
        Vertex toVertex = new Vertex("to", new Coordinate(5, 10));
        Coordinate[] coords = {new Coordinate(10, 5), new Coordinate(5, 10)};
        GeometryFactory geomf = new GeometryFactory(new PrecisionModel(), 4326);
        this.line = geomf.createLineString(coords);
        this.edge = new Edge(123, fromVertex, toVertex, this.line);
    }

    @Test
    public void getEdgeId() throws Exception {
        assertEquals(123, this.edge.getEdgeId());

    }

    @Test
    public void getFromVertex() throws Exception {
        assertEquals("from", this.edge.getFromVertex().getVertexLabel());
    }

    @Test
    public void getToVertex() throws Exception {
        assertEquals("to", this.edge.getToVertex().getVertexLabel());

    }

    @Test
    public void getGeometry() throws Exception {
        assertTrue(this.line.equalsExact(this.edge.getGeometry()));

    }

    @Test
    public void setFromVertex() throws Exception {
        Vertex v = new Vertex("from2", new Coordinate(4, 8));
        this.edge.setFromVertex(v);
        assertEquals("from2", this.edge.getFromVertex().getVertexLabel());

    }

    @Test
    public void setToVertex() throws Exception {
        Vertex v = new Vertex("to2", new Coordinate(8, 4));
        this.edge.setToVertex(v);
        assertEquals("to2", this.edge.getToVertex().getVertexLabel());

    }

    @Test
    public void setGeometry() throws Exception {
        Coordinate[] coords = {new Coordinate(4, 8), new Coordinate(8, 4)};
        GeometryFactory geomf = new GeometryFactory(new PrecisionModel(), 4326);
        LineString line = geomf.createLineString(coords);
        this.edge.setGeometry(line);
        assertTrue(line.equalsExact(this.edge.getGeometry()));
    }

}