package nl.gijspeters.pubint.app;

import nl.gijspeters.pubint.builder.DateManipulator;
import nl.gijspeters.pubint.builder.GraphBuilder;
import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.structure.Leg;
import nl.gijspeters.pubint.structure.Trajectory;
import nl.gijspeters.pubint.tools.PgMongoMigrator;
import nl.gijspeters.pubint.validation.ValidationLeg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * Created by gijspeters on 16-10-16.
 */
public class App {

    public static final String OTP_DIR = "/Users/gijspeters/otp/";

    public static void main(String[] args) {
        OTPHandler.graphDir = OTP_DIR;
        if (args.length > 0) {
            String com = args[0];
            ArrayList<String> arglist = new ArrayList<>(Arrays.asList(args));
            arglist.remove(com);
            if (com.equals("migrate")) {
                migrate();
            } else if (com.equals("help")) {
                help();
            } else if (com.equals("buildgraph")) {
                buildGraph();
            } else if (com.equals("createlegs")) {
                if (arglist.contains("--validate")) {
                    createValidationLegs();
                } else {
                    createLegs();
                }
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

    public static void createLegs() {
        try {
            System.out.println("Building legs...");
            Collection<Trajectory> ts = MorphiaHandler.getInstance().getTrajectories();
            for (Trajectory t : ts) {
                Set<Leg> legs = t.buildLegs(3600000);
                for (Leg leg : legs) {
                    MorphiaHandler.getInstance().saveLeg(leg);
                }
            }
            System.out.println("Leg building finished.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Leg building failed.");
        }
    }

    public static void createValidationLegs() {
        try {
            System.out.println("Building validation legs...");
            Collection<Trajectory> ts = MorphiaHandler.getInstance().getTrajectories();
            for (Trajectory t : ts) {
                Set<ValidationLeg> legs = t.buildValidationLegs(3600000);
                for (Leg leg : legs) {
                    MorphiaHandler.getInstance().saveLeg(leg);
                }
            }
            System.out.println("Leg building finished.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Leg building failed.");
        }
    }

    public static void buildGraph() {
        try {
            GraphBuilder gb = new GraphBuilder(new DateManipulator());
            BasicGraph g = gb.getGraph("amsterdam_complete");
            MorphiaHandler.getInstance().saveLargeGraph(g, true);
            System.out.println("BasicGraph saved");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BasicGraph building failed.");
        }
    }
}
