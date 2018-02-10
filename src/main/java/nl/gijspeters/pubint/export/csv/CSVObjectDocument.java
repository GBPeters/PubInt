package nl.gijspeters.pubint.export.csv;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by gijspeters on 27-02-17.
 * <p>
 * This abstract class represents a CSV document with a specific schema, supplied by the CSVObjectLine object.
 * Extensions of this class should be tailored to extensions of the CSVObjectLine class, and can then be used
 * in the CSVWriter class to write Java objects to CSV.
 *
 * @param <T> The Java class that needs to be written to a CSV, each object in an individual line.
 * @param <E> The CSVObjectLine extension for T
 */
public abstract class CSVObjectDocument<T, E extends CSVObjectLine<T>> implements CSVDocument {

    private Collection<T> data;

    /**
     * Public constructor
     *
     * @param data A Collection of T objects
     */
    public CSVObjectDocument(Collection<T> data) {
        this.data = data;
    }

    /**
     * This method is called on writing each individual line based on the Collection of T objects supplied in
     * the constructor. The most basic implementation is
     * return new CSVObjectLine(T, int);
     *
     * @param lineData A T object to be written to a CSV line
     * @param index    The index of this object in iteration
     * @return A CSVObjectLine extension object representing T
     */
    protected abstract E createCSVLine(T lineData, int index);

    /**
     * Return a header String that can be written as first line in the CSV file.
     *
     * @return A String containing CSV headers
     */
    public final String getHeader() {
        return createCSVLine(data.iterator().next(), 0).getHeaderString();
    }

    /**
     * Returns a relevant iterator for writing this document's contents
     *
     * @return A LineIterator object
     */
    @Override
    public final Iterator<String> iterator() {
        return new LineIterator();
    }

    /**
     * A custom iterator that requests a new line on each next() call.
     */
    private class LineIterator implements Iterator<String> {

        Iterator<T> iter = data.iterator();
        int index = 0;

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String line = createCSVLine(iter.next(), index).getLineString();
            index++;
            return line;
        }
    }

}
