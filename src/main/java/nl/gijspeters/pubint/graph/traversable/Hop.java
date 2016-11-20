package nl.gijspeters.pubint.graph.traversable;

import nl.gijspeters.pubint.graph.Vertex;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.onebusaway.gtfs.model.AgencyAndId;

import java.util.Date;

/**
 * Created by gijspeters on 15-11-16.
 */
@Entity("hop")
public class Hop implements Ridable {

    @Id
    private EdgeAndTrip eat;
    private Date departure;
    private Date arrival;

    public Hop() {
    }

    public Hop(AgencyAndId trip, Date departure, Date arrival, Edge edge) {
        this.departure = departure;
        this.arrival = arrival;
        this.eat = new EdgeAndTrip(edge, trip);
    }

    @Override
    public AgencyAndId getTrip() {
        return eat.getTrip();
    }

    public void setTrip(AgencyAndId trip) {
        eat.setTrip(trip);
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
        return eat.getEdge();
    }

    public void setEdge(Edge edge) {
        this.eat.setEdge(edge);
    }

    public int hashCode() {
        return new HashCodeBuilder(7, 97)
                .append(eat.hashCode())
                .append(departure.hashCode())
                .append(arrival.hashCode())
                .toHashCode();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Hop) {
            Hop h = (Hop) o;
            return departure.equals(h.getDeparture())
                    && arrival.equals(h.getArrival())
                    && getEdge().equals(h.getEdge())
                    && getTrip().equals(h.getTrip());
        } else {
            return false;
        }
    }

    public String toString() {
        return "<Hop " + getTrip().toString() + " on " + getEdge().toString() + ">";
    }

}
