package nl.gijspeters.pubint.otpentry;

import com.vividsolutions.jts.geom.Coordinate;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.spt.ShortestPathTree;
import org.opentripplanner.scripting.api.OtpsEntryPoint;
import org.opentripplanner.scripting.api.OtpsRouter;
import org.opentripplanner.scripting.api.OtpsRoutingRequest;
import org.opentripplanner.scripting.api.OtpsSPT;

import java.util.Date;

/**
 * Created by gijspeters on 02-10-16.
 *
 * This class creates an entry point to OTP and handles all requests to OTP.
 *
 */
public class OTPHandler {

    private static OTPHandler ourInstance = new OTPHandler();

    public static String graphDir = "/var/otp/graphs";

    public static OTPHandler getInstance() throws Exception {
        if (ourInstance.otp == null) {
            restartOTP();
        }
        return ourInstance;
    }

    private OtpsEntryPoint otp;

    public static void restartOTP() throws Exception {
        String[] args = {"--graphs", graphDir, "--autoScan"};
        ourInstance.otp = OtpsEntryPoint.fromArgs(args);
    }

    public enum RouteMode {FROM_ORIGIN, TO_DESTINATION}

    /**
     * Constructor with default graph directory (/var/otp/graphs/)
     *
     * @throws Exception
     */
    private OTPHandler() {

    }

    /**
     * Creates a shortest path tree from a given origin or to a given destination
     * for the given amount of time
     *
     * @param coord The origin or destination coordinate
     * @param date  The date and time of departure or arrival
     * @param maxTime The maximum amount of time in seconds to calculate the shortest path tree for
     * @param mode Whether the shortest path tree is calculated from an origin, or to a destination
     * @return The calculated shortest path tree
     */
    public ShortestPathTree getShortestPathTree(Coordinate coord, Date date, long maxTime, RouteMode mode) throws Exception {
        OtpsRouter router = otp.getRouter();
        OtpsRoutingRequest req = otp.createRequest();
        switch (mode) {
            case TO_DESTINATION:
                req.setArriveBy(true);
                req.setDestination(coord.y, coord.x);
            case FROM_ORIGIN:
                req.setOrigin(coord.y, coord.x);
        }
        req.setDateTime(date);
        req.setMaxTimeSec(maxTime);
        OtpsSPT spt = router.plan(req);
        return spt.getSpt();
    }

    public Graph getGraph() {
        return otp.getRouter().getRouter().graph;
    }

}
