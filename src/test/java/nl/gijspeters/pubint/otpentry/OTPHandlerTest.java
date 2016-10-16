package nl.gijspeters.pubint.otpentry;

import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.opentripplanner.routing.spt.ShortestPathTree;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 02-10-16.
 */
public class OTPHandlerTest {

    public static final String GRAPH_DIR = "/Users/gijspeters/otp/";

    private OTPHandler otp;
    @Before
    public void setUp() throws Exception {
        otp = new OTPHandler(GRAPH_DIR);
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