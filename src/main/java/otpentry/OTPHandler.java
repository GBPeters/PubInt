package otpentry;

import com.vividsolutions.jts.geom.Coordinate;
import org.opentripplanner.routing.spt.ShortestPathTree;
import org.opentripplanner.scripting.api.OtpsEntryPoint;
import org.opentripplanner.scripting.api.OtpsRouter;
import org.opentripplanner.scripting.api.OtpsRoutingRequest;

import java.util.Date;

/**
 * Created by gijspeters on 02-10-16.
 *
 * This class creates an entry point to OTP and handles all requests to OTP.
 *
 */
public class OTPHandler {

    private OtpsEntryPoint otp;
    public enum ROUTE_MODES {FROM_ORIGIN, TO_DESTINATION}

    /**
     * Constructor with default graph directory (/var/otp/graphs/)
     *
     * @throws Exception
     */
    public OTPHandler() throws Exception {
        String[] args = {"--autoScan"};
        this.otp = OtpsEntryPoint.fromArgs(args);
    }

    /**
     * Constructor for custom graph directory
     * @param graphDir The graph directory
     * @throws Exception
     */
    public OTPHandler(String graphDir) throws Exception {
        String[] args = {"--graphs", graphDir, "--autoScan"};
        this.otp = OtpsEntryPoint.fromArgs(args);
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
    public ShortestPathTree getShortestPathTree(Coordinate coord, Date date, long maxTime, ROUTE_MODES mode) {
        OtpsRouter router = otp.getRouter();
        OtpsRoutingRequest req = otp.createRequest();
        req.setDateTime(date);
        req.setMaxTimeSec(maxTime);
        switch (mode) {
            case FROM_ORIGIN:
                req.setArriveBy(false);
                req.setOrigin(coord.y, coord.x);
            case TO_DESTINATION:
                req.setArriveBy(true);
                req.setDestination(coord.y, coord.x);
        }
        return router.plan(req).getSpt();
    }

}
