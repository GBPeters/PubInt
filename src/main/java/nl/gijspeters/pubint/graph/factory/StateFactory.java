package nl.gijspeters.pubint.graph.factory;

import nl.gijspeters.pubint.graph.state.*;

/**
 * Created by gijspeters on 21-11-16.
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

    public PrismState matchStates(OriginState originState, DestinationState destinationState) {
        if (originState.matches(destinationState)) {
            if (originState instanceof OriginUndirectedState) {
                OriginUndirectedState s = (OriginUndirectedState) originState;
                return new BrownianState(s.getTraversable(), s.getEarliestDeparture(), destinationState.getLatestArrival(), Math.min(s.getMinimalTraversalTime(), destinationState.getMinimalTraversalTime()));
            } else if (originState instanceof OriginTransitState) {
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
