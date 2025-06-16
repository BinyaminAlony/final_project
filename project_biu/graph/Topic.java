package project_biu.graph;

import java.util.ArrayList;

/**
 * The Topic class represents a topic in the graph.
 * It manages subscribers and publishers, allowing messages to be published
 * and delivered to subscribers.
 */
public class Topic {
	
    public final String name;
    private ArrayList<Agent> subs = new ArrayList<>();
    private ArrayList<Agent> pubs = new ArrayList<>();
    private Message message;
    
    /**
     * Constructs a Topic with the specified name.
     *
     * @param name The name of the topic.
     */
    Topic(String name){
        this.name=name;
    }

    /**
     * Retrieves the list of subscribers to this topic.
     *
     * @return A list of agents subscribed to this topic.
     */
    public ArrayList<Agent> getSubs() {
		return subs;
	}

    /**
     * Retrieves the list of publishers for this topic.
     *
     * @return A list of agents publishing to this topic.
     */
	public ArrayList<Agent> getPubs() {
		return pubs;
	}

    /**
     * Subscribes an agent to this topic.
     *
     * @param a The agent to subscribe.
     */
	public void subscribe(Agent a){
    	getSubs().add(a);
    }
    /**
     * Unsubscribes an agent from this topic.
     *
     * @param a The agent to unsubscribe.
     */
    public void unsubscribe(Agent a){
    	getSubs().remove(a);
    }

    /**
     * Publishes a message to this topic, delivering it to all subscribers.
     *
     * @param m The message to publish.
     */
    public void publish(Message m){
        this.message = m;
    	getSubs().forEach(a->
        a.callback(this.name, m)
        );
    }

    /**
     * Adds a publisher to this topic.
     *
     * @param a The agent to add as a publisher.
     */
    public void addPublisher(Agent a){
    	getPubs().add(a);
    }

    /**
     * Removes a publisher from this topic.
     *
     * @param a The agent to remove as a publisher.
     */
    public void removePublisher(Agent a){
    	getPubs().remove(a);
    }

    /**
     * Retrieves the last published message for this topic.
     *
     * @return The last published message, or an empty message if none exists.
     */
    public Message getMessage() {
        if (message == null) {
            return new Message("");
        }
        return message;
    }
}
