package marf.Storage;

import java.io.Serializable;

import marf.util.Arrays;
import marf.util.InvalidSampleFormatException;


/**
 * <p>Audio sample data container.</p>
 * <p>
 * <p>$Id: Sample.java,v 1.42 2006/01/02 22:24:00 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @author Jimmy Nicolacopoulos
 * @version $Revision: 1.42 $
 * @since 0.0.1
 */
public class Sample
        implements Serializable, Cloneable {
    /*
     * -------------------
	 * Sample formats
	 * -------------------
	 */

    /**
     * Groupping of file format data.
     *
     * @since 0.3.0.2
     */
    private MARFAudioFileFormat oAudioFileFormat = new MARFAudioFileFormat();

    /**
     * Default sample array's size (1024).
     *
     * @since 0.3.0.2
     */
    public static final int DEFAULT_SAMPLE_SIZE = 1024;

    /**
     * Default sample chunk's size (128).
     *
     * @since 0.3.0.2
     */
    public static final int DEFAULT_CHUNK_SIZE = 128;

	/*
     * -------------------
	 * Data members
	 * -------------------
	 */

    /**
     * Sample data array (amplitudes).
     */
    protected double[] adSample = null;

    /**
     * Chunk pointer in the sample array.
     */
    protected transient int iArrayIndex = 0;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 505671131094530371L;


	/*
	 * -------------------
	 * Methods
	 * -------------------
	 */

    /**
     * Constructs default sample object.
     */
    public Sample() {
    }

    /**
     * Accepts pre-set sample; for testing.
     *
     * @param padData preset amplitude values
     */
    public Sample(double[] padData) {
        setSampleArray(padData);
    }

    /**
     * Constructor with format indication.
     *
     * @param piFormat format number for the enumeration
     * @throws InvalidSampleFormatException if the parameter format is invalid
     */
    public Sample(final int piFormat)
            throws InvalidSampleFormatException {
        setAudioFormat(piFormat);
    }

    /**
     * Constructor with format indication and the sampled data.
     *
     * @param piFormat format number for the enumeration
     * @param padData  preset amplitude values
     * @throws InvalidSampleFormatException if the parameter format is invalid
     * @since 0.3.0.5
     */
    public Sample(final int piFormat, double[] padData)
            throws InvalidSampleFormatException {
        setAudioFormat(piFormat);
        setSampleArray(padData);
    }

    /**
     * Copy-constructor. Prior getting parameter's data,
     * it's cloned. Only the data and format are copied.
     *
     * @param poSample sample objec to copy data off from
     * @throws InvalidSampleFormatException if the parameter format is invalid
     * @since 0.3.0.5
     */
    public Sample(final Sample poSample)
            throws InvalidSampleFormatException {
        Sample oCopy = (Sample) poSample.clone();
        setAudioFormat(oCopy.getAudioFormat());
        setSampleArray(oCopy.adSample);
    }

    /**
     * Retrieves current sample's format.
     *
     * @return an integer representing the format of the sample
     */
    public final int getAudioFormat() {
        return this.oAudioFileFormat.getAudioFormat();
    }

    /**
     * Sets current format of a sample.
     *
     * @param piFormat format number from the enumeration
     * @throws InvalidSampleFormatException if the parameter format is invalid
     */
    public final void setAudioFormat(final int piFormat)
            throws InvalidSampleFormatException {
        this.oAudioFileFormat.setAudioFormat(piFormat);
    }

    /**
     * Sets the internal sample array (adSample) with the specified argument.
     * Index gets reset as well.
     *
     * @param padSampleArray an array of doubles
     */
    public final void setSampleArray(double[] padSampleArray) {
        this.adSample = padSampleArray;
        this.iArrayIndex = 0;
    }

    /**
     * Retrieves array containing audio data of the entire sample.
     *
     * @return an array of doubles
     */
    public final double[] getSampleArray() {
        return this.adSample;
    }

    /**
     * Gets the next chunk of audio data and places it into padChunkArray.
     * Similar to readAudioData() method only it reads from the array instead of
     * the audio stream (file).
     *
     * @param padChunkArray an array of doubles
     * @return number of datums retrieved
     */
    public final int getNextChunk(double[] padChunkArray) {
        int iCount = 0;
        long lSampleSize = getSampleSize();

        while (iCount < padChunkArray.length && this.iArrayIndex < lSampleSize) {
            padChunkArray[iCount] = this.adSample[this.iArrayIndex];
            iCount++;
            this.iArrayIndex++;
        }

        return iCount;
    }

    /**
     * Resets the marker used for reading audio data from sample array.
     */
    public final void resetArrayMark() {
        this.iArrayIndex = 0;
    }

    /**
     * Returns the length of the sample.
     *
     * @return long Array length
     */
    public final long getSampleSize() {
        return this.adSample == null ? 0 : this.adSample.length;
    }

    /**
     * Sets internal size of the sample array.
     * If array did not exist, it its created,
     * else cut or enlarged to the desired size.
     * The previous content is retained unless
     * the desired size is less than the current.
     *
     * @param piDesiredSize new desired size of the array
     * @throws IllegalArgumentException if the parameter is <= 0
     * @since 0.3.0.2
     */
    public void setSampleSize(int piDesiredSize) {
        if (piDesiredSize <= 0) {
            throw new IllegalArgumentException("Sample array size should not be 0 or less.");
        }

        if (this.adSample == null) {
            this.adSample = new double[piDesiredSize];
        } else {
            if (this.adSample.length == piDesiredSize) {
                return;
            }

            double[] adCopy = (double[]) this.adSample.clone();
            this.adSample = new double[piDesiredSize];

            if (this.adSample.length > piDesiredSize) {
                // A portion of data is lost
                Arrays.copy(this.adSample, adCopy, piDesiredSize);
            } else {
                // Trailing data is junk
                Arrays.copy(this.adSample, adCopy, adCopy.length);
            }
        }
    }

	/* Utility Methods */

    /**
     * Clones this sample object by copying the
     * sample format and its data.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        try {
            Sample oCopy = (Sample) super.clone();
            oCopy.setAudioFormat(getAudioFormat());
            oCopy.setSampleArray((double[]) this.adSample.clone());
            return oCopy;
        } catch (InvalidSampleFormatException e) {
            throw new RuntimeException(e.getMessage());
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }

    /**
     * Returns textual representation of the sample object.
     * The contents is the format and lengthon the first line, and then
     * a column of data.
     *
     * @return the sample data string
     * @see java.lang.Object#toString()
     * @since 0.3.0.3
     */
    public String toString() {
        StringBuffer oData = new StringBuffer();

        oData.append(this.oAudioFileFormat).append(", sample data length: ");
        oData.append(this.adSample.length).append("\n");
        oData.append(Arrays.arrayToDelimitedString(this.adSample, "\n"));

        return oData.toString();
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.42 $";
    }
}

// EOF
