package marf.FeatureExtraction;

import marf.Preprocessing.IPreprocessing;


/**
 * <p>Feature Extraction Interface.</p>
 * <p>
 * <p>$Id: IFeatureExtraction.java,v 1.4 2005/08/05 22:19:54 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.4 $
 * @since 0.3.0.3
 */
public interface IFeatureExtraction {
    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.4 $";

    /**
     * Abstract feature extraction routine.
     *
     * @return boolean true if there were features extracted, false otherwise
     * @throws FeatureExtractionException if there was an error while extracting features
     */
    boolean extractFeatures()
            throws FeatureExtractionException;

    /**
     * Allows retrieval of the features.
     *
     * @return array of features (<code>double</code> values)
     */
    double[] getFeaturesArray();

    /**
     * Retrieves inner preprocessing reference.
     *
     * @return the preprocessing reference
     * @since 0.3.0.4
     */
    IPreprocessing getPreprocessing();

    /**
     * Allows setting the source preprocessing module.
     *
     * @param poPreprocessing the preprocessing object to set
     * @since 0.3.0.4
     */
    void setPreprocessing(IPreprocessing poPreprocessing);
}

// EOF
