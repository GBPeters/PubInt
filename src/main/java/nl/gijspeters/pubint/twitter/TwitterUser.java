package nl.gijspeters.pubint.twitter;

import nl.gijspeters.pubint.structure.Agent;
import nl.gijspeters.pubint.structure.SourceType;
import nl.gijspeters.pubint.structure.User;
import org.mongodb.morphia.annotations.Entity;

/**
 * User implementation for Twitter user accounts
 * <p>
 * Created by gijspeters on 03-10-16.
 */
@Entity("User")
public class TwitterUser extends User {

    private long twitterId;


    /**
     * Empty constructor for POJO mapping
     */
    public TwitterUser() {
        super();
    }

    /**
     * Default constructor inherited from superclass
     *
     * @param name the user's name
     * @param twitterId   the user's identifier as used by the original social network
     */
    public TwitterUser(String name, Agent agent, long twitterId) {
        super(name, agent);
        this.twitterId = twitterId;
    }

    public long getTwitterId() {
        return this.twitterId;
    }

    public void setTwitterId(long twitterId) {
        this.twitterId = twitterId;
    }

    /**
     * @return the user's source (e.g. Twitter, Instagram)
     */
    public SourceType getSourceType() {
        return SourceType.TWITTER;
    }
}
