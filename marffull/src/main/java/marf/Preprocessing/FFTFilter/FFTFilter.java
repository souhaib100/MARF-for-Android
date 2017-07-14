package marf.Preprocessing.FFTFilter;

import java.util.Vector;

import marf.MARF;
import marf.Preprocessing.IFilter;
import marf.Preprocessing.IPreprocessing;
import marf.Preprocessing.Preprocessing;
import marf.Preprocessing.PreprocessingException;
import marf.Storage.ModuleParams;
import marf.Storage.Sample;
import marf.math.Algorithms;
import marf.math.MathException;
import marf.util.Arrays;
import marf.util.Debug;


/**
 * <p>FFTFilter class implements filtering using the FFT algorithm.</p>
 * <p>Derivatives must set frequency response based on the type of filter they are.</p>
 * <p>
 * $Id: FFTFilter.java,v 1.30 2006/01/22 23:59:01 mokhov Exp $
 *
 * @author Stephen Sinclair
 * @author Serguei Mokhov
 * @version $Revision: 1.30 $
 * @since 0.0.1
 */
public abstract class FFTFilter
        extends Preprocessing
        implements IFilter {
    /**
     * Default size of the frequency response vector, 128.
     */
    public static transient final int DEFAULT_FREQUENCY_RESPONSE_SIZE = 128;

    /**
     * Frequency repsonse to be multiplied by the incoming value.
     */
    protected transient double[] adFreqResponse = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = 6541078445959523547L;

    /**
     * Default constructor for reflective creation of Preprocessing
     * clones. Typically should not be used unless really necessary
     * for the frameworked modules.
     *
     * @since 0.3.0.5
     */
    public FFTFilter() {
        super();
    }

    /**
     * Pipelined constructor.
     *
     * @param poPreprocessing followup preprocessing module
     * @throws PreprocessingException
     * @since 0.3.0.3
     */
    public FFTFilter(IPreprocessing poPreprocessing)
            throws PreprocessingException {
        super(poPreprocessing);
        genereateResponseCoefficients();
    }

    /**
     * FFTFilter Constructor.
     *
     * @param poSample incoming sample
     * @throws PreprocessingException
     */
    public FFTFilter(Sample poSample)
            throws PreprocessingException {
        super(poSample);
        Debug.debug(FFTFilter.class, "about to generate coefficiencies");
        genereateResponseCoefficients();
    }

    /**
     * FFTFilter implementation of <code>preprocess()</code>.
     * <p>It does call <code>removeNoise()</code> and <code>removeSilence()</code>
     * if they were explicitly requested by an app <em>before</em> applying filtering.</p>
     * <p>
     * <b>NOTE</b>: it alters inner Sample by resetting its data array to the new
     * filtered values.
     *
     * @return <code>true</code> if there was something filtered out
     * @throws PreprocessingException if the frequency response is null
     */
    public boolean preprocess()
            throws PreprocessingException {
        if (this.adFreqResponse == null) {
            throw new PreprocessingException
                    (
                            "FFTFilter.preprocess() - frequency response is null"
                    );
        }

        boolean bChanges = normalize();

        double[] adSampleData = this.oSample.getSampleArray();
        double[] adFilteredData = new double[adSampleData.length];

        // By default we do not remove noise or silence
        boolean bRemoveNoise = false;
        boolean bRemoveSilence = false;

        // Extract any additional params if supplied

        ModuleParams oModuleParams = MARF.getModuleParams();

        if (oModuleParams != null) {
            Vector oParams = oModuleParams.getPreprocessingParams();

            if (oParams.size() > 0) {
                bRemoveNoise = ((Boolean) oParams.elementAt(0)).booleanValue();
                bRemoveSilence = ((Boolean) oParams.elementAt(1)).booleanValue();
            }
        }

        if (bRemoveNoise == true) {
            bChanges |= removeNoise();
        }

        if (bRemoveSilence == true) {
            bChanges |= removeSilence();
        }

        bChanges |= filter(adSampleData, adFilteredData);

        this.oSample.setSampleArray(adFilteredData);

        return bChanges;
    }

    /**
     * Sets frequency response.
     * Derivatives must call this method before any preprocessing occurs.
     *
     * @param padPesponse desired frequency response coefficients
     */
    public final void setFrequencyResponse(final double[] padPesponse) {
        this.adFreqResponse = new double[padPesponse.length * 2];

        // Forward copy
        Arrays.copy(this.adFreqResponse, 0, padPesponse);

        // Backward copy
        for (int i = 0; i < padPesponse.length; i++) {
            this.adFreqResponse[this.adFreqResponse.length - i - 1] = padPesponse[i];
        }
    }

    /**
     * Perform a filter by the following algorithm:
     * (1) sample -&gt; window -&gt; FFT -&gt; buffer<br />
     * (2) buffer * frequency response<br />
     * (3) buffer -&gt; IFFT -&gt; window -&gt; sample.
     * <p>
     * Window used is square root of Hamming window, because
     * the sum at half-window overlap is a constant, which
     * avoids amplitude distortion from noise.
     * <p>
     * Also, start sampling at <code>-responseSize/2</code>, in order to avoid
     * amplitude distortion of the first half of the first window.
     *
     * @param padSample   incoming sample analog data
     * @param padFiltered will contain data after the filter was applied.
     *                    "filtered" must be at least as long as "sample".
     * @return <code>true</code> if some filtering actually happened
     * @throws PreprocessingException if the filtered and sample data
     *                                arrays are not of the same size, the frequency response was not
     *                                set, or there was an underlying FeatureExctractionException while
     *                                executing the underlying FFT algorithm.
     */
    public final boolean filter(final double[] padSample, double[] padFiltered)
            throws PreprocessingException {
        try {
            int iResponseSize = this.adFreqResponse.length;

            double[] adBuffer = new double[iResponseSize];
            double[] adBufferImag = new double[iResponseSize];
            double[] adOutputReal = new double[iResponseSize];
            double[] adOutputImag = new double[iResponseSize];

            if (padFiltered.length < padSample.length) {
                throw new PreprocessingException
                        (
                                "FFTFilter: Output buffer not long enough (" +
                                        padFiltered.length + " < " + padSample.length + ")."
                        );
            }

            int i;

            int iPosition = -iResponseSize / 2;

            Debug.debug(getClass(), "position prior entry while(): " + iPosition + ", sample length: " + padSample.length);

            while (iPosition < padSample.length) {
                for (i = 0; i < iResponseSize; i++) {
                    if (((iPosition + i) < padSample.length) && ((iPosition + i) >= 0)) {
                        adBuffer[i] = padSample[iPosition + i] * Algorithms.Hamming.sqrtHamming(i, iResponseSize);
                    } else {
                        adBuffer[i] = 0;
                    }

                    adBufferImag[i] = 0;
                }

                Algorithms.FFT.doFFT(adBuffer, adBufferImag, adOutputReal, adOutputImag, 1);

                for (i = 0; i < iResponseSize; i++) {
                    adOutputReal[i] *= this.adFreqResponse[i];
                    adOutputImag[i] *= this.adFreqResponse[i];
                }

                Algorithms.FFT.doFFT(adOutputReal, adOutputImag, adBuffer, adBufferImag, -1);

                // Copy & normalize
                for (i = 0; (i < iResponseSize) && ((iPosition + i) < padSample.length); i++) {
                    if ((iPosition + i) >= 0) {
                        padFiltered[iPosition + i] +=
                                adBuffer[i] * Algorithms.Hamming.sqrtHamming(i, iResponseSize) / iResponseSize;
                    }
                }

                iPosition += iResponseSize / 2;

                //Debug.debug(getClass(), "position prior next iteration of while(): " + iPosition);
            }

            Debug.debug(getClass(), "done");

            return true;
        } catch (NullPointerException e) {
            throw new PreprocessingException("FFTFilter: frequency response hasn't been set.");
        } catch (MathException e) {
            throw new PreprocessingException("FFTFilter: " + e.getMessage());
        }
    }

    /**
     * Creates frequency response coefficients and sets applies
     * them to the frequency response vector. Must be overridden
     * by individual filters.
     *
     * @since 0.3.0
     */
    public abstract void genereateResponseCoefficients();

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.30 $";
    }
}

// EOF
