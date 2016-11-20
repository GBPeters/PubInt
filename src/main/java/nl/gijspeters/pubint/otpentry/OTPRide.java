package nl.gijspeters.pubint.otpentry;

import nl.gijspeters.pubint.graph.traversable.GenericRide;
import nl.gijspeters.pubint.graph.traversable.Ride;
import org.opentripplanner.routing.edgetype.TransitBoardAlight;
import org.opentripplanner.routing.graph.Edge;
import org.opentripplanner.routing.graph.Vertex;

import java.util.Collection;

/**
 * Created by gijspeters on 20-11-16.
 */
public class OTPRide extends GenericRide<OTPHop> {

    public Ride getRide() {
        return new Ride(this);
    }

    public Vertex getBoardVertex() {
        Collection<Edge> inEdges = first().getFromOTPVertex().getIncoming();
        Vertex boardVertex = null;
        for (Edge e : inEdges) {
            if (e instanceof TransitBoardAlight) {
                boardVertex = e.getFromVertex();
            }
        }
        return boardVertex;
    }

    public Vertex getAlightVertex() {
        Collection<Edge> outEdges = last().getToOTPVertex().getOutgoing();
        Vertex alightVertex = null;
        for (Edge e : outEdges) {
            if (e instanceof TransitBoardAlight) {
                alightVertex = e.getToVertex();
            }
        }
        return alightVertex;
    }

}
