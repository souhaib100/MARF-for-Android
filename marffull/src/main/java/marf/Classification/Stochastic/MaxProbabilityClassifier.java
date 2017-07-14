package marf.Classification.Stochastic;

import java.util.Vector;

import marf.Classification.ClassificationException;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.MARF;
import marf.MARF.NLP;
import marf.Stats.StatisticalEstimators.StatisticalEstimator;
import marf.Storage.Result;
import marf.Storage.StorageException;
import marf.util.Debug;


/**
 * <p>Maximum Probability Classification Module.
 * <p>
 * Originally came with the <code>LangIdentApp</code> NLP application
 * of Serguei Mokhov.
 * </p>
 * <p>
 * $Id: MaxProbabilityClassifier.java,v 1.24 2006/02/12 23:57:58 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.24 $
 * @since 0.3.0.2
 */
public class MaxProbabilityClassifier
        extends Stochastic {
    /**
     * Local reference to some instance of a statistical
     * estimator for probability computation.
     */
    protected StatisticalEstimator oStatisticalEstimator = null;

    /**
     * A collection of available natural languages.
     */
    protected Vector oAvailLanguages = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 8665926058819588355L;

    /**
     * NLP constructor that takes directly a statistical estimator.
     *
     * @param poStatisticalEstimator statistical estimator to use
     */
    public MaxProbabilityClassifier(StatisticalEstimator poStatisticalEstimator) {
        super(null);
        init(poStatisticalEstimator);
    }

    /**
     * Implements Classification API.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    public MaxProbabilityClassifier(IFeatureExtraction poFeatureExtraction) {
        super(poFeatureExtraction);

        // See if there is a request for a specific
        // statistical estimator.
        if (MARF.getModuleParams() != null) {
            Vector oParams = MARF.getModuleParams().getClassificationParams();

            if (oParams != null && oParams.size() > 1) {
                this.oStatisticalEstimator = (StatisticalEstimator) oParams.elementAt(1);
            }
        }

        init(this.oStatisticalEstimator);
    }

    /**
     * Initializes the classifier with all member variables.
     *
     * @param poStatisticalEstimator statistical estimator to use
     * @throws IllegalArgumentException if poStatisticalEstimator is null
     */
    public void init(StatisticalEstimator poStatisticalEstimator) {
        if (poStatisticalEstimator == null) {
            throw new IllegalArgumentException("MaxProbabilityClassifier: StatisticalEstimator is null!");
        }

        this.oStatisticalEstimator = poStatisticalEstimator;

        this.oAvailLanguages = new Vector();
        this.oObjectToSerialize = this.oAvailLanguages;
        this.strFilename = getClass().getName() + ".gzbin";
    }

    /**
     * Performs training of underlying statistical estimator
     * and goes through restore/dump cycle to save the available
     * languages. Implements Classification API.
     *
     * @return <code>true</code>
     * @throws ClassificationException should there be a problem with dump/restore
     */
    public boolean train()
            throws ClassificationException {
        try {
            this.oStatisticalEstimator.train();

            restore();

            Debug.debug("tr.before.oAvailLanguages=" + this.oAvailLanguages);
            System.out.println("Adding language [" + NLP.getLanguage() + "] ---- ");

            if (this.oAvailLanguages.contains(NLP.getLanguage()) == false) {
                this.oAvailLanguages.add(NLP.getLanguage());
                Debug.debug("tr.after.oAvailLanguages=" + this.oAvailLanguages);

                dump();
            }

            return true;
        } catch (StorageException e) {
            e.printStackTrace(System.err);
            throw new ClassificationException(e);
        }
    }

    /**
     * Performs language classification.
     * Implements Classification API.
     *
     * @return <code>true</code> if classification was successful
     * @throws ClassificationException if there was a problem with I/O
     *                                 or if there are no available languages
     */
    public boolean classify()
            throws ClassificationException {
        try {
            restore();

            Debug.debug("oAvailLanguages=" + this.oAvailLanguages);

            if (this.oAvailLanguages.size() == 0) {
                throw new ClassificationException("MaxProbabilityClassifier: there are no languages available.");
            }

            for (int i = 0; i < this.oAvailLanguages.size(); i++) {
                String strLang = (String) oAvailLanguages.elementAt(i);
                //oStatisticalEstimator.setLang(strLang);

                NLP.setLanguage(strLang);
                this.oStatisticalEstimator.resetFilename();

                double dProbability = this.oStatisticalEstimator.p();
                this.oStatisticalEstimator.getStreamTokenizer().reset();

                System.out.println("lang=" + strLang + ", P=" + dProbability);
                this.oResultSet.addResult(new Result(dProbability, strLang));
            }

            return true;
        } catch (Exception e) {
            throw new ClassificationException(e);
        }
    }

    /**
     * Add a piece of general StorageManager contract.
     * Resets available languages vector from the
     * object-to-serialize reference.
     *
     * @since 0.3.0.5
     */
    public synchronized void backSynchronizeObject() {
        this.oAvailLanguages = (Vector) this.oObjectToSerialize;
    }

    /**
     * An object must know how dump itself or its data structures to a file.
     * It only uses <code>DUMP_GZIP_BINARY</code> and <code>DUMP_BINARY</code> modes.
     *
     * @throws StorageException if saving to a file for some reason fails or
     *                          the dump mode set to an unsupported value
     * @see #dumpGzipBinary()
     * @see #dumpBinary()
     * @see #backSynchronizeObject()
     * @since 0.3.0.5
     */
    public synchronized void dump()
            throws StorageException {
        switch (this.iCurrentDumpMode) {
            case DUMP_GZIP_BINARY:
                dumpGzipBinary();
                break;

            case DUMP_BINARY:
                dumpBinary();
                break;

            default:
                throw new StorageException("Unsupported dump mode: " + this.iCurrentDumpMode);
        }
    }

    /**
     * An object must know how restore itself or its data structures from a file.
     * Options are: Object serialization and CSV, HTML. Internally, the method
     * calls all the <code>restore*()</code> methods based on the current dump mode.
     *
     * @throws StorageException if loading from a file for some reason fails or
     *                          the dump mode set to an unsupported value
     * @see #DUMP_GZIP_BINARY
     * @see #DUMP_BINARY
     * @see #dumpGzipBinary()
     * @see #dumpBinary()
     * @see #backSynchronizeObject()
     * @see #iCurrentDumpMode
     * @since 0.3.0.5
     */
    public synchronized void restore()
            throws StorageException {
        switch (this.iCurrentDumpMode) {
            case DUMP_GZIP_BINARY:
                restoreGzipBinary();
                break;

            case DUMP_BINARY:
                restoreBinary();
                break;

            default:
                throw new StorageException("Unsupported dump mode: " + this.iCurrentDumpMode);
        }
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.24 $";
    }
}

// EOF
