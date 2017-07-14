/*
 * The MARF System.
 */

package marf;

import marf.Classification.ClassificationException;
import marf.Classification.ClassificationFactory;
import marf.Classification.IClassification;
import marf.FeatureExtraction.FeatureExtractionFactory;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.Preprocessing.IPreprocessing;
import marf.Preprocessing.PreprocessingFactory;
import marf.Storage.ISampleLoader;
import marf.Storage.MARFAudioFileFormat;
import marf.Storage.ModuleParams;
import marf.Storage.Result;
import marf.Storage.ResultSet;
import marf.Storage.Sample;
import marf.Storage.SampleLoaderFactory;
import marf.gui.WaveGrapher;
import marf.nlp.NLPException;
import marf.util.Debug;
import marf.util.MARFException;
import marf.util.NotImplementedException;


/**
 * <p>Provides basic recognition pipeline and its configuration.</p>
 * <p>Implements a so-called MARF server.</p>
 * <p>
 * <p>$Id: MARF.java,v 1.98 2006/02/06 12:00:33 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @author Stephen Sinclair
 * @author The MARF Research and Development Group
 * @version $Revision: 1.98 $
 * @since 0.0.1
 */
public class MARF {
    /*
     * --------------------------------------------------------
	 * General
	 * --------------------------------------------------------
	 */

    /**
     * Value indecating that some configuration param is not set.
     */
    public static final int UNSET = -1;

	/*
     * NOTE: when maintaining enumeration, make sure none of them
	 *       overlap, to avoid any possible confusions.
	 */

	/*
	 * --------------------------------------------------------
	 * Preprocessing Modules Enumeration
	 * --------------------------------------------------------
	 */

    /**
     * Indicates to use Dummy preprocessing module (just normalization).
     */
    public static final int DUMMY = 100;

    /**
     * Indicates to use filter boosting high frequencies.
     */
    public static final int HIGH_FREQUENCY_BOOST_FFT_FILTER = 101;

    /**
     * Indicates to use band-pass filter.
     */
    public static final int BANDPASS_FFT_FILTER = 102;

    /**
     * Indicates to use endpointing.
     */
    public static final int ENDPOINT = 103;

    /**
     * Indicates to use low-pass FFT filter.
     */
    public static final int LOW_PASS_FFT_FILTER = 104;

    /**
     * Indicates to use high-pass FFT filter.
     */
    public static final int HIGH_PASS_FFT_FILTER = 105;

    /**
     * Indicates to use high-pass high-frequency boost FFT filter.
     *
     * @since 0.3.0.1
     */
    public static final int HIGH_PASS_BOOST_FILTER = 106;

    /**
     * Indicates to use raw preprocessing, which means no preprocessing.
     *
     * @since 0.3.0.2
     */
    public static final int RAW = 107;

    /**
     * Indicates to employ user-defined preprocessing plugin.
     *
     * @since 0.3.0.3
     */
    public static final int PREPROCESSING_PLUGIN = 108;

    /**
     * Upper boundary for classificantion methods enumeration.
     * Used in error checks.
     * <p>
     * *Update it when add more methods.*
     *
     * @since 0.3.0.1
     */
    public static final int MAX_PREPROCESSING_METHOD = PREPROCESSING_PLUGIN;

    /**
     * Lower boundary for classificantion methods enumeration.
     * Used in error checks.
     *
     * @since 0.3.0.1
     */
    public static final int MIN_PREPROCESSING_METHOD = DUMMY;

	/*
	 * --------------------------------------------------------
	 * Feature Extraction Modules Enumeration
	 * --------------------------------------------------------
	 */

    /**
     * Indicates to use LPC.
     */
    public static final int LPC = 300;

    /**
     * Indicates to use FFT.
     */
    public static final int FFT = 301;

    /**
     * Indicates to use F0.
     */
    public static final int F0 = 302;

    /**
     * Indicates to use segmentation.
     */
    public static final int SEGMENTATION = 303;

    /**
     * Indicates to use cepstral analysis.
     */
    public static final int CEPSTRAL = 304;

    /**
     * Indicates to use random feature extraction.
     *
     * @since 0.2.0
     */
    public static final int RANDOM_FEATURE_EXTRACTION = 305;

    /**
     * Indicates to use min/max amplitude extraction.
     *
     * @since 0.3.0
     */
    public static final int MIN_MAX_AMPLITUDES = 306;

    /**
     * Indicates to employ user-defined feature extraction plugin.
     *
     * @since 0.3.0.3
     */
    public static final int FEATURE_EXTRACTION_PLUGIN = 307;

    /**
     * Indicates to use an aggregation of several feature extraction
     * modules. The modules to aggregate must be specified in the
     * MARF's ModuleParams.
     *
     * @see marf.FeatureExtraction.FeatureExtractionAggregator
     * @see #setModuleParams(ModuleParams)
     * @since 0.3.0.5
     */
    public static final int FEATURE_EXTRACTION_AGGREGATOR = 308;

    /**
     * Upper boundary for classificantion methods enumeration.
     * Used in error checks. *Update it when add more methods.*
     *
     * @since 0.3.0.1
     */
    public static final int MAX_FEATUREEXTRACTION_METHOD = FEATURE_EXTRACTION_AGGREGATOR;

