package twitter;

import com.vividsolutions.jts.geom.Coordinate;
import structure.Agent;
import structure.Anchor;
import structure.User;

import java.util.Date;

/**
 * Class representing a twitter message.
 * For now the only extension of Anchor is the 'message' field,
 * containing the actual message
 *
 * Created by gijspeters on 03-10-16.
 */
public class Tweet extends Anchor {

    private String message;

    /**
     * Default constructor.
     *
     * @param coord   the anchor coordinate
     * @param date    the anchor date and time
     * @param agent   the anchor agent
     * @param user    the anchor-specific user
     * @param message the Twitter message
     */
    public Tweet(Coordinate coord, Date date, Agent agent, User user, String message) {
        super(coord, date, agent, user);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
