package nl.gijspeters.pubint.export.csv;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by gijspeters on 27-02-17.
 */
public class CSVWriter<T extends CSVDocument> {

    private String fileName;

    public CSVWriter(String fileName) {
        this.fileName = fileName;
    }

    public void writeDocument(T document) {
        Path file = Paths.get(fileName);
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println(document.getHeader());
            writer.close();
            Files.write(file, document, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
