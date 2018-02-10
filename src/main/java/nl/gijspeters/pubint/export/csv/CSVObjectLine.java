package nl.gijspeters.pubint.export.csv;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gijspeters on 27-02-17.
 * <p>
 * Extensions of this class represent individual lines in a CSV file, generated from the contents of T
 *
 * @param <T> The class to generate a CSV line from
 */
public abstract class CSVObjectLine<T> extends CSVLine {

    private Map<String, Object> lineData = new HashMap<>();

    protected final int index;

    /**
     * Public constructor
     *
     * @param typevar T object containing the source data
     * @param index   the iteration index
     */
    public CSVObjectLine(T typevar, int index) {
        this.index = index;
        setData(typevar);
    }

    /**
     * Store a value of any type under the given key. Only keys provided by getKeys() can be used
     *
     * @param key   String containing the key
     * @param value The value to be written for this specific key of any type. All types will be converted to their
     *              String representations.
     */
    protected final void setValue(String key, Object value) {
        if (!Arrays.asList(getKeys()).contains(key)) {
            throw new IllegalArgumentException("Key not in KEYS array");
        }
        lineData.put(key, value);
    }

    /**
     * Store the values retrieved from the T object in this line using setValue(key, value)
     * The line index is available at this.index. You can use it, but it is not required.
     *
     * @param data The T object to be written
     */
    protected abstract void setData(T data);

    /**
     * Convert this line's data to a single String
     *
     * @return A String
     */
    @Override
    public final String getLineString() {
        String line = "";
        for (String key : getKeys()) {
            line += lineData.get(key) + SEPARATOR;
        }
        return line.substring(0, line.length() - 1);
    }

}
