package nl.gijspeters.pubint.mongohandler;

import com.mongodb.MongoClient;
import nl.gijspeters.pubint.app.Constants;
import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.structure.*;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by gijspeters on 03-10-16.
 */
public class MorphiaHandler {


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
        datastore = morphia.createDatastore(new MongoClient(), Constants.DB_NAME);
    }

    public Datastore getDs() {
        return datastore;
    }

    public void saveGraph(BasicGraph basicGraph, boolean basedOnExistingGraph) {
        if (!basedOnExistingGraph) {
            saveNodes(basicGraph.getVertices());
            saveTraversable(basicGraph.getTraversables());
        }
        getDs().save(basicGraph);
    }

    public void saveNodes(Collection<Vertex> vertices) {
        saveSimpleCollection(vertices);
    }

    public void saveTraversable(Collection<Traversable> traversables) {
        saveSimpleCollection(traversables);
    }

    public void saveLargeGraph(BasicGraph basicGraph, boolean basedOnExistingGraph) {
        if (!basedOnExistingGraph) {
            saveNodes(basicGraph.getVertices());
            saveTraversable(basicGraph.getTraversables());
        }
        getDs().save(new MongoLargeGraph(basicGraph));
    }

    public BasicGraph loadLargeGraph(String graphId) {
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

    public void saveLeg(Leg l) {
        saveSimpleObject(l);
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

    public Collection<Trajectory> getTrajectories() {
        HashMap<ObjectId, Trajectory> ts = new HashMap<>();
        Query<Agent> agentq = getDs().createQuery(Agent.class);
        for (Agent a : agentq) {
            ts.put(a.getAgentId(), new Trajectory());
        }
        Query<Anchor> anchorq = getDs().createQuery(Anchor.class);
        for (Anchor a : anchorq) {
            Agent agent = a.getUser().getAgent();
            ts.get(agent.getAgentId()).add(a);
        }
        return ts.values();
    }

    public Leg getTestLeg() {
        Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class);
        Iterator<Leg> i = q.iterator();
        Leg l;
        do {
            l = i.next();
        } while (i.hasNext() && l.getDeltaTime() > 1200000);
        return l;
    }

}
