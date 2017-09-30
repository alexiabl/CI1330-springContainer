package cr.ac.ucr.ecci.ci1330.IoC.BeanGraph;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Bean Graph
 * @author Renato Mainieri
 */

public class BeanGraph {
    private ArrayList<Node> nodes;
    private HashMap<String, Node> visited;

    public BeanGraph() {
        this.nodes = new ArrayList<>();
        this.visited = new HashMap<>();
    }

    public void addNode(String name) {
        Node node = new Node(name);
        nodes.add(node);
    }

    public void addEdge(String start, String end) {
        Edge edge = new Edge(getNodeForName(start), getNodeForName(end));
        getNodeForName(start).addEdge(edge);
    }

    public boolean reviewCyclesBean() {
        boolean cycle = false;
        if (nodes.size() > 1) {
            cycle = reviewCyclesBeanRec(this, nodes.get(0));
        }
        return cycle;
    }

    public boolean reviewCyclesBeanRec(BeanGraph graph, Node node) {
        boolean cycle = false;
        if (node.getNumberOfEdges() > 0) {
            visited.put(node.getName(), node);
        }
        Node nodeAdj = node.getNeighbour(0);
        int position = 1;
        if (nodeAdj != null && visited.containsKey(nodeAdj.getName())) {
            cycle = true;
            System.out.println("ERROR! Hay un ciclo, ocurre porque \"" + node.getName() + "\" tiene una referencia de \"" + nodeAdj.getName() + "\".");
        }
        while (!cycle && nodeAdj != null) {
            cycle = reviewCyclesBeanRec(graph, nodeAdj);
            if (!cycle) {
                nodeAdj = node.getNeighbour(position);
                position++;
            }
        }
        if (!cycle) {
            visited.remove(node.getName());
        }
        return cycle;
    }

    public Node getNodeForName(String name) {
        Node node = null;
        int i = 0;
        boolean found = false;
        while (i<nodes.size() && !found) {
            if (nodes.get(i).getName().equals(name)) {
                node = nodes.get(i);
                found = true;
            }
            i++;
        }
        return node;
    }

}

