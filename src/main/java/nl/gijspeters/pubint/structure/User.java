package nl.gijspeters.pubint.structure;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * A generic abstract user class
 * <p>
 * Created by gijspeters on 03-10-16.
 */
@Entity("User")
public abstract class User {

    private String name;
    @Id
    private ObjectId id;
    @Reference
    private Agent agent;

    public User() {

    }

    public User(String name, Agent agent) {
        this.name = name;
        this.agent = agent;
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
    public ObjectId getId() {
        return id;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return thes user's source (e.g. Twitter, Instagram)
     */
    public abstract SourceType getSourceType();

}
