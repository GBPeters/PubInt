package nl.gijspeters.pubint.structure;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * This class represents agents: individuals publishing their locations using social media
 * on their smartphone or other GPS-enabled device. One agent can have multiple 'users', the user
 * representing the user accounts at different social media networks.
 * <p>
 * Created by gijspeters on 03-10-16.
 */
@Entity("agent")
public class Agent {

    @Id
    private ObjectId agentId;

    public ObjectId getAgentId() {
        return agentId;
    }
}
