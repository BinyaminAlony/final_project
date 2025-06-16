package project_biu.configs;

import java.lang.reflect.Constructor;
import java.util.*;

import project_biu.graph.Agent;
import project_biu.graph.ParallelAgent;


/**
 * The GenericConfig class enables us to create the calaulational graph from a configuration file.
 * It reads configuration data from a file and creates instances of agents based on the data.
 * all agent classes must be in the /configs package.
 */
public class GenericConfig implements Config{
	String confFile;
	ArrayList<ParallelAgent> agents = new ArrayList<ParallelAgent>();
	
	/**
	 * Creates the configuration by reading data from the specified file.
	 * The file must contain triplets of class name and two argument lists.
	 */
	@Override
	public void create() {
		if (confFile == null || confFile.isEmpty())
			System.out.println("Config file path is not set.");
//			throw new IllegalStateException("Config file path is not set.");
		else
//		if (confFile != null && !confFile.isEmpty())
		    try {
		    	List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(confFile));
	
		        if (lines.size() % 3 != 0) {
//		            throw new IllegalArgumentException("Invalid config: number of lines must be a multiple of 3");
		        	System.out.println("file doesnt match format (rows arent in triplets");
		        	return;
		        }
	
		        for (int i = 0; i < lines.size(); i += 3) {
		        	//get class name
		            String className = lines.get(i).trim();
		            
		            //get argument arrays
		            ArrayList<String> argList1 = new ArrayList<>();
		            for (String item : lines.get(i + 1).split(",")) argList1.add(item.trim());

		            ArrayList<String> argList2 = new ArrayList<>();
		            for (String item : lines.get(i + 2).split(",")) argList2.add(item.trim());
		            
		            //create the new instance:
		            //get the class
		            Class<?> agentClass = Class.forName(className);
		            //get the constructor
		            Constructor<?> constructor = agentClass.getConstructor(ArrayList.class, ArrayList.class);
		            //create the instance, and add it to the list
		            Agent agent = (Agent) constructor.newInstance(argList1, argList2);
		            ParallelAgent parallelAgent = new ParallelAgent(agent, 1);
		            agents.add(parallelAgent);
		        }
	
		    } catch (Exception e) {
		        throw new RuntimeException("Error reading config file: " + e.toString(), e);
		    }
	}

	/**
	 * Retrieves the name of the configuration file.
	 *
	 * @return The name of the configuration file.
	 */
	@Override
	public String getName() {
		return confFile;
	}

	/**
	 * Retrieves the version of the configuration.
	 *
	 * @return The version of the configuration.
	 */
	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 1;
	}

	/**
	 * Closes the configuration and releases resources.
	 */
	@Override
	public void close() {
		agents.forEach(agent -> agent.close());
	}

	public void setConfFile(String confFile) {
		this.confFile = confFile;
	}
}
