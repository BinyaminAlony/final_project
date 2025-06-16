package project_biu.servlets;

import java.io.IOException;
import java.io.OutputStream;

import project_biu.server.RequestParser.RequestInfo;

/**
 * The Servlet interface defines the contract for handling HTTP requests and responses.
 * Implementations of this interface are responsible for processing incoming requests
 * and generating appropriate responses to be sent back to the client.
 */
public interface Servlet {

    /**
     * Handles an incoming HTTP request and generates a response.
     *
     * @param ri The RequestInfo object containing details about the incoming request.
     * @param toClient The OutputStream to which the response should be written.
     * @throws IOException If an I/O error occurs during request handling.
     */
    void handle(RequestInfo ri, OutputStream toClient) throws IOException;

    /**
     * Closes any resources associated with the servlet.
     *
     * @throws IOException If an I/O error occurs during resource cleanup.
     */
    void close() throws IOException;
}
