package marf.FeatureExtraction;

import java.util.Vector;

import marf.MARF;
import marf.Preprocessing.IPreprocessing;
import marf.Storage.ModuleParams;
import marf.util.Arrays;
import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.ExpandedThreadGroup;


/**
 * <p>This class by itself does not do any feature extraction, but
 * instead allows concatenation of the results of several actual feature
 * extractors to be combined in a single result. This should give the
 * classification modules more descriminatory power (e.g. when combining
 * the results of FFT and F0 together).</p>
 * <p>
 * <p><code>FeatureExtractionAggregator</code> itself still implements
 * the <code>FeatureExtraction</code> API in order to be used in the main
 * pipeline of <code>MARF</code>.</p>
 * <p>
 * <p>The aggregator expects <code>ModuleParams</code> to be set to the
 * enumeration constants of a module to be invoked followed by that module's
 * enclosed instance <code>ModuleParams</code>. As of this implementation,
 * that enclosed instance of <code>ModuleParams</code> isn't really used, so
 * the <b>main limitation</b> of the aggregator is that all the aggregated
 * feature extractors act with their default settings. This will happen until
 * the pipeline is re-designed a bit to include this capability.</p>
 * <p>
 * <p>The aggregator clones the incoming preprocessed sample for each
 * feature extractor and runs each module in a seaparate thread. A the
 * end, the results of each tread are collected in the same order as
 * specified by the initial <code>ModuleParams</code> and returned
 * as a concatenated feature vector. Some meta-information is available
 * if needed.</p>
 * <p>
 * $Id: FeatureExtractionAggregator.java,v 1.5 2005/12/31 01:23:54 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.5 $
 * @see marf.FeatureExtraction.FFT.FFT
 * @see marf.FeatureExtraction.F0.F0
 * @see marf.Storage.ModuleParams
 * @see marf.FeatureExtraction.FeatureExtraction
 * @since 0.3.0.5
 */
