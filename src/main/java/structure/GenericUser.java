package structure;

/**
 * A generic abstract user class
 * <p>
 * Created by gijspeters on 03-10-16.
 */
public abstract class GenericUser implements User {

    private String name;
    private String id;

    public GenericUser(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * @return the user's username
     */
    public String getName() {
        return name;
    }

    /**
     * @return the user's identifier, as used as unique identifier in the original social network
     */
    public String getId() {
        return id;
    }

    /**
     * @return thes user's source (e.g. Twitter, Instagram)
     */

}
