package marf.Classification;

import marf.Classification.Distance.ChebyshevDistance;
import marf.Classification.Distance.DiffDistance;
import marf.Classification.Distance.EuclideanDistance;
import marf.Classification.Distance.MahalanobisDistance;
import marf.Classification.Distance.MinkowskiDistance;
import marf.Classification.Markov.Markov;
import marf.Classification.NeuralNetwork.NeuralNetwork;
import marf.Classification.RandomClassification.RandomClassification;
import marf.Classification.Stochastic.Stochastic;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.MARF;


/**
 * <p>Provides a factory to instantiate requested classification module(s).</p>
 * <p>
 * $Id: ClassificationFactory.java,v 1.1 2005/12/28 03:21:11 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.1 $
 * @since 0.3.0.5
 */
public final class ClassificationFactory {
    /**
     * Disallow instances of this factory as deemed useless.
     */
    private ClassificationFactory() {
    }

    /**
     * Instantiates a Classification module indicated by
     * the first parameter with the 2nd parameter as an argument.
     *
     * @param poClassificationMethod the integer value corresponding to the
     *                               desired classification module
     * @param poFeatureExtraction    passed as an agrument to the classifier per framework requirement
     * @return a reference to the instance of the created feature extraction module
     * @throws ClassificationException if the indicated module is
     *                                 uknown or could not be loaded
     * @see MARF#NEURAL_NETWORK
     * @see MARF#STOCHASTIC
     * @see MARF#MARKOV
     * @see MARF#EUCLIDEAN_DISTANCE
     * @see MARF#CHEBYSHEV_DISTANCE
     * @see MARF#MINKOWSKI_DISTANCE
     * @see MARF#MAHALANOBIS_DISTANCE
     * @see MARF#RANDOM_CLASSIFICATION
     * @see MARF#DIFF_DISTANCE
     * @see MARF#CLASSIFICATION_PLUGIN
     * @see NeuralNetwork
     * @see Stochastic
     * @see Markov
     * @see ChebyshevDistance
     * @see EuclideanDistance
     * @see MinkowskiDistance
     * @see MahalanobisDistance
     * @see RandomClassification
     * @see DiffDistance
     */
    public static final IClassification create(final Integer poClassificationMethod, IFeatureExtraction poFeatureExtraction)
            throws ClassificationException {
        return create(poClassificationMethod.intValue(), poFeatureExtraction);
    }

    /**
     * Instantiates a Classification module indicated by
     * the first parameter with the 2nd parameter as an argument.
     *
     * @param piClassificationMethod the integer value corresponding to the
     *                               desired classification module
     * @param poFeatureExtraction    passed as an agrument to the classifier per framework requirement
     * @return a reference to the instance of the created feature extraction module
     * @throws ClassificationException if the indicated module is
     *                                 uknown or could not be loaded
     * @see MARF#NEURAL_NETWORK
     * @see MARF#STOCHASTIC
     * @see MARF#MARKOV
     * @see MARF#EUCLIDEAN_DISTANCE
     * @see MARF#CHEBYSHEV_DISTANCE
     * @see MARF#MINKOWSKI_DISTANCE
     * @see MARF#MAHALANOBIS_DISTANCE
     * @see MARF#RANDOM_CLASSIFICATION
     * @see MARF#DIFF_DISTANCE
     * @see MARF#CLASSIFICATION_PLUGIN
     * @see NeuralNetwork
     * @see Stochastic
     * @see Markov
     * @see ChebyshevDistance
     * @see EuclideanDistance
     * @see MinkowskiDistance
     * @see MahalanobisDistance
     * @see RandomClassification
     * @see DiffDistance
     */
    public static final IClassification create(final int piClassificationMethod, IFeatureExtraction poFeatureExtraction)
            throws ClassificationException {
        IClassification oClassification = null;

        switch (piClassificationMethod) {
            case MARF.NEURAL_NETWORK:
                oClassification = new NeuralNetwork(poFeatureExtraction);
                break;

            case MARF.STOCHASTIC:
                oClassification = new Stochastic(poFeatureExtraction);
                break;

            case MARF.MARKOV:
                oClassification = new Markov(poFeatureExtraction);
                break;

            case MARF.EUCLIDEAN_DISTANCE:
                oClassification = new EuclideanDistance(poFeatureExtraction);
                break;

            case MARF.CHEBYSHEV_DISTANCE:
                oClassification = new ChebyshevDistance(poFeatureExtraction);
                break;

            case MARF.MINKOWSKI_DISTANCE:
                oClassification = new MinkowskiDistance(poFeatureExtraction);
                break;

            case MARF.MAHALANOBIS_DISTANCE:
                oClassification = new MahalanobisDistance(poFeatureExtraction);
                break;

            case MARF.RANDOM_CLASSIFICATION:
                oClassification = new RandomClassification(poFeatureExtraction);
                break;

            case MARF.DIFF_DISTANCE:
                oClassification = new DiffDistance(poFeatureExtraction);
                break;

            case MARF.CLASSIFICATION_PLUGIN: {
                try {
                    oClassification = (IClassification) MARF.getClassificationPluginClass().newInstance();
                    oClassification.setFeatureExtraction(poFeatureExtraction);
                } catch (Exception e) {
                    throw new ClassificationException(e.getMessage(), e);
                }

                break;
            }

            default: {
                throw new ClassificationException
                        (
                                "Unknown classification method: " + piClassificationMethod
                        );
            }
        }

        return oClassification;
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
