package nl.gijspeters.pubint.structure;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by gijspeters on 03-10-16.
 */
public class AgentTest {
    @Test
    public void getAgentId() throws Exception {
        Agent agent = new Agent();
        assertNotNull(agent);
    }

}