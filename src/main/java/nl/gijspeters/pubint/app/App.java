package nl.gijspeters.pubint.app;

import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.graph.factory.DateManipulator;
import nl.gijspeters.pubint.graph.factory.GraphFactory;
import nl.gijspeters.pubint.mongohandler.MongoLargeGraph;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.mongohandler.PrismContainer;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.structure.Leg;
import nl.gijspeters.pubint.structure.Trajectory;
import nl.gijspeters.pubint.tools.PgMongoMigrator;
import nl.gijspeters.pubint.validation.ValidationLeg;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.kohsuke.args4j.ExampleMode.ALL;

/**
 * Created by gijspeters on 16-10-16.
 */
public class App {

    @Argument
    private List<String> arguments = new ArrayList<>();

    @Option(name = "-v", usage = "Use or create validation legs")
    boolean validate = false;

    @Option(name = "-t", usage = "Use test leg for single testing")
    boolean test = false;

    @Option(name = "-d", usage = "Dump first output to CSV")
    boolean dump = false;

    @Option(name = "-c", usage = "Clear existing documents")
    boolean clear = false;

    public static void main(String[] args) {
        new App().doMain(args);
    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (arguments.isEmpty())
                throw new CmdLineException(parser, "No argument is given");
            String command = arguments.get(0);
            switch (command) {
                case "migrate":
                    migrate();
                    break;
                case "createlegs":
                    if (validate) {
                        createValidationLegs();
                    } else {
                        createLegs();
                    }
                    break;
                case "buildgraph":
                    buildGraph();
                    break;
                case "createprisms":
                    createPrisms();
                    break;
                default:
                    throw new CmdLineException("Invalid command.");
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java App [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java App" + parser.printExample(ALL));
        }
    }

    public void migrate() {
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

    public void createLegs() {
        try {
            if (clear) {
                System.out.println("Clearing legs...");
                MorphiaHandler.getInstance().clearCollection(Leg.class);
            }
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

    public void createValidationLegs() {
        try {
            if (clear) {
                System.out.println("Clearing validation legs...");
                MorphiaHandler.getInstance().clearCollection(ValidationLeg.class);
            }
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

    public void buildGraph() {
        try {
            if (clear) {
                System.out.println("Clearing large graphs...");
                MorphiaHandler.getInstance().clearCollection(MongoLargeGraph.class);
            }
            GraphFactory gb = new GraphFactory(new DateManipulator());
            BasicGraph g = gb.getCompleteGraph("amsterdam_complete");
            MorphiaHandler.getInstance().saveLargeGraph(g, false);
            System.out.println("BasicGraph saved");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BasicGraph building failed.");
        }
    }

    public void createPrisms() {
        if (clear) {
            System.out.println("Clearing prisms...");
            MorphiaHandler.getInstance().clearCollection(PrismContainer.class);
        }
        OTPHandler.graphDir = Constants.OTP_DIR;
        GraphFactory gf = new GraphFactory(new DateManipulator());
        if (test) {
            Leg l = MorphiaHandler.getInstance().getTestLeg();
            Prism p = gf.getPrism(l);
            MorphiaHandler.getInstance().savePrism(p);
        } else {
            Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class);
            for (Leg l : q) {
                Prism p = gf.getPrism(l);
                MorphiaHandler.getInstance().savePrism(p);
            }
        }
        if (dump) {
            String fields = "";
        }
        System.out.println("Prisms created.");
    }
}
