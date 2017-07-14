package marf.FeatureExtraction.MinMaxAmplitudes;

import marf.FeatureExtraction.FeatureExtraction;
import marf.FeatureExtraction.FeatureExtractionException;
import marf.Preprocessing.IPreprocessing;
import marf.util.Arrays;
import marf.util.Debug;


/**
 * <p>Min/Max Amplitudes.</p>
 * <p>
 * <p>Extracts N minimum and X maximum amplitudes from a sample as features.
 * If incoming sample array's length is less than N + X, it is adjusted
 * to be N + X long with the length/2 value repeated N + X - length times.</p>
 * <p>
 * <p>$Id: MinMaxAmplitudes.java,v 1.13 2006/01/01 01:28:14 mokhov Exp $</p>
 * <p>
 * TODO: needs improvement to select different amplitudes as we don't want
 * 20 the same maximums or minimus if others are avaible.
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.13 $
 * @since 0.3.0
 */
public class MinMaxAmplitudes
        extends FeatureExtraction {
    /**
     * Default number of minimums (amplitudes) to collect.
     */
    public static final int MIN_AMPLITUDES = 50;

    /**
     * Default number of maximums (amplitudes) to collect.
     */
    public static final int MAX_AMPLITUDES = 50;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 5096308560737883614L;

    /**
     * MinMaxAmplitudes Constructor.
     *
     * @param poPreprocessing Preprocessing module reference
     */
    public MinMaxAmplitudes(IPreprocessing poPreprocessing) {
        super(poPreprocessing);
    }

    /**
     * MinMaxAmplitudes implementation of <code>extractFeatures()</code>.
     *
     * @return <code>true</code> if features were extracted, <code>false</code> otherwise
     * @throws FeatureExtractionException
     */
    public final boolean extractFeatures()
            throws FeatureExtractionException {
        try {
            Debug.debug("MinMaxAmplitudes.extractFeatures() has begun...");

            // Make a copy so we can sort.
            double[] adSample = (double[]) this.oPreprocessing.getSample().getSampleArray().clone();
            Arrays.sort(adSample);

            // Defaults
            int iMinAmplitudes = MIN_AMPLITUDES;
            int iMaxAmplitudes = MAX_AMPLITUDES;

            this.adFeatures = new double[iMinAmplitudes + iMaxAmplitudes];

            if (adSample.length < this.adFeatures.length) {
                // Initially fill with the middle value and copy the remaining halves.
                Arrays.fill(this.adFeatures, adSample[adSample.length / 2]);

                iMinAmplitudes = adSample.length / 2;
                iMaxAmplitudes = adSample.length - iMinAmplitudes;
            }

            // Copy the first and the last portions as they are sorted
            Arrays.copy(this.adFeatures, adSample, iMinAmplitudes);
            Arrays.copy
                    (
                            this.adFeatures,
                            this.adFeatures.length - iMaxAmplitudes,
                            adSample,
                            adSample.length - iMaxAmplitudes,
                            iMaxAmplitudes
                    );

            Debug.debug("MinMaxAmplitudes.extractFeatures() has finished.");
            Debug.debug("MinMaxAmplitudes data: " + this);

            return (this.adFeatures.length > 0);
        } catch (Exception e) {
            throw new FeatureExtractionException(e);
        }
    }

    /**
     * Creates String representation of the inner feature vector.
     *
     * @return min/max data in String format
     */
    public String toString() {
        return new StringBuffer()
                .append("Min/Max Data: ")
                .append(Arrays.arrayToVector(this.adFeatures))
                .toString();
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.13 $";
    }
}

// EOF
