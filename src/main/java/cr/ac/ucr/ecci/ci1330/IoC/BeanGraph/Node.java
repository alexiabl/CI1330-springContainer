package cr.ac.ucr.ecci.ci1330.IoC.BeanGraph;

import java.util.ArrayList;

/**
 * Node
 * @author Renato Mainieri
 */
public class Node {
    public String name;
    public ArrayList<Edge> edges;

    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Node getNeighbour(int i) {
        if (i < edges.size()) {
            return edges.get(i).getEnd();
        } else {
            return null;
        }
    }

    public int getNumberOfEdges() {
        return edges.size();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
}
