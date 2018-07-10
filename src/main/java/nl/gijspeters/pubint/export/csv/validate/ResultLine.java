package nl.gijspeters.pubint.export.csv.validate;

import nl.gijspeters.pubint.export.csv.CSVObjectLine;
import nl.gijspeters.pubint.validation.VisitedExample;

public class ResultLine extends CSVObjectLine<VisitedExample> {

    public static final String[] KEYS = {
            "user",
            "lon",
            "lat",
            "edgeid",
            "visitprobability"
    };

    /**
     * Public constructor
     *
     * @param typevar T object containing the source data
     * @param index   the iteration index
     */
    public ResultLine(VisitedExample typevar, int index) {
        super(typevar, index);
    }

    public String[] getKeys() {
        return KEYS;
    }

    @Override
    protected void setData(VisitedExample data) {
        setValue("user", data.getAnchor().getUser().getName());
        setValue("lon", data.getAnchor().getCoord().x);
        setValue("lat", data.getAnchor().getCoord().y);
        setValue("edgeid", data.getEdge().getEdgeId());
        setValue("visitprobability", data.getVisitProbability());
    }
}