    /**
     * Lower boundary for classificantion methods enumeration.
     * Used in error checks.
     *
     * @since 0.3.0.1
     */
    public static final int MIN_FEATUREEXTRACTION_METHOD = LPC;


	/*
	 * --------------------------------------------------------
	 * Classification Modules Enumeration
	 * --------------------------------------------------------
	 */

    /**
     * Indicates to use Neural Network for classification.
     */
    public static final int NEURAL_NETWORK = 500;

    /**
     * Indicates to use stochastic models for classification.
     */
    public static final int STOCHASTIC = 501;

    /**
     * Indicates to use Hidden Markov Models for classification.
     */
    public static final int MARKOV = 502;

    /**
     * Indicates to use Euclidean distance for classification.
     */
    public static final int EUCLIDEAN_DISTANCE = 503;

    /**
     * Indicates to use Chebyshev distance for classification.
     */
    public static final int CHEBYSHEV_DISTANCE = 504;

    /**
     * A synonym to Chebyshev distance.
     *
     * @since 0.2.0
     */
    public static final int MANHATTAN_DISTANCE = 504;

    /**
     * A synonym to Chebyshev distance.
     *
     * @since 0.3.0.1
     */
    public static final int CITYBLOCK_DISTANCE = 504;

    /**
     * Indicates to use Minkowski distance for classification.
     *
     * @since 0.2.0
     */
    public static final int MINKOWSKI_DISTANCE = 505;

    /**
     * Indicates to use Mahalanobis distance for classification.
     *
     * @since 0.2.0
     */
    public static final int MAHALANOBIS_DISTANCE = 506;

    /**
     * Indicates to use random classification.
     *
     * @since 0.2.0
     */
    public static final int RANDOM_CLASSIFICATION = 507;

    /**
     * Indicates to use diff-distance classification.
     *
     * @since 0.3.0.2
     */
    public static final int DIFF_DISTANCE = 508;

    /**
     * Indicates to employ user-defined classification plugin.
     *
     * @since 0.3.0.3
     */
    public static final int CLASSIFICATION_PLUGIN = 509;

    /**
     * Upper boundary for classificantion methods enumeration.
     * Used in error checks. *Update it when add more methods.*
     *
     * @since 0.3.0.1
     */
    public static final int MAX_CLASSIFICATION_METHOD = CLASSIFICATION_PLUGIN;

    /**
     * Lower boundary for classificantion methods enumeration.
     * Used in error checks.
     *
     * @since 0.3.0.1
     */
    public static final int MIN_CLASSIFICATION_METHOD = NEURAL_NETWORK;


	/*
	 * --------------------------------------------------------
	 * Supported Sample File Formats Enumeration
	 * --------------------------------------------------------
	 */

    /**
     * Indicates WAVE incoming sample file format.
     */
    public static final int WAV = MARFAudioFileFormat.WAV;

    /**
     * Indicates ULAW incoming sample file format.
     */
    public static final int ULAW = MARFAudioFileFormat.ULAW;

    /**
     * Indicates MP3 incoming sample file format.
     */
    public static final int MP3 = MARFAudioFileFormat.MP3;

    /**
     * Sine sample format.
     *
     * @since 0.3.0.2
     */
    public static final int SINE = MARFAudioFileFormat.SINE;

    /**
     * AIFF sample format.
     *
     * @since 0.3.0.2
     */
    public static final int AIFF = MARFAudioFileFormat.AIFF;

    /**
     * AIFF-C sample format.
     *
     * @since 0.3.0.2
     */
    public static final int AIFFC = MARFAudioFileFormat.AIFFC;

    /**
     * AU sample format.
     *
     * @since 0.3.0.2
     */
    public static final int AU = MARFAudioFileFormat.AU;

    /**
     * SND sample format.
     *
     * @since 0.3.0.2
     */
    public static final int SND = MARFAudioFileFormat.SND;

    /**
     * MIDI sample format.
     *
     * @since 0.3.0.2
     */
    public static final int MIDI = MARFAudioFileFormat.MIDI;

    /**
     * Custom (plugin) sample format.
     *
     * @since 0.3.0.5
     */
    public static final int CUSTOM = MARFAudioFileFormat.CUSTOM;

	
	/*
	 * --------------------------------------------------------
	 * Module Instance References
	 * --------------------------------------------------------
	 */

    /**
     * Internal <code>Sample</code> reference.
     *
     * @since 0.2.0
     */
    private static Sample soSample = null;

    /**
     * Internal <code>SampleLoader</code> reference.
     *
     * @since 0.2.0
     */
    private static ISampleLoader soSampleLoader = null;

    /**
     * Internal <code>Preprocessing</code> reference.
     *
     * @since 0.2.0
     */
    private static IPreprocessing soPreprocessing = null;

    /**
     * Internal <code>FeatureExtraction</code> reference.
     *
     * @since 0.2.0
     */
    private static IFeatureExtraction soFeatureExtraction = null;

    /**
     * Internal <code>Classification</code> reference.
     *
     * @since 0.2.0
     */
    private static IClassification soClassification = null;


	/*
	 * --------------------------------------------------------
	 * Versioning
	 * --------------------------------------------------------
	 */

    /**
     * Indicates major MARF version, like <b>1</b>.x.x.
     * As of 0.3.0.3 made public.
     * As of 0.3.0.5 is always equals to <code>Version.MAJOR_VERSION</code>.
     *
     * @see Version#MAJOR_VERSION
     */
    public static final int MAJOR_VERSION = Version.MAJOR_VERSION;

