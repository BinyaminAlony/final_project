package project_biu.configs;

import java.util.ArrayList;

import project_biu.graph.Agent;
import project_biu.graph.Message;
import project_biu.graph.Topic;
import project_biu.graph.TopicManagerSingleton;
import project_biu.graph.TopicManagerSingleton.TopicManager;

public class TempModifierAgent implements Agent {
	private String inTempTopic;
	private ArrayList<String> outTopics;
	private String name;

	public TempModifierAgent(ArrayList<String> inTopics, ArrayList<String> outTopics) {
		if (!inTopics.isEmpty() && !outTopics.isEmpty()) {
			TopicManager tm = TopicManagerSingleton.get();
			Topic tempTopic = tm.getTopic(inTopics.get(0));
			inTempTopic = tempTopic.name;
			tempTopic.subscribe(this);
			this.outTopics = outTopics;
			this.outTopics.forEach(topicName -> tm.getTopic(topicName).addPublisher(this));
			this.name = "TempModifier";
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void reset() {
		// No state to reset
	}

	@Override
	public void close() {}

	@Override
	public void callback(String topic, Message msg) {
		if (!msg.asDouble.isNaN() && topic.equals(inTempTopic)) {
			double temp = msg.asDouble;
			double modifier =  Math.max(0.0, 1.0 - 0.005 * (temp - 25)); // prevent negative efficiency
			TopicManager tm = TopicManagerSingleton.get();
			outTopics.forEach(topicName -> tm.getTopic(topicName).publish(new Message(modifier)));
		}
	}
}

