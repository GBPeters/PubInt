package nl.gijspeters.pubint.twitter;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.structure.User;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Class representing a nl.gijspeters.pubint.twitter message.
 * For now the only extension of Anchor is the 'message' field,
 * containing the actual message
 *
 * Created by gijspeters on 03-10-16.
 */
@Entity("Anchor")
public class Tweet extends Anchor {

    private String message;
    private long tweetId;


    public Tweet() {
        super();
    }

    /**
     * Default constructor.
     *
     * @param coord   the anchor coordinate
     * @param date    the anchor date and time
     * @param user    the anchor-specific user
     * @param message the Twitter message
     */
    public Tweet(Coordinate coord, Date date, User user, String message, long tweetId) {
        super(coord, date, user);
        this.message = message;
        this.tweetId = tweetId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }
}
