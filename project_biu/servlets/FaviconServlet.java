package project_biu.servlets;

import java.io.*;
import project_biu.server.RequestParser.RequestInfo;

/**
 * The FaviconServlet class is responsible for serving the favicon icon in response to HTTP requests.
 * It reads the favicon file from the specified path and sends it to the client.
 * If the file is not found, it returns a 404 error response.
 */
public class FaviconServlet implements Servlet {

    /**
     * Handles an HTTP request to serve the favicon icon.
     *
     * @param ri The RequestInfo object containing details about the incoming request.
     * @param toClient The OutputStream to which the response should be written.
     * @throws IOException If an I/O error occurs during request handling.
     */
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        File faviconFile = new File("resources/favicon.png"); // Adjust path as needed

        if (!faviconFile.exists()) {
            toClient.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
            toClient.flush();
            return;
        }

        byte[] iconBytes = java.nio.file.Files.readAllBytes(faviconFile.toPath());

        String headers = 
              "HTTP/1.1 200 OK\r\n"
            + "Content-Type: image/x-icon\r\n"
            + "Content-Length: " + iconBytes.length + "\r\n"
            + "\r\n";

        toClient.write(headers.getBytes());
        toClient.write(iconBytes);
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
}