package project_biu.views;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import project_biu.graph.Graph;
import project_biu.graph.Node;

/**
 * HtmlGraphViewer is responsible for rendering a graph in HTML format, which will be views in the graphFrame in index.html.
 */
public class HtmlGraphViewer {

    /**
     * Generates an HTML representation of the graph.
     *
     * @return A string containing the HTML representation of the graph.
     */
    public static String[] getGraphHTML(Graph graph) throws IOException {
        List<Map<String, String>> nodesList = new ArrayList<>();
        List<Map<String, String>> linksList = new ArrayList<>();

        for (Node node : graph) {
            Map<String, String> nodeMap = new HashMap<>();
            String name = node.getName();
            nodeMap.put("id", name.substring(1));  // Remove "A"/"T" prefix for display
            nodeMap.put("type", name.startsWith("A") ? "agent" : "topic");
            nodesList.add(nodeMap);

            for (Node target : node.getEdges()) {
                Map<String, String> link = new HashMap<>();
                link.put("source", name.substring(1));
                link.put("target", target.getName().substring(1));
                linksList.add(link);
            }
        }

        String nodesJson = nodesList.stream()
            .map(m -> String.format("{\"id\":\"%s\",\"type\":\"%s\"}", m.get("id"), m.get("type")))
            .collect(Collectors.joining(",\n"));

        String linksJson = linksList.stream()
            .map(m -> String.format("{\"source\":\"%s\",\"target\":\"%s\"}", m.get("source"), m.get("target")))
            .collect(Collectors.joining(",\n"));

        String html = generateHtmlTemplate(nodesJson, linksJson);
        return html.split("\n");
    }

