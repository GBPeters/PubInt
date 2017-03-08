package nl.gijspeters.pubint.otpentry;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.config.Constants;
import org.junit.Before;
import org.junit.Test;
import org.opentripplanner.routing.spt.ShortestPathTree;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 02-10-16.
 */
public class OTPHandlerTest {

    private OTPEntry otp;
    @Before
    public void setUp() throws Exception {
        OTPEntry.otpDir = Constants.OTP_DIR;
        otp = OTPHandler.getInstance();
    }

    @Test
    public void getShortestPathTree() throws Exception {
        Coordinate coord = new Coordinate(4.889, 52.37);
        Date date = new GregorianCalendar(2015, 10, 5, 12, 0, 0).getTime();
        ShortestPathTree spt = otp.getShortestPathTree(coord, date, 7200, OTPEntry.RouteMode.FROM_ORIGIN);
        assertTrue(spt.getAllStates().size() > 20);
        assertTrue(spt.getVertexCount() > 20);
    }

}