package marf.Preprocessing;

import marf.MARF;
import marf.Preprocessing.Dummy.Dummy;
import marf.Preprocessing.Dummy.Raw;
import marf.Preprocessing.Endpoint.Endpoint;
import marf.Preprocessing.FFTFilter.BandpassFilter;
import marf.Preprocessing.FFTFilter.HighFrequencyBoost;
import marf.Preprocessing.FFTFilter.HighPassFilter;
import marf.Preprocessing.FFTFilter.LowPassFilter;
import marf.Storage.Sample;
import marf.util.Debug;


/**
 * <p>Provides a factory to instantiate requested preprocessing module(s).</p>
 * <p>
 * $Id: PreprocessingFactory.java,v 1.1 2005/12/28 03:21:11 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.1 $
 * @since 0.3.0.5
 */
public final class PreprocessingFactory {
    /**
     * Disallow instances of this factory as deemed useless.
     */
    private PreprocessingFactory() {
    }

    /**
     * Instantiates a Preprocessing module indicated by
     * the first parameter with the 2nd parameter as an argument.
     *
     * @param poPreprocessingMethod the integer value corresponding to the
     *                              desired Preprocessing module
     * @param poSample              passed as an agrument to the preprocessor per framework requirement
     * @return a reference to the instance of the created feature extraction module
     * @throws PreprocessingException if the indicated module is
     *                                uknown or could not be loaded
     * @see MARF#DUMMY
     * @see MARF#BANDPASS_FFT_FILTER
     * @see MARF#ENDPOINT
     * @see MARF#HIGH_FREQUENCY_BOOST_FFT_FILTER
     * @see MARF#LOW_PASS_FFT_FILTER
     * @see MARF#HIGH_PASS_FFT_FILTER
     * @see MARF#HIGH_PASS_BOOST_FILTER
     * @see MARF#RAW
     * @see MARF#PREPROCESSING_PLUGIN
     * @see Dummy
     * @see BandpassFilter
     * @see Endpoint
     * @see HighFrequencyBoost
     * @see LowPassFilter
     * @see HighPassFilter
     * @see Raw
     */
    public static final IPreprocessing create(final Integer poPreprocessingMethod, Sample poSample)
            throws PreprocessingException {
        return create(poPreprocessingMethod.intValue(), poSample);
    }

    /**
     * Instantiates a Preprocessing module indicated by
     * the first parameter with the 2nd parameter as an argument.
     *
     * @param piPreprocessingMethod the integer value corresponding to the
     *                              desired Preprocessing module
     * @param poSample              passed as an agrument to the preprocessor per framework requirement
     * @return a reference to the instance of the created feature extraction module
     * @throws PreprocessingException if the indicated module is
     *                                uknown or could not be loaded
     * @see MARF#DUMMY
     * @see MARF#BANDPASS_FFT_FILTER
     * @see MARF#ENDPOINT
     * @see MARF#HIGH_FREQUENCY_BOOST_FFT_FILTER
     * @see MARF#LOW_PASS_FFT_FILTER
     * @see MARF#HIGH_PASS_FFT_FILTER
     * @see MARF#HIGH_PASS_BOOST_FILTER
     * @see MARF#RAW
     * @see MARF#PREPROCESSING_PLUGIN
     * @see Dummy
     * @see BandpassFilter
     * @see Endpoint
     * @see HighFrequencyBoost
     * @see LowPassFilter
     * @see HighPassFilter
     * @see Raw
     */
    public static final IPreprocessing create(final int piPreprocessingMethod, Sample poSample)
            throws PreprocessingException {
        IPreprocessing oPreprocessing = null;

        switch (piPreprocessingMethod) {
            case MARF.DUMMY:
                oPreprocessing = new Dummy(poSample);
                break;

            case MARF.BANDPASS_FFT_FILTER:
                oPreprocessing = new BandpassFilter(poSample);
                break;

            case MARF.ENDPOINT:
                oPreprocessing = new Endpoint(poSample);
                break;

            case MARF.HIGH_FREQUENCY_BOOST_FFT_FILTER:
                Debug.debug("here1");
                oPreprocessing = new HighFrequencyBoost(poSample);
                Debug.debug("here2");
                break;

            case MARF.LOW_PASS_FFT_FILTER:
                oPreprocessing = new LowPassFilter(poSample);
                break;

            case MARF.HIGH_PASS_FFT_FILTER:
                oPreprocessing = new HighPassFilter(poSample);
                break;

            case MARF.HIGH_PASS_BOOST_FILTER:
                //soPreprocessing = new HighPassBoostFilter(soSample);
                oPreprocessing = new HighFrequencyBoost(new HighPassFilter(poSample));
                //soPreprocessing = new HighPassFilter(new HighFrequencyBoost(soSample));
                break;

            case MARF.RAW:
                oPreprocessing = new Raw(poSample);
                break;

            case MARF.PREPROCESSING_PLUGIN: {
                try {
                    oPreprocessing = (IPreprocessing) MARF.getPreprocessingPluginClass().newInstance();
                    oPreprocessing.setSample(poSample);
                } catch (Exception e) {
                    throw new PreprocessingException(e.getMessage(), e);
                }

                break;
            }

            default: {
                throw new PreprocessingException
                        (
                                "Unknown preprocessing method: " + piPreprocessingMethod
                        );
            }
        }

        return oPreprocessing;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.1 $";
    }
}

// EOF
