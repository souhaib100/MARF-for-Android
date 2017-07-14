package marf.math;

/**
 * <p>Collection of algorithms to be used by the modules.
 * Decouples algorithms from the modules and allows to be
 * used by different types of modules.</p>
 * <p>
 * $Id: Algorithms.java,v 1.9 2006/01/21 02:35:32 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.9 $
 * @since 0.3.0.2
 */
public final class Algorithms {
    /**
     * There shall no be derivatives or instances.
     */
    private Algorithms() {
    }

    /**
     * A collection of Hamming Window-related algorithms.
     *
     * @author Stephen Sinclair
     * @author Serguei Mokhov
     */
    public static final class Hamming {
        /* Hamming Window */

        /**
         * Retrieves a single value of hamming window based on
         * length and index within the window.
         *
         * @param piN      index into the window
         * @param piLength the total length of the window
         * @return the hamming value within the window length; 0 if outside of the window
         */
        public static final double hamming(final int piN, final int piLength) {
            if (piN <= (piLength - 1) && piN >= 0) {
                return 0.54 - (0.46 * Math.cos((2 * Math.PI * piN) / (piLength - 1)));
            }

            return 0;
        }

        /**
         * Applies hamming window to an array of doubles.
         *
         * @param padWindow array of doubles to apply windowing to
         * @since 0.2.0
         */
        public static final void hamming(double[] padWindow) {
            for (int i = 0; i < padWindow.length; i++) {
                padWindow[i] *=
                        (0.54 - (0.46 * Math.cos((2 * Math.PI * i) /
                                (padWindow.length - 1))));
            }
        }

        /**
         * Calculates square root Hamming window value given index and size.
         *
         * @param piIndex window index
         * @param piSize  window size
         * @return resulting squared hamming window
         * @since 0.3.0.2
         */
        public static final double sqrtHamming(int piIndex, int piSize) {
            return Math.sqrt(1 - 0.85185 * Math.cos((2 * piIndex - 1) * Math.PI / piSize));
        }
    }

    /**
     * A collection of FFT-related math.
     *
     * @author Stephen Sinclair
     */
    public static final class FFT {
        /* FFT Methods */

        /**
         * <p>FFT algorithm, translated from "Numerical Recipes in C++" that
         * implements the Fast Fourier Transform, which performs a discrete Fourier transform
         * in O(n*log(n)).</p>
         *
         * @param padInputReal  InputReal is real part of input array
         * @param padInputImag  InputImag is imaginary part of input array
         * @param padOutputReal OutputReal is real part of output array
         * @param padOutputImag OutputImag is imaginary part of output array
         * @param piDirection   Direction is 1 for normal FFT, -1 for inverse FFT
         * @throws MathException if the sizes or direction are wrong
         */
        public static final void doFFT
        (
                final double[] padInputReal,
                double[] padInputImag,
                double[] padOutputReal,
                double[] padOutputImag,
                int piDirection
        )
                throws MathException {
            // Ensure input length is a power of two
            int iLength = padInputReal.length;

            if ((iLength < 1) | ((iLength & (iLength - 1)) != 0)) {
                throw new MathException("Length of input (" + iLength + ") is not a power of 2.");
            }

            if ((piDirection != 1) && (piDirection != -1)) {
                throw new MathException("Bad direction specified. Should be 1 or -1.");
            }

            if (padOutputReal.length < padInputReal.length) {
                throw new MathException("Output length (" + padOutputReal.length + ") < Input length (" + padInputReal.length + ")");
            }

            // Determine max number of bits
            int iMaxBits, n = iLength;

            for (iMaxBits = 0; iMaxBits < 16; iMaxBits++) {
                if (n == 0) break;
                n /= 2;
            }

            iMaxBits -= 1;

            // Binary reversion & interlace result real/imaginary
            int i, t, bit;

            for (i = 0; i < iLength; i++) {
                t = 0;
                n = i;

                for (bit = 0; bit < iMaxBits; bit++) {
                    t = (t * 2) | (n & 1);
                    n /= 2;
                }

                padOutputReal[t] = padInputReal[i];
                padOutputImag[t] = padInputImag[i];
            }

            // put it all back together (Danielson-Lanczos butterfly)
            int mmax = 2, istep, j, m;                                // counters
            double theta, wtemp, wpr, wr, wpi, wi, tempr, tempi;    // trigonometric recurrences

            n = iLength * 2;

            while (mmax < n) {
                istep = mmax * 2;
                theta = (piDirection * 2 * Math.PI) / mmax;
                wtemp = Math.sin(0.5 * theta);
                wpr = -2.0 * wtemp * wtemp;
                wpi = Math.sin(theta);
                wr = 1.0;
                wi = 0.0;

                for (m = 0; m < mmax; m += 2) {
                    for (i = m; i < n; i += istep) {
                        j = i + mmax;
                        tempr = wr * padOutputReal[j / 2] - wi * padOutputImag[j / 2];
                        tempi = wr * padOutputImag[j / 2] + wi * padOutputReal[j / 2];

                        padOutputReal[j / 2] = padOutputReal[i / 2] - tempr;
                        padOutputImag[j / 2] = padOutputImag[i / 2] - tempi;

                        padOutputReal[i / 2] += tempr;
                        padOutputImag[i / 2] += tempi;
                    }

                    wr = (wtemp = wr) * wpr - wi * wpi + wr;
                    wi = wi * wpr + wtemp * wpi + wi;
                }

                mmax = istep;
            }
        }

