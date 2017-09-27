package cr.ac.ucr.ecci.ci1330.IoC;

public
class Edge {
    public Node start;
    public Node end;

    public Edge(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public Node getEnd() {
        return end;
    }
}
