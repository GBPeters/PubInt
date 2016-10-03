package structure;

import java.util.HashMap;

/**
 * This class represents agents: individuals publishing their locations using social media
 * on their smartphone or other GPS-enabled device. One agent can have multiple 'users', the user
 * representing the user accounts at different social media networks.
 * <p>
 * Created by gijspeters on 03-10-16.
 */
public class Agent extends HashMap<Integer, User> {

    private int agentId;

    /**
     * Default constructor
     *
     * @param agentId the agent identifier from the database
     */
    public Agent(int agentId) {
        super();
        this.agentId = agentId;
    }

    /**
     * Constructor using existing hashmap
     *
     * @param agentId the agent identifier from the database
     * @param usermap a HashMap with user keys and users
     */
    public Agent(int agentId, HashMap<Integer, User> usermap) {
        super(usermap);
        this.agentId = agentId;
    }

    /**
     * @return the agent identifier from the database
     */
    public int getAgentId() {
        return agentId;
    }

}
