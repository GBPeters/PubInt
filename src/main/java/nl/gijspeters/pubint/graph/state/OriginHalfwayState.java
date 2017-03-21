package nl.gijspeters.pubint.graph.state;

import nl.gijspeters.pubint.graph.traversable.Edge;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by gijspeters on 19-03-17.
 */
public class OriginHalfwayState extends OriginUndirectedState implements HalfwayState {

    public OriginHalfwayState() {
        super();
    }

    public OriginHalfwayState(Edge edge, Date earliestDeparture) {
        super(edge, earliestDeparture);
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
        if (o instanceof OriginHalfwayState) {
            OriginHalfwayState s = (OriginHalfwayState) o;
            return super.equals(s);
        }
        return false;
    }
}
