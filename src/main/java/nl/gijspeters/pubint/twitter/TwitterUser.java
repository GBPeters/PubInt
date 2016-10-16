package nl.gijspeters.pubint.twitter;

import nl.gijspeters.pubint.structure.Agent;
import nl.gijspeters.pubint.structure.SourceType;
import nl.gijspeters.pubint.structure.User;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

/**
 * User implementation for Twitter user accounts
 * <p>
 * Created by gijspeters on 03-10-16.
 */
@Entity("User")
public class TwitterUser extends User {

    @Indexed(sparse = true, unique = true)
    private String twitterName;


    /**
     * Empty constructor for POJO mapping
     */
    public TwitterUser() {
        super();
    }

    /**
     * Default constructor inherited from superclass
     *
     * @param agent the user's agent
     * @param twitterName   the user's identifier as used by the original social network
     */
    public TwitterUser(String twitterName, Agent agent) {
        super(twitterName, agent);
        this.twitterName = twitterName;
    }

    public String getTwitterName() {
        return this.twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    /**
     * @return the user's source (e.g. Twitter, Instagram)
     */
    public SourceType getSourceType() {
        return SourceType.TWITTER;
    }
}
