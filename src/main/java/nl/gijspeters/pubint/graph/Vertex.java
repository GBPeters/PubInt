package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by gijspeters on 16-10-16.
 *
 * A Class representing a Vertex on a Graph with Vertices and Traversables.
 * A Vertex does not contain information on connectivity, and only has a identifying label and a Coordinate
 */
@Entity("vertex")
public class Vertex {

    @Id
    private String vertexLabel;
    private Coordinate coord;

    public Vertex() {

    }

    public Vertex(String vertexLabel, Coordinate coord) {
        this.vertexLabel = vertexLabel;
        this.setCoord(coord);
    }

    public String getVertexLabel() {
        return vertexLabel;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    @Override
    public String toString() {
        return "Vertex <" + getVertexLabel() + ">";
    }

}
