package nl.gijspeters.pubint.validation;

import nl.gijspeters.pubint.graph.traversable.BasicEdge;

public class ValidatedExample {

    private BasicEdge edge;
    private boolean visited;
    private double visitProbability;

    public ValidatedExample(BasicEdge edge, boolean visited, double visitProbability) {
        setEdge(edge);
        setVisited(visited);
        setVisitProbability(visitProbability);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getVisitProbability() {
        return visitProbability;
    }

    public void setVisitProbability(double visitProbability) {
        assert (0 <= visitProbability & visitProbability <= 1);
        this.visitProbability = visitProbability;
    }

    public BasicEdge getEdge() {
        return edge;
    }

    public void setEdge(BasicEdge edge) {
        this.edge = edge;
    }
}
