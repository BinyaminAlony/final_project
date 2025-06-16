package project_biu.graph;

/**
 * The Agent interface defines the contract for agents in the directed calculational graph.
 * Agents can process messages, reset their state, and manage their name.
 */
public interface Agent {

    /**
     * Retrieves the name of the agent.
     *
     * @return The name of the agent.
     */ 
    String getName();

    /**
     * Resets the state of the agent.
     */
    void reset();

    /**
     * Handles a callback for a message published to a topic.
     *
     * @param topic The topic name.
     * @param msg The message published to the topic.
     */
    void callback(String topic, Message msg);

    /**
     * Closes the agent and releases resources.
     */
    void close();

    /**
     * Sets the name of the agent.
     *
     * @param name The name to set.
     */
    void setName(String name);
}
