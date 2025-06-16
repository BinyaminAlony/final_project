// file was removed, couldnt make this work atm but might work on it in future versions.

// // package project_biu.servlets;

// // import project_biu.configs.GenericConfig;
// // import project_biu.graph.Agent;
// // import project_biu.graph.Graph;
// // import project_biu.graph.Node;
// // import project_biu.graph.ParallelAgent;
// // import project_biu.graph.Topic;

// // import java.io.IOException;
// // import java.io.OutputStream;
// // import java.lang.reflect.Constructor;
// // import java.nio.charset.StandardCharsets;
// // import java.util.ArrayList;
// // import java.util.Arrays;
// // import java.io.FileOutputStream;

// // import project_biu.server.RequestParser.RequestInfo;
// // import project_biu.views.HtmlGraphViewer;
// // import project_biu.graph.TopicManagerSingleton;
// // import project_biu.graph.TopicManagerSingleton.TopicManager;

// import java.io.IOException;
// import java.io.OutputStream;
// import java.lang.reflect.Constructor;
// import java.nio.charset.StandardCharsets;
// import java.util.ArrayList;

// import project_biu.graph.Agent;
// import project_biu.graph.Graph;
// import project_biu.graph.Node;
// import project_biu.graph.ParallelAgent;
// import project_biu.graph.Topic;
// import project_biu.graph.TopicManagerSingleton;
// import project_biu.graph.TopicManagerSingleton.TopicManager;
// import project_biu.server.RequestParser.RequestInfo;
// import project_biu.views.HtmlGraphViewer;

// /**
//  * The AddNodeToGraph class is responsible for handling HTTP requests to add nodes to the graph.
//  * It processes incoming requests to add topics, agents, or relationships between nodes,
//  * and generates an updated graph representation.
//  */
// public class AddNodeToGraph implements Servlet {

//     /**
//      * Handles an HTTP request to add nodes or relationships to the graph.
//      *
//      * @param ri The RequestInfo object containing details about the incoming request.
//      * @param toClient The OutputStream to which the response should be written.
//      * @throws IOException If an I/O error occurs during request handling.
//      */
//     @Override
//     public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
//         TopicManager tm = TopicManagerSingleton.get();
        
//         Graph graph = new Graph();
//         graph.createFromTopics();

//         String action = ri.getParameters().get("addType");
//         String mainName = ri.getParameters().get("mainName");
//         String relatedName = ri.getParameters().get("relatedName");
//         String agentType = ri.getParameters().get("agentType");

//         switch (action) {
//             case "topic":
//                 tm.getTopics().forEach(Topic -> {
//                     Topic.getSubs().forEach(agent -> {
//                         if (agent.getName().equals(relatedName)) {
//                             Topic newTopic = tm.getTopic(mainName);
//                             newTopic.addPublisher(agent);
//                             sendtoClient(toClient, graph, false);
//                             return;
//                         }
//                     });
//                     Topic.getPubs().forEach(agent -> {
//                         if (agent.getName().equals(relatedName)) {
//                             Topic newTopic = tm.getTopic(mainName);
//                             newTopic.addPublisher(agent);
//                             sendtoClient(toClient, graph, false);
//                             return;
//                         }
//                     });
//                 });
//                 sendtoClient(toClient, graph, true);
//                 break;
//             case "agent":
//                 Class<?> agentClass;
//                 try {
//                     //find agent class
//                     agentClass = Class.forName(agentType);
//                     Constructor<?> constructor = agentClass.getConstructor(ArrayList.class, ArrayList.class);
//                     Agent agent = (Agent) constructor.newInstance(new ArrayList<String>(), new ArrayList<String>());
                    
//                     //create new agent, and subscribe to the requested topic
//                     ParallelAgent newAgent = new ParallelAgent(agent, 1);
//                     newAgent.setName(mainName);
//                     tm.getTopic(relatedName).subscribe(newAgent);

//                     Node node = new Node(mainName);
//                     node.addEdge(graph.);

//                     graph.
//                 } catch (Exception e) {
//                     // TODO Auto-generated catch block
//                     e.printStackTrace();
//                     sendtoClient(toClient, graph, true);
//                 }
//                 break;
//             case "subscription":
//                 // Add Subscription logic
//                 // mainName = agent name, relatedName = topic to subscribe
//                 // TODO: implement subscription
//                 break;
//             case "publisher":
//                 // Add Publisher logic
//                 // mainName = agent name, relatedName = topic to publish
//                 // TODO: implement publisher assignment
//                 break;
//             default:
//                 // Unknown action
//                 break;
//         }
//     }
//     private void sendtoClient(OutputStream toClient, Graph graph, Boolean agentDoesntExist) {
//         try{                            
//             String[] html = HtmlGraphViewer.getGraphHTML(graph);
//             if (agentDoesntExist) {
//                 String warning = "<div style='background:#ffcccc;color:#b20000;padding:12px 0 12px 0;text-align:center;font-weight:bold;font-size:1.1em;'>adding the node didnt work. Check the inputs and try again</div>\n";
//                 toClient.write(warning.getBytes(StandardCharsets.UTF_8));
//             }
//             for(String line : html){
//                 toClient.write((line + "\n").getBytes());
//             }
//             toClient.flush();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
//     private String buildStyledErrorHtml(String errorTitle, String errorMsg) {
//         return "<!DOCTYPE html><html><head><title>Error</title>"
//             + "<style>"
//             + "body { background:#222; color:#fff; font-family:'Segoe UI',Arial,sans-serif; display:flex; flex-direction:column; align-items:center; justify-content:center; min-height:100vh; margin:0; }"
//             + ".topics-box { background:#333; border-radius:18px; box-shadow:0 4px 24px #0008; padding:24px 18px 18px 18px; width:80%; max-width:900px; margin: 32px auto 0 auto; text-align:center; box-sizing: border-box; }"
//             + ".error-title { color:#ff5252; font-size:1.5em; margin-bottom:12px; font-weight:bold; }"
//             + ".error-msg { color:#fff; font-size:1.1em; }"
//             + "</style></head><body>"
//             + "<div class='topics-box'>"
//             + "<div class='error-title'>" + errorTitle + "</div>"
//             + "<div class='error-msg'>" + errorMsg + "</div>"
//             + "</div></body></html>";
//     }
//     /**
//      * Closes any resources associated with the servlet.
//      *
//      * @throws IOException If an I/O error occurs during resource cleanup.
//      */
//     @Override
//     public void close() throws IOException {
//         // nothing to close
//     }
// }
