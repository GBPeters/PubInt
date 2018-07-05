package nl.gijspeters.pubint.config;

import nl.gijspeters.pubint.mongohandler.MongoConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    public static final double TRANSIT_WEIGHT = 1;

    public static final Set<Double> GRID_DISPERIONS = new HashSet<>(Arrays.asList(1., 3., 10.));

    public static final Set<Double> GRID_TRANSITIONS = new HashSet<>(Arrays.asList(0.001, 0.003, 0.005));

    public static final Set<Double> GRID_TRANSIT_WEIGHTS = new HashSet<>(Arrays.asList(0.1, 1., 10.));
}
