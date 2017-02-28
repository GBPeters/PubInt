package nl.gijspeters.pubint.graph.factory;

import nl.gijspeters.pubint.graph.state.*;

/**
 * Created by gijspeters on 21-11-16.
 *
 * A StateFactory is used to match OriginStates and DestinationStates into PrismStates
 */
public class StateFactory {

    public class UnmatchableStatesException extends RuntimeException {

        public UnmatchableStatesException() {
            super();
        }

        public UnmatchableStatesException(String message) {
            super(message);
        }

    }

    /**
     * Match an OriginState and a DestinationState to a PrismState. Throws an exception if these states do not match
     *
     * @param originState      An OriginState
     * @param destinationState A DestinationState
     * @return A PrismStat
     */
    public PrismState matchStates(OriginState originState, DestinationState destinationState) {
        if (originState.matches(destinationState)) {
            if (originState instanceof OriginUndirectedState) {
                // If the states are UndirectedStates, create a BrownianState
                OriginUndirectedState s = (OriginUndirectedState) originState;
                BrownianState bs = new BrownianState(s.getTraversable(), s.getEarliestDeparture(), destinationState.getLatestArrival(), Math.min(s.getMinimalTraversalTime(), destinationState.getMinimalTraversalTime()));
                return bs;
            } else if (originState instanceof OriginTransitState) {
                // If the states are TransitStates, create a MarkovState
                OriginTransitState s = (OriginTransitState) originState;
                MarkovState ms = new MarkovState(s.getTraversable(), s.getEarliestDeparture(), s.getLatestDeparture(), s.getEarliestArrival(), destinationState.getLatestArrival());
                return ms;
            } else {
                throw new UnmatchableStatesException("State class not recognised");
            }
        } else {
            throw new UnmatchableStatesException("States do not match");
        }
    }

}
