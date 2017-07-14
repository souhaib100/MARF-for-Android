package marf.FeatureExtraction.RandomFeatureExtraction;

import java.util.Random;

import marf.FeatureExtraction.FeatureExtraction;
import marf.FeatureExtraction.FeatureExtractionException;
import marf.Preprocessing.IPreprocessing;
import marf.util.Arrays;


/**
 * <p>Implementation of random feature extraction for testing as a baseline.</p>
 * <p>
 * <p>$Id: RandomFeatureExtraction.java,v 1.15 2006/01/21 02:35:32 mokhov Exp $<p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.15 $
 * @since 0.2.0
 */
public class RandomFeatureExtraction
        extends FeatureExtraction {
    /**
     * Default number (256) of doubles per chunk in a feature vector.
     */
    public static final int DEFAULT_CHUNK_SIZE = 256;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -5469714962808143269L;

    /**
     * RandomFeatureExtraction Constructor.
     *
     * @param poPreprocessing Preprocessing object reference
     */
    public RandomFeatureExtraction(IPreprocessing poPreprocessing) {
        super(poPreprocessing);

        // XXX: ModuleParams
    }

    /**
     * Random Gaussian-based feature extracton.
     *
     * @return <code>true</code> if successful
     * @throws FeatureExtractionException
     */
    public final boolean extractFeatures()
            throws FeatureExtractionException {
        try {
            double[] adChunk = new double[DEFAULT_CHUNK_SIZE];
            this.adFeatures = new double[DEFAULT_CHUNK_SIZE];

            int iDataRecv = this.oPreprocessing.getSample().getNextChunk(adChunk);

            while (iDataRecv > 0) {
                for (int i = 0; i < DEFAULT_CHUNK_SIZE; i++) {
                    this.adFeatures[i] += adChunk[i] * (new Random(i).nextGaussian());
                }

                iDataRecv = this.oPreprocessing.getSample().getNextChunk(adChunk);

                // Padding to ^2 for the last chunk
                if (iDataRecv < DEFAULT_CHUNK_SIZE && iDataRecv > 0) {
                    Arrays.fill(adChunk, iDataRecv, DEFAULT_CHUNK_SIZE - 1, 0);
                    iDataRecv = 0;
                }
            }

            return true;
        } catch (Exception e) {
            throw new FeatureExtractionException(e);
        }
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.15 $";
    }
}

// EOF
