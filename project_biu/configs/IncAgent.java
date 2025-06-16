package project_biu.configs;

import java.util.ArrayList;

import project_biu.graph.Agent;
import project_biu.graph.Message;
import project_biu.graph.Topic;
import project_biu.graph.TopicManagerSingleton;
import project_biu.graph.TopicManagerSingleton.TopicManager;

/**
 * The IncAgent class represents an agent in the calculational graph.
 * when recieves a message, it increments its value, and publishes the results to output topics.
 */
public class IncAgent implements Agent{
	private String inX;
	private ArrayList<String> outTopics;
	private String name;

	/**
	 * Constructs an IncAgent with the specified input and output topics.
	 *
	 * @param inTopics The list of input topics.
	 * @param outTopics The list of output topics.
	 */
	public IncAgent(ArrayList<String> inTopics, ArrayList<String> outTopics) {
		if (!inTopics.isEmpty() && !outTopics.isEmpty()) {
			TopicManager tm = TopicManagerSingleton.get();
			// subscribe to first 2 topics in the input list
			Topic inXTopic = tm.getTopic(inTopics.get(0));
			inX = inXTopic.name;
			inXTopic.subscribe(this);
			//sign in as a publisher for outputList
			this.outTopics = outTopics;
			this.outTopics.forEach(topicName -> tm.getTopic(topicName).addPublisher(this));
			this.name = inX + "+1";
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
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Resets the state of the agent.
	 */
	@Override
	public void reset() {
		
	}

	/**
	 * Handles a callback for a message published to an input topic.
	 *
	 * @param topic The topic name.
	 * @param msg The message published to the topic.
	 */
	@Override
	public void callback(String topic, Message msg) {
		if (!msg.asDouble.isNaN() && topic.equals(inX)) {
			TopicManager tm = TopicManagerSingleton.get();
			this.outTopics.forEach(topicName -> tm.getTopic(topicName).publish(new Message(msg.asDouble+1)));
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

 
}
