package nl.gijspeters.pubint.graph;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.LineString;
import org.mongodb.morphia.geo.Point;

import java.util.ArrayList;

/**
 * Created by gijspeters on 16-10-16.
 */
@Entity("edge")
public class Edge {

    @Id
    private int edgeId;
    @Reference
    private Vertex fromVertex;
    @Reference
    private Vertex toVertex;
    private LineString geometry;

    public Edge() {
    }

    public Edge(int edgeId, Vertex fromVertex, Vertex toVertex, com.vividsolutions.jts.geom.LineString geom) {
        this.edgeId = edgeId;
        setFromVertex(fromVertex);
        setToVertex(toVertex);
        setGeometry(geom);
    }

    public int getEdgeId() {
        return edgeId;
    }

    public Vertex getFromVertex() {
        return fromVertex;
    }

    public Vertex getToVertex() {
        return toVertex;
    }

    public com.vividsolutions.jts.geom.LineString getGeometry() {
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        for (Point point : this.geometry.getCoordinates()) {
            coords.add(new Coordinate(point.getLongitude(), point.getLatitude()));
        }
        GeometryFactory geomf = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
        com.vividsolutions.jts.geom.LineString geom = geomf.createLineString(coords.toArray(new Coordinate[coords.size()]));
        return geom;
    }

    public void setFromVertex(Vertex fromVertex) {
        this.fromVertex = fromVertex;
    }

    public void setToVertex(Vertex toVertex) {
        this.toVertex = toVertex;
    }

    public void setGeometry(com.vividsolutions.jts.geom.LineString geometry) {
        ArrayList<Point> pointList = new ArrayList<Point>();
        for (Coordinate coord : geometry.getCoordinates()) {
            pointList.add(GeoJson.point(coord.y, coord.x));
        }
        Point[] points = pointList.toArray(new Point[pointList.size()]);
        this.geometry = GeoJson.lineString(points);
    }
}
