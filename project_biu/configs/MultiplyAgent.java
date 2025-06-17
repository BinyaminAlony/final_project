package project_biu.configs;

import java.util.ArrayList;

import project_biu.graph.Agent;
import project_biu.graph.Message;
import project_biu.graph.Topic;
import project_biu.graph.TopicManagerSingleton;
import project_biu.graph.TopicManagerSingleton.TopicManager;

public class MultiplyAgent implements Agent {
	private Double a = 1d, b = 1d;
	private String inA, inB;
	private ArrayList<String> outTopics;
	private String name;

	public MultiplyAgent(ArrayList<String> inTopics, ArrayList<String> outTopics) {
		if (inTopics.size() >= 2 && !outTopics.isEmpty()) {
			TopicManager tm = TopicManagerSingleton.get();
			Topic inATopic = tm.getTopic(inTopics.get(0));
			Topic inBTopic = tm.getTopic(inTopics.get(1));
			inA = inATopic.name;
			inB = inBTopic.name;
			inATopic.subscribe(this);
			inBTopic.subscribe(this);
			this.outTopics = outTopics;
			this.outTopics.forEach(topicName -> tm.getTopic(topicName).addPublisher(this));
			this.name = inA + "*" + inB;
		}
	}

	@Override public String getName() { return name; }
	@Override public void setName(String name) { this.name = name; }
	@Override public void reset() { a = 1d; b = 1d; }
	@Override public void close() {}

	@Override
	public void callback(String topic, Message msg) {
		if (!msg.asDouble.isNaN()) {
			if (topic.equals(inA)) a = msg.asDouble;
			if (topic.equals(inB)) b = msg.asDouble;
			double result = a * b;
			TopicManager tm = TopicManagerSingleton.get();
			this.outTopics.forEach(topicName -> tm.getTopic(topicName).publish(new Message(result)));
		}
	}
}

