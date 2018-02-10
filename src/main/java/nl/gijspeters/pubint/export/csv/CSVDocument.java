package nl.gijspeters.pubint.export.csv;

/**
 * Created by gijspeters on 10-02-18.
 */
public interface CSVDocument extends Iterable<String> {

    String getHeader();

}
