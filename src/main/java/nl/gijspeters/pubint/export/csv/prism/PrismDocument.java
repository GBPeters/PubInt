package nl.gijspeters.pubint.export.csv.prism;

import nl.gijspeters.pubint.export.csv.CSVDocument;
import nl.gijspeters.pubint.graph.state.PrismState;

import java.util.Collection;

/**
 * Created by gijspeters on 27-02-17.
 */
public class PrismDocument extends CSVDocument<PrismState, StateLine> {

    public PrismDocument(Collection<PrismState> data) {
        super(data);
    }

    @Override
    protected StateLine createCSVLine(PrismState lineData, int index) {
        return new StateLine(lineData, index);
    }
}
