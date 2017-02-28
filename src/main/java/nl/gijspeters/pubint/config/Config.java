package nl.gijspeters.pubint.config;

import nl.gijspeters.pubint.mongohandler.MongoConfig;

import static nl.gijspeters.pubint.config.Constants.BASE_DB;

/**
 * Created by gijspeters on 28-02-17.
 */
public class Config {

    private static MongoConfig mongoConfig = BASE_DB;

    public static MongoConfig getMongoConfig() {
        return mongoConfig;
    }

    public static void setMongoConfig(MongoConfig mongoConfig) {
        Config.mongoConfig = mongoConfig;
    }

}
