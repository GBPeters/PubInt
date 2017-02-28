package nl.gijspeters.pubint.mongohandler;

/**
 * Created by gijspeters on 28-02-17.
 */
public class MongoConfig {

    private String dbName;

    public MongoConfig(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

}
