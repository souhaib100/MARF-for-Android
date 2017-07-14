package marf.Storage;

import java.util.Vector;


/**
 * <p>Class <code>ModuleParams</code> provides ability to pass module-specific parameters from an application.</p>
 * <p>The specific module should know in which order and how to downcast those params.</p>
 * <p>
 * $Id: ModuleParams.java,v 1.17 2006/01/02 22:24:00 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.17 $
 * @since 0.0.1
 */
public class ModuleParams
        implements Cloneable {
    /*
     * ---------------------
	 * Data members
	 * ---------------------
	 */

    /**
     * A Vector of preprocessing parameters.
     */
    protected Vector oPreprocessingParams = new Vector();

    /**
     * A Vector of feature extraction parameters.
     */
    protected Vector oFeatureExtractionParams = new Vector();

    /**
     * A Vector of classification parameters.
     */
    protected Vector oClassificationParams = new Vector();


	/*
     * ---------------------
	 * Enumeration
	 * ---------------------
	 */

    /**
     * Indicates that we manipulate on the Preprocessing Vector.
     */
    protected static final int PREPROCESSING = 0;

    /**
     * Indicates that we manipulate on the Feature Extraction Vector.
     */
    protected static final int FEATURE_EXTRACTION = 1;

    /**
     * Indicates that we manipulate on the Classification Vector.
     */
    protected static final int CLASSIFICATION = 2;


	/*
	 * ---------------------
	 * Methods
	 * ---------------------
	 */

    /**
     * Default Constructor.
     * Does nothing extra.
     */
    public ModuleParams() {
    }

    /**
     * Copy-constructor.
     *
     * @param poModuleParams object to make a copy of
     * @since 0.3.0.5
     */
    public ModuleParams(final ModuleParams poModuleParams) {
        this.oPreprocessingParams = (Vector) poModuleParams.getPreprocessingParams().clone();
        this.oFeatureExtractionParams = (Vector) poModuleParams.getFeatureExtractionParams().clone();
        this.oClassificationParams = (Vector) poModuleParams.getClassificationParams().clone();
    }

    /**
     * Overwrites any possible previous value for a given module with a new one.
     *
     * @param poParams     Vector of paramaters
     * @param piModuleType the module these parameters are for
     * @throws IllegalArgumentException if the parameters vector is null or module type is invalid
     */
    private synchronized final void setParams(Vector poParams, final int piModuleType) {
        if (poParams == null) {
            throw new IllegalArgumentException("Parameters vector cannot be null.");
        }

        switch (piModuleType) {
            case PREPROCESSING:
                this.oPreprocessingParams = poParams;
                break;

            case FEATURE_EXTRACTION:
                this.oFeatureExtractionParams = poParams;
                break;

            case CLASSIFICATION:
                this.oClassificationParams = poParams;
                break;

            default:
                throw new IllegalArgumentException("Unknown module type: " + piModuleType + ".");
        }
    }

    /**
     * Appends params vector to whatever there possibly was.
     *
     * @param poParams     Vector of paramaters
     * @param piModuleType the module these parameters are for
     * @throws IllegalArgumentException if module type is invalid
     */
    private synchronized final void addParams(Vector poParams, final int piModuleType) {
        switch (piModuleType) {
            case PREPROCESSING:
                this.oPreprocessingParams.addAll(poParams);
                break;

            case FEATURE_EXTRACTION:
                this.oFeatureExtractionParams.addAll(poParams);
                break;

            case CLASSIFICATION:
                this.oClassificationParams.addAll(poParams);
                break;

            default:
                throw new IllegalArgumentException("Unknown module type: " + piModuleType + ".");
        }
    }

    /**
     * Adds a single object to the corresponding Vector.
     *
     * @param oParam       Parameter object
     * @param piModuleType the module this parameter is for
     * @throws IllegalArgumentException if module type is invalid
     */
    private synchronized final void addParam(Object oParam, final int piModuleType) {
        switch (piModuleType) {
            case PREPROCESSING:
                this.oPreprocessingParams.add(oParam);
                break;

            case FEATURE_EXTRACTION:
                this.oFeatureExtractionParams.add(oParam);
                break;

            case CLASSIFICATION:
                this.oClassificationParams.add(oParam);
                break;

            default:
                throw new IllegalArgumentException("Unknown module type: " + piModuleType + ".");
        }
    }

    /**
     * Returns for a given module it's parameters vector.
     *
     * @param piModuleType module type to get parameters for
     * @return Vector of paramaters
     * @throws IllegalArgumentException if module type is invalid
     */
    private synchronized final Vector getParams(final int piModuleType) {
        switch (piModuleType) {
            case PREPROCESSING:
                return this.oPreprocessingParams;

            case FEATURE_EXTRACTION:
                return this.oFeatureExtractionParams;

            case CLASSIFICATION:
                return this.oClassificationParams;

            default:
                throw new IllegalArgumentException("Unknown module type: " + piModuleType + ".");
        }
    }

    /**
     * Retrieves Preprocessing module's parameters.
     *
     * @return preprocessing parameters vector
     */
    public synchronized final Vector getPreprocessingParams() {
        return getParams(PREPROCESSING);
    }

    /**
     * Sets preprocessing parameters vector.
     *
     * @param poParams parameters vector
     */
    public synchronized final void setPreprocessingParams(Vector poParams) {
        setParams(poParams, PREPROCESSING);
    }

    /**
     * Adds (appends) preprocessing parameters vector.
     *
     * @param poParams parameters vector to append
     */
    public synchronized final void addPreprocessingParams(Vector poParams) {
        addParams(poParams, PREPROCESSING);
    }

    /**
     * Adds (appends) a single preprocessing parameter object.
     *
     * @param poParam object to append
     */
    public synchronized final void addPreprocessingParam(Object poParam) {
        addParam(poParam, PREPROCESSING);
    }

    /**
     * Retrieves Feature Extraction module's parameters.
     *
     * @return feature extraction parameters vector
     */
    public synchronized final Vector getFeatureExtractionParams() {
        return getParams(FEATURE_EXTRACTION);
    }

    /**
     * Sets feature extraction parameters vector.
     *
     * @param poParams parameters vector
     */
    public synchronized final void setFeatureExtractionParams(Vector poParams) {
        setParams(poParams, FEATURE_EXTRACTION);
    }

    /**
     * Adds (appends) feature extraction parameters vector.
     *
     * @param poParams parameters vector to append
     */
    public synchronized final void addFeatureExtractionParams(Vector poParams) {
        addParams(poParams, FEATURE_EXTRACTION);
    }

    /**
     * Adds (appends) a single feature extraction parameter object.
     *
     * @param poParam object to append
     */
    public synchronized final void addFeatureExtractionParam(Object poParam) {
        addParam(poParam, FEATURE_EXTRACTION);
    }

    /**
     * Retrieves Classification module's parameters.
     *
     * @return classification parameters vector
     */
    public synchronized final Vector getClassificationParams() {
        return getParams(CLASSIFICATION);
    }

    /**
     * Sets classification parameters vector.
     *
     * @param poParams parameters vector
     */
    public synchronized final void setClassificationParams(Vector poParams) {
        setParams(poParams, CLASSIFICATION);
    }

    /**
     * Adds (appends) classification parameters vector.
     *
     * @param poParams parameters vector to append
     */
    public synchronized final void addClassificationParams(Vector poParams) {
        addParams(poParams, CLASSIFICATION);
    }

    /**
     * Adds (appends) a single classification parameter object.
     *
     * @param poParam object to append
     */
    public synchronized final void addClassificationParam(Object poParam) {
        addParam(poParam, CLASSIFICATION);
    }

    /**
     * Retrieves string version of all three types of parameters.
     *
     * @return String representation of this ModuleParams object
     * @see java.lang.Object#toString()
     * @since 0.3.0.5
     */
    public synchronized String toString() {
        StringBuffer oBuffer = new StringBuffer();

        oBuffer
                .append("Preprocessing params: ").append(this.oPreprocessingParams).append("\n")
                .append("Feature extraction params: ").append(this.oFeatureExtractionParams).append("\n")
                .append("Classification params: ").append(this.oClassificationParams);

        return oBuffer.toString();
    }

    /**
     * Returns the size of this set of module parameters as a sum
     * of sizes of preprocessing, feature extraction, and classification
     * module parameters.
     *
     * @return the total number of parameters
     * @since 0.3.0.5
     */
    public synchronized int size() {
        return
                this.oPreprocessingParams.size() +
                        this.oFeatureExtractionParams.size() +
                        this.oClassificationParams.size();
    }

    /**
     * Implements Cloneable interface for ModuleParams.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new ModuleParams(this);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.17 $";
    }
}

// EOF