public class FeatureExtractionAggregator
        extends FeatureExtraction {
    /**
     * Error message for no modules found.
     */
    public static final String ERR_NO_MODULES =
            "No feature extraction modules defined for aggregation.";

    /**
     * Error message for no malformed ModuleParams data structure.
     */
    public static final String ERR_MALFORMED_PARAMS =
            "Malformed module parameters collection.\n"
                    + "HINT: Make sure it is a pairwise collection of (Integer, ModuleParams or Vector) types\n"
                    + "      where there latter may be null.";

    /**
     * Error indicating the fact that one or more aggregated modules
     * failed. Should be suffixed with the detailed error information.
     */
    public static final String ERR_AGGR_MODULES_FAILED =
            "There were errors in one or more aggregated modules: ";

    /**
     * A rare error when all feature extractors ran, but there
     * were a total of zero features extracted.
     */
    public static final String ERR_NO_FEATURES =
            "There were no features extracted!";

    /**
     * A collection of the feature extraction threads.
     */
    protected transient ExpandedThreadGroup oFeatureExtractors = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     */
    private static final long serialVersionUID = -8848546172469832656L;

    /**
     * Implementation of the feature extraction interface.
     * Internally also instantiates the thread group for feature
     * extractors.
     *
     * @param poPreprocessing the preprocessing module to suck the data from
     */
    public FeatureExtractionAggregator(IPreprocessing poPreprocessing) {
        super(poPreprocessing);
        this.oFeatureExtractors = new ExpandedThreadGroup("MARF FeatureExtractionAggregator");
    }

    /**
     * Implementation of the feature extraction interface.
     * The main business logic described in the preamble of
     * the class is implemented here.
     *
     * @throws FeatureExtractionException if ModuleParams isn't there or
     *                                    is in incorrect format, there were errors while cloning, and extracting
     *                                    features, and other errors.
     * @see marf.FeatureExtraction.IFeatureExtraction#extractFeatures()
     */
    public boolean extractFeatures()
            throws FeatureExtractionException {
        ModuleParams oModuleParams = MARF.getModuleParams();

        if
                (
                oModuleParams == null
                        || oModuleParams.getFeatureExtractionParams() == null
                        || oModuleParams.getFeatureExtractionParams().size() == 0
                ) {
            throw new FeatureExtractionException(ERR_NO_MODULES);
        }

        Vector oParams = oModuleParams.getFeatureExtractionParams();

		/* 
         * So that aggregated modules by mistake do not query
		 * for our module parameters. This is the reason for now
		 * why the unaware modules cannot be set to the parameters
		 * other than default as they always query MARF as a
		 * parameters provider.
		 */
        MARF.getModuleParams().setFeatureExtractionParams(new Vector());

        // At the very least, must be even (type, params pairs)
        if (oParams.size() % 2 == 1) {
            throw new FeatureExtractionException(ERR_MALFORMED_PARAMS);
        }

        try {
            // Create modules and threads
            for (int i = 0; i < oParams.size(); i += 2) {
                Integer oFeatureExtractionMethod = (Integer) oParams.elementAt(i);

				/*
                 * Queried here for type checks, but presently unused
				 * in the follow up logic. TODO: make use of this when
				 * parameter providers framework is in.
				 */
                Object oAggrParams = oParams.elementAt(i + 1);
                ModuleParams oAggrModuleParams = null;

                if (oAggrParams != null) {
                    if (oAggrParams instanceof ModuleParams) {
                        oAggrModuleParams = (ModuleParams) oAggrParams;
                    } else if (oAggrParams instanceof Vector) {
                        oAggrModuleParams = new ModuleParams();
                        oAggrModuleParams.setFeatureExtractionParams((Vector) oAggrParams);
                    } else {
                        throw new FeatureExtractionException
                                (
                                        ERR_MALFORMED_PARAMS + ": " + oAggrParams
                                );
                    }
                }

                Debug.debug("Specific module params got: " + oAggrModuleParams);

                IFeatureExtraction oModule = FeatureExtractionFactory.create
                        (
                                oFeatureExtractionMethod,
                                (IPreprocessing) this.oPreprocessing.clone()
                        );

                new FeatureExtractionThread(oModule, this.oFeatureExtractors);
            }

            // Run all the threads and wait for all of them
            this.oFeatureExtractors.start();
            this.oFeatureExtractors.join();

            // Get all the threads
            Thread[] aoFeatureExtractors = this.oFeatureExtractors.enumerate(false);

            // Collect all the data and/or errors.
            int iTotalFEVectorSize = 0;

            Vector oData = new Vector();
            Vector oErrors = new Vector();

            for (int i = 0; i < aoFeatureExtractors.length; i++) {
                FeatureExtractionThread oFEThread = (FeatureExtractionThread) aoFeatureExtractors[i];

                if (oFEThread.getLastException() == null) {
                    double[] adFeatureVector = oFEThread.getFeatureExtraction().getFeaturesArray();
                    assert adFeatureVector != null;
                    oData.add(adFeatureVector);
                    iTotalFEVectorSize += adFeatureVector.length;
                } else {
                    oErrors.add(oFEThread.getLastException());
                }
            }

            if (oErrors.size() > 0) {
                throw new FeatureExtractionException
                        (
                                ERR_AGGR_MODULES_FAILED
                                        + oErrors
                        );
            }

            if (iTotalFEVectorSize == 0) {
                throw new FeatureExtractionException(ERR_NO_FEATURES);
            }

            // Finally, consolidate the data
            this.adFeatures = new double[iTotalFEVectorSize];
            int iDestinationOffset = 0;

            for (int i = 0; i < oData.size(); i++) {
                double[] adFeatureVector = (double[]) oData.elementAt(i);
                Arrays.copy(this.adFeatures, iDestinationOffset, adFeatureVector, 0, adFeatureVector.length);
                iDestinationOffset += adFeatureVector.length;
            }

            // Restore
            MARF.getModuleParams().setFeatureExtractionParams(oParams);

            // Done
            return true;
        } catch (FeatureExtractionException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new FeatureExtractionException(e.getMessage(), e);
        }
    }

    /**
     * <p>A feature extraction thread simply calls its concrete encapsulated
     * feature extraction module and collects any results or errors from
     * its run.</p>
     *
     * @author Serguei Mokhov
     * @since 0.3.0.5
     */
    public class FeatureExtractionThread
            extends BaseThread {
        /**
         * A reference to the feature extraction module.
         */
        protected IFeatureExtraction oFeatureExtraction = null;

        /**
         * Keeps return value from <code>extractFeatures()</code>
         * in case someone is interested.
         *
         * @see IFeatureExtraction#extractFeatures()
         */
        protected boolean bRetVal = false;

        /**
         * Last exception captured after feature extraction run.
         */
        protected Exception oLastException = null;

        /**
         * Constructs a feature extraction thread with a given
         * module.
         *
         * @param poFeatureExtraction the module; must not be null
         * @param poGroup             the thread group to attach this thread to
         */
        public FeatureExtractionThread(IFeatureExtraction poFeatureExtraction, ExpandedThreadGroup poGroup) {
            super(poGroup, poFeatureExtraction.getClass().getName());
            assert poFeatureExtraction != null;
            this.oFeatureExtraction = poFeatureExtraction;
        }

        /**
         * Internally calls <code>extractFeatures()</code> and
         * collects its return value and possibly its last error.
         *
         * @see java.lang.Runnable#run()
         * @see IFeatureExtraction#extractFeatures()
         */
        public void run() {
            try {
                this.bRetVal = this.oFeatureExtraction.extractFeatures();
            } catch (Exception e) {
                this.oLastException = e;
            }
        }

        /**
         * Allows to query for the feature extraction success return value.
         *
         * @return the return value of <code>extractFeatures()</code>
         * @see IFeatureExtraction#extractFeatures()
         */
        public synchronized boolean getRetVal() {
            return this.bRetVal;
        }

        /**
         * Allows obtaining the reference to the contained
         * feature extraction module.
         *
         * @return the internal feature extraction module reference
         */
        public synchronized IFeatureExtraction getFeatureExtraction() {
            return this.oFeatureExtraction;
        }

        /**
         * Allows to query for the last error happend while
         * extracting features.
         *
         * @return the last exception; null if there were none (yet or postmortem)
         */
        public synchronized Exception getLastException() {
            return this.oLastException;
        }
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.5 $";
    }
}

// EOF