    /**
     * Indicates minor MARF version, like 1.<b>1</b>.x.
     * As of 0.3.0.3 made public.
     * As of 0.3.0.5 is always equals to <code>Version.MINOR_VERSION</code>.
     *
     * @see Version#MINOR_VERSION
     */
    public static final int MINOR_VERSION = Version.MINOR_VERSION;

    /**
     * Indicates MARF revision, like 1.1.<b>1</b>.
     * As of 0.3.0.3 made public.
     * As of 0.3.0.5 is always equals to <code>Version.REVISION</code>.
     *
     * @see Version#REVISION
     */
    public static final int REVISION = Version.REVISION;

    /**
     * Indicates MARF minor development revision, like 1.1.1.<b>1</b>.
     * This is primarily for development releases. On the final release
     * the counting stops and is reset to 0 every minor version.
     * As of 0.3.0.3 made public.
     * As of 0.3.0.5 is always equals to <code>Version.MINOR_REVISION</code>.
     *
     * @see #MINOR_VERSION
     * @see Version#MINOR_REVISION
     * @since 0.3.0.2
     */
    public static final int MINOR_REVISION = Version.MINOR_REVISION;


	/*
	 * --------------------------------------------------------
	 * Current state of MARF
	 * --------------------------------------------------------
	 */

    /**
     * Indicates what preprocessing method to use in the pipeline.
     */
    private static int siPreprocessingMethod = UNSET;

    /**
     * Indicates what feature extraction method to use in the pipeline.
     */
    private static int siFeatureExtractionMethod = UNSET;

    /**
     * Indicates what classification method to use in the pipeline.
     */
    private static int siClassificationMethod = UNSET;

    /**
     * Indicates what sample format is in use.
     */
    private static int siSampleFormat = UNSET;

    /**
     * ID of the currently trained speaker.
     */
    private static int siCurrentSubject = UNSET;

    /**
     * Indicates current incoming sample filename.
     */
    private static String sstrFileName = "";

    /**
     * Indicates directory name with training samples.
     */
    private static String sstrSamplesDir = "";

    /**
     * Stores module-specific parameters in an independent way.
     */
    private static ModuleParams soModuleParams = null;

    /**
     * Indicates whether or not to dump a spectrogram at the end of feature extraction.
     */
    private static boolean sbDumpSpectrogram = false;

    /**
     * Indicates whether or not to dump a wave graph.
     */
    private static boolean sbDumpWaveGraph = false;

    /**
     * Class of a sample loader plugin.
     *
     * @since 0.3.0.5
     */
    private static Class soSampleLoaderPluginClass = null;

    /**
     * Class of a preprocessing plugin.
     *
     * @since 0.3.0.4
     */
    private static Class soPreprocessingPluginClass = null;

    /**
     * Class of a feature extraction plugin.
     *
     * @since 0.3.0.4
     */
    private static Class soFeatureExtractionPluginClass = null;

    /**
     * Class of a classification plugin.
     *
     * @since 0.3.0.4
     */
    private static Class soClassificationPluginClass = null;


	/*
	 * --------------------------------------------------------
	 * Methods
	 * --------------------------------------------------------
	 */

    /**
     * Must never be instantiated or inherited from...
     * Or should it be allowed?
     * <p><b>TODO:</b> The server part</p>.
     */
    private MARF() {
    }


	/*
	 * --------------------------------------------------------
	 * Setting/Getting MARF Configuration Parameters
	 * --------------------------------------------------------
	 */

    /**
     * Sets preprocessing method to be used.
     *
     * @param piPreprocessingMethod one of the allowed preprocessing methods
     * @throws MARFException if the parameter outside of the valid range
     */
    public static synchronized final void setPreprocessingMethod(final int piPreprocessingMethod)
            throws MARFException {
        if (piPreprocessingMethod < MIN_PREPROCESSING_METHOD || piPreprocessingMethod > MAX_PREPROCESSING_METHOD) {
            throw new MARFException
                    (
                            "Preprocessing method (" + piPreprocessingMethod +
                                    ") is out of range [" + MIN_PREPROCESSING_METHOD + "," + MAX_PREPROCESSING_METHOD + "]."
                    );
        }

        siPreprocessingMethod = piPreprocessingMethod;
    }

    /**
     * Gets currently selected preprocessing method.
     *
     * @return one of the preprocessing methods
     */
    public static synchronized final int getPreprocessingMethod() {
        return siPreprocessingMethod;
    }

    /**
     * Sets feature extraction method to be used.
     *
     * @param piFeatureExtractionMethod one of the allowed feature extraction methods
     * @throws MARFException if the parameter outside of the valid range
     */
    public static synchronized final void setFeatureExtractionMethod(final int piFeatureExtractionMethod)
            throws MARFException {
        if (piFeatureExtractionMethod < MIN_FEATUREEXTRACTION_METHOD || piFeatureExtractionMethod > MAX_FEATUREEXTRACTION_METHOD) {
            throw new MARFException
                    (
                            "Feature extraction method (" + piFeatureExtractionMethod +
                                    ") is out of range [" + MIN_FEATUREEXTRACTION_METHOD + "," + MAX_FEATUREEXTRACTION_METHOD + "]."
                    );
        }

        siFeatureExtractionMethod = piFeatureExtractionMethod;
    }

