import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import project_biu.server.MyHTTPServer;
import project_biu.server.RequestParser;
import project_biu.servlets.AgentClassUpload;
// import project_biu.servlets.AddNodeFormDisplayer;
// import project_biu.servlets.AddNodeToGraph;
import project_biu.servlets.ConfLoader;
import project_biu.servlets.FaviconServlet;
import project_biu.servlets.HtmlLoader;
import project_biu.servlets.TopicDisplayer;


public class Main { // RequestParser


    public static void startServer() throws Exception{
		MyHTTPServer server = new MyHTTPServer(80, 3);
		server.addServlet("GET", "/app", new HtmlLoader("html_files"));
		server.addServlet("POST", "/upload", new ConfLoader());
		server.addServlet("GET", "/publish", new TopicDisplayer());
		server.addServlet("POST", "/uploadAgent", new AgentClassUpload());

		// server.addServlet("GET", "/addNodeForm", new AddNodeFormDisplayer());
		// server.addServlet("GET", "/addNode", new AddNodeToGraph());

		server.addServlet("GET", "/favicon.ico", new FaviconServlet());
		server.start();
    }
    
    public class ThreadMonitor {
        public static void startMonitoring() {
            Thread monitor = new Thread(() -> {
                while (true) {
                    int threadCount = Thread.activeCount();
                    System.out.println("Thread count is " + threadCount);
    
                    try {
                        Thread.sleep(100); // check every 100ms
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
    
            monitor.setDaemon(true); // won't prevent program from exiting
            monitor.start();
        }
    }

    public static void main(String[] args) {
    	// ThreadMonitor.startMonitoring();
        try{
            startServer(); // 60
        }catch(Exception e){
            System.out.println("your server throwed an exception");
            e.printStackTrace();
        }
        System.out.println("server up and running on port 80");
    }
}
