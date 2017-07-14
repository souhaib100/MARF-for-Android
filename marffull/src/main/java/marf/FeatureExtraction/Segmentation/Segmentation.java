package marf.FeatureExtraction.Segmentation;

import marf.FeatureExtraction.FeatureExtraction;
import marf.FeatureExtraction.FeatureExtractionException;
import marf.Preprocessing.IPreprocessing;
import marf.util.NotImplementedException;


/**
 * <p>Class Segmentation.</p>
 * <p>
 * <p>$Id: Segmentation.java,v 1.15 2005/08/13 23:09:37 susan_fan Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.15 $
 * @since 0.0.1
 */
public class Segmentation
        extends FeatureExtraction {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -8113388768153118543L;

    /**
     * Segmentation Constructor.
     *
     * @param poPreprocessing Preprocessing object reference
     */
    public Segmentation(IPreprocessing poPreprocessing) {
        super(poPreprocessing);
    }

    /**
     * Not Implemented.
     *
     * @return nothing
     * @throws NotImplementedException
     * @throws FeatureExtractionException never thrown
     */
    public final boolean extractFeatures()
            throws FeatureExtractionException {
        throw new NotImplementedException("Segmentation.extractFeatures()");
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.15 $";
    }
}

// EOF
