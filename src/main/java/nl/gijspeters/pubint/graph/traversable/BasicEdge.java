package nl.gijspeters.pubint.graph.traversable;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import nl.gijspeters.pubint.graph.Vertex;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.LineString;
import org.mongodb.morphia.geo.Point;

import java.util.ArrayList;

/**
 * Created by gijspeters on 16-10-16.
 *
 * An Edge class with a fixed geometry
 */
@Entity("edge")
public class BasicEdge extends Edge {

    private LineString geometry;

    public BasicEdge() {
        super();
    }

    public BasicEdge(int edgeId, Vertex fromVertex, Vertex toVertex, com.vividsolutions.jts.geom.LineString geom, boolean streetEdge) {
        super(edgeId, fromVertex, toVertex, streetEdge);
        setGeometry(geom);
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

    public void setGeometry(com.vividsolutions.jts.geom.LineString geometry) {
        ArrayList<Point> pointList = new ArrayList<Point>();
        for (Coordinate coord : geometry.getCoordinates()) {
            pointList.add(GeoJson.point(coord.y, coord.x));
        }
        Point[] points = pointList.toArray(new Point[pointList.size()]);
        this.geometry = GeoJson.lineString(points);
    }
}
