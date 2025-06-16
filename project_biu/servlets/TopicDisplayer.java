package project_biu.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import project_biu.graph.Message;
import project_biu.graph.Topic;
import project_biu.graph.TopicManagerSingleton;
import project_biu.graph.TopicManagerSingleton.TopicManager;
import project_biu.server.RequestParser.RequestInfo;

public class TopicDisplayer implements Servlet {

    /**
     * Handles an HTTP request - publishing a message to a topic, and displaying topic contents.
     *
     * @param ri The RequestInfo object containing details about the incoming request.
     * @param toClient The OutputStream to which the response should be written.
     * @throws IOException If an I/O error occurs during request handling.
     */
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        Boolean topicDoesntExist = false;
        //extracting topic name and message from request parameters
        String topicName = ri.getParameters().get("topic");
        String message = ri.getParameters().get("message");
        // If topic name is not null and exists, publish the message to the topic
        // If the topic does not exist, remove it from the manager and tell the client the topic doesn't exist.
        if (topicName != null && topicName != null) {
            TopicManager tm = TopicManagerSingleton.get();
            Topic targetTopic = tm.getTopic(topicName);
            if (targetTopic.getPubs().isEmpty() && targetTopic.getSubs().isEmpty()){
                tm.removeTopic(topicName);
                topicDoesntExist = true;
            } else {
                targetTopic.publish(new Message(message));
            }
        }
        // Build the HTML response, and send it to the client.
        String html = buildTopicsHtml(topicDoesntExist);
        toClient.write(html.getBytes());
        toClient.flush();
    }

    /**
     * Closes any resources associated with the servlet.
     *
     * @throws IOException If an I/O error occurs during resource cleanup.
     */
    @Override
    public void close() throws IOException {
        // nothing to close
    }

    /**
     * Builds an HTML representation of the current topics.
     *
     * @param topicDoesntExist A flag indicating whether a topic does not exist.
     * @return A string containing the HTML representation of the topics.
     */
    private String buildTopicsHtml(Boolean topicDoesntExist) {
        TopicManager tm = TopicManagerSingleton.get();
        Collection<Topic> topics = tm.getTopics();
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>Topics</title>");
        html.append("""
        <style>
        body {
            background:#222;
            color:#fff;
            font-family:'Segoe UI',Arial,sans-serif;
            display:flex;
            flex-direction:column;
            align-items:center;
            justify-content:center;
            min-height:100vh;
            margin:0;
        }
        .topics-box {
            background:#333;
            border-radius:18px;
            box-shadow:0 4px 24px #0008;
            padding:24px 18px 18px 18px;
            width:80%%;
            max-width:900px;
            margin: 32px auto 0 auto;
            text-align:center;
            box-sizing: border-box;
        }
        table {
            border-collapse:separate;
            border-spacing:0;
            width:100%%;
            background:transparent;
            border-radius:14px;
            overflow:hidden;
            margin:0 auto;
        }
        th,td {
            border:1px solid #888;
            padding:8px;
        }
        th {
            background:#4fc3f7;
            color:#222;
        }
        td {
            color:#fff;
            background: #333;
        }
        table tr:first-child th:first-child { border-top-left-radius: 12px; }
        table tr:first-child th:last-child { border-top-right-radius: 12px; }
        table tr:last-child td:first-child { border-bottom-left-radius: 12px; }
        table tr:last-child td:last-child { border-bottom-right-radius: 12px; }
        h2 {
            color:#4fc3f7;
        }
        </style>
        """);
        html.append("</head><body>");
        if (Boolean.TRUE.equals(topicDoesntExist)) {
            html.append("<div style='color:#ff5252; font-weight:bold; margin-bottom:16px;'>The topic you tried to publish to doesn't exist.</div>");
        }
        html.append("<div class='topics-box'>");

        html.append("<h2>Topics and Messages</h2>");
        html.append("<table>");
        html.append("<tr><th>Topic Name</th><th>Message</th></tr>");
        for (Topic topic : topics) {
            html.append("<tr>");
            html.append("<td>").append(topic.name).append("</td>");
            String msg = topic.getMessage().asText;
            if (msg == null || msg.isEmpty()) {
                html.append("<td>0.0</td>");
            } else {
                html.append("<td>").append(msg).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("</div>");
        html.append("</body></html>");
        return html.toString();
    }
}