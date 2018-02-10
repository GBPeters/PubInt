package nl.gijspeters.pubint.export.csv.resultgraph;

import nl.gijspeters.pubint.export.csv.CSVDocument;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.model.ModelResultGraph;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by gijspeters on 10-02-18.
 */
public class ResultGraphDocument implements CSVDocument {

    private ModelResultGraph<Edge> resultGraph;

    public ResultGraphDocument(ModelResultGraph<Edge> resultGraph) {
        this.resultGraph = resultGraph;
    }

    @Override
    public String getHeader() {
        return null;
    }

    @Override
    public Iterator<String> iterator() {
        return new LineIterator();
    }

    private class LineIterator implements Iterator<String> {

        Iterator<Edge> iter = resultGraph.keySet().iterator();

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Edge e = iter.next();
            return new EdgeProbabilityLine(e, resultGraph.get(e)).getLineString();
        }
    }
}