    /**
     * Gets currently selected feature extraction method.
     *
     * @return current feature extraction method
     */
    public static synchronized final int getFeatureExtractionMethod() {
        return siFeatureExtractionMethod;
    }

    /**
     * Sets classification method to be used.
     *
     * @param piClassificationMethod one of the allowed classification methods
     * @throws MARFException if the parameter outside of the valid range
     */
    public static synchronized final void setClassificationMethod(final int piClassificationMethod)
            throws MARFException {
        if (piClassificationMethod < MIN_CLASSIFICATION_METHOD || piClassificationMethod > MAX_CLASSIFICATION_METHOD) {
            throw new MARFException
                    (
                            "Classification method (" + piClassificationMethod +
                                    ") is out of range [" + MIN_CLASSIFICATION_METHOD + "," + MAX_CLASSIFICATION_METHOD + "]."
                    );
        }

        siClassificationMethod = piClassificationMethod;
    }

    /**
     * Gets classification method to be used.
     *
     * @return current classification method
     */
    public static synchronized final int getClassificationMethod() {
        return siClassificationMethod;
    }

    /**
     * Sets input sample file format.
     *
     * @param piSampleFormat one of the allowed sample formats
     */
    public static synchronized final void setSampleFormat(final int piSampleFormat) {
        siSampleFormat = piSampleFormat;
    }

    /**
     * Gets input sample file format.
     *
     * @return current sample format
     */
    public static synchronized final int getSampleFormat() {
        return siSampleFormat;
    }

    /**
     * Sets input sample file name.
     *
     * @param pstrFileName string representing sample file to be read
     */
    public static synchronized final void setSampleFile(final String pstrFileName) {
        sstrFileName = pstrFileName;
    }

    /**
     * Obtains filename of a sample currently being processed.
     *
     * @return file name of a string representing sample file
     */
    public static synchronized final String getSampleFile() {
        return sstrFileName;
    }

    /**
     * Sets directory with sample files to be read from.
     *
     * @param pstrSamplesDir string representing directory name
     */
    public static synchronized final void setSamplesDir(final String pstrSamplesDir) {
        sstrSamplesDir = pstrSamplesDir;
    }

    /**
     * Sets module-specific parameters an application programmer wishes to pass on to the module.
     *
     * @param poModuleParams parameters' instance
     */
    public static synchronized final void setModuleParams(final ModuleParams poModuleParams) {
        soModuleParams = poModuleParams;
    }

    /**
     * Gets module-specific parameters an application programmer passed on to the module.
     *
     * @return ModuleParams object reference
     */
    public static synchronized final ModuleParams getModuleParams() {
        return soModuleParams;
    }

    /**
     * Indicates whether spectrogram is wanted as an output of a FeatureExtraction module.
     *
     * @param pbDump <code>true</code> if wanted, <code>false</code> if not
     */
    public static synchronized final void setDumpSpectrogram(final boolean pbDump) {
        sbDumpSpectrogram = pbDump;
    }

    /**
     * Whether spectrogram wanted or not.
     *
     * @return <code>true</code> if spectrogram being dumped, <code>false</code> otherwise
     */
    public static synchronized final boolean getDumpSpectrogram() {
        return sbDumpSpectrogram;
    }

    /**
     * Indicates whether wave graph is wanted as an output.
     *
     * @param pbDump <code>true</code> if wanted, <code>false</code> if not
     */
    public static synchronized final void setDumpWaveGraph(final boolean pbDump) {
        sbDumpWaveGraph = pbDump;
    }

    /**
     * Whether wave graph wanted or not.
     *
     * @return <code>true</code> if graph wanted being dumped, <code>false</code> otherwise
     */
    public static synchronized final boolean getDumpWaveGraph() {
        return sbDumpWaveGraph;
    }

    /**
     * Sets ID of a subject currently being trained on.
     *
     * @param piSubjectID integer ID of the subject
     */
    public static synchronized final void setCurrentSubject(final int piSubjectID) {
        siCurrentSubject = piSubjectID;
    }

    /**
     * Gets ID of a subject currently being trained on.
     *
     * @return integer ID of the subject
     * @since 0.2.0
     */
    public static synchronized final int getCurrentSubject() {
        return siCurrentSubject;
    }

    /**
     * Allows loading a sample loader plugin by its name.
     *
     * @param pstrClassName class name of the plugin sample loader module
     * @throws MARFException if class cannot be loaded for any reason
     * @since 0.3.0.5
     */
    public static synchronized final void setSampleLoaderPluginClass(String pstrClassName)
            throws MARFException {
        try {
            soSampleLoaderPluginClass = Class.forName(pstrClassName);
        } catch (Exception e) {
            throw new MARFException(e.getMessage(), e);
        }
    }

    /**
     * Allows setting a loaded sample loader plugin class.
     *
     * @param poClass class represting a sample loader plugin object
     * @throws MARFException if the parameter is <code>null</code>
     * @since 0.3.0.5
     */
    public static synchronized final void setSampleLoaderPluginClass(Class poClass)
            throws MARFException {
        if (poClass == null) {
            throw new MARFException("Plugin class cannot be null.");
        }

        soSampleLoaderPluginClass = poClass;
    }

