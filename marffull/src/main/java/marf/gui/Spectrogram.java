package marf.gui;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import marf.MARF;
import marf.Storage.StorageException;
import marf.Storage.StorageManager;
import marf.util.Debug;
import marf.util.NotImplementedException;


/**
 * <p>Class Spectrogram dumps a spectrogram to a PPM file.</p>
 * <p>
 * <p>$Id: Spectrogram.java,v 1.30 2005/08/13 23:09:38 susan_fan Exp $</p>
 *
 * @author Ian Clement
 * @author Serguei Mokhov
 * @version $Revision: 1.30 $
 * @since 0.0.1
 */
public class Spectrogram
        extends StorageManager {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -8219029755014757254L;
    /**
     * The data vector.
     */
    protected Vector data = null;

    /**
     * Current minimum.
     */
    protected double min = 0.0;

    /**
     * Current maximum.
     */
    protected double max = 0.0;

    /**
     * To differentiate file names based on the feature extraction method name.
     */
    protected String strMethod = "";

    /**
     * Constructor.
     */
    public Spectrogram() {
        this.data = new Vector();
    }

    /**
     * Constructor with a feature extraction method name.
     *
     * @param pstrMethodName String representing FE module name
     */
    public Spectrogram(String pstrMethodName) {
        this();
        this.strMethod = pstrMethodName;
    }

    /**
     * Adds LPC spectrum to the data to dump.
     *
     * @param lpc_coeffs LPC coefficiencies to dump
     * @param num_coeffs number of LPC coefficiencies to dump
     * @param size       size of the spectrogram (width)
     */
    public final void addLPC(final double[] lpc_coeffs, final int num_coeffs, final int size) {
        double[] to_insert = new double[size];

        for (int i = 0; i < size; i++) {
            double Ar = 1.0;
            double Ai = 0.0;

            for (int k = 0; k < num_coeffs; k++) {
                Ar -= lpc_coeffs[k] * Math.cos(2 * Math.PI * i * -k / 128);
                Ai -= lpc_coeffs[k] * Math.sin(2 * Math.PI * i * -k / 128);
            }

            double A = Math.sqrt(Ar * Ar + Ai * Ai);
            double H = 1.0 / A;

            if (H > this.max)
                this.max = H;
            else if (H < this.min)
                this.min = H;

            to_insert[i] = H;
        }

        this.data.add(to_insert);
    }

    /**
     * Adds FFT spectrum to the data to dump.
     *
     * @param values array of doubles (FFT coefficiencies)
     */
    public final void addFFT(final double[] values) {
        double[] to_insert = new double[values.length / 2];

        if (this.data.size() == 0)
            this.min = this.max = values[0];

        for (int k = 0; k < to_insert.length; k++) {
            if (values[k] > max)
                this.max = values[k];

            if (values[k] < min)
                this.min = values[k];

            to_insert[k] = values[k];
        }

        this.data.add(to_insert);
    }

    /**
     * Dumps spectrogram.
     *
     * @throws StorageException
     */
    public final void dump()
            throws StorageException {
        try {
            Debug.debug("Dumping spectrogram " + MARF.getSampleFile() + "." + this.strMethod + ".ppm");
            Debug.debug("Spectrogram.dump() - data size in vectors: " + data.size());

            FileOutputStream fos = null;
            DataOutputStream outfile = null;

            fos = new FileOutputStream(MARF.getSampleFile() + "." + this.strMethod + ".ppm");
            outfile = new DataOutputStream(fos);

            // Ouput PPM header
/*
        man ppm:

         - A "magic number" for identifying the file type.  A pgm file's magic number is the two characters "P6".

         - Whitespace (blanks, TABs, CRs, LFs).

         - A width, formatted as ASCII characters in decimal.

         - Whitespace.

         - A height, again in ASCII decimal.

         - Whitespace.

         - The maximum color value (Maxval), again in ASCII decimal.  Must be less than 65536.

         - Newline or other single whitespace character.

         - A raster of Width * Height pixels, proceeding through the image in normal English reading order.  Each pixel is  a  triplet  of  red,
           green,  and  blue  samples,  in that order.  Each sample is represented in pure binary by either 1 or 2 bytes.  If the Maxval is less
           than 256, it is 1 byte.  Otherwise, it is 2 bytes.  The most significant byte is first.
*/
            // Output data

            // Make max be at 75%
            this.max *= 0.75;

            int length = ((double[]) this.data.elementAt(0)).length;

            outfile.writeBytes("P6\n" + this.data.size() + "\n" + length + "\n255\n");

            for (int j = length - 1; j >= 0; j--) {
                for (int i = 0; i < this.data.size(); i++) {
                    double[] adData = (double[]) this.data.elementAt(i);

                    for (int m = 0; m < 3; m++) {
                        double val;

                        if (adData[j] > this.max)
                            val = this.max;
                        else if (adData[j] < this.min)
                            val = this.min;
                        else
                            val = adData[j];

                        outfile.writeByte((int) (((this.max - val) / this.max) * 256));
                    }
                }
            }

            Debug.debug
                    (
                            "Done dumping spectrogram " +
                                    MARF.getSampleFile() + "." + this.strMethod +
                                    ".ppm [" + (this.data.size() * length * 3) + " bytes]"
                    );
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Not implemented.
     *
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public void restore()
            throws StorageException {
        throw new NotImplementedException("Spectrogram.restore()");
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.30 $";
    }
}

// EOF
