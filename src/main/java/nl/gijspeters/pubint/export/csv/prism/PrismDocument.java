package nl.gijspeters.pubint.export.csv.prism;

import nl.gijspeters.pubint.export.csv.CSVObjectDocument;
import nl.gijspeters.pubint.graph.state.PrismState;

import java.util.Collection;

/**
 * Created by gijspeters on 27-02-17.
 *
 * CSVObjectDocument for writing prisms (list of PrismStates) to a CSV file
 */
public class PrismDocument extends CSVObjectDocument<PrismState, StateLine> {

    public PrismDocument(Collection<PrismState> data) {
        super(data);
    }

    @Override
    protected StateLine createCSVLine(PrismState lineData, int index) {
        return new StateLine(lineData, index);
    }
}
