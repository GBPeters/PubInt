package otpentry;

import com.vividsolutions.jts.geom.Coordinate;
import org.opentripplanner.routing.spt.ShortestPathTree;
import org.opentripplanner.scripting.api.OtpsEntryPoint;
import org.opentripplanner.scripting.api.OtpsRouter;
import org.opentripplanner.scripting.api.OtpsRoutingRequest;

import java.util.Date;

/**
 * Created by gijspeters on 02-10-16.
 */
public class OTPHandler {

    private OtpsEntryPoint otp;
    public enum ROUTE_MODES {FROM_ORIGIN, TO_DESTINATION}

    public OTPHandler() throws Exception {
        String[] args = {"--autoScan"};
        this.otp = OtpsEntryPoint.fromArgs(args);
    }

    public OTPHandler(String graphDir) throws Exception {
        String[] args = {"--graphs", graphDir, "--autoScan"};
        this.otp = OtpsEntryPoint.fromArgs(args);
    }

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
