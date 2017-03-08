package nl.gijspeters.pubint.config;

import nl.gijspeters.pubint.mongohandler.MongoConfig;

/**
 * Created by gijspeters on 19-11-16.
 * Container class for application-scope constants
 *
 */
public class Constants {

    public static final String OTP_DIR = "/var/otp/";

    public static final String CSV_DUMP_FILE = "pubint_dump.csv";

    public static final MongoConfig BASE_DB = new MongoConfig("pubint");

    public static final MongoConfig VALIDATE_DB = new MongoConfig("pubint_v");
}
