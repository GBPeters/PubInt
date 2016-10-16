package nl.gijspeters.pubint.mongohandler;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by gijspeters on 03-10-16.
 */
public class MorphiaConnection {

    public static final String DB_NAME = "pubint";
    public static final String[] PACKAGES = {"nl.gijspeters.pubint.structure",
            "nl.gijspeters.pubint.twitter"};

    private final Morphia morphia;
    private final Datastore datastore;

    public MorphiaConnection() {
        morphia = new Morphia();
        for (String pack : PACKAGES) {
            morphia.mapPackage(pack);
        }
        datastore = morphia.createDatastore(new MongoClient(), DB_NAME);
    }

    public Datastore getDs() {
        return datastore;
    }

}
