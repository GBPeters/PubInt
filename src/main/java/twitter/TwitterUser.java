package twitter;

import structure.SourceType;
import structure.User;

/**
 * User implementation for Twitter user accounts
 * <p>
 * Created by gijspeters on 03-10-16.
 */
public class TwitterUser extends User {

    private SourceType sourceType = SourceType.TWITTER;

    /**
     * Default constructor inherited from superclass
     *
     * @param name the user's name
     * @param id   the user's identifier as used by the original social network
     */
    public TwitterUser(String name, String id) {
        super(name, id);
    }

    /**
     * @return thes user's source (e.g. Twitter, Instagram)
     */
    public SourceType getSourceType() {
        return sourceType;
    }
}
