package project_biu.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import project_biu.graph.Agent;
import project_biu.server.RequestParser.RequestInfo;

/**
 * The AgentClassUpload class is responsible for handling HTTP requests to upload java files for agent classes.
 * It processes incoming requests to upload and validate the java file, 
 * and puts it in the .config directory, to be used in configs by the user.
 */
public class AgentClassUpload implements Servlet {

    /**
     * Handles an HTTP request to load an Agent class file.
     *
     * @param ri The RequestInfo object containing details about the incoming request.
     * @param toClient The OutputStream to which the response should be written.
     * @throws IOException If an I/O error occurs during request handling.
     */
    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        try{
            if ("POST".equalsIgnoreCase(ri.getHttpCommand())) {   
                
                
                
                // Validate and save the configuration file
                String classFilePath = "project_biu/configs/" + ri.getParameters().get("filename");
                File configsDir = new File("project_biu/configs");

                // Check if the file name already exists in the configs directory.
                if (configsDir.exists() && configsDir.isDirectory()) {
                    String[] existingFiles = configsDir.list();
                    if (existingFiles != null && Arrays.asList(existingFiles).contains(ri.getParameters().get("filename"))) {
                        String errorMessage = "Class name is reserved. Please change the class name and try again.";
                        toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                        toClient.flush();
                        return;
                    }
                }
                // Ensure the file is a .java file
                if (!classFilePath.endsWith(".java")) {
                    String errorMessage = "Invalid file type. Only .conf files are allowed.";
                    toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                    toClient.flush();
                    return;
                }
                // save the uploaded file. if an error occurs, send an error message to the client.
                try (FileOutputStream fos = new FileOutputStream(classFilePath)) {
                    fos.write(ri.getContent());
                }
                catch(Exception e) {
                    String errorMessage = "Error saving file: " + e.getMessage();
                    toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                    toClient.flush();
                    return;
                }

                // Compile the .java file before loading the class
                try {
                    Process compileProcess = Runtime.getRuntime().exec(
                        "javac " + classFilePath
                    );
                    int compileResult = compileProcess.waitFor();
                    if (compileResult != 0) {
                        String errorMessage = "Compilation failed. Please check your code.";
                        toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                        toClient.flush();
                        removeFile(classFilePath);
                        return;
                    }
                } catch (Exception e) {
                    String errorMessage = "Error during compilation: " + e.getMessage();
                    toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                    toClient.flush();
                    removeFile(classFilePath);
                    return;
                }

                // Now check if the uploaded class implements the Agent interface
                try {
                    String className = classFilePath.replace(".java", "").replace("project_biu/configs/", "project_biu.configs.");
                    Class<?> clazz = Class.forName(className);
                    if (!Agent.class.isAssignableFrom(clazz) || Modifier.isAbstract(clazz.getModifiers())) {
                        String errorMessage = "Uploaded class does not implement the Agent interface or is abstract.";
                        toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                        toClient.flush();
                        removeFile(classFilePath);
                        // Also remove the .class file
                        File classFile = new File(classFilePath.replace(".java", ".class"));
                        if (classFile.exists()) classFile.delete();
                        return;
                    }
                } catch (ClassNotFoundException e) {
                    String errorMessage = "Error loading class: " + e.getMessage();
                    toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
                    toClient.flush();
                    removeFile(classFilePath);
                    File classFile = new File(classFilePath.replace(".java", ".class"));
                    if (classFile.exists()) classFile.delete();
                    return;
                }
                // If the class is valid, add it to the configs directory and send a success message to the client.
                String successMessage = "Upload successful - you can now use "+ ri.getParameters().get("filename").replace(".java", "") + " agent in your graph.";
                toClient.write(successMessage.getBytes(StandardCharsets.UTF_8));
                toClient.flush();
            }
        } catch (IOException e) {
            String errorMessage = "Error - check file and try again: " + e.getMessage();
            toClient.write(errorMessage.getBytes(StandardCharsets.UTF_8));
            toClient.flush();
            return;
        }
    }

    private void removeFile(String classFilePath) {
        File uploadedFile = new File(classFilePath);
            if (uploadedFile.exists()) {
                uploadedFile.delete();
            }
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
