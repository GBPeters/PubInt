package nl.gijspeters.pubint.graph.traversable;

import nl.gijspeters.pubint.graph.Vertex;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

/**
 * Created by gijspeters on 15-11-16.
 *
 * A Ridable class representing a ride from one transit stop to the next, over a specific Edge.
 */
@Entity("hop")
public class Hop implements Ridable {

    @Id
    private String serialId;
    @Reference
    private Edge edge;
    private Trip trip;
    private Date departure;
    private Date arrival;

    public Hop() {
    }

    public Hop(Trip trip, Date departure, Date arrival, Edge edge) {
        if (departure.getTime() > arrival.getTime()) {
            throw new AssertionError(departure.toString() + " > " + arrival.toString());
        }
        this.departure = departure;
        this.arrival = arrival;
        this.trip = trip;
        this.edge = edge;
        setSerialId();
    }

    @Override
    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
        setSerialId();
    }

    @Override
    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    @Override
    public Date getArrival() {
        return arrival;
    }

    @Override
    public long getRideTime() {
        return getArrival().getTime() - getDeparture().getTime();
    }

    @Override
    public Vertex getFromVertex() {
        return getEdge().getFromVertex();
    }

    @Override
    public Vertex getToVertex() {
        return getEdge().getToVertex();
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
        setSerialId();
    }

    public int hashCode() {
        return new HashCodeBuilder(7, 97)
                .append(edge.hashCode())
                .append(trip.hashCode())
                .append(departure.hashCode())
                .append(arrival.hashCode())
                .toHashCode();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Hop) {
            Hop h = (Hop) o;
            return edge.equals(h.getEdge())
                    && trip.equals(h.getTrip())
                    && arrival.equals(h.getArrival())
                    && departure.equals(h.getDeparture());
        } else {
            return false;
        }
    }

    private void setSerialId() {
        serialId = String.valueOf(edge.getEdgeId()) + ":" + trip.getAgencyAndId().toString();
    }

    public String toString() {
        return "<Hop - " + getTrip().toString() + " on " + getEdge().toString() + ">";
    }

    public String getSerialId() {
        return serialId;
    }
}
