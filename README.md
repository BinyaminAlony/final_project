# Java Agent-Based Graph Project

## Overview
This project is a configurable, agent-driven graph computation system with a web-based interface and custom HTTP server. It allows users to define computational graphs, upload new agent classes, and interact with the system through a modern web UI.

## Features
- **Configurable Graphs:** Define graphs using configuration files.
- **Agent System:** Extend the system by uploading Java agent classes implementing the `Agent` interface.
- **Parallel and Custom Agents:** Support for parallel agent execution and custom logic.
- **HTTP Server & Servlets:** Custom server and servlets for file uploads, configuration, and graph visualization.
- **Web UI:** Upload configuration files, agent classes, and publish messages via a styled HTML interface

## Documentation
- [Watch the demo on YouTube](https://www.youtube.com/watch?v=epoeeHTeOoo)
- Full JavaDoc is available in the [doc](doc/index.html) folder. Open `doc/index.html` in your browser to view the full API documentation.

## Getting Started
1. **Build the Project:**
   - Use your preferred Java IDE or `javac` to compile the source files.
2. **Run the Server:**
   - Start the HTTP server (see `Main.java` or `MainTrain.java`).
3. **Access the Web UI:**
   - Open `http://localhost:80/app/index.html` in your browser.
4. **Upload Configurations and Agents:**
   - Use the web forms to upload configuration files and agent classes.
   - Uploaded agent classes must implement `project_biu.graph.Agent` and not be abstract.

## Usage
- **Upload Configuration:** Deploy a `.conf` file to define your graph. Nodes are named automatically.
- **Upload Agent Class:** if Neccesary, upload a `.java` file for a new agent. The system checks for interface compliance and name conflicts.
- **Publish to Topic:** Send messages to topics via the web UI.

## File Formats

### Configuration File (.conf)
Each configuration file should contain triplets of lines:
1. The fully qualified class name of the agent (e.g., `project_biu.configs.PlusAgent`)
2. Comma-separated list of arguments for the input topics - topics the agent subscribes to.
3. Comma-separated list of arguments for the output topics - topics the agent publishes to.

**Example:**
```
project_biu.configs.PlusAgent
1,2,3
A,B,C
project_biu.configs.IncAgent
5,10
X,Y
```

### Agent Class File (.java)
- The file must define a public class in the `project_biu.configs` package.
- The class must implement the `project_biu.graph.Agent` interface and must not be abstract.
- The class name must not be the name of a class already in `project_biu/configs` package.

**Example:**
```java
package project_biu.configs;

import project_biu.graph.Agent;
import java.util.ArrayList;

public class MyAgent implements Agent {
    public MyAgent(ArrayList<String> args1, ArrayList<String> args2) {
        // constructor logic
    }
    // implement Agent methods
}
```

## API & Creating your own server

### Adding a New Servlet

To add a new HTTP servlet to the server:

1. **Create a Servlet Class**
   - Implement the `Servlet` interface (see `project_biu/servlets/Servlet.java`).

2. **Register the Servlet in Main.java**
   - In your `Main.java`, add your servlet to the server with a URL path:
     ```java
     server.addMapping("/myNewServlet", new MyNewServlet());
     ```
   - Now, HTTP requests to `/myNewServlet` will be handled by your servlet.

3. **Restart the Server**
   - Rebuild and restart your server to apply the changes.

## Authors & Contact:
- Binyamin Alony
For questions or support, contact:
- binyaminalony1@gmail.com
