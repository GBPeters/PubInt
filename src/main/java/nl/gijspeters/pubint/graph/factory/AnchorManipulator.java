package nl.gijspeters.pubint.graph.factory;

import nl.gijspeters.pubint.structure.Anchor;

/**
 * Created by gijspeters on 18-10-16.
 *
 * Interface for manipulating anchors before they are being used in calculations.
 */
public interface AnchorManipulator {

    void manipulate(Anchor anchor);

}