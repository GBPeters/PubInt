package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.graph.traversable.Traversable;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;
import java.util.Set;

/**
 * Mongo representation of the BasicGraph by only edge references
 * <p>
 * Created by gijspeters on 17-10-16.
 */
@Entity("largegraph")
public class MongoLargeGraph {

    @Id
    private String id;
    private HashSet<Integer> edgeIds = new HashSet<>();

    public MongoLargeGraph() {
    }

    public MongoLargeGraph(BasicGraph basicGraph) {
        for (Traversable t : basicGraph.getTraversables()) {
            edgeIds.add(t.hashCode());
        }
        id = basicGraph.getId();
    }

    public BasicGraph getGraph() {
        Set<Edge> edges = new HashSet<>();
        MorphiaHandler con = MorphiaHandler.getInstance();
        for (int i : edgeIds) {
            edges.add(con.getDs().get(Edge.class, i));
        }
        return new BasicGraph(id, edges);
    }

}
