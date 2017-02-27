package nl.gijspeters.pubint.export.csv.prism;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import nl.gijspeters.pubint.export.csv.CSVLine;
import nl.gijspeters.pubint.graph.state.BrownianState;
import nl.gijspeters.pubint.graph.state.MarkovState;
import nl.gijspeters.pubint.graph.state.PrismState;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Hop;
import nl.gijspeters.pubint.graph.traversable.Ride;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gijspeters on 27-02-17.
 */
public class StateLine extends CSVLine<PrismState> {

    public static final String[] KEYS = {
            "oid",
            "geom",
            "type",
            "eDepart",
            "lDepart",
            "eArrival",
            "lArrival"
    };

    @Override
    public String[] getKeys() {
        return KEYS;
    }

    public StateLine(PrismState typevar, int index) {
        super(typevar, index);
    }

    @Override
    protected void setData(PrismState data) {
        try {
            setValue("oid", index);
            if (data instanceof BrownianState) {
                BrownianState b = (BrownianState) data;
                if (b.getTraversable() instanceof BasicEdge) {
                    setValue("geom", ((BasicEdge) b.getTraversable()).getGeometry());
                }
            } else if (data instanceof MarkovState) {
                Ride r = ((MarkovState) data).getTraversable();
                List<LineString> linelist = new ArrayList<>();
                for (Hop h : r) {
                    Edge e = h.getEdge();
                    if (e instanceof BasicEdge) {
                        BasicEdge be = (BasicEdge) e;
                        linelist.add(be.getGeometry());
                    }
                }
                GeometryFactory gf = new GeometryFactory();
                setValue("geom", gf.createMultiLineString(linelist.toArray(new LineString[]{})));
            } else {
                throw new IllegalArgumentException();
            }
            setValue("eDepart", data.getEarliestDeparture());
            setValue("lDepart", data.getLatestDeparture());
            setValue("eArrival", data.getEarliestArrival());
            setValue("lArrival", data.getLatestArrival());
            setValue("type", data.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
