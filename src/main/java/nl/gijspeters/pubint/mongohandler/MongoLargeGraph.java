package nl.gijspeters.pubint.mongohandler;

import nl.gijspeters.pubint.graph.Edge;
import nl.gijspeters.pubint.graph.Graph;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;

/**
 * Mongo representation of the Graph by only edge references
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

    public MongoLargeGraph(Graph graph) {
        for (Edge e : graph.getEdges()) {
            edgeIds.add(e.getEdgeId());
        }
        id = graph.getId();
    }

    public Graph getGraph() {
        HashSet<Edge> edges = new HashSet<Edge>();
        MorphiaHandler con = MorphiaHandler.getInstance();
        for (int i : edgeIds) {
            edges.add(con.getDs().get(Edge.class, i));
        }
        return new Graph(id, edges);
    }

}
