package nl.gijspeters.pubint.graph.times;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by gijspeters on 07-04-17.
 */
public abstract class TimeComparator<T> implements Comparator<T> {

    public int compareDates(Date d1, Date d2) {
        if (d1.getTime() < d2.getTime()) {
            return -1;
        } else if (d1.getTime() > d2.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }

}