    /**
     * Allows querying for the current preprocessing plugin class.
     *
     * @return the internal plugin class
     * @since 0.3.0.5
     */
    public static synchronized final Class getSampleLoaderPluginClass() {
        return soSampleLoaderPluginClass;
    }

    /**
     * Allows loading a preprocessing plugin by its name.
     *
     * @param pstrClassName class name of the plugin preprocessing module
     * @throws MARFException if class cannot be loaded for any reason
     * @since 0.3.0.4
     */
    public static synchronized final void setPreprocessingPluginClass(String pstrClassName)
            throws MARFException {
        try {
            soPreprocessingPluginClass = Class.forName(pstrClassName);
        } catch (Exception e) {
            throw new MARFException(e.getMessage(), e);
        }
    }

    /**
     * Allows setting a loaded preprocessing plugin class.
     *
     * @param poClass class represting a preprocessing plugin object
     * @throws MARFException if the parameter is <code>null</code>
     * @since 0.3.0.4
     */
    public static synchronized final void setPreprocessingPluginClass(Class poClass)
            throws MARFException {
        if (poClass == null) {
            throw new MARFException("Plugin class cannot be null.");
        }

        soPreprocessingPluginClass = poClass;
    }

    /**
     * Allows querying for the current preprocessing plugin class.
     *
     * @return the internal plugin class
     * @since 0.3.0.4
     */
    public static synchronized final Class getPreprocessingPluginClass() {
        return soPreprocessingPluginClass;
    }

    /**
     * Allows loading a feature extraction plugin by its name.
     *
     * @param pstrClassName class name of the plugin feature extraction module
     * @throws MARFException if class cannot be loaded for any reason
     * @since 0.3.0.4
     */
    public static synchronized final void setFeatureExtractionPluginClass(String pstrClassName)
            throws MARFException {
        try {
            soFeatureExtractionPluginClass = Class.forName(pstrClassName);
        } catch (Exception e) {
            throw new MARFException(e.getMessage(), e);
        }
    }

    /**
     * Allows setting a loaded feature extraction class plugin class.
     *
     * @param poClass class represting a feature extraction plugin object
     * @throws MARFException if the parameter is <code>null</code>
     * @since 0.3.0.4
     */
    public static synchronized final void setFeatureExtractionPluginClass(Class poClass)
            throws MARFException {
        if (poClass == null) {
            throw new MARFException("Plugin class cannot be null.");
        }

        soFeatureExtractionPluginClass = poClass;
    }

    /**
     * Allows querying for the current feature extraction plugin class.
     *
     * @return the internal plugin class
     * @since 0.3.0.4
     */
    public static synchronized final Class getFeatureExtractionPluginClass() {
        return soFeatureExtractionPluginClass;
    }

    /**
     * Allows loading a classification plugin by its name.
     *
     * @param pstrClassName class name of the plugin classification module
     * @throws MARFException if class cannot be loaded for any reason
     * @since 0.3.0.4
     */
    public static synchronized final void setClassificationPluginClass(String pstrClassName)
            throws MARFException {
        try {
            soClassificationPluginClass = Class.forName(pstrClassName);
        } catch (Exception e) {
            throw new MARFException(e.getMessage(), e);
        }
    }

    /**
     * Allows setting a loaded classification plugin class.
     *
     * @param poClass class represting a classification plugin object
     * @throws MARFException if the parameter is <code>null</code>
     * @since 0.3.0.4
     */
    public static synchronized final void setClassificationPluginClass(Class poClass)
            throws MARFException {
        if (poClass == null) {
            throw new MARFException("Plugin class cannot be null.");
        }

        soClassificationPluginClass = poClass;
    }

    /**
     * Allows querying for the current classification plugin class.
     *
     * @return the internal plugin class
     * @since 0.3.0.4
     */
    public static synchronized final Class getClassificationPluginClass() {
        return soClassificationPluginClass;
    }

    /**
     * Returns a string representation of the MARF version.
     * As of 0.3.0.3 MINOR_REVISION is also returned.
     *
     * @return version String
     * @see #MINOR_REVISION
     */
    public static final String getVersion() {
        return Version.getStringVersion();
    }

    /**
     * Returns an integer representation of the MARF version.
     * As of 0.3.0.3, MINOR_REVISION is included into calculations
     * and the formula changed to begin with 1000 as a MAJOR_VERSION
     * coefficient.
     *
     * @return integer version as <code>MAJOR_VERSION * 1000 + MINOR_VERSION * 100 + REVISION * 10 + MINOR_REVISION</code>
     * @see #MAJOR_VERSION
     * @see #MINOR_VERSION
     * @see #REVISION
     * @see #MINOR_REVISION
     */
    public static final int getIntVersion() {
        return Version.getIntVersion();
    }

    /**
     * Retrieves double version of MARF. Unlike the integer version, the double
     * one begins with 100 and the minor revision is returned after the point,
     * e.g. 123.4 for 1.2.3.4.
     *
     * @return double version as <code>MAJOR_VERSION * 100 + MINOR_VERSION * 10 + REVISION + MINOR_REVISION / 10</code>
     * @see #MAJOR_VERSION
     * @see #MINOR_VERSION
     * @see #REVISION
     * @see #MINOR_REVISION
     * @since 0.3.0.3
     */
    public static final double getDoubleVersion() {
        return Version.getDoubleVersion();
    }

