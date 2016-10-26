package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.structure.Anchor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gijspeters on 18-10-16.
 */
public class DateManipulator implements AnchorManipulator {

    @Override
    public void manipulate(Anchor anchor) {
        Date date = anchor.getDate();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        Calendar newCal = new GregorianCalendar();
        newCal.setTime(date);
        newCal.set(Calendar.YEAR, 2015);
        newCal.set(Calendar.MONTH, Calendar.OCTOBER);
        newCal.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK));
        Date newDate = newCal.getTime();
        anchor.setDate(newDate);
    }

}
