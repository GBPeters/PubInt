package structure;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.Date;

/**
 * Generic Anchor class. Object is immutable after creation
 * <p>
 * Created by gijspeters on 03-10-16.
 */
public class GenericAnchor implements Anchor {

    private Coordinate coord;
    private Date date;
    private Agent agent;
    private int userKey;

    /**
     * Default constructor.
     *
     * @param coord   the anchor coordinate
     * @param date    the anchor date and time
     * @param agent   the anchor agent
     * @param userKey the key to the anchor-specific user
     */
    public GenericAnchor(Coordinate coord, Date date, Agent agent, int userKey) {
        this.coord = coord;
        this.date = date;
        this.agent = agent;
        this.userKey = userKey;
    }

    /**
     * @return The anchor's location
     */
    public Coordinate getLocation() {
        return coord;
    }

    /**
     * @return The anchor's date and time
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return The anchor's agent
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * @return The key to the anchor-specific user in the corresponding agent
     */
    public int getUserKey() {
        return userKey;
    }

    /**
     * @return The anchor's user
     */
    public User getUser() {
        return agent.get(userKey);
    }
}
