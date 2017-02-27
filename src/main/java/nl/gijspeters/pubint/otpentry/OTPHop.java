package nl.gijspeters.pubint.otpentry;

import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Hop;
import nl.gijspeters.pubint.graph.traversable.Trip;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.opentripplanner.routing.edgetype.PatternHop;
import org.opentripplanner.routing.graph.Vertex;

import java.util.Date;

/**
 * Created by gijspeters on 20-11-16.
 */
public class OTPHop extends Hop {

    private Vertex fromOTPVertex;
    private Vertex toOTPVertex;

    public OTPHop() {
        super();
    }

    public OTPHop(AgencyAndId trip, Date departure, Date arrival, Edge edge, PatternHop pHop) {
        super(new Trip(trip), departure, arrival, edge);
        this.fromOTPVertex = pHop.getFromVertex();
        this.toOTPVertex = pHop.getToVertex();
    }

    public Vertex getFromOTPVertex() {
        return fromOTPVertex;
    }

    public void setFromOTPVertex(Vertex fromOTPVertex) {
        this.fromOTPVertex = fromOTPVertex;
    }

    public Vertex getToOTPVertex() {
        return toOTPVertex;
    }

    public void setToOTPVertex(Vertex toOTPVertex) {
        this.toOTPVertex = toOTPVertex;
    }

    public Hop getHop() {
        return new Hop(getTrip(), getDeparture(), getArrival(), getEdge());
    }

}
