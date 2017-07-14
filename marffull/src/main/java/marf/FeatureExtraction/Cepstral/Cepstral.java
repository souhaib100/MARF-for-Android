package marf.FeatureExtraction.Cepstral;

import marf.FeatureExtraction.FeatureExtraction;
import marf.FeatureExtraction.FeatureExtractionException;
import marf.Preprocessing.IPreprocessing;
import marf.util.NotImplementedException;


/**
 * <p>Class Cepstral.</p>
 * <p>
 * <p>$Id: Cepstral.java,v 1.13 2005/08/13 23:46:43 susan_fan Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.13 $
 * @since 0.0.1
 */
public class Cepstral
        extends FeatureExtraction {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -1963503086097237420L;

    /**
     * Cepstral Constructor.
     *
     * @param poPreprocessing Preprocessing module reference
     */
    public Cepstral(IPreprocessing poPreprocessing) {
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
        throw new NotImplementedException("Cepstral.extractFeatures()");
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.13 $";
    }
}

// EOF
