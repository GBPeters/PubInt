package nl.gijspeters.pubint.otpentry;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.app.App;
import org.junit.Before;
import org.junit.Test;
import org.opentripplanner.routing.spt.ShortestPathTree;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 02-10-16.
 */
public class OTPHandlerTest {

    private OTPHandler otp;
    @Before
    public void setUp() throws Exception {
        OTPHandler.graphDir = App.OTP_DIR;
        otp = OTPHandler.getInstance();
    }

    @Test
    public void getShortestPathTree() throws Exception {
        Coordinate coord = new Coordinate(4.889, 52.37);
        Date date = new Date(1449316800000L);
        System.out.println(date.toString());
        ShortestPathTree spt = otp.getShortestPathTree(coord, date, 14400, OTPHandler.ROUTE_MODES.FROM_ORIGIN);
        assertTrue(!spt.getAllStates().isEmpty());
    }

}