package nl.gijspeters.pubint.structure;

import com.vividsolutions.jts.geom.Coordinate;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

/**
 * Generic Anchor class
 * <p>
 * Created by gijspeters on 03-10-16.
 */
@Entity("anchor")
public abstract class Anchor {

    @Id
    private ObjectId objectId;
    private Coordinate coord;
    private Date date;
    @Reference
    private User user;


    /**
     * Empty POJO constructor
     */
    public Anchor() {
    }

    /**
     * Default constructor. Agent should contain the user, this is not asserted.
     *
     * @param coord   the anchor coordinate
     * @param date    the anchor date and time
     * @param user    the anchor-specific user
     */
    public Anchor(Coordinate coord, Date date, User user) {
        this.setCoord(coord);
        this.setDate(date);
        this.setUser(user);
    }


    /**
     * @return The anchor's date and time
     */
    public Date getDate() {
        return date;
    }


    /**
     * @return The anchor's user
     */
    public User getUser() {
        return user;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return getDate().toString() + ": " + getUser().getName() + " at " + getCoord().toString();
    }
}
