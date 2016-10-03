package structure;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gijspeters on 03-10-16.
 */
public class AgentTest {
    @Test
    public void getAgentId() throws Exception {
        Agent agent = new Agent(2);
        assertEquals(2, agent.getAgentId());
    }

}