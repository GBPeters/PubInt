package nl.gijspeters.pubint.validation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.linearref.LinearLocation;
import com.vividsolutions.jts.linearref.LocationIndexedLine;
import nl.gijspeters.pubint.graph.traversable.BasicEdge;
import nl.gijspeters.pubint.graph.traversable.Edge;
import nl.gijspeters.pubint.model.ModelConfig;
import nl.gijspeters.pubint.model.Transect;
import nl.gijspeters.pubint.model.TransectBuilder;
import nl.gijspeters.pubint.structure.Anchor;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.NullProgressListener;
import org.mongodb.morphia.query.Query;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.util.*;

public class ValidationResultBuilder {

    private static final String FEATURE_SCHEMA = "edge:LineString:srid=4326,edgeid=Integer";
    private static final String FEATURE_NAME = "Edge";

    public ModelConfig getConfig() {
        return config;
    }

    public void setConfig(ModelConfig config) {
        this.config = config;
    }

    private ModelConfig config;
    private static final double MAX_SEARCH_SPAN_FRACTION = 0.01;

    private double maxSearchDistance;
    private final SpatialIndex index = new STRtree();
    private final Map<LocationIndexedLine, BasicEdge> featureMap = new HashMap<>();


    private DefaultFeatureCollection features;

    public ValidationResultBuilder(ModelConfig modelConfig, Query<Edge> edges) {
        config = modelConfig;
        createSpatialIndexedMap(edges);
    }

    private void createSpatialIndexedMap(Query<Edge> edges) {
        SimpleFeatureType featureType = null;
        try {
            featureType = DataUtilities.createType(FEATURE_NAME, FEATURE_SCHEMA);
        } catch (SchemaException e) {
            e.printStackTrace();
        }
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(featureType);
        features = new DefaultFeatureCollection();
        Map<Integer, BasicEdge> edgeMap = new HashMap<>();
        for (Edge e : edges) {
            if (e instanceof BasicEdge) {
                BasicEdge be = (BasicEdge) e;
                if (!be.getGeometry().isEmpty()) {
                    sfb.add(be.getGeometry());
                    sfb.add(be.getEdgeId());
                    features.add(sfb.buildFeature(String.valueOf(be.getEdgeId())));
                    edgeMap.put(be.getEdgeId(), be);
                } else {
                    System.out.println("Empty geometry for " + e.toString());
                }
            }
        }
        System.out.println("Indexing...");
        maxSearchDistance = features.getBounds().getSpan(0) * MAX_SEARCH_SPAN_FRACTION;
        try {
            features.accepts(
                    new FeatureVisitor() {

                        @Override
                        public void visit(Feature feature) {
                            SimpleFeature simpleFeature = (SimpleFeature) feature;
                            Geometry geom = (LineString) simpleFeature.getDefaultGeometry();
                            // Just in case: check for  null or empty geometry
                            if (geom != null) {
                                Envelope env = geom.getEnvelopeInternal();
                                if (!env.isNull()) {
                                    LocationIndexedLine line = new LocationIndexedLine(geom);
                                    index.insert(env, line);
                                    try {
                                        int edgeId = Integer.parseInt(simpleFeature.getID());
                                        featureMap.put(line, edgeMap.get(edgeId));
                                    } catch (ClassCastException e) {
                                        System.out.println("Something very weird happens here...");
                                    }
                                }
                            }
                        }
                    },
                    new NullProgressListener());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<ValidationResult> buildResult(ValidationLeg leg) {
        Set<ValidationResult> results = new HashSet<>();
        TransectBuilder builder = new TransectBuilder(leg, config);
        for (Anchor a : leg.getValidators()) {
            Transect transect = builder.buildTransect(a.getDate()).getNormalisedTransect();
            BasicEdge anchorEdge = selectAnchorEdge(a);
            double anchorProbability = transect.getOrDefault(anchorEdge, 0.);
            ValidationResult result;
            try {
                result = new ValidationResult(leg, a, transect, anchorEdge, anchorProbability, config);
                results.add(result);
            } catch (SchemaException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private BasicEdge selectAnchorEdge(Anchor anchor) {
        Coordinate pt = anchor.getCoord();
        Envelope search = new Envelope(pt);
        search.expandBy(maxSearchDistance);

        /*
         * Query the spatial index for objects within the search envelope.
         * Note that this just compares the point envelope to the line envelopes
         * so it is possible that the point is actually more distant than
         * MAX_SEARCH_DISTANCE from a line.
         */
        List<LocationIndexedLine> lines = index.query(search);

        // Initialize the minimum distance found to our maximum acceptable
        // distance plus a little bit
        double minDist = maxSearchDistance + 1.0e-6;
        LocationIndexedLine minDistLine = null;

        for (LocationIndexedLine line : lines) {
            LinearLocation here = line.project(pt);
            Coordinate point = line.extractPoint(here);
            double dist = point.distance(pt);
            if (dist < minDist) {
                minDist = dist;
                minDistLine = line;
            }
        }

        return featureMap.getOrDefault(minDistLine, null);

    }

}
