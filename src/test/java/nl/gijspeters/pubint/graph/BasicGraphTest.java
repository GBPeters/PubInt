package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Traversable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 17-10-16.
 */
public class BasicGraphTest {

    BasicGraph basicGraph;
    BasicEdge outgoingBasicEdge;
    BasicEdge incomingBasicEdge;
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
        outgoingBasicEdge = new BasicEdge(1, testVertex, v1, outgoingLine, false);
        BasicEdge basicEdge2 = new BasicEdge(2, v1, v2, line2, false);
        BasicEdge basicEdge3 = new BasicEdge(3, v2, v3, line3, false);
        incomingBasicEdge = new BasicEdge(4, v3, testVertex, incomingLine, false);
        HashSet<Edge> edges = new HashSet<>();
        edges.add(outgoingBasicEdge);
        edges.add(basicEdge2);
        edges.add(basicEdge3);
        edges.add(incomingBasicEdge);
        basicGraph = new BasicGraph("test", edges);

    }

    @Test
    public void getEdges() throws Exception {
        ArrayList<Traversable> basicEdges = new ArrayList<>(basicGraph.getTraversables());
        assertEquals(4, basicEdges.size());
        assertTrue(basicEdges.contains(outgoingBasicEdge));
        assertTrue(basicEdges.contains(incomingBasicEdge));
    }

    @Test
    public void getVertices() throws Exception {
        ArrayList<Vertex> vertices = new ArrayList<>(basicGraph.getVertices());
        assertEquals(4, vertices.size());
        assertTrue(vertices.contains(testVertex));
    }

    @Test
    public void getOutgoingEdges() throws Exception {
        assertEquals(1, basicGraph.getOutgoingTraversables(testVertex).size());
        assertTrue(basicGraph.getOutgoingTraversables(testVertex).contains(outgoingBasicEdge));
    }

    @Test
    public void getIncomingEdges() throws Exception {
        assertEquals(1, basicGraph.getIncomingTraversables(testVertex).size());
        assertTrue(basicGraph.getIncomingTraversables(testVertex).contains(incomingBasicEdge));
    }

    @Test
    public void getConnectedEdges() throws Exception {
        assertEquals(2, basicGraph.getConnectedTraversables(testVertex).size());
        assertTrue(basicGraph.getConnectedTraversables(testVertex).contains(outgoingBasicEdge));
        assertTrue(basicGraph.getConnectedTraversables(testVertex).contains(incomingBasicEdge));
    }

}