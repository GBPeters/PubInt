package nl.gijspeters.pubint.export.csv.validate;

import nl.gijspeters.pubint.export.csv.CSVObjectLine;
import nl.gijspeters.pubint.validation.ValidatedExample;

public class ValidationLine extends CSVObjectLine<ValidatedExample> {


    public static final String[] KEYS = {
            "edgeid",
            "streetedge",
            "visited",
            "visitprobability"
    };

    /**
     * Public constructor
     *
     * @param typevar T object containing the source data
     * @param index   the iteration index
     */
    public ValidationLine(ValidatedExample typevar, int index) {
        super(typevar, index);
    }

    @Override
    protected void setData(ValidatedExample data) {
        setValue("edgeid", data.getEdge().getEdgeId());
        setValue("streetedge", data.getEdge().isStreetEdge());
        setValue("visited", data.isVisited());
        setValue("visitprobability", data.getVisitProbability());
    }

    @Override
    public String[] getKeys() {
        return KEYS;
    }
}
