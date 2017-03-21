package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by gijspeters on 19-03-17.
 */
public class DestinationHalfwayState extends DestinationUndirectedState implements HalfwayState {

    public DestinationHalfwayState() {
        super();
    }

    public DestinationHalfwayState(Edge edge, Date latestArrival) {
        super(edge, latestArrival);
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 3)
                .append(super.hashCode())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof DestinationHalfwayState) {
            DestinationHalfwayState s = (DestinationHalfwayState) o;
            return super.equals(s);
        }
        return false;
    }
}
