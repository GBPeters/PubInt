package structure;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.Date;

/**
 * An interface defining the attributes of an anchor, i.e. a geotagged twitter message
 * or Instagram photo
 * <p>
 * Created by gijspeters on 03-10-16.
 */
public interface Anchor {

    /**
     * @return The anchor's location
     */
    Coordinate getLocation();

    /**
     * @return The anchor's date and time
     */
    Date getDate();

    /**
     * @return The anchor's agent
     */
    Agent getAgent();

    /**
     * @return The key to the anchor-specific user in the corresponding agent
     */
    int getUserKey();

    /**
     * @return The anchor's user
     */
    User getUser();

}
