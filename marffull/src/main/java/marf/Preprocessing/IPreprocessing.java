package marf.Preprocessing;

import marf.Storage.Sample;

/**
 * <p>Preprocessing Interface.</p>
 * <p>
 * <p>$Id: IPreprocessing.java,v 1.6 2005/12/27 23:55:30 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.6 $
 * @since 0.3.0
 */
public interface IPreprocessing {
    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.6 $";

    /**
     * Derivatives must implement this method to do general
     * preprocessing and perhaps calling <code>removeNoise()</code>
     * and <code>removeSilence()</code>.
     *
     * @return boolean that sample has changed as a result of preprocessing
     * @throws PreprocessingException if an error occurred while preprocessing
     * @see #removeNoise()
     * @see #removeSilence()
     */
    boolean preprocess()
            throws PreprocessingException;

    /**
     * Derivatives should implement this method to remove noise from the sample.
     *
     * @return boolean that sample has changed (noise removed)
     * @throws PreprocessingException declared but never thrown
     */
    boolean removeNoise()
            throws PreprocessingException;

    /**
     * Derivatives should implement this method to remove silence.
     *
     * @return boolean that sample has changed (silence removed)
     * @throws PreprocessingException declared but never thrown
     */
    boolean removeSilence()
            throws PreprocessingException;

    /**
     * Normalization of entire incoming samples by amplitude.
     *
     * @return <code>true</code> if the sample has been successfully normalized;
     * <code>false</code> otherwise
     * @throws PreprocessingException if internal sample reference is null
     */
    boolean normalize()
            throws PreprocessingException;

    /**
     * Normalization of incoming samples by amplitude starting from certain index.
     * Useful in case where only the last portion of a sample needs to be normalized.
     *
     * @param piIndexFrom sample array index to start normalization from
     * @return <code>true</code> if the sample has been successfully normalized;
     * <code>false</code> otherwise
     * @throws PreprocessingException if internal sample reference is null
     * @see #normalize(int, int)
     */
    boolean normalize(int piIndexFrom)
            throws PreprocessingException;

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
     */
    boolean normalize(int piIndexFrom, int piIndexTo)
            throws PreprocessingException;

    /**
     * Derivatives implement this method to crop arbitrary
     * part of the audio sample.
     *
     * @param pdStartingFrequency double Frequency to start to crop from
     * @param pdEndFrequency      double Frequency to crop the sample to
     * @return boolean <code>true</code> - cropped, <code>false</code> - not
     * @throws PreprocessingException declared, but is never thrown
     */
    boolean cropAudio(double pdStartingFrequency, double pdEndFrequency)
            throws PreprocessingException;

    /**
     * Returns enclosed sample.
     *
     * @return Sample object
     */
    Sample getSample();

    /**
     * Allows setting a sample object reference.
     *
     * @param poSample new sample object
     * @since 0.3.0.4
     */
    void setSample(Sample poSample);

    /**
     * Enable cloning of preprocessing modules in accordance
     * with the Cloneable interface.
     *
     * @return a copy of the object implementing this interface
     * @throws CloneNotSupportedException if for some reason cloning is unsupported.
     * @since 0.3.0.5
     */
    Object clone()
            throws CloneNotSupportedException;
}

// EOF