    private static String generateHtmlTemplate(String nodesJson, String linksJson) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>Graph Visualization</title>
            <script src="https://d3js.org/d3.v7.min.js"></script>
            <style>
                body {
                    background: #222;
                    color: #fff;
                    font-family: 'Segoe UI', Arial, sans-serif;
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: center;
                    min-height: 100vh;
                    margin: 0;
                }
                .graph-box {
                    background: #333;
                    border-radius: 16px;
                    box-shadow: 0 4px 24px #0008;
                    padding: 18px 18px 0 18px;
                    margin-top: 0;
                    text-align: center;
                    min-width: 0;
                    width: 80%%;
                    max-width: none;
                    height: 600px;
                    position: relative;
                    overflow: auto;
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: flex-start;
                    box-sizing: border-box;
                }
                h1 {
                    color: #4fc3f7;
                    font-size: 2em;
                    margin: 0 0 12px 0;
                    letter-spacing: 1px;
                }
                .agent {
                    fill: #1f77b4;
                    filter: drop-shadow(0 2px 8px #0008);
                    stroke: #fff;
                    stroke-width: 2px;
                }
                .topic {
                    fill: #ff7f0e;
                    filter: drop-shadow(0 2px 8px #0008);
                    stroke: #fff;
                    stroke-width: 2px;
                }
                .link {
                    stroke: #4fc3f7;
                    stroke-opacity: 0.7;
                    stroke-width: 3px;
                    fill: none;
                }
                text {
                    font: 18px 'Segoe UI', Arial, sans-serif;
                    fill: #fff;
                    text-anchor: middle;
                    dominant-baseline: middle;
                    pointer-events: none;
                    paint-order: stroke fill;
                    stroke: #222;
                    stroke-width: 2px;
                }
                .graph-svg {
                    background: transparent;
                    display: block;
                    margin: 0 auto 18px auto;
                    border-radius: 18px;
                    width: 100%%;
                    height: 100%%;
                }
            </style>
        </head>
        <body>
            <div class="graph-box">
                <h1>Graph Viewer</h1>
                <svg class="graph-svg" viewBox="0 0 900 500" preserveAspectRatio="xMidYMid meet"></svg>
            </div>
            <script>
                const nodes = [ %s ];
                const links = [ %s ];

                const svg = d3.select("svg.graph-svg");
                const width = 900;
                const height = 500;

                // Arrowhead marker, refX=10 so tip is at end of line
                svg.append("defs").append("marker")
                    .attr("id", "arrowhead")
                    .attr("viewBox", "0 -5 10 10")
                    .attr("refX", 10)
                    .attr("refY", 0)
                    .attr("markerWidth", 10)
                    .attr("markerHeight", 10)
                    .attr("orient", "auto")
                    .append("path")
                    .attr("d", "M0,-5L10,0L0,5")
                    .attr("fill", "#4fc3f7");

                const simulation = d3.forceSimulation(nodes)
                    .force("link", d3.forceLink(links).id(d => d.id).distance(140))
                    .force("charge", d3.forceManyBody().strength(-400))
                    .force("center", d3.forceCenter(width / 2, height / 2));

                // Draw links as straight lines
                const link = svg.append("g")
                    .selectAll("line")
                    .data(links)
                    .enter().append("line")
                    .attr("class", "link")
                    .attr("marker-end", "url(#arrowhead)");

                // Draw nodes
                const node = svg.append("g")
                    .selectAll("g")
                    .data(nodes)
                    .enter().append("g")
                    .call(d3.drag()
                        .on("start", function(event, d) {
                            if (!event.active) simulation.alphaTarget(0.3).restart();
                            d.fx = d.x;
                            d.fy = d.y;
                        })
                        .on("drag", function(event, d) {
                            d.fx = event.x;
                            d.fy = event.y;
                        })
                        .on("end", function(event, d) {
                            if (!event.active) simulation.alphaTarget(0);
                            d.fx = null;
                            d.fy = null;
                        })
                    );

                // Draw agent as circle, topic as rounded rect
                node.each(function(d) {
                    if (d.type === "agent") {
                        d3.select(this).append("circle")
                            .attr("r", 32)
                            .attr("class", "agent");
                    } else {
                        d3.select(this).append("rect")
                            .attr("x", -38)
                            .attr("y", -28)
                            .attr("width", 76)
                            .attr("height", 56)
                            .attr("rx", 18)
                            .attr("ry", 18)
                            .attr("class", "topic");
                    }
                });

                // Add text label
                node.append("text")
                    .text(d => d.id)
                    .attr("dy", 3)
                    .style("font-weight", "bold");

                simulation.on("tick", () => {
                    // Draw straight links with arrowhead at the edge of the target node
                    link
                        .attr("x1", d => {
                            const dx = d.target.x - d.source.x;
                            const dy = d.target.y - d.source.y;
                            const dist = Math.sqrt(dx*dx + dy*dy);
                            const pad = d.source.type === "agent" ? 32 : 38;
                            return d.source.x + (dx/dist)*pad;
                        })
                        .attr("y1", d => {
                            const dx = d.target.x - d.source.x;
                            const dy = d.target.y - d.source.y;
                            const dist = Math.sqrt(dx*dx + dy*dy);
                            const pad = d.source.type === "agent" ? 32 : 28;
                            return d.source.y + (dy/dist)*pad;
                        })
                        .attr("x2", d => {
                            const dx = d.source.x - d.target.x;
                            const dy = d.source.y - d.target.y;
                            const dist = Math.sqrt(dx*dx + dy*dy);
                            const pad = d.target.type === "agent" ? 32 : 38;
                            // Move endpoint back by marker length (10) so arrowhead sits at node edge
                            return d.target.x + (dx/dist)*pad - (dx/dist)*10;
                        })
                        .attr("y2", d => {
                            const dx = d.source.x - d.target.x;
                            const dy = d.source.y - d.target.y;
                            const dist = Math.sqrt(dx*dx + dy*dy);
                            const pad = d.target.type === "agent" ? 32 : 28;
                            return d.target.y + (dy/dist)*pad - (dy/dist)*10;
                        });

                    node.attr("transform", d => {
                        d.x = Math.max(40, Math.min(width-40, d.x));
                        d.y = Math.max(40, Math.min(height-40, d.y));
                        return `translate(${d.x},${d.y})`;
                    });
                });
            </script>
        </body>
        </html>
        """.formatted(nodesJson, linksJson);
    }
}
