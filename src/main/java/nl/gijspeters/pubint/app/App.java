package nl.gijspeters.pubint.app;

import nl.gijspeters.pubint.graph.Graph;
import nl.gijspeters.pubint.graph.GraphBuilder;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.tools.PgMongoMigrator;

/**
 * Created by gijspeters on 16-10-16.
 */
public class App {

    public static final String OTP_DIR = "/Users/gijspeters/otp/";

    public static void main(String[] args) {
        OTPHandler.graphDir = OTP_DIR;
        if (args.length > 0) {
            String com = args[0];
            if (com.equals("migrate")) {
                migrate();
            } else if (com.equals("help")) {
                help();
            } else if (com.equals("buildgraph")) {
                buildGraph();
            } else {
                invalidCom();
            }
        } else {
            invalidCom();
        }
    }

    public static void invalidCom() {
        System.out.println("PubInt - Invalid command.\n" +
                "Type 'pubint help' for help page");
    }

    public static void migrate() {
        PgMongoMigrator migrator;
        System.out.println("Migrating PotsgreSQL Tweets and Users to MongoDB...");
        try {
            migrator = new PgMongoMigrator();
            migrator.migrateTweetsAndUsers(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Migration failed. \n" +
                    "Data in MongoDB may be corrupt.");
        }
        System.out.println("Migration finished.");
    }

    public static void help() {
        System.out.println("PubInt Help \n" +
                "Commands: \n" +
                "migrate  -- migrates data from PostgreSQL to MongoDB \n" +
                "help -- shows this help");
    }

    public static void buildGraph() {
        try {
            GraphBuilder gb = new GraphBuilder();
            Graph g = gb.getGraph("amsterdam_complete");
            MorphiaHandler.getInstance().saveLargeGraph(g, true);
            System.out.println("Graph saved");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Graph building failed.");
        }
    }
}
