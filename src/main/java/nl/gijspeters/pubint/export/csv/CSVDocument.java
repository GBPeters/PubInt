package nl.gijspeters.pubint.export.csv;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by gijspeters on 27-02-17.
 */
public abstract class CSVDocument<T, E extends CSVLine<T>> implements Iterable<String> {

    private Collection<T> data;

    public CSVDocument(Collection<T> data) {
        this.data = data;
    }

    protected abstract E createCSVLine(T lineData, int index);

    public final String getHeader() {
        return createCSVLine(data.iterator().next(), 0).getHeaderString();
    }

    @Override
    public final Iterator<String> iterator() {
        return new LineIterator();
    }

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
