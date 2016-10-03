package twitter;

import com.vividsolutions.jts.geom.Coordinate;
import structure.Agent;
import structure.GenericAnchor;

import java.util.Date;

/**
 * Created by gijspeters on 03-10-16.
 */
public class Tweet extends GenericAnchor {

    private String message;

    /**
     * Default constructor.
     *
     * @param coord   the anchor coordinate
     * @param date    the anchor date and time
     * @param agent   the anchor agent
     * @param userKey the key to the anchor-specific user
     * @param message the Twitter message
     */
    public Tweet(Coordinate coord, Date date, Agent agent, int userKey, String message) {
        super(coord, date, agent, userKey);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
