package marf.Storage.Loaders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import lib.sound.sampled.AudioFileFormat;
import lib.sound.sampled.AudioFormat;
import lib.sound.sampled.AudioInputStream;
import lib.sound.sampled.AudioSystem;
import lib.sound.sampled.UnsupportedAudioFileException;
import marf.Storage.MARFAudioFileFormat;
import marf.Storage.Sample;
import marf.Storage.SampleLoader;
import marf.Storage.StorageException;
import marf.util.ByteUtils;
import marf.util.InvalidSampleFormatException;


/**
 * <p>Loads/stores samples if WAVE format.</p>
 * <p>
 * <p>$Id: WAVLoader.java,v 1.23 2006/01/14 22:30:13 mokhov Exp $</p>
 *
 * @author Jimmy Nicolacopoulos
 * @author Serguei Mokhov
 * @version $Revision: 1.23 $
 * @since 0.0.1
 */
public class WAVLoader
        extends SampleLoader {
    /*
     * ----------------
	 * Methods
	 * ----------------
	 */

    /**
     * WAVLoader Constructor.
     *
     * @throws InvalidSampleFormatException if the WAV file isn't really in WAV format
     *                                      or any other error took place.
     */
    public WAVLoader()
            throws InvalidSampleFormatException {
        this.oSample = new Sample(MARFAudioFileFormat.WAV);
        AudioFormat.Encoding oEncoding = AudioFormat.Encoding.PCM_SIGNED;

        float fRate = DEFAULT_FREQUENCY;
        int iBitSampleSize = DEFAULT_SAMPLE_BIT_SIZE;
        int iChannels = DEFAULT_CHANNELS;

        this.oAudioFormat = new AudioFormat
                (
                        oEncoding,
                        fRate,
                        iBitSampleSize,
                        iChannels,
                        (iBitSampleSize / 8) * iChannels,
                        fRate,
                        false
                );
    }

    /**
     * Loads WAV sample data from a file.
     *
     * @param poInFile incoming sample File object
     * @return Sample object
     * @throws StorageException if there was a problem loading the sample
     */
    public Sample loadSample(File poInFile)
            throws StorageException {
        try {
            AudioInputStream oNewInputStream;

            // The parameter should not be null and should be a regular file.
            if (poInFile != null && poInFile.isFile()) {
                // Check the file format of the file

                //Commented for android
//				AudioFileFormat oFileFormat = AudioSystem.getAudioFileFormat(poInFile);
//
//				if(oFileFormat.getType().equals(AudioFileFormat.Type.WAVE) == false)
//				{
//					throw new InvalidSampleFormatException("Audio file type is not WAVE");
//				}

                // Get input stream from the file
                oNewInputStream = AudioSystem.getAudioInputStream(poInFile);

                // Check internal audio format characteristics we require
                validateAudioFormat(oNewInputStream.getFormat());
            } else {
                throw new FileNotFoundException("Filename is either null or is not a regular file.");
            }

            // Set the stream and fill out the sample's buffer with data
            this.oAudioInputStream = oNewInputStream;
            updateSample();

            return this.oSample;
        }

        // To avoid re-wrapping into StorageException again.
        catch (StorageException e) {
            throw e;
        }

        // Wrap all the other exceptions here.
        catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Buffers out the contents of an audio buffer into the parameter.
     *
     * @param padAudioData data array to fill in
     * @return the number of words of data read (a word is two bytes)
     * @throws StorageException if there was a problem reading the audio data
     */
    public final int readAudioData(double[] padAudioData)
            throws StorageException {
        try {
            byte[] atAudioBuffer = new byte[padAudioData.length * 2];
            int iNbrBytes = this.oAudioInputStream.read(atAudioBuffer);
            int iWordCount = (iNbrBytes / 2) + (iNbrBytes % 2);

            for (int i = 0; i < iWordCount; i++) {
                padAudioData[i] = (double) ByteUtils.byteArrayToShort
                        (
                                atAudioBuffer,
                                2 * i,
                                this.oAudioFormat.isBigEndian()
                        ) / 32768;
            }

            return iWordCount;
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    /**
     * Buffers the contents of padAudioData into atAudioBuffer.
     *
     * @param padAudioData array of data to be written
     * @param piNbrWords   number of words to be written
     * @return the number of data written
     * @throws StorageException if there was an error writing audio data
     */
    public final int writeAudioData(final double[] padAudioData, final int piNbrWords)
            throws StorageException {
        int iWord = 0;

        byte[] atAudioBytes;
        byte[] atAudioBuffer = new byte[piNbrWords * 2];

        for (int i = 0; i < piNbrWords; i++) {
            iWord = (int) (padAudioData[i] * 32768);
            atAudioBuffer[2 * i] = (byte) (iWord & 255);
            atAudioBuffer[2 * i + 1] = (byte) (iWord >>> 8);
        }

        this.oByteArrayOutputStream.write(atAudioBuffer, 0, atAudioBuffer.length);
        atAudioBytes = oByteArrayOutputStream.toByteArray();

        ByteArrayInputStream oBais = new ByteArrayInputStream(atAudioBytes);

        this.oAudioInputStream = new AudioInputStream
                (
                        oBais,
                        this.oAudioFormat,
                        atAudioBytes.length / this.oAudioFormat.getFrameSize()
                );

        return atAudioBuffer.length;
    }

    /**
     * Saves the wave into a file for playback.
     *
     * @param poOutFile File object for output
     * @throws StorageException if there was an error saving sample
     */
    public final void saveSample(File poOutFile)
            throws StorageException {
        try {
            AudioSystem.write(this.oAudioInputStream, AudioFileFormat.Type.WAVE, poOutFile);
            reset();
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    /**
     * Resets the marker for the audio and byte-array streams.
     * Used after writing audio data into the sample's audio stream.
     *
     * @throws StorageException if there was an error resetting the streams
     * @since 0.3.0
     */
    public void reset()
            throws StorageException {
        super.reset();
        this.oByteArrayOutputStream.reset();
    }

    /**
     * Validates audio file stream format for WAVE files.
     * Checks the format has the required bit size, number
     * of channels, and required sampling frequency.
     *
     * @param poFormat the audio format to validate
     * @throws UnsupportedAudioFileException if any of the three criteria are not met
     * @since 0.3.0.5
     */
    public void validateAudioFormat(final AudioFormat poFormat)
            throws UnsupportedAudioFileException {
        if (poFormat.getSampleSizeInBits() != this.iRequiredBitSize) {
            throw new UnsupportedAudioFileException
                    (
                            "Wave file not " + this.iRequiredBitSize + "-bit"
                    );
        }

        if (poFormat.getChannels() != this.iRequiredChannels) {
            throw new UnsupportedAudioFileException("Wave file is not mono.");
        }

        if (poFormat.getFrameRate() != this.iRequiredFrequency) {
            throw new UnsupportedAudioFileException
                    (
                            "Wave file is not " + this.iRequiredFrequency + " Hz"
                    );
        }
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.23 $";
    }
}

// EOF
