package nl.gijspeters.pubint.export.csv;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gijspeters on 27-02-17.
 */
public abstract class CSVLine<T> {

    private Map<String, Object> lineData = new HashMap<>();

    public static final String SEPARATOR = ";";

    public abstract String[] getKeys();

    protected final int index;

    public CSVLine(T typevar, int index) {
        this.index = index;
        setData(typevar);
    }

    protected final void setValue(String key, Object value) {
        if (!Arrays.asList(getKeys()).contains(key)) {
            throw new IllegalArgumentException("Key not in KEYS array");
        }
        lineData.put(key, value);
    }

    protected abstract void setData(T data);

    public final String getLineString() {
        String line = "";
        for (String key : getKeys()) {
            line += lineData.get(key) + SEPARATOR;
        }
        return line.substring(0, line.length() - 1);
    }

    public final String getHeaderString() {
        String line = "";
        for (String key : getKeys()) {
            line += key + SEPARATOR;
        }
        return line.substring(0, line.length() - 1);
    }
}
