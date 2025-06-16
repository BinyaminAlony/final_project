// file was removed, couldnt make this work atm but might work on it in future versions.

// package project_biu.servlets;

// import java.io.IOException;
// import java.io.OutputStream;
// import java.util.List;

// import project_biu.server.RequestParser.RequestInfo;
// import project_biu.views.AddNodeForm;

// public class AddNodeFormDisplayer implements Servlet {

//     @Override
//     public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
//         List<String> agentTypes = List.of("sensor", "actuator", "controller", "monitor");
//         String html = AddNodeForm.getHtml(agentTypes);
//         toClient.write(html.getBytes());
//         toClient.flush();
//     }

//     @Override
//     public void close() throws IOException {
//         // nothing to close
//     }

// }
