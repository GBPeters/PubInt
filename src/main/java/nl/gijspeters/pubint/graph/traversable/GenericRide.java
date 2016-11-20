package nl.gijspeters.pubint.graph.traversable;

import nl.gijspeters.pubint.builder.HopTimeComparator;
import nl.gijspeters.pubint.graph.Vertex;
import org.onebusaway.gtfs.model.AgencyAndId;

import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

/**
 * Created by gijspeters on 20-11-16.
 */
public class GenericRide<T extends Hop> extends TreeSet<T> implements Ridable {

    private AgencyAndId trip;

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
            assert h.getTrip() == trip;
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
    public AgencyAndId getTrip() {
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
}
