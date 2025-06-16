package project_biu.server;
import project_biu.servlets.Servlet;

/**
 * The HTTPServer interface defines the contract for an HTTP server.
 * It provides methods to add and remove servlets, start the server, and close it gracefully.
 */
public interface HTTPServer extends Runnable {

    /**
     * Adds a servlet to handle requests for the specified HTTP command and URI.
     *
     * @param httpCommand The HTTP command (e.g., GET, POST, DELETE).
     * @param uri The URI for which the servlet will handle requests.
     * @param s The servlet to add.
     */
    void addServlet(String httpCommand, String uri, Servlet s);

    /**
     * Removes a servlet for the specified HTTP command and URI.
     *
     * @param httpCommand The HTTP command (e.g., GET, POST, DELETE).
     * @param uri The URI for which the servlet will be removed.
     */
    void removeServlet(String httpCommand, String uri);

    /**
     * Starts the server and begins listening for incoming requests.
     */
    void start();

    /**
     * Closes the server and releases resources.
     */
    void close();
}
