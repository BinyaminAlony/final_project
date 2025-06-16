package project_biu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import project_biu.server.RequestParser.RequestInfo;
import project_biu.servlets.Servlet;

/**
 * The MyHTTPServer class is an implementation of the HTTPServer interface.
 * It provides functionality to handle incoming HTTP requests, manage servlets,
 * and run the server using a thread pool for concurrent request processing.
 */
public class MyHTTPServer extends Thread implements HTTPServer{
    
	private ServerSocket serverSocket;
    private ExecutorService threadPool;
	private boolean stopMe;
	
	private ConcurrentHashMap<String,Servlet> getCommandMap;
	private ConcurrentHashMap<String,Servlet> putCommandMap;
	private ConcurrentHashMap<String,Servlet> deleteCommandMap;
	private ConcurrentHashMap<String, ConcurrentHashMap<String,Servlet>> requestTypeMap;

    /**
     * Constructs a MyHTTPServer instance with the specified port and thread pool size.
     *
     * @param port The port number on which the server will listen.
     * @param nThreads The number of threads in the thread pool.
     * @throws Exception If an error occurs during server initialization.
     */
    public MyHTTPServer(int port,int nThreads) throws Exception{
        this.serverSocket = new ServerSocket(port);
        this.threadPool = Executors.newFixedThreadPool(nThreads);
    	this.stopMe = true;

        this.getCommandMap = new ConcurrentHashMap<String,Servlet>();
        this.putCommandMap = new ConcurrentHashMap<String,Servlet>();
        this.deleteCommandMap = new ConcurrentHashMap<String,Servlet>();

        this.requestTypeMap = new ConcurrentHashMap<String, ConcurrentHashMap<String,Servlet>>();
        this.requestTypeMap.put("GET", getCommandMap);
        this.requestTypeMap.put("POST", putCommandMap);
        this.requestTypeMap.put("DELETE", deleteCommandMap);
        // Add more HTTP methods as needed
        // Initialize servlets - in Main.java
    }

    /**
     * Adds a servlet to handle requests for the specified HTTP command and URI.
     *
     * @param httpCommand The HTTP command (e.g., GET, POST, DELETE).
     * @param uri The URI for which the servlet will handle requests.
     * @param s The servlet to add.
     */
    public void addServlet(String httpCommand, String uri, Servlet s){
    	ConcurrentHashMap<String,Servlet> commandMap;
    	if((commandMap = requestTypeMap.get(httpCommand))!= null) {
    		commandMap.putIfAbsent(uri, s);
    	}else {
    		throw new IllegalArgumentException("Unexpected value: " + httpCommand);
    	}
    }

    /**
     * Removes a servlet that handles requests for the specified HTTP command and URI.
     *
     * @param httpCommand The HTTP command (e.g., GET, POST, DELETE).
     * @param uri The URI for which the servlet will be removed.
     */
    public void removeServlet(String httpCommand, String uri){
    	ConcurrentHashMap<String,Servlet> commandMap;
    	if((commandMap = requestTypeMap.get(httpCommand))!= null) {
    		commandMap.remove(uri);
    	}else {
			throw new IllegalArgumentException("Unexpected value: " + httpCommand);
		}
    }

    /**
     * Starts the server and begins listening for incoming requests.
     */
    @Override
    public void run() {
        stopMe = false;
        while (!stopMe) {
            try {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();

                RequestInfo ri = RequestParser.parseRequest(in);
                Servlet s = findMatchingServlet(ri.getHttpCommand(), ri.getUriSegments());

                if (s != null) {
                    threadPool.submit(() -> {
                        try {
                            s.handle(ri, out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    clientSocket.close();
                    throw new IllegalArgumentException("Unexpected value: " + ri.getHttpCommand() + " " + String.join("\\", ri.getUriSegments()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the server and releases resources.
     */
    public void close(){
        stopMe = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }
    
    private Servlet findMatchingServlet(String httpCommand, String[] uriSegments) {
        ConcurrentHashMap<String, Servlet> commandMap = requestTypeMap.get(httpCommand);
        if (commandMap == null) return null;
        for (int i = uriSegments.length; i >= 0; i--) {
            StringBuilder pathBuilder = new StringBuilder();
            for (int j = 0; j < i; j++) {
                pathBuilder.append("/").append(uriSegments[j]);
            }
            String candidatePath = pathBuilder.toString();
            Servlet s = commandMap.get(candidatePath);
            if (s != null) {
                return s;
            }
        }
        return null;
    }

}
