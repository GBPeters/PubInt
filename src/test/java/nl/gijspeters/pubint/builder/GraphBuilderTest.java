package nl.gijspeters.pubint.builder;

import nl.gijspeters.pubint.app.App;
import nl.gijspeters.pubint.graph.Cone;
import nl.gijspeters.pubint.graph.OriginState;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.otpentry.OTPHandler;
import nl.gijspeters.pubint.structure.Anchor;
import nl.gijspeters.pubint.structure.Leg;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.query.Query;

import java.util.Iterator;

import static org.junit.Assert.assertTrue;

/**
 * Created by gijspeters on 18-10-16.
 */
public class GraphBuilderTest {

    private Anchor anchor;

    @Before
    public void setUp() throws Exception {
        OTPHandler.graphDir = App.OTP_DIR;
        Query<Leg> q = MorphiaHandler.getInstance().getDs().createQuery(Leg.class);
        Iterator<Leg> i = q.iterator();
        Leg l;
        do {
            l = i.next();
        } while (i.hasNext() && l.getDeltaTime() > 1200000);
        anchor = l.getOrigin();
        System.out.println(anchor.toString());
    }

    @Test
    public void getCone() throws Exception {
        GraphBuilder gb = new GraphBuilder(new DateManipulator());
        Cone<OriginState> cone = gb.getOriginCone(anchor, 7200);
        assertTrue(cone.getStates().size() > 20);
    }

}