package marf.Storage.Loaders;

import java.io.File;

import marf.Storage.Sample;
import marf.Storage.SampleLoader;
import marf.Storage.StorageException;
import marf.util.NotImplementedException;


/**
 * Not Implemented.
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.13 $
 * @since 0.0.1
 */
public class MP3Loader
        extends SampleLoader {
    /**
     * MP3 Loader Constructor.
     */
    public MP3Loader() {
    }

    /**
     * Not Implemented.
     *
     * @param padSample unused
     * @return nothing
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public final int readAudioData(double[] padSample)
            throws StorageException {
        throw new NotImplementedException("readAudioData()");
    }

    /**
     * Not Implemented.
     *
     * @param padSample unused
     * @param piNbrData unused
     * @return nothing
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public final int writeAudioData(final double[] padSample, final int piNbrData)
            throws StorageException {
        throw new NotImplementedException("writeAudioData()");
    }

    /**
     * Not Implemented.
     *
     * @param poFile unused
     * @return nothing
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public Sample loadSample(File poFile)
            throws StorageException {
        throw new NotImplementedException("loadSample()");
    }

    /**
     * Not Implemented.
     *
     * @param poFile unused
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public void saveSample(File poFile)
            throws StorageException {
        throw new NotImplementedException("saveSample()");
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.13 $";
    }
}

// EOF
