package project_biu.configs;

import java.util.ArrayList;

import project_biu.graph.Agent;
import project_biu.graph.Message;
import project_biu.graph.Topic;
import project_biu.graph.TopicManagerSingleton;
import project_biu.graph.TopicManagerSingleton.TopicManager;

public class FinalPowerAgent implements Agent {
	private Double insolation = 0d;
	private Double modifier = 1d;
	private String inInsolation;
	private String inModifier;
	private ArrayList<String> outTopics;
	private String name;

	public FinalPowerAgent(ArrayList<String> inTopics, ArrayList<String> outTopics) {
		if (inTopics.size() >= 2 && !outTopics.isEmpty()) {
			TopicManager tm = TopicManagerSingleton.get();
			Topic topic1 = tm.getTopic(inTopics.get(0));
			Topic topic2 = tm.getTopic(inTopics.get(1));
			inInsolation = topic1.name;
			inModifier = topic2.name;
			topic1.subscribe(this);
			topic2.subscribe(this);
			this.outTopics = outTopics;
			this.outTopics.forEach(topicName -> tm.getTopic(topicName).addPublisher(this));
			this.name = inInsolation + "*" + inModifier + " → FinalPower";
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
		insolation = 0d;
		modifier = 1d;
	}

	@Override
	public void close() {}

	@Override
	public void callback(String topic, Message msg) {
		if (!msg.asDouble.isNaN()) {
			if (topic.equals(inInsolation)) insolation = msg.asDouble;
			if (topic.equals(inModifier)) modifier = msg.asDouble;
			double power = insolation * modifier;
			TopicManager tm = TopicManagerSingleton.get();
			outTopics.forEach(topicName -> tm.getTopic(topicName).publish(new Message(power)));
		}
	}
}

