package project_biu.graph;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The ParallelAgent class represents an agent in the graph
 * that processes messages in parallel using a blocking queue.
 */
public class ParallelAgent implements Agent{
	
	Agent agent;
	BlockingQueue<TopicMessagePair> queue;
	TakeMessagesLoop takeMessageLoop;
	
	/**
	 * Constructs a ParallelAgent with the specified agent and queue capacity.
	 *
	 * @param agent The underlying agent to wrap.
	 * @param capacity The capacity of the blocking queue.
	 */
	public ParallelAgent(Agent agent, int capacity) {
		this.agent = agent;
		this.queue = new ArrayBlockingQueue<TopicMessagePair>(capacity);
		this.takeMessageLoop = new TakeMessagesLoop();
		Thread takeMessageLoopThread = new Thread(takeMessageLoop);
		takeMessageLoopThread.start();
	}
	
	/**
	 * Sets the name of the agent.
	 *
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.agent.setName(name);
	}
	
	/**
	 * Retrieves the name of the agent.
	 *
	 * @return The name of the agent.
	 */
	@Override
	public String getName() {
		return this.agent.getName();
	}

	/**
	 * Resets the agent and its message processing loop.
	 */
	@Override
	public void reset() {
		this.takeMessageLoop = new TakeMessagesLoop();
		this.agent.reset();
	}

	/**
	 * Handles a callback for a message published to a topic.
	 *
	 * @param topic The topic name.
	 * @param msg The message published to the topic.
	 */
	@Override
	public void callback(String topic, Message msg) {
		try {
			this.queue.put(new TopicMessagePair(topic, msg));
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Closes the agent and stops its message processing loop.
	 */
	@Override
	public void close() {
		this.takeMessageLoop.stopMe = true;
		this.agent.close();
	}
	
	
	private class TopicMessagePair {
		String topic;
		Message message;
		
		TopicMessagePair(String topic, Message message){
			this.topic = topic;
			this.message = message;
		}
	}
	
	private class TakeMessagesLoop implements Runnable{
		Boolean stopMe;
		public TakeMessagesLoop() {
			stopMe = false;
		}
		
		@Override
		public void run() {
			while(!stopMe) {
				try {
					TopicMessagePair pair = queue.poll(100, TimeUnit.MILLISECONDS);
					if(pair != null) {
						agent.callback(pair.topic, pair.message);						
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}
		
	}

}
