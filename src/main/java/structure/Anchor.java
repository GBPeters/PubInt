package structure;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.Date;

/**
 * Generic Anchor class. Object is immutable after creation
 * <p>
 * Created by gijspeters on 03-10-16.
 */
public abstract class Anchor {

    private Coordinate coord;
    private Date date;
    private Agent agent;
    private User user;

    /**
     * Default constructor. Agent should contain the user, this is asserted.
     *
     * @param coord   the anchor coordinate
     * @param date    the anchor date and time
     * @param agent   the anchor agent
     * @param user    the anchor-specific user
     */
    public Anchor(Coordinate coord, Date date, Agent agent, User user) {
        assert (agent.contains(user));
        this.coord = coord;
        this.date = date;
        this.agent = agent;
        this.user = user;
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
     * @return The anchor's user
     */
    public User getUser() {
        return user;
    }
}
