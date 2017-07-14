package marf.Storage.Loaders;

import java.io.File;

import marf.Storage.MARFAudioFileFormat;
import marf.Storage.Sample;
import marf.Storage.SampleLoader;
import marf.Storage.StorageException;
import marf.util.InvalidSampleFormatException;


/**
 * <p>Loads (by computing) a simple sine wave into the sample.
 * Used for simple and quick testing of fake waves.</p>
 * <p>
 * $Id: SineLoader.java,v 1.12 2006/01/14 22:30:13 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.12 $
 * @since 0.3.0.2
 */
public class SineLoader
        extends SampleLoader {
    /**
     * Default sine frequency at 3 kHz, in Hz.
     */
    public static final double DEFAULT_SINE_FREQUENCY = 3000.0;

    /**
     * Default sine frequency deviation of 0.1 kHz, in Hz.
     */
    public static final double DEFAULT_SINE_DEVIATION = 100.0;

    /**
     * Constructs default Sine Loader.
     *
     * @throws InvalidSampleFormatException
     */
    public SineLoader()
            throws InvalidSampleFormatException {
        this.oSample = new Sample(MARFAudioFileFormat.SINE);
        this.oSample.setSampleSize(Sample.DEFAULT_SAMPLE_SIZE);
    }

    /**
     * Generates sine wave at 3 kHz +- 0.0 kHz with 8 kHz sampling rate.
     *
     * @param padSampleData to fill in with sample data
     * @return the length of padSampleData
     * @throws StorageException
     */
    public final int readAudioData(double[] padSampleData)
            throws StorageException {
        for (int i = 0; i < padSampleData.length; i++)
            padSampleData[i] =
                    (
                            Math.sin(2 * Math.PI * (DEFAULT_SINE_FREQUENCY / DEFAULT_FREQUENCY) * i) +
                                    Math.sin(2 * Math.PI * (DEFAULT_SINE_DEVIATION / DEFAULT_FREQUENCY) * i)
                    ) / 2.0;

        return padSampleData.length;
    }

    /**
     * Does nothing.
     *
     * @param padSample used to verify the length
     * @param piLength  used to verify the length
     * @return the smallest of padSample.length or piLength
     * @throws StorageException if piLength is less than zero
     */
    public final int writeAudioData(final double[] padSample, final int piLength)
            throws StorageException {
        int iSampleArrayLength = padSample == null ? 0 : padSample.length;

        if (piLength < 0)
            throw new StorageException("Parameter length of a sample should not be < 0.");

        return iSampleArrayLength < piLength ? iSampleArrayLength : piLength;
    }

    /**
     * Fills in the contained sample's data array with a sine wave.
     * Internally calls <code>readAudioData()</code>.
     *
     * @param poInFile unused, can be null
     * @return generated sample reference
     * @throws StorageException
     * @see #readAudioData(double[])
     */
    public Sample loadSample(File poInFile)
            throws StorageException {
        readAudioData(this.oSample.getSampleArray());
        return this.oSample;
    }

    /**
     * Does nothing.
     *
     * @param poOutFile unused, can be null
     * @throws StorageException, never thrown
     */
    public void saveSample(File poOutFile)
            throws StorageException {
    }

    /**
     * Merely calls <code>readAudioData()</code>.
     *
     * @param pstrFilename unused, can be null
     * @return Sample object reference
     * @throws StorageException if readAudioData fails
     * @see #readAudioData(double[])
     */
    public Sample loadSample(final String pstrFilename)
            throws StorageException {
        readAudioData(this.oSample.getSampleArray());
        return this.oSample;
    }

    /**
     * Does nothing.
     *
     * @param pstrFilename unused, can be null
     * @throws StorageException, never thrown
     */
    public void saveSample(final String pstrFilename)
            throws StorageException {
    }

    /**
     * Overridden to reset sample's array mark only.
     *
     * @throws StorageException, never thrown
     */
    public void updateSample()
            throws StorageException {
        this.oSample.resetArrayMark();
    }

    /**
     * Retrieves the length of the sample (# of audio data in the audio stream).
     *
     * @return sample size, long
     * @throws StorageException if there was an error getting sample size
     */
    public long getSampleSize()
            throws StorageException {
        return this.oSample.getSampleArray().length;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.12 $";
    }
}

// EOF
