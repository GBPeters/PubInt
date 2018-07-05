package nl.gijspeters.pubint.export.csv.validate;

import nl.gijspeters.pubint.export.csv.CSVObjectDocument;
import nl.gijspeters.pubint.validation.ValidatedExample;

import java.util.Collection;

public class ValidationDocument extends CSVObjectDocument<ValidatedExample, ValidationLine> {
    /**
     * Public constructor
     *
     * @param data A Collection of T objects
     */
    public ValidationDocument(Collection<ValidatedExample> data) {
        super(data);
    }

    @Override
    protected ValidationLine createCSVLine(ValidatedExample lineData, int index) {
        return new ValidationLine(lineData, index);
    }
}
