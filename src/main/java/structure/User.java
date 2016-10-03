package structure;

/**
 * Interface that defines the attributes of a user object
 * <p>
 * Created by gijspeters on 03-10-16.
 */
public interface User {

    /**
     * @return the user's username
     */
    String getName();

    /**
     * @return the user's identifier, as used as unique identifier in the original social network
     */
    String getId();

    /**
     * @return thes user's source (e.g. Twitter, Instagram)
     */
    SourceType getSourceType();

}
