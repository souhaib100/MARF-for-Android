package marf.Storage;

import marf.MARF;
import marf.Storage.Loaders.AIFFCLoader;
import marf.Storage.Loaders.AIFFLoader;
import marf.Storage.Loaders.AULoader;
import marf.Storage.Loaders.MIDILoader;
import marf.Storage.Loaders.MP3Loader;
import marf.Storage.Loaders.SNDLoader;
import marf.Storage.Loaders.SineLoader;
import marf.Storage.Loaders.ULAWLoader;
import marf.Storage.Loaders.WAVLoader;
import marf.util.InvalidSampleFormatException;


/**
 * <p>Provides a factory to instantiate requested SampleLoader module(s).</p>
 * <p>
 * $Id: SampleLoaderFactory.java,v 1.1 2005/12/28 03:21:12 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.1 $
 * @since 0.3.0.5
 */
public final class SampleLoaderFactory {
    /**
     * Disallow instances of this factory as deemed useless.
     */
    private SampleLoaderFactory() {
    }

    /**
     * Instantiates a SampleLoader module indicated by
     * the first parameter with the 2nd parameter as an argument.
     *
     * @param poSampleFormat the integer value corresponding to the
     *                       desired SampleLoader module
     * @return a reference to the instance of the created sample loader
     * @throws InvalidSampleFormatException if the indicated module is
     *                                      uknown or could not be loaded due sample format mismatch
     * @see MARF#WAV
     * @see MARF#MP3
     * @see MARF#ULAW
     * @see MARF#SINE
     * @see MARF#AIFF
     * @see MARF#AIFFC
     * @see MARF#AU
     * @see MARF#SND
     * @see MARF#MIDI
     * @see WAVLoader
     * @see MP3Loader
     * @see ULAWLoader
     * @see SineLoader
     * @see AIFFLoader
     * @see AIFFCLoader
     * @see AULoader
     * @see SNDLoader
     * @see MIDILoader
     */
    public static final ISampleLoader create(final Integer poSampleFormat)
            throws InvalidSampleFormatException {
        return create(poSampleFormat.intValue());
    }

    /**
     * Instantiates a SampleLoader module indicated by
     * the first parameter with the 2nd parameter as an argument.
     *
     * @param piSampleFormat the integer value corresponding to the
     *                       desired SampleLoader module
     * @return a reference to the instance of the created sample loader
     * @throws InvalidSampleFormatException if the indicated module is
     *                                      uknown or could not be loaded due sample format mismatch
     * @see MARF#WAV
     * @see MARF#MP3
     * @see MARF#ULAW
     * @see MARF#SINE
     * @see MARF#AIFF
     * @see MARF#AIFFC
     * @see MARF#AU
     * @see MARF#SND
     * @see MARF#MIDI
     * @see MARF#CUSTOM
     * @see WAVLoader
     * @see MP3Loader
     * @see ULAWLoader
     * @see SineLoader
     * @see AIFFLoader
     * @see AIFFCLoader
     * @see AULoader
     * @see SNDLoader
     * @see MIDILoader
     */
    public static final ISampleLoader create(final int piSampleFormat)
            throws InvalidSampleFormatException {
        ISampleLoader oSampleLoader = null;

        switch (piSampleFormat) {
            case MARF.WAV:
                oSampleLoader = new WAVLoader();
                break;

            case MARF.MP3:
                oSampleLoader = new MP3Loader();
                break;

            case MARF.ULAW:
                oSampleLoader = new ULAWLoader();
                break;

            case MARF.SINE:
                oSampleLoader = new SineLoader();
                break;

            case MARF.AIFF:
                oSampleLoader = new AIFFLoader();
                break;

            case MARF.AIFFC:
                oSampleLoader = new AIFFCLoader();
                break;

            case MARF.AU:
                oSampleLoader = new AULoader();
                break;

            case MARF.SND:
                oSampleLoader = new SNDLoader();
                break;

            case MARF.MIDI:
                oSampleLoader = new MIDILoader();
                break;

            case MARF.CUSTOM: {
                try {
                    oSampleLoader = (ISampleLoader) MARF.getSampleLoaderPluginClass().newInstance();
                } catch (Exception e) {
                    throw new InvalidSampleFormatException(e.getMessage(), e);
                }

                break;
            }

            default: {
                throw new InvalidSampleFormatException
                        (
                                "Unknown sample file format: " + piSampleFormat
                        );
            }
        }

        return oSampleLoader;
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
