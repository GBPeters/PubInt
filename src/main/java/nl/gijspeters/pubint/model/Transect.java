package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.structure.Leg;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by gijspeters on 03-04-17.
 */
public class Transect extends ModelResultGraph {

    private Date t;

    public Transect(Leg leg, Date t) {
        super(leg);
        this.t = t;
    }

    public Transect getNormalisedTransect() {
        double sum = 0;
        for (double d : values()) {
            sum += d;
        }
        Transect transect = new Transect(getLeg(), t);
        for (Traversable t : keySet()) {
            transect.put(t, get(t) / sum);
        }
        return transect;
    }

    public Date getT() {
        return t;
    }

    public void setT(Date t) {
        this.t = t;
    }

    public int hashCode() {
        return new HashCodeBuilder(57, 75)
                .append(leg.hashCode())
                .append(t.hashCode())
                .toHashCode();
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof Transect) {
            Transect tr = (Transect) o;
            return getLeg().equals(tr.leg)
                    && getT().equals(tr.getT())
                    && super.equals(o);
        }
        return false;
    }
}
