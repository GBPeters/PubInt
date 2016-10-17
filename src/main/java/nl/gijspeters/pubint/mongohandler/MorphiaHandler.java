package nl.gijspeters.pubint.mongohandler;

import com.mongodb.MongoClient;
import nl.gijspeters.pubint.graph.Edge;
import nl.gijspeters.pubint.graph.Graph;
import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.structure.Agent;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.structure.Trajectory;
import nl.gijspeters.pubint.structure.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by gijspeters on 03-10-16.
 */
public class MorphiaHandler {


    public static final String DB_NAME = "pubint";
    public static final String[] PACKAGES = {"nl.gijspeters.pubint.structure",
            "nl.gijspeters.pubint.twitter",
            "nl.gijspeters.pubint.graph",
            "nl.gijspeters.pubint.mongohandler"};

    private final Morphia morphia;
    private final Datastore datastore;

    private static MorphiaHandler ourInstance = new MorphiaHandler();

    public static MorphiaHandler getInstance() {
        return ourInstance;
    }

    private MorphiaHandler() {
        morphia = new Morphia();
        for (String pack : PACKAGES) {
            morphia.mapPackage(pack);
        }
        datastore = morphia.createDatastore(new MongoClient(), DB_NAME);
    }

    public Datastore getDs() {
        return datastore;
    }

    public void saveGraph(Graph graph, boolean basedOnExistingGraph) {
        if (!basedOnExistingGraph) {
            saveNodes(Arrays.asList(graph.getVertices()));
            saveEdges(Arrays.asList(graph.getEdges()));
        }
        getDs().save(graph);
    }

    public void saveNodes(Collection<Vertex> vertices) {
        saveSimpleCollection(vertices);
    }

    public void saveEdges(Collection<Edge> edges) {
        saveSimpleCollection(edges);
    }

    public void saveLargeGraph(Graph graph, boolean basedOnExistingGraph) {
        if (!basedOnExistingGraph) {
            saveNodes(Arrays.asList(graph.getVertices()));
            saveEdges(Arrays.asList(graph.getEdges()));
        }
        getDs().save(new MongoLargeGraph(graph));
    }

    public Graph loadLargeGraph(String graphId) {
        MongoLargeGraph graph = getDs().get(MongoLargeGraph.class, graphId);
        return graph.getGraph();
    }

    private void saveSimpleObject(Object o) {
        getDs().save(o);
    }

    private void saveSimpleCollection(Collection c) {
        for (Object o : c) {
            getDs().save(o);
        }
    }

    public void saveAnchor(Anchor a) {
        saveSimpleObject(a);
    }

    public void saveAgent(Agent a) {
        saveSimpleObject(a);
    }

    public void saveUser(User u) {
        saveSimpleObject(u);
    }

    public Trajectory getTrajectory(Agent agent) {
        Trajectory t = new Trajectory();
        Agent a = getDs().get(Agent.class, agent.getAgentId());
        Query<User> userq = getDs().createQuery(User.class).field("agent").equal(a);
        HashSet<User> users = new HashSet<User>();
        for (User u : userq) {
            users.add(u);
        }
        Query<Anchor> anchorq = getDs().createQuery(Anchor.class).field("user").in(users);
        for (Anchor anchor : anchorq) {
            t.add(anchor);
        }
        return t;
    }

}
