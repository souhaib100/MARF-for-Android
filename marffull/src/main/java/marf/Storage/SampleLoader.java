package marf.Storage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import lib.sound.sampled.AudioFormat;
import lib.sound.sampled.AudioInputStream;


/**
 * <p>Absract class that provides samle loading interface.
 * Must be overriden by a concrete sample loader.</p>
 * <p>
 * <p>$Id: SampleLoader.java,v 1.22 2005/06/16 19:58:54 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @author Jimmy Nicolacopoulos
 * @version $Revision: 1.22 $
 * @since 0.0.1
 */
public abstract class SampleLoader
        implements ISampleLoader {
    /*
     * ----------------
	 * Data Members
	 * ----------------
	 */

    /**
     * Current bit size of a sample.
     */
    protected int iRequiredBitSize = DEFAULT_SAMPLE_BIT_SIZE;

    /**
     * Current number of channels.
     */
    protected int iRequiredChannels = DEFAULT_CHANNELS;

    /**
     * Sample references of the sample to be loaded.
     */
    protected Sample oSample = null;

    /**
     * Properties of a sound sample.
     */
    protected AudioFormat
            oAudioFormat = null;

    /**
     * Stream representing sound sample.
     */
    protected AudioInputStream oAudioInputStream = null;

    /**
     * Output stream used for writing audio data.
     */
    protected ByteArrayOutputStream oByteArrayOutputStream = null;

    /**
     * Current frequency.
     *
     * @since 0.3.0
     */
    protected float iRequiredFrequency = DEFAULT_FREQUENCY;

    /**
     * Default constructor.
     * Instantiates <code>ByteArrayOutputStream</code>.
     */
    public SampleLoader() {
        this.oByteArrayOutputStream = new ByteArrayOutputStream();
    }

    /**
     * Same as loadSample(File) but takes filename as an argument.
     *
     * @param pstrFilename filename of a sample to be read from
     * @return Sample object reference
     * @throws StorageException if there was an error loading the sample
     */
    public Sample loadSample(final String pstrFilename)
            throws StorageException {
        return loadSample(new File(pstrFilename));
    }

    /**
     * Same as saveSample(File) but takes filename as an argument.
     *
     * @param pstrFilename filename of a sample to be saved to
     * @throws StorageException if there was an error saving the sample
     */
    public void saveSample(final String pstrFilename)
            throws StorageException {
        saveSample(new File(pstrFilename));
    }

    /**
     * <p><code>updateSample()</code> is just used whenever the
     * <code>AudioInputStream</code> is assigned to a new value (wave file).
     * Then you would simply call this method to update the
     * <code>Sample</code> member with the contents of the new <code>AudioInputStream</code>.</p>
     *
     * @throws StorageException if there was an error updating the sample data array
     */
    public void updateSample()
            throws StorageException {
        double[] adSampleArray = new double[(int) getSampleSize()];
        readAudioData(adSampleArray);
        this.oSample.setSampleArray(adSampleArray);
    }

    /**
     * Resets the marker for the audio stream. Used after writing audio data
     * into the sample's audio stream.
     *
     * @throws StorageException if there was an error resetting the audio stream
     */
    public void reset()
            throws StorageException {
        try {
            this.oAudioInputStream.reset();
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Retrieves the length of the sample (# of audio data in the audio stream).
     *
     * @return sample size, long
     * @throws StorageException if there was an error getting sample size
     */
    public long getSampleSize()
            throws StorageException {
        return this.oAudioInputStream.getFrameLength();
    }

    /**
     * Returns internal reference to a Sample object.
     *
     * @return Sample reference
     */
    public final Sample getSample() {
        return this.oSample;
    }

    /**
     * Sets internal sample reference from outside.
     *
     * @param poSample Sample object
     */
    public final void setSample(Sample poSample) {
        this.oSample = poSample;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.22 $";
    }
}

// EOF
