package nl.gijspeters.pubint.graph.traversable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Embedded;
import org.onebusaway.gtfs.model.AgencyAndId;

/**
 * Created by gijspeters on 19-11-16.
 */

@Embedded
public class EdgeAndTrip {

    private Edge edge;
    private AgencyAndId trip;

    public EdgeAndTrip() {

    }

    public EdgeAndTrip(Edge edge, AgencyAndId trip) {
        this.edge = edge;
        this.trip = trip;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public AgencyAndId getTrip() {
        return trip;
    }

    public void setTrip(AgencyAndId trip) {
        this.trip = trip;
    }

    public int hashCode() {
        return new HashCodeBuilder(5, 5)
                .append(edge.getEdgeId())
                .append(trip.hashCode())
                .toHashCode();
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof EdgeAndTrip) {
            EdgeAndTrip eat = (EdgeAndTrip) o;
            if (edge.equals(eat.getEdge()) && trip.equals(eat.getTrip())) {
                return true;
            }
        }
        return false;
    }
}
