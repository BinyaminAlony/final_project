package project_biu.graph;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TopicManagerSingleton {

    /**
     * The TopicManager class manages the topics in our calculational graph.
     * It provides methods to create, retrieve, and remove topics, as well as clear all topics.
	 * 
	 * we are using a singleton pattern to ensure that there is only one instance of TopicManager
     */
    public static class TopicManager {

        private ConcurrentHashMap<String, Topic> map = new ConcurrentHashMap<>();

        /**
         * Retrieves a topic by name, creating it if it does not exist.
         *
         * @param topicName The name of the topic to retrieve.
         * @return The Topic object corresponding to the given name.
         */
        public Topic getTopic(String topicName) {
            return this.map.computeIfAbsent(topicName, name -> new Topic(name));
        }

        /**
         * Retrieves all topics managed by the TopicManager.
         *
         * @return A collection of all Topic objects.
         */
        public Collection<Topic> getTopics() {
            return this.map.values();
        }

        /**
         * Clears all topics managed by the TopicManager.
         */
        public void clear() {
            this.map = new ConcurrentHashMap<>();
        }

        /**
         * Removes a topic by name.
         *
         * @param topicName The name of the topic to remove.
         */
        public void removeTopic(String topicName) {
            this.map.remove(topicName);
        }
    }

    private static final TopicManager instance = new TopicManager();

    /**
     * Retrieves the singleton instance of the TopicManager.
     *
     * @return The singleton TopicManager instance.
     */
    public static TopicManager get() {
        return instance;
    }
}
