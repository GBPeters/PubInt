package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 17-10-16.
 */
public class GraphTest {

    Graph graph;
    Edge outgoingEdge;
    Edge incomingEdge;
    Vertex testVertex;

    @Before
    public void setUp() throws Exception {
        testVertex = new Vertex("test", new Coordinate(0, 0));
        Vertex v1 = new Vertex("v1", new Coordinate(0, 1));
        Vertex v2 = new Vertex("v2", new Coordinate(1, 1));
        Vertex v3 = new Vertex("v3", new Coordinate(1, 0));
        GeometryFactory geomf = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate[] outgoingCoords = {testVertex.getCoord(), v1.getCoord()};
        Coordinate[] coords2 = {v1.getCoord(), v2.getCoord()};
        Coordinate[] coords3 = {v2.getCoord(), v3.getCoord()};
        Coordinate[] incomingCoords = {v3.getCoord(), testVertex.getCoord()};
        LineString outgoingLine = geomf.createLineString(outgoingCoords);
        LineString line2 = geomf.createLineString(coords2);
        LineString line3 = geomf.createLineString(coords3);
        LineString incomingLine = geomf.createLineString(incomingCoords);
        outgoingEdge = new Edge(1, testVertex, v1, outgoingLine);
        Edge edge2 = new Edge(2, v1, v2, line2);
        Edge edge3 = new Edge(3, v2, v3, line3);
        incomingEdge = new Edge(4, v3, testVertex, incomingLine);
        HashSet<Edge> edges = new HashSet<Edge>();
        edges.add(outgoingEdge);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(incomingEdge);
        graph = new Graph(edges);

    }

    @Test
    public void getEdges() throws Exception {
        ArrayList<Edge> edges = new ArrayList<Edge>(Arrays.asList(graph.getEdges()));
        assertEquals(4, edges.size());
        assertTrue(edges.contains(outgoingEdge));
        assertTrue(edges.contains(incomingEdge));
    }

    @Test
    public void getVertices() throws Exception {
        ArrayList<Vertex> vertices = new ArrayList<Vertex>(Arrays.asList(graph.getVertices()));
        assertEquals(4, vertices.size());
        assertTrue(vertices.contains(testVertex));
    }

    @Test
    public void getOutgoingEdges() throws Exception {
        assertEquals(1, graph.getOutgoingEdges(testVertex).size());
        assertTrue(graph.getOutgoingEdges(testVertex).contains(outgoingEdge));
    }

    @Test
    public void getIncomingEdges() throws Exception {
        assertEquals(1, graph.getIncomingEdges(testVertex).size());
        assertTrue(graph.getIncomingEdges(testVertex).contains(incomingEdge));
    }

    @Test
    public void getConnectedEdges() throws Exception {
        assertEquals(2, graph.getConnectedEdges(testVertex).size());
        assertTrue(graph.getConnectedEdges(testVertex).contains(outgoingEdge));
        assertTrue(graph.getConnectedEdges(testVertex).contains(incomingEdge));
    }

}