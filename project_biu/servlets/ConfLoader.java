package project_biu.servlets;

import project_biu.configs.GenericConfig;
import project_biu.graph.Graph;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;

import project_biu.server.RequestParser.RequestInfo;
import project_biu.views.HtmlGraphViewer;
import project_biu.graph.TopicManagerSingleton;

/**
 * The ConfLoader class is responsible for handling HTTP requests to load configuration files.
 * It processes incoming requests to upload and validate configuration files, and generates
 * an HTML response displaying the graph created from the configuration.
 */
public class ConfLoader implements Servlet {

    /**
     * Handles an HTTP request to load a configuration file and generate a graph.
     *
     * @param ri The RequestInfo object containing details about the incoming request.
     * @param toClient The OutputStream to which the response should be written.
     * @throws IOException If an I/O error occurs during request handling.
     */
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        try{
            if ("POST".equalsIgnoreCase(ri.getHttpCommand())) {
                // Clear existing topics to avoid conflicts with new configuration
                TopicManagerSingleton.get().clear();
                
                // Validate and save the configuration file
                String configFilePath = "config_files/" + ri.getParameters().get("filename");
                if (!configFilePath.endsWith(".conf")) {
                    String errorMessage = "Invalid file type. Only .conf files are allowed.";
                    toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                    toClient.flush();
                    return;
                }
                try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
                    fos.write(ri.getContent());
                }
                GenericConfig gc = new GenericConfig();
                gc.setConfFile("config_files/" + ri.getParameters().get("filename"));
                try{
                    gc.create();
                }
                catch (Exception e) {
                    String errorMessage = buildStyledErrorHtml("Error Loading Configuration", e.getMessage());
                    toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                    toClient.flush();
                    return;
                }
                // Create the graph
                Graph graph = new Graph();
                graph.createFromTopics();

                //checking for cycles
                if (graph.hasCycles()) {
                    String errorMessage = buildStyledErrorHtml("Cycle Detected", "The graph contains cycles, which is not allowed in calculational graphs.");
                    toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                    toClient.flush();
                    return;
                }

                // Generate the HTML representation of the graph
                String[] html = HtmlGraphViewer.getGraphHTML(graph);
                // Send the HTML response to the client
                for(String line : html){
                    toClient.write((line + "\n").getBytes());
                }
                toClient.flush();

            }
            else{
                String errorMessage = "Unsupported HTTP method: " + ri.getHttpCommand();
                toClient.write(errorMessage.getBytes());
                toClient.flush();
            }
        } catch (IOException e) {
            String errorMessage = buildStyledErrorHtml("Error", e.getMessage());
            toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
            toClient.flush();
        }
    }
    /**
     * Builds a styled HTML error message.
     *
     * @param errorTitle The title of the error.
     * @param errorMsg The error message to display.
     * @return A string containing the styled HTML representation of the error.
     */
    private String buildStyledErrorHtml(String errorTitle, String errorMsg) {
        return "<!DOCTYPE html><html><head><title>Error</title>"
            + "<style>"
            + "body { background:#222; color:#fff; font-family:'Segoe UI',Arial,sans-serif; display:flex; flex-direction:column; align-items:center; justify-content:center; min-height:100vh; margin:0; }"
            + ".topics-box { background:#333; border-radius:18px; box-shadow:0 4px 24px #0008; padding:24px 18px 18px 18px; width:80%; max-width:900px; margin: 32px auto 0 auto; text-align:center; box-sizing: border-box; }"
            + ".error-title { color:#ff5252; font-size:1.5em; margin-bottom:12px; font-weight:bold; }"
            + ".error-msg { color:#fff; font-size:1.1em; }"
            + "</style></head><body>"
            + "<div class='topics-box'>"
            + "<div class='error-title'>" + errorTitle + "</div>"
            + "<div class='error-msg'>" + errorMsg + "</div>"
            + "</div></body></html>";
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
}
