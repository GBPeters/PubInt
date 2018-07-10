package nl.gijspeters.pubint.app;

import nl.gijspeters.pubint.config.Config;
import nl.gijspeters.pubint.export.csv.CSVWriter;
import nl.gijspeters.pubint.export.csv.resultgraph.ResultGraphDocument;
import nl.gijspeters.pubint.export.csv.validate.ResultDocument;
import nl.gijspeters.pubint.export.csv.validate.ValidationDocument;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.model.*;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.mutlithreading.*;
import nl.gijspeters.pubint.structure.Leg;
import nl.gijspeters.pubint.structure.Trajectory;
import nl.gijspeters.pubint.tools.PgMongoMigrator;
import nl.gijspeters.pubint.validation.ValidationLeg;
import nl.gijspeters.pubint.validation.ValidationResult;
import nl.gijspeters.pubint.validation.ValidationResultBuilder;
import nl.gijspeters.pubint.validation.VisitedExample;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.mongodb.morphia.query.Query;

import java.io.File;
import java.util.*;

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

    @Option(name = "-l", usage = "Limit query size")
    int limit = 0;

    @Option(name = "-o", usage = "Use multiple OTP instances")
    int oi = 1;

    @Option(name = "--skip", usage = "Skip first # legs in test leg selection")
    int skip = 0;
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
//            OTPEntry.otpDir = otpDir;
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
                case "calcdistance":
                    calcDistance();
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

    private void calcDistance() {
        if (test) {
            Leg l = MorphiaHandler.getInstance().getTestLeg();
            double d = l.getOrigin().getCoord().distance(l.getDestination().getCoord());
            l.setDistance(d);
            System.out.println(l);
            System.out.println("Distance = " + d);

        } else {
            Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class).disableCursorTimeout()
                    .field("distance").doesNotExist();
            TaskCursor cursor = new DistanceCursor(q);
            TaskManager tm = new TaskManager(cursor, mt);
            tm.start();
        }
    }

    private void runModel() {

        if (validate) {
            if (clear) {
                MorphiaHandler.getInstance().clearCollection("validationresults");
            }
            Query<Edge> edges = MorphiaHandler.getInstance().iterateEdges();
            if (test) {

                ValidationResultBuilder builder = new ValidationResultBuilder(new ModelConfig(), edges);
                System.out.println("Map indexed");

                Query<ValidationLeg> q = MorphiaHandler.getInstance().getDs().createQuery(ValidationLeg.class).field("prism").exists().field("distance").greaterThanOrEq(0.025);
                Iterator<ValidationLeg> it = q.iterator();
                int skipped = -1;

                search:
                do {
                    ValidationLeg leg;
                    do {
                        leg = it.next();
                        skipped++;
                    } while (skipped < skip || leg.getDeltaTime() < 1200000 || leg.getValidators().size() < 4);

                    System.out.println(leg);
                    Set<ValidationResult> results = builder.buildResult(leg);
                    int i = 0;
                    List<VisitedExample> examples = new ArrayList<>();
                    VisitedExample originExample = new VisitedExample(leg.getOrigin(),
                            builder.selectAnchorEdge(leg.getOrigin()), 1);
                    examples.add(originExample);
                    for (ValidationResult r : results) {
                        System.out.println("--- New result ---");
                        System.out.println(r.getAnchor());
                        System.out.println("Transect size " + r.getTransect().size());
                        System.out.println(r.getAnchorEdge());
                        System.out.println(r.getAnchorProbability());
                        if (r.getAnchorProbability() <= 0) {
                            System.out.println("Invalid prism, continuing");
                            skip = skipped + 20;
                            continue search;
                        }
                        VisitedExample example = new VisitedExample(r.getAnchor(), r.getAnchorEdge(), r.getAnchorProbability());
                        examples.add(example);
                        if (dump) {
                            CSVWriter<ResultGraphDocument> writer = new CSVWriter<>("transectdump_" + i + ".csv");
                            ResultGraphDocument transectdoc = new ResultGraphDocument(r.getTransect());
                            writer.writeDocument(transectdoc);
                            System.out.println("Validation transect dumped");
                        }
                        i++;
                    }
                    VisitedExample destinationExample = new VisitedExample(leg.getDestination(),
                            builder.selectAnchorEdge(leg.getDestination()), 1);
                    examples.add(destinationExample);
                    if (dump) {
                        CSVWriter<ResultDocument> writer = new CSVWriter<>("resultdump.csv");
                        ResultDocument resultDocument = new ResultDocument(examples);
                        writer.writeDocument(resultDocument);
                        System.out.println("Results dumped");
                    }
                    break;
                } while (true);
            } else {
                System.out.println("Validating prisms...");
                ConfigGrid grid = new ConfigGrid(1, 0.001, 1, INTERVAL_SECONDS);
                ValidationResultBuilder builder = new ValidationResultBuilder(null, edges);
                for (ModelConfig config : grid.getConfigs()) {
                    if (new File("validations_" +
                            config.getDispersion() + "_" + config.getTransition() + "_" + config.getTransitWeight() +
                            ".csv").exists()) {
                        System.out.println("Validation for dispersion=" + config.getDispersion() + ", transition="
                                + config.getTransition() + ", transitWeight=" + config.getTransitWeight() + "exists. " +
                                "Continuing.");
                        continue;
                    }
                    builder.setConfig(config);
                    System.out.println("Validating for dispersion=" + config.getDispersion() + ", transition="
                            + config.getTransition() + ", transitWeight=" + config.getTransitWeight());
                    Query<ValidationLeg> q = MorphiaHandler.getInstance().getDs().createQuery(ValidationLeg.class)
                            .field("prism").exists().limit(limit);//.field("distance").greaterThanOrEq(0.005);
                    TaskCursor cursor = new ValidateCursor(q, builder);
                    ValidationTaskManager tm = new ValidationTaskManager(cursor, mt);
                    tm.start();
                    tm.join();
                    if (dump) {
                        ValidationDocument doc = new ValidationDocument(new HashSet<>(tm.getValidations()));
                        CSVWriter<ValidationDocument> writer = new CSVWriter<>("validations_" +
                                config.getDispersion() + "_" + config.getTransition() + "_" + config.getTransitWeight() +
                                ".csv");
                        writer.writeDocument(doc);
                    }
                }
            }
        } else {
            if (clear) {
                MorphiaHandler.getInstance().clearCollection("resultgraph");
            }
            if (test) {
                Leg leg = MorphiaHandler.getInstance().getTestLeg();
                System.out.println(leg);
                ResultGraphBuilder builder = new ResultGraphBuilder(new ModelConfig(), leg);
                builder.addAll();
                System.out.println("builder created");
                Network network = builder.buildProbabilityNetwork();
                System.out.println(network.size());
                System.out.println("p-network created");
                ModelResultGraph<Edge> edgeProbs = network.getResultGraph().getEdgeProbabilities();
                MorphiaHandler.getInstance().saveResultGraph(edgeProbs);
                System.out.println("p-network saved");
                if (dump) {
                    CSVWriter<ResultGraphDocument> writer = new CSVWriter<>("probdump.csv");
                    ResultGraphDocument probdoc = new ResultGraphDocument(edgeProbs);
                    writer.writeDocument(probdoc);
                    System.out.println("CSV file dumped");
                }
                network = builder.buildVisitTimeNetwork();
                System.out.println("t-network created");
                ModelResultGraph<Edge> edgeTimes = network.getEdgeProbabilities();
                MorphiaHandler.getInstance().saveResultGraph(edgeTimes);
                System.out.println("t-network saved");
                System.out.println(edgeProbs.size());
                Edge e = edgeProbs.keySet().iterator().next();
                System.out.println(e.toString() + edgeProbs.get(e));
                if (dump) {
                    CSVWriter<ResultGraphDocument> writer = new CSVWriter<>("timedump.csv");
                    ResultGraphDocument timedoc = new ResultGraphDocument(edgeTimes);
                    writer.writeDocument(timedoc);
                    System.out.println("CSV file dumped");
                }
            } else {
                System.out.println("Creating probability networks...");
                Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class).field("prism").exists().
                        limit(limit);
                //.field("distance").greaterThanOrEq(0.005);
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
        System.out.println("Graph building not implemented in this branch");
//        try {
//            if (clear) {
//                System.out.println("Clearing large graphs...");
//                MorphiaHandler.getInstance().clearCollection("largegraph");
//                MorphiaHandler.getInstance().clearCollection("edge");
//                MorphiaHandler.getInstance().clearCollection("vertex");
//            }
//            GraphFactory gb = new GraphFactory();
//            BasicGraph g = gb.getCompleteGraph("amsterdam_complete");
//            MorphiaHandler.getInstance().saveLargeGraph(g, false);
//            System.out.println("BasicGraph saved");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("BasicGraph building failed.");
//        }
    }

    /**
     * Entry method for creating prisms from legs
     */
    public void createPrisms() {
        System.out.println("Prism building not implemented in this branch.");
//        Prism p = new Prism();
//        if (clear) {
//            System.out.println("Clearing prisms from legs...");
//            Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class).field("prism").exists();
//            for (Leg l : q) {
//                l.setPrism(null);
//                MorphiaHandler.getInstance().saveLeg(l);
//            }
//            MorphiaHandler.getInstance().clearCollection("state");
//        }
//        if (test) {
//            GraphFactory gf = new GraphFactory();
//            Leg l = MorphiaHandler.getInstance().getTestLeg();
//            p = gf.getPrism(l);
//            l.setPrism(p);
//            MorphiaHandler.getInstance().saveLeg(l);
//        } else {
//            Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class).field("prism").doesNotExist();
//            List<OTPEntry> instances = OTPHandler.getInstances(oi);
//            List<GraphFactory> factories = new ArrayList<>();
//            for (OTPEntry instance : instances) {
//                factories.add(new GraphFactory(instance));
//            }
//            TaskCursor cursor = new CreatePrismCursor(q, factories);
//            TaskManager tm = new TaskManager(cursor, mt);
//            tm.start();
//        }
//        if (dump) {
//            PrismDocument pd = new PrismDocument(p.getStates());
//            new CSVWriter<PrismDocument>(CSV_DUMP_FILE).writeDocument(pd);
//        }
//        System.out.println("Prisms created.");
    }
}
