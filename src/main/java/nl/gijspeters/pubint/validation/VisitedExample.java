package nl.gijspeters.pubint.validation;

import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.structure.Anchor;

public class VisitedExample extends ValidatedExample {

    private Anchor anchor;

    public VisitedExample(Anchor anchor, BasicEdge edge, double visitProbability) {
        super(edge, true, visitProbability);
        setAnchor(anchor);
    }


    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

}
