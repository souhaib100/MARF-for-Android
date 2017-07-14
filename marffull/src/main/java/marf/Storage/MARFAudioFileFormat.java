package marf.Storage;

import lib.sound.sampled.AudioFileFormat;
import lib.sound.sampled.AudioFormat;
import marf.util.InvalidSampleFormatException;


/**
 * <p>Supported MARF Audio File Formats.</p>
 * <p>
 * NOTE: this code is highly experimental.<br />
 * <p>
 * <p>$Id: MARFAudioFileFormat.java,v 1.9 2005/12/28 03:21:12 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.9 $
 * @since 0.3.0.2
 */
public class MARFAudioFileFormat
        extends AudioFileFormat {
    /*
     * --------------------------------------------------------
	 * Supported Sample File Formats Enumeration
	 * --------------------------------------------------------
	 */

    /**
     * Unknown sample format.
     */
    public static final int UNK = -1;

    /**
     * Indicates WAVE incoming sample file format.
     */
    public static final int WAV = 700;

    /**
     * Indicates ULAW incoming sample file format.
     */
    public static final int ULAW = 701;

    /**
     * Indicates MP3 incoming sample file format.
     */
    public static final int MP3 = 702;

    /**
     * Sine sample format.
     *
     * @since 0.3.0
     */
    public static final int SINE = 703;

    /**
     * AIFF sample format.
     *
     * @since 0.3.0
     */
    public static final int AIFF = 704;

    /**
     * AIFF-C sample format.
     *
     * @since 0.3.0
     */
    public static final int AIFFC = 705;

    /**
     * AU sample format.
     *
     * @since 0.3.0
     */
    public static final int AU = 706;

    /**
     * SND sample format.
     *
     * @since 0.3.0
     */
    public static final int SND = 707;

    /**
     * MIDI sample format.
     *
     * @since 0.3.0
     */
    public static final int MIDI = 708;

    /**
     * Custom (plugin) sample format.
     *
     * @since 0.3.0.5
     */
    public static final int CUSTOM = 709;

    /**
     * Lowest possible sample format value.
     * For boundaries check.
     */
    private static final int LOWEST_FORMAT = UNK;

    /**
     * Highest possible sample format value.
     * For boundaries check.
     */
    private static final int HIGHEST_FORMAT = CUSTOM;

    /**
     * Default sample array's size (1024).
     *
     * @since 0.3.0
     */
    public static final int DEFAULT_SAMPLE_SIZE = 1024;

    /**
     * Default sample chunk's size (128).
     *
     * @since 0.3.0
     */
    public static final int DEFAULT_CHUNK_SIZE = 128;


	/*
     * -------------------
	 * Data members
	 * -------------------
	 */

    /**
     * Current sample's format.
     */
    protected int iFormat = UNK;

    /**
     * File type.
     * Has to be added as the parent's is private.
     */
    protected AudioFileFormat.Type oType = null;


    /**
     * Default constructor creates a WAVE-type format by default,
     * which is PCM-signed, 16 bits, mono, little-endian.
     */
    public MARFAudioFileFormat() {
        this
                (
                        MARFAudioFileFormat.Type.WAVE,
                        new AudioFormat
                                (
                                        AudioFormat.Encoding.PCM_SIGNED,
                                        ISampleLoader.DEFAULT_FREQUENCY,
                                        ISampleLoader.DEFAULT_SAMPLE_BIT_SIZE,
                                        ISampleLoader.DEFAULT_CHANNELS,
                                        (ISampleLoader.DEFAULT_SAMPLE_BIT_SIZE / 8) * ISampleLoader.DEFAULT_CHANNELS,
                                        ISampleLoader.DEFAULT_FREQUENCY,
                                        false
                                ),
                        ISampleLoader.DEFAULT_SAMPLE_BIT_SIZE
                );

        this.iFormat = WAV;
        this.oType = MARFAudioFileFormat.Type.WAVE;
    }

    /**
     * Mimics parent's constructor.
     *
     * @param poType        file format
     * @param poFormat      audio format
     * @param piFrameLength frame length
     */
    public MARFAudioFileFormat(AudioFileFormat.Type poType, AudioFormat poFormat, int piFrameLength) {
        super(poType, poFormat, piFrameLength);
    }

    /**
     * MARF-related constructor.
     *
     * @param poType        file format
     * @param poFormat      audio format
     * @param piFrameLength frame length
     */
    public MARFAudioFileFormat(MARFAudioFileFormat.Type poType, AudioFormat poFormat, int piFrameLength) {
        super(poType, poFormat, piFrameLength);
    }

    /**
     * Retrieves current sample's format.
     *
     * @return an integer representing the format of the sample
     */
    public final int getAudioFormat() {
        return this.iFormat;
    }

    /**
     * Sets current format of a sample.
     *
     * @param piFormat format number from the enumeration
     * @throws InvalidSampleFormatException if piFormat is out of range
     */
    public final void setAudioFormat(final int piFormat)
            throws InvalidSampleFormatException {
        if (piFormat < LOWEST_FORMAT || piFormat > HIGHEST_FORMAT) {
            throw new InvalidSampleFormatException("Not a valid audio format: " + piFormat);
        }

        this.iFormat = piFormat;

        switch (this.iFormat) {
            case WAV:
                this.oType = MARFAudioFileFormat.Type.WAVE;
                return;

            case ULAW:
                this.oType = MARFAudioFileFormat.Type.ULAW;
                return;

            case MP3:
                this.oType = MARFAudioFileFormat.Type.MP3;
                return;

            case SINE:
                this.oType = MARFAudioFileFormat.Type.SINE;
                return;

            case AIFF:
                this.oType = MARFAudioFileFormat.Type.AIFF;
                return;

            case AIFFC:
                this.oType = MARFAudioFileFormat.Type.AIFC;
                return;

            case AU:
                this.oType = MARFAudioFileFormat.Type.AU;
                return;

            case SND:
                this.oType = MARFAudioFileFormat.Type.SND;
                return;

            case MIDI:
                this.oType = MARFAudioFileFormat.Type.MIDI;
                return;

            case CUSTOM:
                this.oType = MARFAudioFileFormat.Type.CUSTOM;
                return;
        }
    }

    /**
     * Constructs an audio file format object.
     * Mimic's parent's protected constructor.
     *
     * @param poType        type of the audio file
     * @param piByteLength  length of the file in bytes, or <code>AudioSystem.NOT_SPECIFIED</code>
     * @param poFormat      format of the audio data in the file
     * @param piFrameLength the audio data length in sample frames, or <code>AudioSystem.NOT_SPECIFIED</code>
     */
    protected MARFAudioFileFormat(Type poType, int piByteLength, AudioFormat poFormat, int piFrameLength) {
        super(poType, piByteLength, poFormat, piFrameLength);
    }

    /**
     * In addtion to the types defined in <code>AudioFileFormat.Type</code>
     * defines MP3, MIDI, and ULAW formats and their extensions.
     */
    public static class Type
            extends AudioFileFormat.Type {
        /**
         * Specifies MP3 file.
         */
        public static final Type MP3 = new MARFAudioFileFormat.Type("MP3", "mp3");

        /**
         * Specifies SINE file.
         */
        public static final Type SINE = new MARFAudioFileFormat.Type("SINE", "sine");

        /**
         * Specifies MIDI file.
         */
        public static final Type MIDI = new MARFAudioFileFormat.Type("MIDI", "mid");

        /**
         * Specifies ULAW file.
         */
        public static final Type ULAW = new MARFAudioFileFormat.Type("ULAW", "ulaw");

        /**
         * Specifies custom plugin file.
         *
         * @since 0.3.0.5
         */
        public static final Type CUSTOM = new MARFAudioFileFormat.Type("CUSTOM", "plugin");

        /**
         * Mimics parent's constructor.
         *
         * @param pstrName      format name
         * @param pstrExtension typical file extension
         */
        public Type(String pstrName, String pstrExtension) {
            super(pstrName, pstrExtension);
        }
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.9 $";
    }
}

// EOF
