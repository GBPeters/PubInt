package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Hop;
import nl.gijspeters.pubint.graph.traversable.LinkEdge;
import nl.gijspeters.pubint.otpentry.OTPHop;
import org.opentripplanner.routing.core.State;
import org.opentripplanner.routing.edgetype.AreaEdge;
import org.opentripplanner.routing.edgetype.PatternHop;
import org.opentripplanner.routing.edgetype.StreetEdge;
import org.opentripplanner.routing.edgetype.StreetTransitLink;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 20-11-16.
 */
public class TraversableFactory {

    public static final Set<Class> STREET_EDGE_CLASSES = new HashSet<>(Arrays.asList(new Class[]{StreetEdge.class, StreetTransitLink.class, AreaEdge.class}));

    public Vertex makeVertex(org.opentripplanner.routing.graph.Vertex v) {
        return new Vertex(v.getLabel(), v.getCoordinate());
    }

    public Edge makeEdge(org.opentripplanner.routing.graph.Edge e) {
        Edge edge;
        Vertex fromV = makeVertex(e.getFromVertex());
        Vertex toV = makeVertex(e.getToVertex());
        if (e.getGeometry() == null) {
            edge = new LinkEdge(e.getId(), fromV, toV);
        } else {
            boolean streetEdge = STREET_EDGE_CLASSES.contains(e.getClass());
            edge = new BasicEdge(e.getId(), fromV, toV, e.getGeometry(), streetEdge);
        }
        return edge;
    }

    public Hop makeHop(OTPHop h) {
        return new Hop(h.getTrip(), h.getDeparture(), h.getArrival(), h.getEdge());
    }

    public OTPHop makeOTPHop(State s) {
        assert s.getBackEdge() instanceof PatternHop;
        Date departure = new Date(s.getBackState().getTimeInMillis());
        Date arrival = new Date(s.getTimeInMillis());
        return new OTPHop(s.getBackTrip().getId(), departure, arrival, makeEdge(s.getBackEdge()), (PatternHop) s.getBackEdge());
    }

}
