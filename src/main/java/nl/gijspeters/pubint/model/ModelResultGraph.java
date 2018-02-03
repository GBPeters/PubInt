package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.graph.Graph;
import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Ridable;
import nl.gijspeters.pubint.graph.traversable.Traversable;
import nl.gijspeters.pubint.structure.Leg;
import org.mongodb.morphia.annotations.Reference;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 04-04-17.
 */
public class ModelResultGraph extends HashMap<Traversable, Double> implements Graph<Traversable> {

    @Reference
    protected Leg leg;

    public ModelResultGraph(Leg leg) {
        this.leg = leg;
    }

    public ModelResultGraph(ModelResultGraph resultGraph) {
        this(resultGraph.getLeg());
        for (Traversable t : resultGraph.keySet()) {
            put(t, resultGraph.get(t));
        }
    }

    public Leg getLeg() {
        return leg;
    }

    public Double add(Traversable t, double p) {
        return put(t, getOrDefault(t, 0.0) + p);
    }

    public void addAll(Collection<? extends Traversable> traversables, double p) {
        for (Traversable t : traversables) {
            add(t, p);
        }
    }


    public ModelResultGraph getEdgeProbabilities() {
        ModelResultGraph graph = new ModelResultGraph(leg);
        for (Traversable t : keySet()) {
            graph.addAll(t.getEdges(), get(t));
        }
        return graph;
    }

    public ModelResultGraph getTransitProbabilities() {
        ModelResultGraph graph = new ModelResultGraph(leg);
        for (Traversable t : keySet()) {
            if (t instanceof Ridable) {
                graph.add(t, get(t));
            }
        }
        return graph;
    }

    public ModelResultGraph getTransitEdgeProbabilities() {
        ModelResultGraph graph = new ModelResultGraph(leg);
        for (Traversable t : keySet()) {
            if (t instanceof Ridable) {
                graph.addAll(t.getEdges(), get(t));
            }
        }
        return graph;
    }

    public ModelResultGraph getStreetEdgeProbabilities() {
        ModelResultGraph graph = new ModelResultGraph(leg);
        for (Traversable t : keySet()) {
            if (t instanceof BasicEdge && ((BasicEdge) t).isStreetEdge()) {
                graph.add(t, get(t));
            }
        }
        return graph;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    @Override
    public Collection<Traversable> getTraversables() {
        return keySet();
    }

    @Override
    public Set<Vertex> getVertices() {
        Set<Vertex> vertices = new HashSet<>();
        for (Traversable t : keySet()) {
            vertices.add(t.getFromVertex());
            vertices.add(t.getToVertex());
        }
        return vertices;
    }
}
