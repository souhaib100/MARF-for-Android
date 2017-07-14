package marf.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import marf.MARF;
import marf.Storage.StorageException;
import marf.Storage.StorageManager;
import marf.util.Debug;
import marf.util.NotImplementedException;


/**
 * Class WaveGrapher.
 * <p>
 * <p>$Id: WaveGrapher.java,v 1.15 2006/01/21 02:35:32 mokhov Exp $</p>
 *
 * @author Stephen Sinclair
 * @version $Revision: 1.15 $
 * @since 0.0.1
 */
public class WaveGrapher
        extends StorageManager {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -8014202943411824894L;

    /**
     * Data to graph.
     */
    private double[] adDataArray = null;

    /**
     * Range of data for the X axis -- Minimum.
     */
    private double dXmin = 0;

    /**
     * Range of data for the X axis -- Maximum.
     */
    private double dXmax = 0;

    /**
     * Constructor.
     *
     * @param padData
     * @param pdMin
     * @param pdMax
     * @param pstrName
     * @param pstrDescriptor
     */
    public WaveGrapher(double[] padData, double pdMin, double pdMax, String pstrName, String pstrDescriptor) {
        this.adDataArray = padData;

        this.dXmin = pdMin;
        this.dXmax = pdMax;

        this.strFilename = new String(pstrName + "." + MARF.getPreprocessingMethod() + "." + pstrDescriptor);
    }

    /**
     * Dumps graph of wave in the CSV format.
     *
     * @throws StorageException
     */
    public final void dump()
            throws StorageException {
        try {
            BufferedWriter oBW = new BufferedWriter(new FileWriter(this.strFilename + ".csv"));

            oBW.write(this.strFilename);
            oBW.newLine();

            double dRange = this.dXmax - this.dXmin;

            for (int i = 0; i < this.adDataArray.length; i++) {
                oBW.write((dXmin + i * dRange / this.adDataArray.length) + "," + this.adDataArray[i]);
                oBW.newLine();
            }

            oBW.close();

            Debug.debug("Wave graph dumped to " + this.strFilename + ".csv");
        } catch (IOException e) {
            System.err.println
                    (
                            "WaveGrapher couldn't generate graph (" + this.strFilename + ".csv): " +
                                    e.getMessage()
                    );
        }
    }

    /**
     * Not implemented.
     *
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public final void restore()
            throws StorageException {
        throw new NotImplementedException("WaveGrapher.restore()");
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.15 $";
    }
}

// EOF
