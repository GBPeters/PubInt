package nl.gijspeters.pubint.export.csv.validate;

import nl.gijspeters.pubint.export.csv.CSVObjectDocument;
import nl.gijspeters.pubint.validation.VisitedExample;

import java.util.Collection;

public class ResultDocument extends CSVObjectDocument<VisitedExample, ResultLine> {

    /**
     * Public constructor
     *
     * @param data A Collection of T objects
     */
    public ResultDocument(Collection<VisitedExample> data) {
        super(data);
    }

    @Override
    protected ResultLine createCSVLine(VisitedExample lineData, int index) {
        return new ResultLine(lineData, index);
    }
}