    /**
     * Returns a string representation of the current MARF configuration.
     *
     * @return configuration string
     */
    public static synchronized final String getConfig() {
        // TODO: more human-readable output
        return new StringBuffer()
                .append("[")
                .append("PR: ").append(siPreprocessingMethod).append(", ")
                .append("FE: ").append(siFeatureExtractionMethod).append(", ")
                .append("CL: ").append(siClassificationMethod).append(", ")
                .append("ID: ").append(siCurrentSubject)
                .append("]")
                .toString();
    }

    /**
     * Retrieves current <code>Sample</code> reference.
     *
     * @return Sample object
     * @since 0.2.0
     */
    public static synchronized final Sample getSample() {
        return soSample;
    }

    /**
     * Retrieves current <code>SampleLoader</code> reference.
     *
     * @return SampleLoader object
     * @since 0.2.0
     */
    public static synchronized final ISampleLoader getSampleLoader() {
        return soSampleLoader;
    }

    /**
     * Retrieves current <code>Preprocessing</code> reference.
     *
     * @return Preprocessing object
     * @since 0.2.0
     */
    public static synchronized final IPreprocessing getPreprocessing() {
        return soPreprocessing;
    }

    /**
     * Retrieves current <code>FeatureExtraction</code> reference.
     *
     * @return FeatureExtraction object
     * @since 0.2.0
     */
    public static synchronized final IFeatureExtraction getFeatureExtraction() {
        return soFeatureExtraction;
    }

    /**
     * Retrieves current <code>Classification</code> reference.
     *
     * @return Classification object
     * @since 0.2.0
     */
    public static synchronized final IClassification getClassification() {
        return soClassification;
    }

    /**
     * Queries for the final classification result.
     *
     * @return integer ID of the indentified subject
     */
    public static synchronized final int queryResultID() {
        return soClassification.getResult().getID();
    }

    /**
     * Gets the entire Result object of the likely outcome.
     *
     * @return Result ID and all the stats of the classification
     */
    public static synchronized final Result getResult() {
        return soClassification.getResult();
    }

    /**
     * Gets the entire collection of results.
     *
     * @return ResultSet object with one or more results.
     * @since 0.3.0.2
     */
    public static synchronized final ResultSet getResultSet() {
        return soClassification.getResultSet();
    }

	/* API */

    /**
     * Recognition/Identification mode.
     *
     * @throws MARFException if there was an error in the pipeline
     *                       or classification
     * @since 0.2.0
     */
    public static final void recognize()
            throws MARFException {
        startRecognitionPipeline();

        Debug.debug("MARF: Classifying...");

        synchronized (soClassification) {
            if (soClassification.classify() == false) {
                throw new ClassificationException("Classification returned false.");
            }
        }
    }

    /**
     * Training mode.
     *
     * @throws MARFException if the subject is unset or there was
     *                       an error in training in the underlying classification module
     * @since 0.2.0
     */
    public static final void train()
            throws MARFException {
        //if(siCurrentSubject == UNSET)
        if (getCurrentSubject() == UNSET) {
            throw new MARFException("Unset subject ID for training.");
        }

        startRecognitionPipeline();

        Debug.debug("MARF: Training...");

        synchronized (soClassification) {
            //XXX batch: soClassification.restore();

            if (soClassification.train() == false) {
                throw new ClassificationException("Training returned false.");
            }

            //XXX batch: soClassification.dump();
        }
    }

    /**
     * The core processing pipeline. The pipeline work
     * through almost entire process up until creation
     * of the classification module. Then the application's
     * choice depicts whether to call train() or classify()
     * on this module via MARF's train() or recognize() methods.
     * The pipeline is granularly synchronized on each object
     * that gets instantiated, so no indirect blocking occurs
     * on MARF itself (e.g. betwen the main thread and the inner
     * threads of the framework that maybe accessing the MARF
     * class concurrently).
     *
     * @throws MARFException in case any underlying error happens
     * @see #train()
     * @see #recognize()
     */
    private static final void startRecognitionPipeline()
            throws MARFException {
		/*
		 * Checking minimal required settings
		 */
        checkSettings();

		/*
		 * Sample Loading Stage
		 */
        synchronized (sstrFileName) {
            Debug.debug("Loading sample \"" + getSampleFile() + "\"");
            soSampleLoader = SampleLoaderFactory.create(siSampleFormat);

            synchronized (soSampleLoader) {
                soSample = soSampleLoader.loadSample(sstrFileName);

				/*
				 * Preprocessing Stage
				 */
                synchronized (soSample) {
                    Debug.debug("Preprocessing...");
                    soPreprocessing = PreprocessingFactory.create(siPreprocessingMethod, soSample);

                    synchronized (soPreprocessing) {
                        // TODO: [SM]: Should this be in the preprocessing itself somewhere?

                        if (sbDumpWaveGraph) {
                            Debug.debug("Duming initial wave graph...");

                            new WaveGrapher
                                    (
                                            soSample.getSampleArray(),
                                            0,
                                            soSample.getSampleArray().length,
                                            getSampleFile(),
                                            "initial"
                                    ).dump();
                        }

                        Debug.debug("Invoking preprocess() of " + soPreprocessing.getClass().getName());
                        soPreprocessing.preprocess();
                        Debug.debug("Done preprocess() of " + soPreprocessing.getClass().getName());

                        if (sbDumpWaveGraph) {
                            Debug.debug("Duming preprocessed wave graph...");

                            new WaveGrapher
                                    (
                                            soSample.getSampleArray(),
                                            0,
                                            soSample.getSampleArray().length,
                                            getSampleFile(),
                                            "preprocessed"
                                    ).dump();
                        }

						/*
						 * Feature Extraction Stage
						 */
                        Debug.debug("Feature extraction...");
                        soFeatureExtraction = FeatureExtractionFactory.create(siFeatureExtractionMethod, soPreprocessing);

                        synchronized (soFeatureExtraction) {
                            soFeatureExtraction.extractFeatures();

							/*
							 * Classification Stage
							 */
                            Debug.debug("Classification...");
                            soClassification = ClassificationFactory.create(siClassificationMethod, soFeatureExtraction);
						
							/*
							 * Classification ends in here, as it is continue in one
							 * way or the other in train() or recognize() depending on
							 * the curren run-time mode.
							 */
                        }// feat
                    }// prep
                }//sample itself
            }//sampleloader
        }//samplefilename
    }

