package nl.gijspeters.pubint.app;

import nl.gijspeters.pubint.config.Config;
import nl.gijspeters.pubint.export.csv.CSVWriter;
import nl.gijspeters.pubint.export.csv.prism.PrismDocument;
import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.Prism;
import nl.gijspeters.pubint.graph.factory.GraphFactory;
import nl.gijspeters.pubint.model.ModelConfig;
import nl.gijspeters.pubint.model.ModelResultGraph;
import nl.gijspeters.pubint.model.Network;
import nl.gijspeters.pubint.model.ResultGraphBuilder;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.mutlithreading.CreateNetworkCursor;
import nl.gijspeters.pubint.mutlithreading.CreatePrismCursor;
import nl.gijspeters.pubint.mutlithreading.TaskCursor;
import nl.gijspeters.pubint.mutlithreading.TaskManager;
import nl.gijspeters.pubint.otpentry.OTPEntry;
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

import static nl.gijspeters.pubint.config.Constants.*;
import static org.kohsuke.args4j.ExampleMode.ALL;

/**
 * Created by gijspeters on 16-10-16.
 *
 * Main Command Line Application class
 *
 */
public class App {

    @Argument
    private List<String> arguments = new ArrayList<>();

    @Option(name = "-v", usage = "Use or create validation legs")
    boolean validate = false;

    @Option(name = "-t", usage = "Use test leg for single testing")
    boolean test = false;

    @Option(name = "-d", usage = "Dump last output to CSV")
    boolean dump = false;

    @Option(name = "-c", usage = "Clear existing documents")
    boolean clear = false;

    @Option(name = "--otpDir", usage = "Use other OTP directory")
    String otpDir = OTP_DIR;

    @Option(name = "-m", usage = "Use multiple threads")
    int mt = 1;

    @Option(name = "-o", usage = "Use multiple OTP instances")
    int oi = 1;

    /**
     * Main method. Starting point for the application.
     *
     * @param args Arguments supplied in the command line
     */
    public static void main(String[] args) {
        new App().doMain(args);
    }

    /**
     * Non-static main method. Parses arguments and calls required methods.
     *
     * @param args Arguments supplied in the command line
     */
    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (arguments.isEmpty())
                throw new CmdLineException(parser, "No argument is given");
            String command = arguments.get(0);
            if (validate) {
                Config.setMongoConfig(VALIDATE_DB);
            }
            OTPEntry.otpDir = otpDir;
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
                case "runmodel":
                    runModel();
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

    private void runModel() {
        if (clear) {
            MorphiaHandler.getInstance().clearCollection("resultgraph");
        }
        if (validate) {

        } else {
            if (test) {
                Leg leg = MorphiaHandler.getInstance().getTestLeg();
                ResultGraphBuilder builder = new ResultGraphBuilder(new ModelConfig(), leg);
                System.out.println("builder created");
                Network network = builder.buildProbabilityNetwork();
                System.out.println("p-network created");
                ModelResultGraph edgeProbs = network.getEdgeProbabilities();
                MorphiaHandler.getInstance().saveResultGraph(edgeProbs);
                System.out.println("p-network saved");
                network = builder.buildVisitTimeNetwork();
                System.out.println("t-network created");
                ModelResultGraph edgeTimes = network.getEdgeProbabilities();
                MorphiaHandler.getInstance().saveResultGraph(edgeTimes);
                System.out.println("t-network saved");
            } else {
                System.out.println("Creating probability networks...");
                Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class).field("prism").exists();
                TaskCursor cursor = new CreateNetworkCursor(q, new ModelConfig());
                TaskManager tm = new TaskManager(cursor, mt);
                tm.start();
            }
        }
    }

    /**
     * Entry method for PostgreSQL -> MongoDB migration.
     */
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

    /**
     * Entry method for creating legs, based on combinations of two consequent anchors with the same agent.
     */
    public void createLegs() {
        try {
            if (clear) {
                System.out.println("Clearing legs...");
                MorphiaHandler.getInstance().clearCollection("leg");
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

    /**
     * Entry method for creating legs used for validation based on combinations of at least
     * three consequent anchors with the same agent
     */
    public void createValidationLegs() {
        try {
            if (clear) {
                System.out.println("Clearing validation legs...");
                MorphiaHandler.getInstance().clearCollection("leg");
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

    /**
     * Entry method that retrieves the complete graph from OTP and stores it in the MongoDB instance
     */
    public void buildGraph() {
        try {
            if (clear) {
                System.out.println("Clearing large graphs...");
                MorphiaHandler.getInstance().clearCollection("largegraph");
                MorphiaHandler.getInstance().clearCollection("edge");
                MorphiaHandler.getInstance().clearCollection("vertex");
            }
            GraphFactory gb = new GraphFactory();
            BasicGraph g = gb.getCompleteGraph("amsterdam_complete");
            MorphiaHandler.getInstance().saveLargeGraph(g, false);
            System.out.println("BasicGraph saved");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BasicGraph building failed.");
        }
    }

    /**
     * Entry method for creating prisms from legs
     */
    public void createPrisms() {
        Prism p = new Prism();
        if (clear) {
            System.out.println("Clearing prisms from legs...");
            Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class).field("prism").exists();
            for (Leg l : q) {
                l.setPrism(null);
                MorphiaHandler.getInstance().saveLeg(l);
            }
            MorphiaHandler.getInstance().clearCollection("state");
        }
        if (test) {
            GraphFactory gf = new GraphFactory();
            Leg l = MorphiaHandler.getInstance().getTestLeg();
            p = gf.getPrism(l);
            l.setPrism(p);
            MorphiaHandler.getInstance().saveLeg(l);
        } else {
            Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class).field("prism").doesNotExist();
            List<OTPEntry> instances = OTPHandler.getInstances(oi);
            List<GraphFactory> factories = new ArrayList<>();
            for (OTPEntry instance : instances) {
                factories.add(new GraphFactory(instance));
            }
            TaskCursor cursor = new CreatePrismCursor(q, factories);
            TaskManager tm = new TaskManager(cursor, mt);
            tm.start();
        }
        if (dump) {
            PrismDocument pd = new PrismDocument(p.getStates());
            new CSVWriter<PrismDocument>(CSV_DUMP_FILE).writeDocument(pd);
        }
        System.out.println("Prisms created.");
    }
}
