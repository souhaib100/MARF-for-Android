package marf.Preprocessing.FFTFilter;

import marf.Preprocessing.IPreprocessing;
import marf.Preprocessing.PreprocessingException;
import marf.Storage.Sample;
import marf.util.Debug;


/**
 * <p>Bandpass Filter Implementation based on the FFTFilter.</p>
 * <p>
 * <p>$Id: BandpassFilter.java,v 1.23 2005/12/31 23:17:37 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.23 $
 * @see FFTFilter
 * @since 0.2.0
 */
public class BandpassFilter
        extends FFTFilter {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -8413024016319719417L;

    /**
     * Default constructor for reflective creation of Preprocessing
     * clones. Typically should not be used unless really necessary
     * for the frameworked modules.
     *
     * @since 0.3.0.5
     */
    public BandpassFilter() {
        super();
    }

    /**
     * Implements preprocessing pipeline.
     *
     * @param poPreprocessing follow up preprocessing module
     * @throws PreprocessingException
     * @since 0.3.0.3
     */
    public BandpassFilter(IPreprocessing poPreprocessing)
            throws PreprocessingException {
        super(poPreprocessing);
    }

    /**
     * BandpassFilter Constructor.
     *
     * @param poSample incoming sample
     * @throws PreprocessingException
     */
    public BandpassFilter(Sample poSample)
            throws PreprocessingException {
        super(poSample);
    }

    /**
     * Stub implementation of removeNoise().
     *
     * @return <code>false</code>
     * @throws PreprocessingException never thrown
     */
    public final boolean removeNoise()
            throws PreprocessingException {
        Debug.debug("BandpassFilter.removeNoise()");
        return false;
    }

    /**
     * Stub implementation of removeSilence().
     *
     * @return <code>false</code>
     * @throws PreprocessingException never thrown
     */
    public final boolean removeSilence()
            throws PreprocessingException {
        Debug.debug("BandpassFilter.removeSilence()");
        return false;
    }

    /**
     * Stub implementation of <code>cropAudio()</code>.
     *
     * @param pdStartingFrequency unused
     * @param pdEndFrequency      unused
     * @return <code>false</code>
     * @throws PreprocessingException never thrown
     */
    public final boolean cropAudio(double pdStartingFrequency, double pdEndFrequency)
            throws PreprocessingException {
        Debug.debug("BandpassFilter.cropAudio()");
        return false;
    }

    /**
     * Creates band-pass frequency response coefficients and sets applies
     * them to the frequency response vector.
     *
     * @since 0.3.0
     */
    public void genereateResponseCoefficients() {
        double[] adResponse = new double[DEFAULT_FREQUENCY_RESPONSE_SIZE];

        // Note: Frequencies kept: ~= 1000Hz - 2853Hz
        for (int i = 0; i < adResponse.length; i++) {
            if (i >= 25 && i <= 70) {
                adResponse[i] = 1;
            }
        }

        setFrequencyResponse(adResponse);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.23 $";
    }
}

// EOF
