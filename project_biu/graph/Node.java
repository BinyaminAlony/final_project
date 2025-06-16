package project_biu.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * The Node class represents a node in a directed calculational graph.
 * It contains edges to other nodes and can store a message.
 */
public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;
    
    /**
     * Constructs a Node with the specified name.
     *
     * @param name The name of the node.
     */
    public Node(String name) {
		this.setName(name);
		this.setEdges(new ArrayList<Node>());
	}

    /**
     * Retrieves the name of the node.
     *
     * @return The name of the node.
     */
	public String getName() {
		return name;
	}

    /**
     * Sets the name of the node.
     *
     * @param name The name to set.
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Retrieves the edges of the node.
     *
     * @return A list of nodes connected to this node.
     */
	public List<Node> getEdges() {
		return edges;
	}

    /**
     * Sets the edges of the node.
     *
     * @param edges The list of nodes to connect to this node.
     */
	public void setEdges(List<Node> edges) {
		this.edges = edges;
	}

    /**
     * Retrieves the message stored in the node.
     *
     * @return The message stored in the node.
     */
	public Message getMsg() {
		return msg;
	}

    /**
     * Sets the message stored in the node.
     *
     * @param msg The message to store in the node.
     */
	public void setMsg(Message msg) {
		this.msg = msg;
	}
	
    /**
     * Adds an edge to another node.
     *
     * @param node The node to connect to.
     */
	public void addEdge(Node node) {
		this.edges.add(node);
	}
	
    /**
     * Checks if the graph contains cycles starting from this node.
     *
     * @return True if cycles are detected, false otherwise.
     */
	public boolean hasCycles() {
		return this.hasCycles(new ArrayList<Node>());
	}
	
	public boolean hasCycles(ArrayList<Node> beenTo) {
		// if there are no children, this node is not a part of a cycle.
		if (edges.isEmpty()) return false; 
		// if the list we received has me, then there is a cycle.
		if (beenTo.contains(this)) {
// 			for debugging:
//			System.out.println("my name: " + this.name);
//			beenTo.forEach(node -> System.out.println(node.getName()));
			return true;
		}
		// add this node to a clone of beenTo, and continue the search.
		@SuppressWarnings("unchecked")
		ArrayList<Node> newBeenTo =  (ArrayList<Node>) beenTo.clone();
		newBeenTo.add(this);
		return edges.stream().anyMatch(b -> b.hasCycles(newBeenTo));
	}


}