package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 18-10-16.
 */
public class CubeTest {

    Cube cube;
    State outgoingState;
    State incomingState;
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
        BasicEdge outgoingBasicEdge = new BasicEdge(1, testVertex, v1, outgoingLine);
        BasicEdge basicEdge2 = new BasicEdge(2, v1, v2, line2);
        BasicEdge basicEdge3 = new BasicEdge(3, v2, v3, line3);
        BasicEdge incomingBasicEdge = new BasicEdge(4, v3, testVertex, incomingLine);
        HashSet<State> states = new HashSet<>();
        Date d1 = new Date(1000);
        Date d2 = new Date(2000);
        Date d3 = new Date(3000);
        Date d4 = new Date(4000);
        outgoingState = new MarkovState(outgoingBasicEdge, d1, d2, d3, d4);
        State state2 = new MarkovState(basicEdge2, d1, d2, d3, d4);
        State state3 = new MarkovState(basicEdge3, d1, d2, d3, d4);
        incomingState = new MarkovState(incomingBasicEdge, d1, d2, d3, d4);
        states.add(outgoingState);
        states.add(state2);
        states.add(state3);
        states.add(incomingState);
        cube = new Cube(states);

    }

    @Test
    public void getEdges() throws Exception {
        ArrayList<Edge> basicEdges = new ArrayList<Edge>(Arrays.asList(cube.getEdges()));
        assertEquals(4, basicEdges.size());
        assertTrue(basicEdges.contains(outgoingState.getEdge()));
        assertTrue(basicEdges.contains(incomingState.getEdge()));
    }

    @Test
    public void getVertices() throws Exception {
        ArrayList<Vertex> vertices = new ArrayList<Vertex>(Arrays.asList(cube.getVertices()));
        assertEquals(4, vertices.size());
        assertTrue(vertices.contains(testVertex));
    }

    @Test
    public void getOutgoingEdges() throws Exception {
        assertEquals(1, cube.getOutgoingEdges(testVertex).size());
        assertTrue(cube.getOutgoingEdges(testVertex).contains(outgoingState.getEdge()));
    }

    @Test
    public void getIncomingEdges() throws Exception {
        assertEquals(1, cube.getIncomingEdges(testVertex).size());
        assertTrue(cube.getIncomingEdges(testVertex).contains(incomingState.getEdge()));
    }

    @Test
    public void getConnectedEdges() throws Exception {
        assertEquals(2, cube.getConnectedEdges(testVertex).size());
        assertTrue(cube.getConnectedEdges(testVertex).contains(outgoingState.getEdge()));
        assertTrue(cube.getConnectedEdges(testVertex).contains(incomingState.getEdge()));
    }


}