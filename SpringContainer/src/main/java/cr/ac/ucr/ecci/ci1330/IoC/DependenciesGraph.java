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
        return cycle;
    }

    public boolean edgeExist(String start, String end) {
        boolean found = false;
        Node node = getNodePerName(start);
        int i = 0;
        while (i < node.getEdgesSize() && !found) {
            if (node.getNeighbour(i).getName() == end) {
                found = true;
            }
            i++;
        }
        return found;
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

    public int getNodesSize() {
        return nodes.size();
    }

    public int getEdgeNodeSize(String name) {
        return getNodePerName(name).getEdgesSize();
    }
}

class Node {
    public String name;
    public ArrayList<Edge> edges;

    public Node() {
    }

    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getNeighbour(int i) {
        if (i < edges.size()) {
            return edges.get(i).getEnd();
        } else {
            return null;
        }
    }

    public int getEdgesSize() {
        return edges.size();
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
}

class Edge {
    public Node start;
    public Node end;

    public Edge(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }
}
