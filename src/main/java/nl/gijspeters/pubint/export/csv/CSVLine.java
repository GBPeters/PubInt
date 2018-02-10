package nl.gijspeters.pubint.export.csv;

/**
 * Created by gijspeters on 10-02-18.
 */
public abstract class CSVLine {


    public static final String SEPARATOR = ";";

    /**
     * Each CSVObjectLine extension should return a fixed set of keys. Only data for these keys are accepted, and these keys
     * are used as header for the CSV file.
     *
     * @return An array of Strings
     */
    public abstract String[] getKeys();

    public abstract String getLineString();

    /**
     * Convert this line's keys to a single header String
     * @return A String
     */
    public final String getHeaderString() {
        String line = "";
        for (String key : getKeys()) {
            line += key + SEPARATOR;
        }
        return line.substring(0, line.length() - 1);
    }
}
