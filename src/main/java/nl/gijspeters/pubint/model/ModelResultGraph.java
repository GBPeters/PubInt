package nl.gijspeters.pubint.model;

import nl.gijspeters.pubint.graph.Graph;
import nl.gijspeters.pubint.graph.Vertex;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;
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
public class ModelResultGraph<T extends Traversable> extends HashMap<T, Double> implements Graph<T> {

    @Reference
    protected Leg leg;

    public ModelResultGraph(Leg leg) {
        this.leg = leg;
    }

    public ModelResultGraph(ModelResultGraph<T> resultGraph) {
        this(resultGraph.getLeg());
        for (T t : resultGraph.keySet()) {
            put(t, resultGraph.get(t));
        }
    }

    public Leg getLeg() {
        return leg;
    }

    public Double add(T t, double p) {
        return put(t, getOrDefault(t, 0.0) + p);
    }

    public void addAll(Collection<? extends T> traversables, double p) {
        for (T t : traversables) {
            add(t, p);
        }
    }


    public ModelResultGraph<Edge> getEdgeProbabilities() {
        ModelResultGraph<Edge> graph = new ModelResultGraph<>(leg);
        for (T t : keySet()) {
            graph.addAll(t.getEdges(), get(t));
        }
        return graph;
    }

    public ModelResultGraph<Ridable> getTransitProbabilities() {
        ModelResultGraph<Ridable> graph = new ModelResultGraph<>(leg);
        for (T t : keySet()) {
            if (t instanceof Ridable) {
                graph.add((Ridable) t, get(t));
            }
        }
        return graph;
    }

    public ModelResultGraph<Edge> getTransitEdgeProbabilities() {
        ModelResultGraph<Edge> graph = new ModelResultGraph<>(leg);
        for (Traversable t : keySet()) {
            if (t instanceof Ridable) {
                graph.addAll(t.getEdges(), get(t));
            }
        }
        return graph;
    }

    public ModelResultGraph<BasicEdge> getStreetEdgeProbabilities() {
        ModelResultGraph<BasicEdge> graph = new ModelResultGraph<>(leg);
        for (T t : keySet()) {
            if (t instanceof BasicEdge && ((BasicEdge) t).isStreetEdge()) {
                graph.add((BasicEdge) t, get(t));
            }
        }
        return graph;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    @Override
    public Collection<T> getTraversables() {
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
