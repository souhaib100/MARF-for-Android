package marf.Preprocessing.FFTFilter;

import java.util.Vector;

import marf.MARF;
import marf.Preprocessing.IPreprocessing;
import marf.Preprocessing.PreprocessingException;
import marf.Storage.ModuleParams;
import marf.Storage.Sample;
import marf.util.Debug;


/**
 * <p>HighFrequencyBoost class implements filtering using high frequency booster on
 * top of the FFTFilter.</p>
 * <p>
 * <p>$Id: HighFrequencyBoost.java,v 1.24 2005/12/31 23:17:37 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.24 $
 * @since 0.0.1
 */
public class HighFrequencyBoost
        extends FFTFilter {
    /**
     * Create a response that boosts all frequencies above 1000 Hz.
     * Note: 1000Hz ~= 25 * 128 / 8000Hz.
     *
     * @since 0.3.0.2
     */
    public static final int DEFAULT_HIGH_FREQUENCY_CUTOFF = 25;

    /**
     * Default boost rate of 5*PI to be applied to apmplitude values.
     * Made up out of the blue.
     *
     * @since 0.3.0.3
     */
    public static final double BASE_BOOST_COEFFICIENT = 5 * Math.PI;

    /**
     * Current boost coefficient. By default is set to
     * <code>BASE_BOOST_COEFFICIENT</code>.
     *
     * @see #BASE_BOOST_COEFFICIENT
     * @since 0.3.0.5
     */
    protected double dBoostCoefficient = BASE_BOOST_COEFFICIENT;

    /**
     * Current high frequenct cut off. By default is set to
     * <code>DEFAULT_HIGH_FREQUENCY_CUTOFF</code>.
     *
     * @see #DEFAULT_HIGH_FREQUENCY_CUTOFF
     * @since 0.3.0.5
     */
    protected double dHighFrequencyCutoff = DEFAULT_HIGH_FREQUENCY_CUTOFF;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 7235987711570372477L;

    /**
     * Default constructor for reflective creation of Preprocessing
     * clones. Typically should not be used unless really necessary
     * for the frameworked modules.
     *
     * @since 0.3.0.5
     */
    public HighFrequencyBoost() {
        super();
    }

    /**
     * Pipelined preprocessing constructor.
     *
     * @param poPreprocessing followup preprocessing module
     * @throws PreprocessingException
     * @since 0.3.0.3
     */
    public HighFrequencyBoost(IPreprocessing poPreprocessing)
            throws PreprocessingException {
        super(poPreprocessing);
        Debug.debug("HighFrequencyBoost constructed with preprocessing [" + poPreprocessing + "].");
    }

    /**
     * HighFrequencyBoost Constructor.
     *
     * @param poSample incoming sample
     * @throws PreprocessingException
     */
    public HighFrequencyBoost(Sample poSample)
            throws PreprocessingException {
        super(poSample);
        Debug.debug("HighFrequencyBoost constructed with sample [" + poSample + "].");
    }

    /**
     * Overrides FFTFilter's <code>preprocess()</code> with extra normalization after the boost.
     * TODO: Normalization only applied to the boosted part.
     *
     * @return <code>true</code> if there were changes to the sample
     * @throws PreprocessingException if there were problems during
     *                                undelying <code>supper.preprocess()</code> or <code>normalize()</code>.
     * @since 0.2.0
     */
    public final boolean preprocess()
            throws PreprocessingException {
        boolean bChanges = super.preprocess();

        //TODO: in scale of sample, not response array bChanges |= normalize(DEFAULT_HIGH_FREQUENCY_CUTOFF);
        bChanges |= normalize();
        Debug.debug("HighFrequencyBoost preprocess() done: [" + bChanges + "].");

        return bChanges;
    }

    /**
     * Stub implementation of <code>removeNoise()</code>.
     *
     * @return <code>false</code>
     * @throws PreprocessingException never thrown
     */
    public final boolean removeNoise()
            throws PreprocessingException {
        Debug.debug("HighFrequencyBoost.removeNoise()");
        return false;
    }

    /**
     * Stub implementation of <code>removeSilence()</code>.
     *
     * @return <code>false</code>
     * @throws PreprocessingException never thrown
     */
    public final boolean removeSilence()
            throws PreprocessingException {
        Debug.debug("HighFrequencyBoost.removeSilence()");
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
        Debug.debug("HighFrequencyBoost.cropAudio()");
        return false;
    }

    /**
     * Creates high-frequency boost response coefficients and sets applies
     * them to the frequency response vector.
     *
     * @since 0.3.0
     */
    public void genereateResponseCoefficients() {
        double[] adResponse = new double[DEFAULT_FREQUENCY_RESPONSE_SIZE];

        for (int i = 0; i < adResponse.length; i++) {
            if (i < this.dHighFrequencyCutoff) {
                adResponse[i] = 1;
            } else {
                adResponse[i] = this.dBoostCoefficient;
            }
        }

        setFrequencyResponse(adResponse);
    }

    /**
     * Allows to override the current boost coefficient.
     *
     * @param pdBoostCoefficient the new boost coefficient to set
     * @since 0.3.0.5
     */
    public void setBoostCoefficient(double pdBoostCoefficient) {
        this.dBoostCoefficient = pdBoostCoefficient;
    }

    /**
     * Retrieves the current boost coefficient value.
     *
     * @return returns the boost coefficient
     * @since 0.3.0.5
     */
    public double getBoostCoefficient() {
        return this.dBoostCoefficient;
    }

    /**
     * Allows to override the current boost coefficient.
     *
     * @param pdHighFrequencyCutoff the new boost coefficient to set
     * @since 0.3.0.5
     */
    public void setHighFrequencyCutoff(double pdHighFrequencyCutoff) {
        this.dHighFrequencyCutoff = pdHighFrequencyCutoff;
    }

    /**
     * Retrieves the current boost coefficient value.
     *
     * @return returns the boost coefficient
     * @since 0.3.0.5
     */
    public double getHighFrequencyCutoff() {
        return this.dBoostCoefficient;
    }

    /**
     * A common local routine for extraction filter
     * parameters via the ModuleParams machinery.
     *
     * @since 0.3.0.5
     */
    protected void processModuleParams() {
        ModuleParams oModuleParams = MARF.getModuleParams();

        if (oModuleParams != null) {
            Vector oFilterParams = oModuleParams.getPreprocessingParams();

            if (oFilterParams != null) {
                if (oFilterParams.size() > 0) {
                    setBoostCoefficient(((Double) oFilterParams.firstElement()).doubleValue());
                }

                if (oFilterParams.size() > 1) {
                    setHighFrequencyCutoff(((Double) oFilterParams.elementAt(1)).doubleValue());
                }
            }
        }
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.24 $";
    }
}

// EOF
