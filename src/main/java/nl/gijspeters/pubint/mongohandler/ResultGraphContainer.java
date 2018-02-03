package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.model.ModelResultGraph;
import nl.gijspeters.pubint.structure.Leg;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 10-04-17.
 */
@Entity("resultgraph")
public class ResultGraphContainer {

    @Id
    private ObjectId oid = new ObjectId();

    private Set<TraversableContainer> tcontainers = new HashSet<>();

    @Reference
    private Leg leg;
    private Class<? extends ModelResultGraph> resultGraphClass;

    public ResultGraphContainer(ModelResultGraph resultGraph) {
        leg = resultGraph.getLeg();
        for (Traversable t : resultGraph.keySet()) {
            tcontainers.add(new TraversableContainer(t, resultGraph.get(t)));
        }
        resultGraphClass = resultGraph.getClass();
    }

    public Leg getLeg() {
        return leg;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    public ObjectId getOid() {
        return oid;
    }

    public ModelResultGraph getResultGraph() {
        ModelResultGraph resultGraph;
        Constructor<? extends ModelResultGraph> con;
        try {
            con = resultGraphClass.getConstructor(Leg.class);
            resultGraph = con.newInstance(leg);
        } catch (Exception e) {
            throw new RuntimeException("Constructor not found. This should never happen.");
        }
        for (TraversableContainer tc : tcontainers) {
            resultGraph.put(tc.getTraversable(), tc.getP());
        }
        return resultGraph;
    }
}
