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

    public static final double DISPERSION = 3;

    public static final double TRANSITION = 0.003;

    public static final String OTP_DIR_DEBUG = "/Users/gijspeters/otp";

    public static final int INTERVAL_SECONDS = 30;
}
