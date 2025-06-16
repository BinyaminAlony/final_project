package project_biu.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import project_biu.server.RequestParser.RequestInfo;

/**
 * The HtmlLoader class is responsible for serving HTML files in response to HTTP requests.
 * It loads the requested HTML file from the specified directory and sends it to the client.
 * If the file is not found, it returns a 404 error page.
 */
public class HtmlLoader implements Servlet {
	private static final byte[] NOT_FOUND_HTML = """
		<!DOCTYPE html>
		<html lang="en">
		<head>
			<meta charset="UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<title>404 - Page Not Found</title>
			<style>
				body {
					font-family: Arial, sans-serif;
					text-align: center;
					padding: 50px;
					background-color: #f8f9fa;
					color: #333;
				}
				h1 {
					font-size: 2em;
					color: #dc3545;
				}
				p {
					font-size: 1.2em;
				}
			</style>
		</head>
		<body>
			<h1>404 - Page Not Found</h1>
			<p>Sorry, the page you are looking for does not exist.</p>
		</body>
		</html>
	""".getBytes();

	String dirName;

	/**
	 * Constructs an HtmlLoader with the specified directory name.
	 *
	 * @param dirName The name of the directory containing the HTML files.
	 */
	public HtmlLoader(String dirName) {
		this.dirName = dirName;
	}

	/**
	 * Handles an HTTP request to serve an HTML file.
	 *
	 * @param ri The RequestInfo object containing details about the incoming request.
	 * @param toClient The OutputStream to which the response should be written.
	 * @throws IOException If an I/O error occurs during request handling.
	 */
	@Override
	public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
		byte[] responseBody;
		// System.out.println(ri.getUriSegments()[1]);
		java.nio.file.Path filePath = Paths.get(dirName + "\\" + ri.getUriSegments()[1]);
		if (Files.exists(filePath)) {
			responseBody = Files.readAllBytes(filePath);
		} else {
			responseBody = NOT_FOUND_HTML;
		}
	    toClient.write(responseBody);
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
