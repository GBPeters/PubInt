package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.model.ModelResultGraph;
import nl.gijspeters.pubint.structure.Leg;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

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

    public ResultGraphContainer(ModelResultGraph<? extends Traversable> resultGraph) {
        leg = resultGraph.getLeg();
        for (Traversable t : resultGraph.keySet()) {
            tcontainers.add(new TraversableContainer(t, resultGraph.get(t)));
        }
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
        ModelResultGraph<Traversable> resultGraph = new ModelResultGraph<>(leg);
        for (TraversableContainer tc : tcontainers) {
            resultGraph.put(tc.getTraversable(), tc.getP());
        }
        return resultGraph;
    }
}