        /**
         * <p>Performs a normal FFT, taking a real input (supposedly an audio sample) and returns
         * the frequency analysis in terms of "magnitude" and "phase angle".</p>
         *
         * @param padSample     must be an array of size (2^k)
         * @param padMagnitude  must be half the size of "sample"
         * @param padPhaseAngle must be half the size of "sample"
         * @throws MathException
         */
        public static final void normalFFT(final double[] padSample, double[] padMagnitude, double[] padPhaseAngle)
                throws MathException {
            double[] adSampleImag = new double[padSample.length];
            double[] adOutputReal = new double[padSample.length];
            double[] adOutputImag = new double[padSample.length];

            doFFT(padSample, adSampleImag, adOutputReal, adOutputImag, 1);

            // convert complex output to magnitude and phase angle
            int iLen = padMagnitude.length;

            if (padMagnitude.length > (padSample.length / 2)) {
                iLen = padSample.length / 2;
            }

            for (int i = 0; i < iLen; i++) {
                padMagnitude[i] = Math.sqrt(adOutputReal[i] * adOutputReal[i] + adOutputImag[i] * adOutputImag[i]);

                if (padPhaseAngle != null) {
                    padPhaseAngle[i] = Math.atan(adOutputImag[i] / adOutputReal[i]);
                }
            }
        }

        /**
         * <p>Performs a normal FFT, taking a real input (supposedly an audio sample) and returns
         * the frequency analysis in terms of "magnitude".</p>
         *
         * @param padSample    must be an array of size (2^k)
         * @param padMagnitude must be half the size of "sample"
         * @throws MathException
         */
        public static final void normalFFT(final double[] padSample, double[] padMagnitude)
                throws MathException {
            normalFFT(padSample, padMagnitude, null);
        }
    }

    /**
     * A collection of LPC-related algorithms.
     *
     * @author Ian Clement
     */
    public static final class LPC {
        /* LPC methods */

        /**
         * Does the LPC algorithm.
         * <b>NOTE:</b> input is assumed to be windowed, ie: input.length = N.
         *
         * @param padInput  windowed part of incoming sample
         * @param padOutput resulting LPC coefficiencies
         * @param padError  output LPC error
         * @param piPoles   number of poles
         * @throws MathException
         */
        public static final void doLPC(final double[] padInput, double[] padOutput, double[] padError, int piPoles)
                throws MathException {
            if (piPoles <= 0) {
                throw new MathException("Number of poles should be > 0; supplied: " + piPoles);
            }

            if (padOutput.length != piPoles) {
                throw new MathException("Output array should be of length p (" + piPoles + ")!");
            }

            if (padError.length != piPoles) {
                throw new MathException("Error array should be of length p (" + piPoles + ")!");
            }

            double[] k = new double[piPoles];
            double[][] A = new double[piPoles][piPoles];

            padError[0] = autocorrelation(padInput, 0);

            A[0][0] = k[0] = 0.0;

            for (int m = 1; m < piPoles; m++) {
                // calculate k[m]
                double dTmp = autocorrelation(padInput, m);

                for (int i = 1; i < m; i++) {
                    dTmp -= A[m - 1][i] * autocorrelation(padInput, m - i);
                }

                k[m] = dTmp / padError[m - 1];

                // update A[m][*]
                for (int i = 0; i < m; i++) {
                    A[m][i] = A[m - 1][i] - k[m] * A[m - 1][m - i];
                }

                A[m][m] = k[m];

                // update error[m]
                padError[m] = (1 - (k[m] * k[m])) * padError[m - 1];
            }

            // [SM]: kludge?
            for (int i = 0; i < piPoles; i++) {
                if (Double.isNaN(A[piPoles - 1][i])) {
                    padOutput[i] = 0.0;
                } else {
                    padOutput[i] = A[piPoles - 1][i];
                }
            }
        }

        /**
         * Implements the least-square autocorrelation method.
         *
         * @param padInput windowed input signal
         * @param piX      coefficient number
         * @return double - correlation number
         */
        public static final double autocorrelation(final double[] padInput, int piX) {
            double dRet = 0.0;

            for (int i = piX; i < padInput.length; i++) {
                dRet += padInput[i] * padInput[i - piX];
            }

            return dRet;
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
