package project_biu.configs;

import java.util.ArrayList;

import project_biu.graph.Agent;
import project_biu.graph.Message;
import project_biu.graph.Topic;
import project_biu.graph.TopicManagerSingleton;
import project_biu.graph.TopicManagerSingleton.TopicManager;


/**
 * The PlusAgent class represents an agent in the directed calculational graph.
 * when a message is recieved, it adds the values of the input topics (first two topics in the argument lists), 
 * and publishes the results to output topics.
 */
public class PlusAgent implements Agent{
	private Double x = 0d;
	private Double y = 0d;
	private String inX;
	private String inY;
	private ArrayList<String> outTopics;
	private String name;
	
	/**
	 * Constructs a PlusAgent with the specified input and output topics.
	 *
	 * @param inTopics The list of input topics.
	 * @param outTopics The list of output topics.
	 */
	public PlusAgent(ArrayList<String> inTopics, ArrayList<String> outTopics) {
		if (inTopics.size() >= 2 && !outTopics.isEmpty()) {
			TopicManager tm = TopicManagerSingleton.get();
			// subscribe to first 2 topics in the input list
			Topic inXTopic = tm.getTopic(inTopics.get(0));
			Topic inYTopic = tm.getTopic(inTopics.get(1));
			inX = inXTopic.name;
			inXTopic.subscribe(this);
			inY = inYTopic.name;
			inYTopic.subscribe(this);
			//sign in as a publisher for outputList
			this.outTopics = outTopics;
			this.outTopics.forEach(topicName -> tm.getTopic(topicName).addPublisher(this));
			this.name = inX + "+" + inY;
		}
	}
	
	/**
	 * Retrieves the name of the agent.
	 *
	 * @return The name of the agent.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the agent.
	 *
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Resets the state of the agent.
	 */
	@Override
	public void reset() {
		x = 0d;
		y = 0d;
	}

	/**
	 * Handles a callback for a message published to an input topic.
	 *
	 * @param topic The topic name.
	 * @param msg The message published to the topic.
	 */
	@Override
	public void callback(String topic, Message msg) {
		if (!msg.asDouble.isNaN()) {
			if (topic.equals(inX))
				x = msg.asDouble;
			if (topic.equals(inY))
				y = msg.asDouble;
			TopicManager tm = TopicManagerSingleton.get();
			this.outTopics.forEach(topicName -> tm.getTopic(topicName).publish(new Message(x+y)));
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}


}
