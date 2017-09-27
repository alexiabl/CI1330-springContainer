package cr.ac.ucr.ecci.ci1330.IoC;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Renato on 27/09/2017.
 */
public class DependenciesGraph {
    private ArrayList<Node> nodes;
    private HashMap<String, Node> visited;

    public DependenciesGraph() {
        this.nodes = new ArrayList<>();
        this.visited = new HashMap<>();
    }

    public void addNode(String name) {
        Node node = new Node(name);
        nodes.add(node);
    }

    public void addEdge(String start, String end) {
        Edge edge = new Edge(getNodePerName(start), getNodePerName(end));
        getNodePerName(start).addEdge(edge);
    }

    public boolean reviewCyclesBean() {
        boolean cycle = false;
        if (nodes.size() > 1) {
            cycle = reviewCyclesBeanRec(this, nodes.get(0));
        }
        return cycle;
    }

    public boolean reviewCyclesBeanRec(DependenciesGraph graph, Node node) {
        boolean cycle = false;
        if (node.getEdgesSize()>0) {
            visited.put(node.getName(), node);
        }
        Node nodeAdj = node.getNeighbour(0);
        int position = 1;
        if (nodeAdj != null && visited.containsKey(nodeAdj.getName())) {
            cycle = true;
            System.out.println("El ciclo es porque " + node.getName() +" vuelve a: " + nodeAdj.getName());
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

    public Node getNodePerName(String name) {
        Node node = null;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getName() == name) {
                node = nodes.get(i);
            }
        }
        return node;
    }

    public int getEdgeNodeSize(String name) {
        return getNodePerName(name).getEdgesSize();
    }
}

