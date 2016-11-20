package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import nl.gijspeters.pubint.graph.state.BrownianState;
import nl.gijspeters.pubint.graph.state.PrismState;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Traversable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 18-10-16.
 */
public class CubeTest {

    Cube cube;
    PrismState outgoingState;
    PrismState incomingState;
    Vertex testVertex;
    Edge outgoingEdge;

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
        outgoingEdge = new BasicEdge(1, testVertex, v1, outgoingLine, false);
        BasicEdge basicEdge2 = new BasicEdge(2, v1, v2, line2, false);
        BasicEdge basicEdge3 = new BasicEdge(3, v2, v3, line3, false);
        BasicEdge incomingBasicEdge = new BasicEdge(4, v3, testVertex, incomingLine, false);
        HashSet<PrismState> states = new HashSet<>();
        Date d1 = new Date(1000);
        Date d2 = new Date(2000);
        Date d3 = new Date(3000);
        Date d4 = new Date(4000);
        outgoingState = new BrownianState(outgoingEdge, d1, d2, 1000);
        PrismState state2 = new BrownianState(basicEdge2, d2, d3, 1000);
        PrismState state3 = new BrownianState(basicEdge3, d3, d4, 1000);
        incomingState = new BrownianState(incomingBasicEdge, d3, d4, 1000);
        states.add(outgoingState);
        states.add(state2);
        states.add(state3);
        states.add(incomingState);
        cube = new Cube(states, 1);
    }

    @Test
    public void getEdges() throws Exception {
        List<Traversable> basicEdges = new ArrayList<>(cube.getTraversables());
        assertEquals(4, basicEdges.size());
        assertTrue(basicEdges.contains(outgoingState.getTraversable()));
        assertTrue(basicEdges.contains(incomingState.getTraversable()));
    }

    @Test
    public void getVertices() throws Exception {
        List<Vertex> vertices = new ArrayList<>(cube.getVertices());
        assertEquals(4, vertices.size());
        assertTrue(vertices.contains(testVertex));
    }

    @Test
    public void getOutgoingEdges() throws Exception {
        assertEquals(1, cube.getOutgoingTraversables(testVertex).size());
        assertTrue(cube.getOutgoingTraversables(testVertex).contains(outgoingState.getTraversable()));
    }

    @Test
    public void getIncomingEdges() throws Exception {
        assertEquals(1, cube.getIncomingTraversables(testVertex).size());
        assertTrue(cube.getIncomingTraversables(testVertex).contains(incomingState.getTraversable()));
    }

    @Test
    public void getConnectedEdges() throws Exception {
        assertEquals(2, cube.getConnectedTraversables(testVertex).size());
        assertTrue(cube.getConnectedTraversables(testVertex).contains(outgoingState.getTraversable()));
        assertTrue(cube.getConnectedTraversables(testVertex).contains(incomingState.getTraversable()));
    }

    @Test
    public void getTraversingStates() throws Exception {
        assertEquals(1, cube.getTraversingStates(outgoingEdge).size());
        assertTrue(cube.getTraversingStates(outgoingEdge).contains(outgoingState));
    }

    @Test
    public void getOutgoingStates() throws Exception {
        assertEquals(1, cube.getOutgoingStates(testVertex).size());
        assertTrue(cube.getOutgoingStates(testVertex).contains(outgoingState));
    }

    @Test
    public void getIncomingStates() throws Exception {
        assertEquals(1, cube.getIncomingStates(testVertex).size());
        assertTrue(cube.getIncomingStates(testVertex).contains(incomingState));
    }

    @Test
    public void getConnectedStates() throws Exception {
        assertEquals(2, cube.getConnectedStates(testVertex).size());
        assertTrue(cube.getConnectedStates(testVertex).contains(outgoingState));
        assertTrue(cube.getConnectedStates(testVertex).contains(incomingState));
    }

}