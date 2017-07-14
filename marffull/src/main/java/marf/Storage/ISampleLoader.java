package marf.Storage;

import java.io.File;


/**
 * <p>Samle loading interface.
 * Must be overriden by a concrete sample loader. Derivatives
 * should try their best to inherit from SampleLoader class; otherwise
 * they must implement this.</p>
 * <p>
 * <p>$Id: ISampleLoader.java,v 1.9 2005/06/12 23:09:37 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.9 $
 * @since 0.3.0
 */
public interface ISampleLoader {
    /*
     * ----------------
	 * Constants
	 * ----------------
	 */

    /**
     * Default 16.
     */
    int DEFAULT_SAMPLE_BIT_SIZE = 16;

    /**
     * Default 1.
     */
    int DEFAULT_CHANNELS = 1;

    /**
     * Default sampling frequency of 8000 Hz.
     */
    float DEFAULT_FREQUENCY = 8000;

    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.9 $";

    /**
     * Reads audio data from the sample's audio stream into padAudioData.
     *
     * @param padAudioData an array of doubles
     * @return integer the number of datums read
     * @throws StorageException if there was an error reading audio data
     */
    int readAudioData(double[] padAudioData)
            throws StorageException;

    /**
     * Writes audio data into the sample's audio stream.
     *
     * @param padAudioData an array of doubles
     * @param piWords      the number of audio data to written from the paiAudiodata
     * @return the number of data written
     * @throws StorageException if there was an error loading the sample
     */
    int writeAudioData(double[] padAudioData, int piWords)
            throws StorageException;

    /**
     * Prime SampleLoader interface.
     * Must be overriden by a concrete loader that knows how to load a specific sample format.
     *
     * @param poInFile file object a sample to be read from
     * @return Sample object refernce
     * @throws StorageException
     */
    Sample loadSample(File poInFile)
            throws StorageException;

    /**
     * Same as loadSample(File) but takes filename as an argument.
     *
     * @param pstrFilename filename of a sample to be read from
     * @return Sample object reference
     * @throws StorageException if there was an error loading the sample
     */
    Sample loadSample(final String pstrFilename)
            throws StorageException;

    /**
     * Prime SampleLoader interface.
     * Must be overriden by a concrete loader that knows how to save a specific sample.
     *
     * @param poOutFile File object of a sample to be saved to
     * @throws StorageException if there was an error saving the sample
     */
    void saveSample(File poOutFile)
            throws StorageException;

    /**
     * Same as saveSample(File) but takes filename as an argument.
     *
     * @param pstrFilename filename of a sample to be saved to
     * @throws StorageException if there was an error saving the sample
     */
    void saveSample(final String pstrFilename)
            throws StorageException;

    /**
     * <p><code>updateSample()</code> is just used whenever the
     * <code>AudioInputStream</code> is assigned to a new value (wave file).
     * Then you would simply call this method to update the
     * <code>Sample</code> member with the contents of the new <code>AudioInputStream</code>.</p>
     *
     * @throws StorageException if there was an error updating the sample data array
     */
    void updateSample()
            throws StorageException;

    /**
     * Resets the marker for the audio stream. Used after writing audio data
     * into the sample's audio stream.
     *
     * @throws StorageException if there was an error resetting the audio stream
     */
    void reset()
            throws StorageException;

    /**
     * Retrieves the length of the sample (# of audio data in the audio stream).
     *
     * @return sample size, long
     * @throws StorageException if there was an error getting sample size
     */
    long getSampleSize()
            throws StorageException;

    /**
     * Returns internal reference to a Sample object.
     *
     * @return Sample reference
     */
    Sample getSample();

    /**
     * Sets internal sample reference from outside.
     *
     * @param poSample Sample object
     */
    void setSample(Sample poSample);
}

// EOF
