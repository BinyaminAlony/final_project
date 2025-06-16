package project_biu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * The RequestParser class is responsible for parsing HTTP requests.
 * It extracts the request method, URI, headers, query parameters, and content
 * from the incoming request and organizes them into a RequestInfo object.
 */
public class RequestParser {

    /**
     * Parses an HTTP request from the provided BufferedReader.
     *
     * @param reader The BufferedReader containing the HTTP request.
     * @return A RequestInfo object containing the parsed request details.
     * @throws IOException If an I/O error occurs during request parsing.
     */
    public static RequestInfo parseRequest(BufferedReader reader) throws IOException {
	    // Read request line
	    String requestLine = reader.readLine();
	    if (requestLine == null || requestLine.isEmpty()) {
	        throw new IOException("Empty request");
	    }

	    String[] requestParts = requestLine.split(" ");
	    if (requestParts.length < 2) {
	        throw new IOException("Invalid request line");
	    }

	    String method = requestParts[0];
	    String fullUri = requestParts[1];

	    // Parse URI and query parameters
	    String path = fullUri;
	    Map<String, String> parameters = new HashMap<>();
	    byte[] content = {};// Read content until an empty line (just "\n")
	    
	    int queryIndex = fullUri.indexOf('?');
	    if (queryIndex != -1) {
	        path = fullUri.substring(0, queryIndex);
	        String queryString = fullUri.substring(queryIndex + 1);
	        parseParameterString(queryString, parameters);
	    }

	    // Parse URI segments
	    String[] uriSegmentsRaw = path.split("/");
	    List<String> filteredSegments = new ArrayList<>();
	    for (String segment : uriSegmentsRaw) {
	        if (!segment.isEmpty()) filteredSegments.add(segment);
	    }
	    String[] uriSegments = filteredSegments.toArray(new String[0]);

	    // Read headers until empty line
	    Map<String, String> headers = new HashMap<>();
	    String line;
	    while ((line = reader.readLine()) != null && !line.isEmpty()) {
	        int colonIndex = line.indexOf(":");
	        if (colonIndex != -1) {
	            String key = line.substring(0, colonIndex).trim();
	            String value = line.substring(colonIndex + 1).trim();
	            headers.put(key, value);
	        }
	    }
	    // making sure we read body parameters and content only if there is a "C
	    String contentLengthString;
	    if((contentLengthString = headers.get("Content-Length"))!= null) {
	    	int contentLengthLeft = Integer.parseInt(contentLengthString);

			String contentType = headers.get("Content-Type");
			String boundary = null;
			if (contentType != null && contentType.contains("multipart/form-data")) {
				int index = contentType.indexOf("boundary=");
				if (index != -1) {
					boundary = "--" + contentType.substring(index + 9).trim() + "--"; // Extract boundary
				}
			}
		    // Read body parameters until empty line
		    while ((line = reader.readLine()) != null && line != boundary && !line.isEmpty() && contentLengthLeft > 0) {
		        String[] LinesPutTogether = line.split(";");
				for (String codeLine : LinesPutTogether) {
					String[] keyVal = codeLine.split("=", 2);
					if (keyVal.length == 2) {
						parameters.put(keyVal[0].replaceAll("\"","").trim(), keyVal[1].replaceAll("\"","").trim());
					}
				}
		        contentLengthLeft -= 1;
		    }

		    // Read content until EOF or Content-Length
		    StringBuilder contentBuilder = new StringBuilder();
		    while ((line = reader.readLine()) != null && !line.equals(boundary) && contentLengthLeft > 0) {
		        // if (line.isEmpty()) {
		        //     break; // Stop reading content if we encounter a blank line
		        // }
		        contentBuilder.append(line).append('\n');
		        contentLengthLeft -= 1;
		    }
		    content = contentBuilder.toString().getBytes();
	    }
		System.out.println("Request parsed: " + method + " " + fullUri);
	    return new RequestInfo(
	        method,
	        fullUri,
	        uriSegments,
	        parameters,
	        content
	    );
	}

    /**
     * Parses a query string into a map of key-value pairs.
     *
     * @param queryString The query string to parse.
     * @param parameters The map to store the parsed key-value pairs.
     */
	private static void parseParameterString(String queryString, Map<String, String> parameters) {
	    for (String pair : queryString.split("&")) {
	        String[] keyVal = pair.split("=", 2);
	        if (keyVal.length == 2) {
	            parameters.put(keyVal[0], keyVal[1]);
	        }
	    }
	}
	
	/**
	 * Represents the parsed details of an HTTP request.
	 */
    public static class RequestInfo {
        private final String httpCommand;
        private final String uri;
        private final String[] uriSegments;
        private final Map<String, String> parameters;
        private final byte[] content;

        public RequestInfo(String httpCommand, String uri, String[] uriSegments, Map<String, String> parameters, byte[] content) {
            this.httpCommand = httpCommand;
            this.uri = uri;
            this.uriSegments = uriSegments;
            this.parameters = parameters;
            this.content = content;
        }

        public String getHttpCommand() {
            return httpCommand;
        }

        public String getUri() {
            return uri;
        }

        public String[] getUriSegments() {
            return uriSegments;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public byte[] getContent() {
            return content;
        }
    }
}
