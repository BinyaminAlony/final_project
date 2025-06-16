import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import project_biu.configs.GenericConfig;
import project_biu.graph.Agent;
import project_biu.graph.Graph;
import project_biu.graph.Message;
import project_biu.graph.TopicManagerSingleton;
import project_biu.views.HtmlGraphViewer;

public class MainTrain {
    
    public static void main(String[] args) {

        GenericConfig gc=new GenericConfig();
        gc.setConfFile("C:\\Users\\I7\\Documents\\studies\\סמסטר ח\\תכנות מתקדם\\final_project\\config_files\\simple.conf"); // change to the exact loaction where you put the file.
        gc.create();

        gc.close();
        
        Graph graph = new Graph();
        graph.createFromTopics();

        try {
            String[] htmlOutput = HtmlGraphViewer.getGraphHTML(graph);
            BufferedWriter bw = new BufferedWriter(new FileWriter("html_files/graph.html"));
            bw.write(String.join("\n", htmlOutput));
            bw.close();
            System.out.println("Graph HTML file created successfully at html_files/graph.html");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
