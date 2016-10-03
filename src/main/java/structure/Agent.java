package structure;

import java.util.HashSet;

/**
 * This class represents agents: individuals publishing their locations using social media
 * on their smartphone or other GPS-enabled device. One agent can have multiple 'users', the user
 * representing the user accounts at different social media networks.
 * <p>
 * Created by gijspeters on 03-10-16.
 */
public class Agent extends HashSet<User> {

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
     * @param userSet a HashSet with users
     */
    public Agent(int agentId, HashSet<User> userSet) {
        super(userSet);
        this.agentId = agentId;
    }

    /**
     * @return the agent identifier from the database
     */
    public int getAgentId() {
        return agentId;
    }

}
