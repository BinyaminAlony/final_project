// package project_biu.views;

// import java.util.List;

// /**
//  * AddNodeForm is responsible for handling the form used to add nodes to the graph.
//  * This class provides methods to render the form and process user input.
//  */
// public class AddNodeForm {
//     /**
//      * Renders the HTML form for adding a node.
//      *
//      * @return A string containing the HTML representation of the form.
//      */
//     public static String getHtml(List<String> agentTypes) {
//         StringBuilder agentTypeOptions = new StringBuilder();
//         for (String type : agentTypes) {
//             agentTypeOptions.append("<option value=\"").append(type).append("\">")
//                 .append(type.substring(0, 1).toUpperCase()).append(type.substring(1)).append(" Agent</option>");
//         }
//         System.out.println("Done generating agent types: " + agentTypes);
//         String html = """
//             <!DOCTYPE html>
//             <html lang=\"en\">
//             <head>
//                 <meta charset=\"UTF-8\">
//                 <title>Add Node</title>
//                 <style>
//                     body { background: transparent; color: #fff; font-family: 'Segoe UI', Arial, sans-serif; }
//                     .form-box {
//                         background: #333;
//                         border-radius: 18px;
//                         box-shadow: 0 4px 24px #0008;
//                         padding: 14px 10px;
//                         margin: 40px auto 0 auto;
//                         text-align: center;
//                         width: 80%%;
//                         max-width: none;
//                         min-width: 0;
//                         box-sizing: border-box;
//                     }
//                     h3 {
//                         color: #4fc3f7;
//                         margin-top: 0;
//                         margin-bottom: 10px;
//                         letter-spacing: 1px;
//                         font-size: 1.15em;
//                     }
//                     label {
//                         display: block;
//                         margin: 6px 0 3px 0;
//                         color: #bbb;
//                         font-size: 0.98em;
//                         text-align: left;
//                     }
//                     .input-group {
//                         margin-bottom: 10px;
//                         text-align: left;
//                     }
//                     input[type=\"text\"], select {
//                         padding: 5px 7px;
//                         border-radius: 6px;
//                         border: 1px solid #555;
//                         background: #222;
//                         color: #fff;
//                         font-size: 0.98em;
//                         margin-bottom: 4px;
//                         width: 95%%;
//                         transition: border 0.2s;
//                     }
//                     input[type=\"text\"]:focus, select:focus {
//                         border: 1.5px solid #4fc3f7;
//                         outline: none;
//                     }
//                     button {
//                         background: #4fc3f7;
//                         color: #222;
//                         border: none;
//                         border-radius: 6px;
//                         padding: 7px 18px;
//                         font-size: 1em;
//                         cursor: pointer;
//                         margin-top: 6px;
//                         margin-bottom: 6px;
//                         transition: background 0.2s, color 0.2s;
//                         font-weight: 500;
//                     }
//                     button:hover {
//                         background: #0288d1;
//                         color: #fff;
//                     }
//                 </style>
//             </head>
//             <body>
//                 <div class=\"form-box\">
//                     <h3>Add Node</h3>
//                     <form id=\"addNodeForm\" action=\"/addNode\" method=\"GET\" target=\"graphFrame\">
//                         <div class=\"input-group\">
//                             <label for=\"addType\">Action:</label>
//                             <select name=\"addType\" id=\"addType\" required onchange=\"updateAddNodeFields()\">
//                                 <option value=\"topic\">Add Topic</option>
//                                 <option value=\"agent\">Add Agent</option>
//                                 <option value=\"subscription\">Add Subscription</option>
//                                 <option value=\"publisher\">Add Publisher</option>
//                             </select>
//                         </div>
//                         <div class=\"input-group\">
//                             <label for=\"mainName\" id=\"mainNameLabel\">Topic/Agent Name:</label>
//                             <input type=\"text\" name=\"mainName\" id=\"mainName\" required>
//                         </div>
//                         <div class=\"input-group\" id=\"agentTypeGroup\" style=\"display:none;\">
//                             <label for=\"agentType\">Agent Type:</label>
//                             <select name=\"agentType\" id=\"agentType\">
//                                 %s
//                             </select>
//                         </div>
//                         <div class=\"input-group\">
//                             <label for=\"relatedName\" id=\"relatedNameLabel\">Publisher Name:</label>
//                             <input type=\"text\" name=\"relatedName\" id=\"relatedName\" required>
//                         </div>
//                         <button type=\"submit\">Add</button>
//                     </form>
//                 </div>
//                 <script>
//                 function updateAddNodeFields() {
//                     const type = document.getElementById('addType').value;
//                     const mainLabel = document.getElementById('mainNameLabel');
//                     const relatedLabel = document.getElementById('relatedNameLabel');
//                     const agentTypeGroup = document.getElementById('agentTypeGroup');
//                     if (type === 'topic') {
//                         mainLabel.textContent = 'Topic Name:';
//                         relatedLabel.textContent = 'Publisher Name:';
//                         agentTypeGroup.style.display = 'none';
//                     } else if (type === 'agent') {
//                         mainLabel.textContent = 'Agent Name:';
//                         relatedLabel.textContent = 'Subscribe to Topic:';
//                         agentTypeGroup.style.display = '';
//                     } else if (type === 'subscription') {
//                         mainLabel.textContent = 'Agent Name:';
//                         relatedLabel.textContent = 'Subscribing to:';
//                         agentTypeGroup.style.display = 'none';
//                     } else if (type === 'publisher') {
//                         mainLabel.textContent = 'Agent Name:';
//                         relatedLabel.textContent = 'Publishing to:';
//                         agentTypeGroup.style.display = 'none';
//                     }
//                 }
//                 document.getElementById('addType').addEventListener('change', updateAddNodeFields);
//                 updateAddNodeFields();
//                 </script>
//             </body>
//             </html>
//             """.formatted(agentTypeOptions.toString());
//         System.out.println("Generated Add Node Form HTML: ");
//         return html;
//     }

//     /**
//      * Processes the user input from the form.
//      *
//      * @param input The user input data.
//      */
//     public void processInput(String input) {
//         // Logic to process user input.
//         // This may involve validating the input and updating the graph.
//     }

//     /**
//      * Validates the user input.
//      *
//      * @param input The user input data.
//      * @return True if the input is valid, false otherwise.
//      */
//     public boolean validateInput(String input) {
//         // Logic to validate user input.
//         // This may involve checking for empty fields or invalid characters.
//     }
// }
