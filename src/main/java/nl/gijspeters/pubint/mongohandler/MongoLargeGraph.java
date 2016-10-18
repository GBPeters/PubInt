package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.BasicGraph;
import nl.gijspeters.pubint.graph.Edge;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;

/**
 * Mongo representation of the BasicGraph by only edge references
 * <p>
 * Created by gijspeters on 17-10-16.
 */
@Entity("largegraph")
public class MongoLargeGraph {

    @Id
    private String id;
    private HashSet<Integer> edgeIds = new HashSet<Integer>();

    public MongoLargeGraph() {
    }

    public MongoLargeGraph(BasicGraph basicGraph) {
        for (Edge e : basicGraph.getEdges()) {
            edgeIds.add(e.getEdgeId());
        }
        id = basicGraph.getId();
    }

    public BasicGraph getGraph() {
        HashSet<Edge> edges = new HashSet<Edge>();
        MorphiaHandler con = MorphiaHandler.getInstance();
        for (int i : edgeIds) {
            edges.add(con.getDs().get(Edge.class, i));
        }
        return new BasicGraph(id, edges);
    }

}
