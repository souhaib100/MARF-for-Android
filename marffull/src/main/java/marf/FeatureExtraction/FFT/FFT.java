package marf.FeatureExtraction.FFT;

import java.util.Vector;

import marf.FeatureExtraction.FeatureExtraction;
import marf.FeatureExtraction.FeatureExtractionException;
import marf.MARF;
import marf.Preprocessing.IPreprocessing;
import marf.Storage.ModuleParams;
import marf.Storage.SampleLoader;
import marf.gui.Spectrogram;
import marf.gui.WaveGrapher;
import marf.math.Algorithms;
import marf.util.Arrays;
import marf.util.Debug;


/**
 * <p>Class FFT implements Fast Fourier Transform.</p>
 * <p>
 * <p>$Id: FFT.java,v 1.52 2005/12/30 05:54:25 mokhov Exp $</p>
 *
 * @author Stephen Sinclair
 * @author Serguei Mokhov
 * @version $Revision: 1.52 $
 * @since 0.0.1
 */
public class FFT
        extends FeatureExtraction {
    /**
     * Default number (1024) of doubles per chunk in the window.
     * Feature vector will be half of the chunk size.
     */
    public static final int DEFAULT_CHUNK_SIZE = 1024;

    /**
     * FFT chunk size.
     * Must be a power of 2.
     *
     * @since 0.3.0.4
     */
    protected int iChunkSize = DEFAULT_CHUNK_SIZE;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 4400274959804693096L;

    /**
     * FFT Constructor.
     *
     * @param poPreprocessing Preprocessing module reference
     * @throws ClassCastException       if a feature extraction module parameter
     *                                  supplied and is not of type Integer
     * @throws IllegalArgumentException if the chunk size parameter is
     *                                  less than 1 or not a power of 2
     */
    public FFT(IPreprocessing poPreprocessing) {
        super(poPreprocessing);

		/*
         * Allow getting a non-default chunk size
		 * from an application via the core pipeline.
		 */
        ModuleParams oModuleParams = MARF.getModuleParams();

        if (oModuleParams != null) {
            Vector oFFTParams = oModuleParams.getFeatureExtractionParams();

            if (oFFTParams != null && oFFTParams.size() > 0) {
                setChunkSize(((Integer) oFFTParams.firstElement()).intValue());
            }
        }
    }

	/* Feature Extraction API */

    /**
     * FFT Implementation of <code>extractFeatures()</code>.
     *
     * @return <code>true</code> if there were features extracted, <code>false</code> otherwise
     * @throws FeatureExtractionException in case of any errors while doing stuff
     */
    public final synchronized boolean extractFeatures()
            throws FeatureExtractionException {
        try {
            Debug.debug("FFT.extractFeatures() has begun...");

            int iHalfChunkSize = this.iChunkSize / 2;

            double[] adSampleChunk = new double[this.iChunkSize];
            double[] adSampleArray = null;

            int iNbrDataRecv = 0;
            int iWindowPos = 0;
            int iCount = 0;

            int i;

            Spectrogram oSpectrogram = null;

            this.adFeatures = new double[iHalfChunkSize];

            double[] adMagnitude = new double[iHalfChunkSize];
            double[] adPhaseAngle = new double[iHalfChunkSize];

            // For the case when we want intermediate spectrogram
            if (MARF.getDumpSpectrogram() == true) {
                oSpectrogram = new Spectrogram("fft");
            }

            iWindowPos = 0;
            iNbrDataRecv = this.oPreprocessing.getSample().getNextChunk(adSampleChunk);

            while (iNbrDataRecv > 0) {
                // Fill SampleArray with new window
                for (i = 0; i < this.iChunkSize; i++) {
                    if (iWindowPos >= this.iChunkSize) {
                        iNbrDataRecv = this.oPreprocessing.getSample().getNextChunk(adSampleChunk);
                        iWindowPos = 0;

                        // Padding to ^2 for the last chunk
                        if (iNbrDataRecv < this.iChunkSize && iNbrDataRecv > 0) {
                            Arrays.fill(adSampleChunk, iNbrDataRecv, this.iChunkSize - 1, 0);
                            iNbrDataRecv = 0;
                        }
                    }

                    //adSampleArray[i] = adSampleChunk[iWindowPos++] * hamming(i, iChunkSize);
                    iWindowPos++;
                }

                //XXX: hamming(adSampleChunk);
                adSampleArray = (double[]) adSampleChunk.clone();
                Algorithms.Hamming.hamming(adSampleArray);

                // overlap windows
                iWindowPos = (iWindowPos - iHalfChunkSize) % this.iChunkSize;

                if (iWindowPos < 0) {
                    iWindowPos += this.iChunkSize;
                }

                //XXX: normalFFT(adSampleChunk, adMagnitude, adPhaseAngle);
                Algorithms.FFT.normalFFT(adSampleArray, adMagnitude, adPhaseAngle);

                iCount++;

                if (MARF.getDumpSpectrogram() == true) {
                    oSpectrogram.addFFT(adMagnitude);
                }

                for (i = 0; i < iHalfChunkSize; i++) {
                    this.adFeatures[i] += adMagnitude[i];
                }

                iNbrDataRecv = this.oPreprocessing.getSample().getNextChunk(adSampleChunk);
            }

            if (iCount > 1) {
                for (i = 0; i < iHalfChunkSize; i++) {
                    this.adFeatures[i] /= iCount;
                }
            }

            // For the case when we want intermediate spectrogram
            if (MARF.getDumpSpectrogram() == true) {
                oSpectrogram.dump();
            }

            // If we want to graph the FFT output
            if (MARF.getDumpWaveGraph() == true) {
                new WaveGrapher
                        (
                                this.adFeatures,
                                0,
                                SampleLoader.DEFAULT_FREQUENCY / 2,
                                MARF.getSampleFile(),
                                "fft"
                        ).dump();
            }

            Debug.debug("FFT.extractFeatures() has finished.");

            return (this.adFeatures.length > 0);
        } catch (Exception e) {
            throw new FeatureExtractionException(e.toString(), e);
        }
    }

    /**
     * Allows setting a non-default chunk size.
     * Must be a power of 2.
     *
     * @param piChunkSize new chunk size
     * @return the old chunk size
     * @throws IllegalArgumentException if the chunk size parameter is
     *                                  less than 1 or not a power of 2
     * @since 0.3.0.4
     */
    public synchronized int setChunkSize(int piChunkSize) {
        if ((piChunkSize < 1) | ((piChunkSize & (piChunkSize - 1)) != 0)) {
            throw new IllegalArgumentException
                    (
                            "Chunk size (" + piChunkSize +
                                    ") is not a power of 2 or less than 1."
                    );
        }

        int iOldChunkSize = this.iChunkSize;
        this.iChunkSize = piChunkSize;
        return iOldChunkSize;
    }

    /**
     * Allows getting the current chunk size.
     *
     * @return the current chunk size
     * @since 0.3.0.4
     */
    public synchronized int getChunkSize() {
        return this.iChunkSize;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.52 $";
    }
}

// EOF
