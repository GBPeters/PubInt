package nl.gijspeters.pubint.graph.traversable;

import nl.gijspeters.pubint.graph.Vertex;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

/**
 * Created by gijspeters on 20-11-16.
 *
 * Generic implementation for a Ride: An ordered set of Hops with the same Trip, representing a specific
 * transit trip from one stop to another stop.
 */
public class GenericRide<T extends Hop> extends TreeSet<T> implements Ridable {

    private Trip trip;

    public GenericRide() {
        super(new HopTimeComparator());
    }

    public GenericRide(T h) {
        this();
        add(h);
    }

    public GenericRide(Collection<? extends T> hops) {
        this();
        addAll(hops);
    }

    protected void assertStatesAreValid(T h) {
        assert h.getTrip() != null;
        if (trip == null) {
            trip = h.getTrip();
        } else {
            assert h.getTrip().equals(trip);
        }
    }

    protected void assertStatesAreValid(Collection<? extends T> hops) {
        GenericRide<T> testTrips = new GenericRide<>();
        for (T h : hops) {
            testTrips.add(h);
        }
    }

    @Override
    public boolean add(T hop) {
        assertStatesAreValid(hop);
        return super.add(hop);
    }

    @Override
    public boolean addAll(Collection<? extends T> hops) {
        assertStatesAreValid(hops);
        return super.addAll(hops);
    }

    @Override
    public Trip getTrip() {
        return trip;
    }

    @Override
    public Date getDeparture() {
        return first().getDeparture();
    }

    @Override
    public Date getArrival() {
        return last().getArrival();
    }

    @Override
    public long getRideTime() {
        return getArrival().getTime() - getDeparture().getTime();
    }

    @Override
    public Vertex getFromVertex() {
        return first().getEdge().getFromVertex();
    }

    @Override
    public Vertex getToVertex() {
        return last().getEdge().getToVertex();
    }

    @Override
    public Set<Edge> getEdges() {
        Set<Edge> edges = new HashSet<>();
        for (Hop h : this) {
            edges.add(h.getEdge());
        }
        return edges;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(47, 93);
        for (T t : this) {
            builder.append(t.hashCode());
        }
        return builder.toHashCode();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof GenericRide)) {
            return false;
        }
        GenericRide r = (GenericRide) o;
        if (r.size() != size()) {
            return false;
        }
        Iterator<T> thisi = iterator();
        Iterator thati = r.iterator();
        while (thisi.hasNext()) {
            if (!thisi.next().equals(thati.next())) {
                return false;
            }
        }
        return true;
    }

}
