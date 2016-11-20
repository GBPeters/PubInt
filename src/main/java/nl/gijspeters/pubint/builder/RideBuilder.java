package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.otpentry.OTPRide;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.onebusaway.gtfs.model.Trip;
import org.opentripplanner.routing.core.State;

import java.util.*;

/**
 * Created by gijspeters on 02-11-16.
 */
public class RideBuilder implements Set<State> {

    private final GraphFactory gf;

    private HashMap<Trip, Set<State>> sets = new HashMap<>();

    public RideBuilder(GraphFactory gf) {
        this.gf = gf;
    }

    public RideBuilder(GraphFactory gf, State s) {
        this(gf);
        add(s);
    }

    public RideBuilder(GraphFactory gf, Collection<State> states) {
        this(gf);
        addAll(states);
    }

    public Set<OTPRide> createRides() {
        Set<OTPRide> ridesets = new HashSet<>();
        for (Trip t : sets.keySet()) {
            OTPRide r = new OTPRide();
            for (State s : sets.get(t)) {
                r.add(gf.makeOTPHop(s));
            }
            ridesets.add(r);
        }
        return ridesets;
    }

    private Set<State> createStateSet() {
        Set<State> states = new HashSet<>();
        for (Set<State> set : sets.values()) {
            states.addAll(set);
        }
        return states;
    }

    @Override
    public int size() {
        return createStateSet().size();
    }

    @Override
    public boolean isEmpty() {
        return sets.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return createStateSet().contains(o);
    }

    @Override
    public Iterator<State> iterator() {
        return createStateSet().iterator();
    }

    @Override
    public Object[] toArray() {
        return createStateSet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return createStateSet().toArray(a);
    }

    @Override
    public boolean add(State state) {
        Trip p = state.getBackTrip();
        if (!sets.containsKey(p)) {
            sets.put(p, new TreeSet<>(new StateTimeComparator()));
        }
        return sets.get(p).add(state);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof State) {
            State s = (State) o;
            return sets.get(s.getBackTrip()).remove(s);
        } else return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return createStateSet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends State> c) {
        boolean added = false;
        for (State s : c) {
            if (add(s)) {
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean retained = false;
        for (Set<State> set : sets.values()) {
            if (set.retainAll(c)) {
                retained = true;
            }
        }
        return retained;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = false;
        for (Set<State> set : sets.values()) {
            if (set.removeAll(c)) {
                removed = true;
            }
        }
        return removed;
    }

    @Override
    public void clear() {
        sets.clear();
    }

    public int hashCode() {
        return new HashCodeBuilder(77, 4).append(sets.hashCode()).hashCode();
    }

    public boolean equals(Object o) {
        return createStateSet().contains(o);
    }
}