    /**
     * Checks for all necessary settings to be present.
     * Specifically, checks whether preprocessing, feature
     * extraction, classification methods are set as well as
     * the audio sample format. The filename of the sample
     * or a directory must also be present.
     *
     * @throws MARFException if any of the settings are unset
     * @since 0.3.0.5
     */
    private static synchronized void checkSettings()
            throws MARFException {
        if
                (
                siPreprocessingMethod == UNSET ||
                        siFeatureExtractionMethod == UNSET ||
                        siClassificationMethod == UNSET ||
                        siSampleFormat == UNSET ||

                        // TODO: either filename or dir must present
                        (sstrFileName.equals("") && sstrSamplesDir.equals(""))
                ) {
            // TODO: Enhance error reporting here
            String strSetupErrMsg =
                    "MARF.startRecognitionPipeline() - Some configuration parameters were unset.\n" +
                            getConfig();

            throw new MARFException(strSetupErrMsg);
        }
    }

    /**
     * Meant to provide implementation of the buffered sample processing for large samples.
     * Not implemented.
     *
     * @throws NotImplementedException
     */
    public static synchronized final void streamedRecognition() {
        throw new NotImplementedException("MARF.streamedRecognition()");
    }

    /**
     * <p>Enumeration of Statistical Estimators.
     * In 0.3.0.5 renamed from <code>IStatisticalEstimators</code> to
     * <code>EStatisticalEstimators</code>.
     * </p>
     *
     * @author Serguei Mokhov
     * @since 0.3.0.2
     */
    public interface EStatisticalEstimators {
        /**
         * Indicates to use Maximum Likelyhood Estimate estimator/smoothing.
         */
        public static final int MLE = 800;

        /**
         * Indicates to use Add One estimator/smoothing.
         */
        public static final int ADD_ONE = 801;

        /**
         * Indicates to use Add Delta estimator/smoothing.
         */
        public static final int ADD_DELTA = 802;

        /**
         * Indicates to use Witten-Bell estimator/smoothing.
         */
        public static final int WITTEN_BELL = 803;

        /**
         * Indicates to use Good-Turing estimator/smoothing.
         */
        public static final int GOOD_TURING = 804;

        /**
         * Indicates to use SLI estimator/smoothing.
         */
        public static final int SLI = 805;

        /**
         * Indicates to use GLI estimator/smoothing.
         */
        public static final int GLI = 806;

        /**
         * Indicates to use Katz Backoff estimator/smoothing.
         */
        public static final int KATZ_BACKOFF = 807;

        /**
         * Indicates to use ELE estimator/smoothing.
         *
         * @since 0.3.0.5
         */
        public static final int ELE = 808;

        /**
         * Lower boundary for statistical estimators enumeration.
         * Used in error checks.
         * <p>
         * *Update it when add more methods.*
         *
         * @since 0.3.0.5
         */
        public static final int MIN_STATS_ESTIMATOR = MLE;

        /**
         * Upper boundary for statistical estimators enumeration.
         * Used in error checks.
         * <p>
         * *Update it when add more methods.*
         *
         * @since 0.3.0.5
         */
        public static final int MAX_STATS_ESTIMATOR = KATZ_BACKOFF;
    }

    /**
     * <p>Enumeration of N-gram Models.
     * In 0.3.0.5 renamed from <code>INgramModels</code> to
     * <code>ENgramModels</code>.
     * </p>
     *
     * @author Serguei Mokhov
     * @since 0.3.0.2
     */
    public interface ENgramModels {
        /**
         * Indicates to use unigram language model.
         */
        public static final int UNIGRAM = 900;

        /**
         * Indicates to use bigram language model.
         */
        public static final int BIGRAM = 901;

        /**
         * Indicates to use trigram language model.
         */
        public static final int TRIGRAM = 902;

        /**
         * Indicates to use n-gram language model.
         */
        public static final int NGRAM = 903;

        /**
         * Lower boundary for n-gram models enumeration.
         * Used in error checks.
         * <p>
         * *Update it when add more methods.*
         *
         * @since 0.3.0.5
         */
        public static final int MIN_NGRAM_MODEL = UNIGRAM;

