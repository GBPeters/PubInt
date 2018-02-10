package nl.gijspeters.pubint.export.csv.resultgraph;

import com.vividsolutions.jts.io.WKTWriter;
import nl.gijspeters.pubint.export.csv.CSVLine;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;

/**
 * Created by gijspeters on 10-02-18.
 */
public class EdgeProbabilityLine extends CSVLine {

    Edge edge;
    double probability;

    public EdgeProbabilityLine(Edge edge, double probability) {
        this.edge = edge;
        this.probability = probability;
    }

    public static String[] KEYS = {
            "oid",
            "class",
            "geometry",
            "streetedge",
            "probability"
    };

    @Override
    public String[] getKeys() {
        return KEYS;
    }

    @Override
    public String getLineString() {
        String geom;
        if (edge instanceof BasicEdge) {
            geom = new WKTWriter().write(((BasicEdge) edge).getGeometry());
        } else {
            geom = "";
        }
        return edge.getEdgeId() + SEPARATOR
                + edge.getClass().getSimpleName() + SEPARATOR
                + geom + SEPARATOR
                + edge.isStreetEdge() + SEPARATOR
                + probability;
    }
}
