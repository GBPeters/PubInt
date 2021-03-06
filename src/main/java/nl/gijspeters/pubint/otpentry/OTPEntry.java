package nl.gijspeters.pubint.otpentry;

import com.vividsolutions.jts.geom.Coordinate;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.spt.ShortestPathTree;
import org.opentripplanner.scripting.api.OtpsEntryPoint;
import org.opentripplanner.scripting.api.OtpsRouter;
import org.opentripplanner.scripting.api.OtpsRoutingRequest;
import org.opentripplanner.scripting.api.OtpsSPT;

import java.util.Date;

import static nl.gijspeters.pubint.config.Constants.OTP_DIR;

/**
 * Created by gijspeters on 02-03-17.
 */
public class OTPEntry {


    public static String otpDir = OTP_DIR;

    public enum RouteMode {FROM_ORIGIN, TO_DESTINATION}

    private OtpsEntryPoint otp;

    void restartOTP() throws Exception {
        String[] args = {"--basePath", OTPEntry.otpDir, "--autoScan"};
        otp = OtpsEntryPoint.fromArgs(args);
    }

    /**
     * Constructor with default graph directory (/var/otp/graphs/)
     *
     * @throws Exception
     */
    OTPEntry() {
    }

    /**
     * Creates a shortest path tree from a given origin or to a given destination
     * for the given amount of time
     *
     * @param coord          The origin or destination coordinate
     * @param date           The date and time of departure or arrival
     * @param maxTimeSeconds The maximum amount of time in seconds to calculate the shortest path tree for
     * @param mode           Whether the shortest path tree is calculated from an origin, or to a destination
     * @return The calculated shortest path tree
     */
    public ShortestPathTree getShortestPathTree(Coordinate coord, Date date, int maxTimeSeconds, OTPEntry.RouteMode mode) throws Exception {
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
        req.setMaxTimeSec(maxTimeSeconds);
        OtpsSPT spt = router.plan(req);
        return spt.getSpt();
    }

    public boolean hasStarted() {
        return (otp != null);
    }

    public Graph getGraph() {
        return otp.getRouter().getRouter().graph;
    }
}
