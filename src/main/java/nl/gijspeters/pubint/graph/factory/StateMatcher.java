package nl.gijspeters.pubint.graph.factory;

import nl.gijspeters.pubint.graph.state.*;
import nl.gijspeters.pubint.graph.traversable.Traversable;

import java.util.*;

/**
 * Created by gijspeters on 21-03-17.
 */
public class StateMatcher implements Set<DestinationState> {

    private Map<Traversable, DestinationState> destinationStates = new HashMap<>();

    public StateMatcher() {
    }

    public PrismState matchStates(OriginState os) {
        DestinationState ds = destinationStates.get(os.getTraversable());
        if (os instanceof TransitState && ds != null) {
            OriginTransitState o = (OriginTransitState) os;
            DestinationTransitState d = (DestinationTransitState) ds;
            return new MarkovState(o.getTraversable(), o.getEarliestDeparture(), o.getLatestDeparture(),
                    o.getEarliestArrival(), d.getLatestArrival());
        } else if (os instanceof UndirectedState) {
            if (ds != null) {
                if (os instanceof OriginTraversedUndirectedState && ds instanceof DestinationTraversedUndirectedState
                        && os.getTraversable().equals(ds.getTraversable())) {
                    OriginTraversedUndirectedState otus = (OriginTraversedUndirectedState) os;
                    DestinationTraversedUndirectedState dtus = (DestinationTraversedUndirectedState) ds;
                    if (otus.getEarliestDeparture().getTime() <= dtus.getLatestDeparture().getTime()
                            && otus.getEarliestArrival().getTime() <= dtus.getLatestArrival().getTime()) {
                        return new BrownianTraversedState(otus.getTraversable(), otus.getEarliestDeparture(),
                                dtus.getLatestArrival(), Math.min(otus.getMinimalTraversalTime(),
                                dtus.getMinimalTraversalTime()));
                    }
                }
            }
            OriginUndirectedState ous = (OriginUndirectedState) os;
            DestinationUndirectedState dus =
                    (DestinationUndirectedState) destinationStates.get(ous.getTraversable().reverse());
            if (dus != null) {
                if (ous.getEarliestDeparture().getTime() <= dus.getLatestArrival().getTime()) {
                    return new BrownianUturnState(ous.getTraversable(), ous.getEarliestDeparture(), dus.getLatestArrival());
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return destinationStates.size();
    }

    @Override
    public boolean isEmpty() {
        return destinationStates.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return destinationStates.containsValue(o);
    }

    @Override
    public Iterator<DestinationState> iterator() {
        return destinationStates.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return destinationStates.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return destinationStates.values().toArray(a);
    }

    @Override
    public boolean add(DestinationState destinationState) {
        destinationStates.put(destinationState.getTraversable(), destinationState);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o != null) {
            if (o instanceof DestinationState) {
                DestinationState d = (DestinationState) o;
                return destinationStates.remove(d.getTraversable(), d);
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return destinationStates.values().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends DestinationState> c) {
        boolean s = false;
        for (DestinationState ds : c) {
            s = add(ds) || s;
        }
        return s;
    }

    private Set<Traversable> createKeySet(Collection<?> c) {
        Set<Traversable> keys = new HashSet<>();
        for (Object o : c) {
            if (destinationStates.values().contains(o)) {
                DestinationState d = (DestinationState) o;
                keys.add(d.getTraversable());
            }
        }
        return keys;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean s = false;
        Set<Traversable> keys = createKeySet(c);
        for (Traversable t : destinationStates.keySet()) {
            if (!keys.contains(t)) {
                destinationStates.remove(t);
                s = true;
            }
        }
        return s;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean s = false;
        Set<Traversable> keys = createKeySet(c);
        for (Traversable t : keys) {
            destinationStates.remove(t);
            s = true;
        }
        return s;
    }

    @Override
    public void clear() {
        destinationStates.clear();
    }
}
