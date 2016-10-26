package nl.gijspeters.pubint.builder;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.structure.Agent;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.structure.User;
import nl.gijspeters.pubint.twitter.Tweet;
import nl.gijspeters.pubint.twitter.TwitterUser;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by gijspeters on 18-10-16.
 */
public class DateManipulatorTest {

    Anchor anchor;

    @Before
    public void setUp() throws Exception {
        Coordinate coord = new Coordinate(4, 52);
        Date date = new Date(1449316800000L);
        User user = new TwitterUser("A", new Agent());
        anchor = new Tweet(coord, date, user, "", 123);
    }

    @Test
    public void manipulate() throws Exception {
        Calendar oldCal = new GregorianCalendar();
        oldCal.setTime(anchor.getDate());
        new DateManipulator().manipulate(anchor);
        Calendar newCal = new GregorianCalendar();
        newCal.setTime(anchor.getDate());
        assertEquals(2015, newCal.get(Calendar.YEAR));
        assertEquals(Calendar.OCTOBER, newCal.get(Calendar.MONTH));
        assertEquals(oldCal.get(Calendar.DAY_OF_WEEK), newCal.get(Calendar.DAY_OF_WEEK));
        assertEquals(oldCal.get(Calendar.HOUR_OF_DAY), newCal.get(Calendar.HOUR_OF_DAY));
        assertEquals(oldCal.get(Calendar.MINUTE), newCal.get(Calendar.MINUTE));
        assertEquals(oldCal.get(Calendar.SECOND), newCal.get(Calendar.SECOND));

    }

}