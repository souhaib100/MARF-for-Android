package marf.FeatureExtraction;

import marf.FeatureExtraction.Cepstral.Cepstral;
import marf.FeatureExtraction.F0.F0;
import marf.FeatureExtraction.FFT.FFT;
import marf.FeatureExtraction.LPC.LPC;
import marf.FeatureExtraction.MinMaxAmplitudes.MinMaxAmplitudes;
import marf.FeatureExtraction.RandomFeatureExtraction.RandomFeatureExtraction;
import marf.FeatureExtraction.Segmentation.Segmentation;
import marf.MARF;
import marf.Preprocessing.IPreprocessing;


/**
 * <p>Provides a factory to instantiate requested feature extraction module(s).</p>
 * <p>
 * $Id: FeatureExtractionFactory.java,v 1.2 2005/12/28 03:21:11 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.2 $
 * @since 0.3.0.5
 */
public final class FeatureExtractionFactory {
    /**
     * Disallow instances of this factory as deemed useless.
     */
    private FeatureExtractionFactory() {
    }

    /**
     * Instantiates a FeatureExtraction module indicated by
     * the first parameter with the 2nd parameter as an argument.
     *
     * @param poFeatureExtractionMethod the integer value corresponding to the
     *                                  desired feature extraction module
     * @param poPreprocessing           passed as an agrument to the feature extractor per framework requirement
     * @return a reference to the instance of the created feature extraction module
     * @throws FeatureExtractionException if the indicated module is
     *                                    uknown or could not be loaded
     * @see MARF#LPC
     * @see MARF#FFT
     * @see MARF#F0
     * @see MARF#SEGMENTATION
     * @see MARF#CEPSTRAL
     * @see MARF#RANDOM_FEATURE_EXTRACTION
     * @see MARF#MIN_MAX_AMPLITUDES
     * @see MARF#FEATURE_EXTRACTION_PLUGIN
     * @see MARF#FEATURE_EXTRACTION_AGGREGATOR
     * @see LPC
     * @see FFT
     * @see F0
     * @see Segmentation
     * @see Cepstral
     * @see RandomFeatureExtraction
     * @see MinMaxAmplitudes
     * @see FeatureExtractionAggregator
     */
    public static final IFeatureExtraction create(final Integer poFeatureExtractionMethod, IPreprocessing poPreprocessing)
            throws FeatureExtractionException {
        return create(poFeatureExtractionMethod.intValue(), poPreprocessing);
    }

    /**
     * Instantiates a FeatureExtraction module indicated by
     * the first parameter with the 2nd parameter as an argument.
     *
     * @param piFeatureExtractionMethod the integer value corresponding to the
     *                                  desired feature extraction module
     * @param poPreprocessing           passed as an agrument to the feature extractor per framework requirement
     * @return a reference to the instance of the created feature extraction module
     * @throws FeatureExtractionException if the indicated module is
     *                                    uknown or could not be loaded
     * @see MARF#LPC
     * @see MARF#FFT
     * @see MARF#F0
     * @see MARF#SEGMENTATION
     * @see MARF#CEPSTRAL
     * @see MARF#RANDOM_FEATURE_EXTRACTION
     * @see MARF#MIN_MAX_AMPLITUDES
     * @see MARF#FEATURE_EXTRACTION_PLUGIN
     * @see MARF#FEATURE_EXTRACTION_AGGREGATOR
     * @see LPC
     * @see FFT
     * @see F0
     * @see Segmentation
     * @see Cepstral
     * @see RandomFeatureExtraction
     * @see MinMaxAmplitudes
     * @see FeatureExtractionAggregator
     */
    public static final IFeatureExtraction create(final int piFeatureExtractionMethod, IPreprocessing poPreprocessing)
            throws FeatureExtractionException {
        IFeatureExtraction oFeatureExtraction = null;

        switch (piFeatureExtractionMethod) {
            case MARF.LPC:
                oFeatureExtraction = new LPC(poPreprocessing);
                break;

            case MARF.FFT:
                oFeatureExtraction = new FFT(poPreprocessing);
                break;

            case MARF.F0:
                oFeatureExtraction = new F0(poPreprocessing);
                break;

            case MARF.SEGMENTATION:
                oFeatureExtraction = new Segmentation(poPreprocessing);
                break;

            case MARF.CEPSTRAL:
                oFeatureExtraction = new Cepstral(poPreprocessing);
                break;

            case MARF.RANDOM_FEATURE_EXTRACTION:
                oFeatureExtraction = new RandomFeatureExtraction(poPreprocessing);
                break;

            case MARF.MIN_MAX_AMPLITUDES:
                oFeatureExtraction = new MinMaxAmplitudes(poPreprocessing);
                break;

            case MARF.FEATURE_EXTRACTION_PLUGIN: {
                try {
                    oFeatureExtraction = (IFeatureExtraction) MARF.getFeatureExtractionPluginClass().newInstance();
                    oFeatureExtraction.setPreprocessing(poPreprocessing);
                } catch (Exception e) {
                    throw new FeatureExtractionException(e.getMessage(), e);
                }

                break;
            }

            case MARF.FEATURE_EXTRACTION_AGGREGATOR: {
                oFeatureExtraction = new FeatureExtractionAggregator(poPreprocessing);
                break;
            }

            default: {
                throw new FeatureExtractionException
                        (
                                "Unknown feature extraction method: " + piFeatureExtractionMethod
                        );
            }
        }

        return oFeatureExtraction;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.2 $";
    }
}

// EOF
