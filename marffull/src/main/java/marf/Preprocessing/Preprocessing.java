package marf.Preprocessing;

import marf.MARF;
import marf.Storage.Sample;
import marf.Storage.StorageManager;
import marf.util.Arrays;
import marf.util.Debug;
import marf.util.NotImplementedException;


/**
 * <p>Abstract Preprocessing Module.</p>
 * <p>
 * <p>$Id: Preprocessing.java,v 1.42 2006/01/02 22:24:00 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.42 $
 * @since 0.0.1
 */
public abstract class Preprocessing
        extends StorageManager
        implements IPreprocessing {
    /**
     * Sample reference.
     */
    protected Sample oSample = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = 1311696194896319668L;

    /**
     * Default constructor for reflective creation of Preprocessing
     * clones. Typically should not be used unless really necessary
     * for the frameworked modules.
     *
     * @since 0.3.0.5
     */
    protected Preprocessing() {
    }

    /**
     * Main Preprocessing constructor that performs normalization
     * as a part of construction process.
     *
     * @param poSample loaded sample by a concrete implementation of SampleLoader
     * @throws PreprocessingException if <code>normalize()</code> fails
     * @see #normalize()
     */
    protected Preprocessing(Sample poSample)
            throws PreprocessingException {
        super
                (
                        poSample,
                        poSample.getClass().getName() + "." +
                                poSample.getAudioFormat() + "." +
                                MARF.getPreprocessingMethod() + ".sample"
                );

        this.oSample = poSample;

        //Debug.debug(Preprocessing.class, "constructed");
    }

    /**
     * Allows <i>chaining</i> of preprocessing modules.
     * Makes it possible to apply several preprocessing modules on the
     * same incoming sample, in the form of
     * <code>new FooBarBaz(new HighFrequencyBoost(new HighPassFilter(poSample)))</code>.
     * <p>
     * Notice, it calls <code>preprocess()</code> for all inner preprocessing,
     * but not the outer. The outer is supposed to be called by either <code>MARF</code>
     * or an application as a part of defined API.
     *
     * @param poPreprocessing follow up preprocessing module
     * @throws PreprocessingException if underlying <code>preprocess()</code> fails or
     *                                the parameter is null.
     * @see #preprocess()
     * @see MARF
     * @see marf.Preprocessing.FFTFilter.HighFrequencyBoost
     * @see marf.Preprocessing.FFTFilter.HighPassFilter
     * @since 0.3.0.3
     */
    protected Preprocessing(IPreprocessing poPreprocessing)
            throws PreprocessingException {
        if (poPreprocessing == null) {
            throw new IllegalArgumentException("Preprocessing parameter cannot be null.");
        }

        boolean bChanged = poPreprocessing.preprocess();

        if (bChanged == false) {
            Debug.debug
                    (
                            "WARNING: " +
                                    poPreprocessing.getClass().getName() +
                                    ".preprocess() returned false."
                    );
        }

        this.oObjectToSerialize = this.oSample = poPreprocessing.getSample();
    }

    /**
     * Derivatives should implement this method to remove noise from the sample.
     *
     * @return boolean that sample has changed (noise removed)
     * @throws NotImplementedException
     * @throws PreprocessingException  declared but never thrown
     */
    public boolean removeNoise()
            throws PreprocessingException {
        throw new NotImplementedException(this, "removeNoise()");
    }

    /**
     * Derivatives should implement this method to remove silence.
     *
     * @return boolean that sample has changed (silence removed)
     * @throws NotImplementedException
     * @throws PreprocessingException  declared but never thrown
     */
    public boolean removeSilence()
            throws PreprocessingException {
        throw new NotImplementedException(this, "removeSilence()");
    }

    /**
     * Normalization of entire incoming samples by amplitude.
     * Equivalent to <code>normalize(0)</code>.
     *
     * @return <code>true</code> if the sample has been successfully normalized;
     * <code>false</code> otherwise
     * @throws PreprocessingException if internal sample reference is null
     * @see #normalize(int)
     */
    public final boolean normalize()
            throws PreprocessingException {
        return normalize(0);
    }

    /**
     * Normalization of incoming samples by amplitude starting from certain index.
     * Useful in case where only the last portion of a sample needs to be normalized, like
     * in <code>HighFrequencyBoost</code>.
     * <p>
     * Equivalent to <code>normalize(piIndexFrom, sample array length - 1)</code>.
     *
     * @param piIndexFrom sample array index to start normalization from
     * @return <code>true</code> if the sample has been successfully normalized;
     * <code>false</code> otherwise
     * @throws PreprocessingException if internal sample reference is null
     * @see #normalize(int, int)
     * @see marf.Preprocessing.FFTFilter.HighFrequencyBoost
     * @since 0.3.0
     */
    public final boolean normalize(int piIndexFrom)
            throws PreprocessingException {
        if (this.oSample == null) {
            throw new PreprocessingException
                    (
                            "Preprocessing.normalize(from) - sample is not available (null)"
                    );
        }

        return normalize(piIndexFrom, this.oSample.getSampleArray().length - 1);
    }

    /**
     * Normalization of incoming samples by amplitude between specified indexes.
     * Useful in case where only a portion of a sample needs to be normalized.
     *
     * @param piIndexFrom sample array index to start normalization from
     * @param piIndexTo   sample array index to end normalization at
     * @return <code>true</code> if the sample has been successfully normalized;
     * <code>false</code> otherwise
     * @throws PreprocessingException if internal sample reference is null or one or
     *                                both indexes are out of range
     * @since 0.3.0
     */
    public final boolean normalize(int piIndexFrom, int piIndexTo)
            throws PreprocessingException {
        try {
            if (this.oSample == null) {
                throw new PreprocessingException
                        (
                                "Preprocessing.normalize(from, to) - sample is not available (null)"
                        );
            }

            Debug.debug
                    (
                            "Preprocessing.normalize(" + piIndexFrom + "," +
                                    piIndexTo + ") has begun..."
                    );

            double dMax = Double.MIN_VALUE;

            double[] adAmplitude = this.oSample.getSampleArray();

            // Find max absolute amplitude
            for (int i = piIndexFrom; i < piIndexTo; i++) {
                if (Math.abs(adAmplitude[i]) > dMax) {
                    dMax = Math.abs(adAmplitude[i]);
                }
            }

            // Prevent devision by zero
            if (dMax == 0.0) {
                Debug.debug("NOTICE: Preprocessing.normalize() - dMax = " + dMax);
                return false;
            }

            // Actual normalization
            for (int i = piIndexFrom; i < piIndexTo; i++) {
                adAmplitude[i] /= dMax;
            }

            Debug.debug
                    (
                            "Preprocessing.normalize(" + piIndexFrom + "," + piIndexTo +
                                    ") has normally finished..."
                    );

            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PreprocessingException
                    (
                            "Normalization period [" + piIndexFrom + "," + piIndexTo + "] is " +
                                    "out of range [0," + this.oSample.getSampleArray().length + "]."
                    );
        }
    }

    /**
     * Compresses input data by removing duplicate values.
     *
     * @param padDataVector source data vector to apply compression to
     * @return a newly allocated compressed array of doubles
     * @since 0.3.0.5
     */
    public static double[] compress(final double[] padDataVector) {
        int iLastIndex = -1;

        double[] adTempData = new double[padDataVector.length];
        adTempData[++iLastIndex] = padDataVector[0];

        for (int i = 1; i < padDataVector.length; i++) {
            if (padDataVector[i] != padDataVector[i - 1]) {
                adTempData[++iLastIndex] = padDataVector[i];
            }
        }

        double[] adCompressed = new double[iLastIndex + 1];
        Arrays.copy(adCompressed, 0, adTempData, 0, adCompressed.length);

        return adCompressed;
    }

    /**
     * Compresses the instance of the stored sample by removing duplicates.
     *
     * @return <code>true</code>, if the compression was
     * successful.
     * @see #compress(double[])
     * @since 0.3.0.5
     */
    public synchronized boolean compress() {
        this.oSample.setSampleArray(compress(this.oSample.getSampleArray()));
        return true;
    }

    /**
     * Derivatives implement this method to crop arbitrary
     * part of the audio sample.
     *
     * @param pdStartingFrequency double Frequency to start to crop from
     * @param pdEndFrequency      double Frequency to crop the sample to
     * @return boolean <code>true</code> - cropped, <code>false</code> - not
     * @throws NotImplementedException
     * @throws PreprocessingException  declared, but is never thrown
     */
    public boolean cropAudio(double pdStartingFrequency, double pdEndFrequency)
            throws PreprocessingException {
        throw new NotImplementedException(this, "cropAudio()");
    }

    /**
     * Returns enclosed sample.
     *
     * @return Sample object
     */
    public final Sample getSample() {
        return this.oSample;
    }

    /**
     * Allows setting a sample object reference.
     *
     * @param poSample new sample object
     * @since 0.3.0.4
     */
    public void setSample(Sample poSample) {
        this.oSample = poSample;
    }

	/* StorageManager Interface */

    /**
     * Implementaion of back-synchronization of Sample loaded object.
     *
     * @since 0.3.0.2
     */
    public void backSynchronizeObject() {
        this.oSample = (Sample) this.oObjectToSerialize;
    }

	/* Utility Methods */

    /**
     * Implementes Cloneable interface for the Preprocessing object.
     * Performs "deep" copy, including the contained sample.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        Preprocessing oCopy = (Preprocessing) super.clone();

        oCopy.oSample =
                this.oSample == null ?
                        null :
                        (Sample) this.oSample.clone();

        return oCopy;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.42 $";
    }
}

// EOF
