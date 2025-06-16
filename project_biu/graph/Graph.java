package project_biu.graph;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Graph class represents a directed graph.
 * It manages nodes representing topics and agents, and provides methods
 * to create the graph from topics and check for cycles.
 */
public class Graph extends ArrayList<Node> {
	private ConcurrentHashMap<String, Node> topics = new ConcurrentHashMap<String, Node>();
	private ConcurrentHashMap<String, Node> agents = new ConcurrentHashMap<String, Node>();

	/**
	 * Checks if the graph contains cycles.
	 *
	 * @return True if cycles are detected, false otherwise.
	 */
	public boolean hasCycles() {
		return this.stream().anyMatch(node -> node.hasCycles());
	}

	/**
	 * Creates the graph from topics, connecting nodes based on subscriptions and publications.
	 */
	public void createFromTopics(){
		// clear previously saved data
		this.clear();
		this.topics.clear();
		this.agents.clear();
		
		// get list of topics
		TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
		for (Topic topic : tm.getTopics()) {
			// create a node for the topic
			Node topicNode = this.topics.computeIfAbsent(topic.name, name -> new Node("T" + name));
			
			// for each sub, get the relevant node and add the edge from the topic to the sub
			for (Agent agent : topic.getSubs()) {
				Node agentNode = this.agents.computeIfAbsent(agent.getName(), name -> new Node("A" + name));
				topicNode.addEdge(agentNode);
			}
			// for each pub, get the relevant node and add the edge from the pub to the topic
			for (Agent agent : topic.getPubs()) {
				Node agentNode = this.agents.computeIfAbsent(agent.getName(), name -> new Node("A" + name));
				agentNode.addEdge(topicNode);
			}
		}
		// add all nodes to graph
		this.addAll(topics.values());
		this.addAll(agents.values());
		// this.forEach(Node -> {System.out.println(Node.getName());});
    }
}