        /**
         * Upper boundary for n-gram models enumeration.
         * Used in error checks.
         * <p>
         * *Update it when add more methods.*
         *
         * @since 0.3.0.5
         */
        public static final int MAX_NGRAM_MODEL = NGRAM;
    }

    /**
     * <p>Class NLP is more related to the Natural Language Processing
     * part of MARF.</p>
     *
     * @author Serguei Mokhov
     * @since 0.3.0.2
     */
    public static class NLP {
        /**
         * Indicates to use stemming module.
         */
        public static final int STEMMING = 1000;

        /**
         * Indicates to use case-sesitive processing of text.
         */
        public static final int CASE_SENSITIVE = 1001;

        /**
         * When parsing text, also parse numbers as tokens.
         */
        public static final int PARSE_NUMBERS = 1002;

        /**
         * When parsing text, also parse quoted literals.
         */
        public static final int PARSE_QUOTED_STRINGS = 1003;

        /**
         * When parsing text, also parse typical ends of sentences.
         */
        public static final int PARSE_ENDS_OF_SENTENCE = 1004;

        /**
         * Perform a raw dump of Zipf's Law data.
         */
        public static final int RAW_ZIPFS_LAW_DUMP = 1005;

        /**
         * Work in character n-gram mode.
         */
        public static final int CHARACTER_MODE = 1006;

        /**
         * Action to train a classifier.
         */
        public static final int TRAIN = 1007;

        /**
         * Action to perform a classification task.
         */
        public static final int CLASSIFY = 1008;

        /**
         * Use interactive mode.
         */
        public static final int INTERACTIVE = 1009;

        /**
         * When classifying, cheat with Zipf's Law implementation.
         */
        public static final int ZIPFS_LAW_CHEAT = 1010;

        /**
         * Similarly to <code>CHARACTER_MODE</code> work in word mode
         * for n-grams.
         *
         * @sicne 0.3.0.5
         * @see #CHARACTER_MODE
         */
        public static final int WORD_MODE = 1011;

        /**
         * Current smoothing method.
         * Default is MLE.
         *
         * @see EStatisticalEstimators#MLE
         */
        private static int siSmoothingMethod = EStatisticalEstimators.MLE;

        /**
         * Current N-gram model.
         * Default is BIGRAM.
         *
         * @see ENgramModels#BIGRAM
         */
        private static int siNgramModel = ENgramModels.BIGRAM;

        /**
         * Current natural language.
         * Default is "en".
         */
        private static String sstrLanguage = "en";

        /**
         * Retrieves current smoothing method.
         *
         * @return inner smoothing method
         */
        public static synchronized final int getSmoothingMethod() {
            return siSmoothingMethod;
        }

        /**
         * Sets current smoothing method.
         *
         * @param piSmoothingMethod new smoothing method to use
         * @throws NLPException if the parameter is outside the valid range
         * @see EStatisticalEstimators#MIN_STATS_ESTIMATOR
         * @see EStatisticalEstimators#MAX_STATS_ESTIMATOR
         */
        public static synchronized void setSmoothingMethod(final int piSmoothingMethod)
                throws NLPException {
            if
                    (
                    piSmoothingMethod < EStatisticalEstimators.MIN_STATS_ESTIMATOR
                            || piSmoothingMethod > EStatisticalEstimators.MAX_STATS_ESTIMATOR
                    ) {
                throw new NLPException
                        (
                                "Statistical smoothing estimartor (" + piSmoothingMethod +
                                        ") is out of range [" + EStatisticalEstimators.MIN_STATS_ESTIMATOR +
                                        "," + EStatisticalEstimators.MAX_STATS_ESTIMATOR + "]."
                        );
            }

            siSmoothingMethod = piSmoothingMethod;
        }

        /**
         * Retrieves current n-gram modeld.
         *
         * @return inner n-gram model
         */
        public static synchronized final int getNgramModel() {
            return siNgramModel;
        }

        /**
         * Sets current n-gram model.
         *
         * @param piNgramModel new n-gram model to use
         * @throws NLPException if the parameter is outside the valid range
         * @see ENgramModels#MIN_NGRAM_MODEL
         * @see ENgramModels#MAX_NGRAM_MODEL
         */
        public static synchronized final void setNgramModel(final int piNgramModel)
                throws NLPException {
            if (piNgramModel < ENgramModels.MIN_NGRAM_MODEL || piNgramModel > ENgramModels.MAX_NGRAM_MODEL) {
                throw new NLPException
                        (
                                "N-gram model (" + piNgramModel +
                                        ") is out of range [" + ENgramModels.MIN_NGRAM_MODEL + "," + ENgramModels.MAX_NGRAM_MODEL + "]."
                        );
            }

            siNgramModel = piNgramModel;
        }

        /**
         * Retrieves current language being processed.
         *
         * @return inner smoothing method
         */
        public static synchronized String getLanguage() {
            return sstrLanguage;
        }

        /**
         * Sets current processed language.
         *
         * @param pstrLanguages new language value
         * @throws NLPException if the parameter is null or empty
         */
        public static synchronized void setLanguage(String pstrLanguages)
                throws NLPException {
            if (pstrLanguages == null || pstrLanguages.length() == 0) {
                throw new NLPException("Null or empty language value specified.");
            }

            sstrLanguage = pstrLanguages;
        }
    } // clas NLP

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.98 $";
    }
}

// EOF
