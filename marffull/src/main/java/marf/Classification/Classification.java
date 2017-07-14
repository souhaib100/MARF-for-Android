package marf.Classification;

import java.util.Vector;

import marf.FeatureExtraction.IFeatureExtraction;
import marf.MARF;
import marf.Storage.ResultSet;
import marf.Storage.StorageException;
import marf.Storage.StorageManager;
import marf.Storage.TrainingSet;
import marf.util.Debug;


/**
 * <p>Abstract Classification Module.
 * A generic implementation of the IClassification interface.
 * The derivatives must inherit from this module, and if they cannot,
 * they should implement IClassification themselves.
 * </p>
 * <p>
 * <p>$Id: Classification.java,v 1.41 2006/01/02 22:24:00 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.41 $
 * @since 0.0.1
 */
public abstract class Classification
        extends StorageManager
        implements IClassification {
    /* Data Members */

    /**
     * Reference to the enclosed FeatureExtraction object.
     */
    protected IFeatureExtraction oFeatureExtraction = null;

    /**
     * TrainingSet Container.
     */
    protected TrainingSet oTrainingSet = null;

    /**
     * Classification result set. May contain
     * one or more results (in case of similarity).
     *
     * @since 0.3.0.2
     */
    protected ResultSet oResultSet = new ResultSet();

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = 7933249658173204609L;

	/* Constructors */

    /**
     * Generic Classification Constructor.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    protected Classification(IFeatureExtraction poFeatureExtraction) {
        // TODO: null validation?
        this.oFeatureExtraction = poFeatureExtraction;

        // See if there is a request for dump format
        if (MARF.getModuleParams() != null) {
            Vector oParams = MARF.getModuleParams().getClassificationParams();

            // TODO: Must be validated of what's coming in
            if (oParams != null && oParams.size() > 0) {
                this.iCurrentDumpMode = ((Integer) oParams.elementAt(0)).intValue();
            }
        }
    }

	/* Classification API */

    /**
     * Generic training routine for building/updating
     * mean vectors in the training set.
     * Can be overridden, and if the overriding classifier is using
     * <code>TrainingSet</code>, it should call <code>super.train();</code>
     *
     * @return <code>true</code> if training was successful
     * (i.e. mean vector was updated); <code>false</code> otherwise
     * @throws ClassificationException if there was a problem while training
     * @see TrainingSet
     */
    public boolean train()
            throws ClassificationException {
        // For exception handling
        String strPhase = "[start]";

		/*
         * It is important to use saveTrainingSet() and loadTrainingSet()
		 * throughout this method, as the dump() and restore() may easily
		 * (and likely) to be overridden by the derivatives.
		 */
        try {
            if (this.oTrainingSet != null) {
                // Wrong global cluster loaded, reload the correct one.
                if
                        (
                        (this.oTrainingSet.getPreprocessingMethod() != MARF.getPreprocessingMethod())
                                ||
                                (this.oTrainingSet.getFeatureExtractionMethod() != MARF.getFeatureExtractionMethod())
                        ) {
                    strPhase = "[dumping previous cluster]";

                    saveTrainingSet();
                    this.oTrainingSet = null;
                }
            }

            strPhase = "[restoring training set]";
            loadTrainingSet();

            // Add the new feature vector.
            strPhase = "[adding feature vector]";

            boolean bVectorAdded = this.oTrainingSet.addFeatureVector
                    (
                            this.oFeatureExtraction.getFeaturesArray(),
                            MARF.getSampleFile(),
                            MARF.getCurrentSubject(),
                            MARF.getPreprocessingMethod(),
                            MARF.getFeatureExtractionMethod()
                    );

            // No point of doing I/O if we didn't add anything.
            if (bVectorAdded) {
                strPhase = "[dumping updated training set]";
                saveTrainingSet();
            }

            return true;
        } catch (NullPointerException e) {
            throw new ClassificationException
                    (
                            new StringBuffer()
                                    .append("NullPointerException in Classification.train(): oTrainingSet = ")
                                    .append(this.oTrainingSet)
                                    .append(", oFeatureExtraction = ").append(this.oFeatureExtraction)
                                    .append(", FeaturesArray = ").append(this.oFeatureExtraction.getFeaturesArray())
                                    .append(", phase: ").append(strPhase).toString()
                    );
        } catch (Exception e) {
            throw new ClassificationException("Phase: " + strPhase, e);
        }
    }

	/* From Storage Manager */

    /**
     * Generic implementation of dump() to dump the TrainingSet.
     *
     * @throws StorageException if there's a problem saving training set to disk
     * @since 0.2.0
     */
    public void dump()
            throws StorageException {
        saveTrainingSet();
    }

    /**
     * Generic implementation of restore() for TrainingSet.
     *
     * @throws StorageException if there is a problem loading the training set from disk
     * @since 0.2.0
     */
    public void restore()
            throws StorageException {
        loadTrainingSet();
    }

    /**
     * Saves TrainingSet to a file. Called by <code>dump()</code>.
     *
     * @throws StorageException if there's a problem saving training set to disk
     * @see #dump()
     * @see TrainingSet
     * @since 0.2.0
     */
    private final void saveTrainingSet()
            throws StorageException {
        try {
            // Dump stuff is there's anything to dump
            if (this.oTrainingSet != null) {
                this.oTrainingSet.setDumpMode(this.iCurrentDumpMode);
                this.oTrainingSet.setFilename(getTrainingSetFilename());
                this.oTrainingSet.dump();
            }

            // TODO: if TrainingSet is null
            else {
                // [SM, 2003-05-02] Should here be something? Like a debug() call or
                // more severe things?
                Debug.debug
                        (
                                "WARNING: Classification.saveTrainingSet() -- TrainingSet is null.\n" +
                                        "         No TrainingSet is saved."
                        );
            }
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Loads TrainingSet from a file. Called by <code>restore()</code>.
     *
     * @throws StorageException if there is a problem loading the training set from disk
     * @since 0.2.0
     */
    private final void loadTrainingSet()
            throws StorageException {
        try {
            if (this.oTrainingSet == null) {
                this.oTrainingSet = new TrainingSet();
                this.oTrainingSet.setDumpMode(this.iCurrentDumpMode);
                this.oTrainingSet.setFilename(getTrainingSetFilename());
                this.oTrainingSet.restore();
            }

            //TODO: if TrainingSet is not null
            else {
                // [SM, 2003-05-02] Should here be something? Like a debug() call or
                // more severe things?
                Debug.debug
                        (
                                "WARNING: Classification.loadTrainingSet() -- TrainingSet is not null.\n" +
                                        "         No TrainingSet is loaded."
                        );
            }
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    /**
     * Retrieves the enclosed result set.
     *
     * @return the enclosed ResultSet object
     * @since 0.3.0.2
     */
    public ResultSet getResultSet() {
        return this.oResultSet;
    }

    /**
     * Constructs a global cluster file name for the TrainingSet.
     * <p>
     * <p>Filename is constructed using fully-qualified class of
     * either TrainingSet or a classifier name with global
     * clustering info such as preprocessing and feature
     * extraction methods, so that ony that cluster can be reloaded
     * after.</p>
     * <p>
     * May be overridden by the drivatives when necessary.
     *
     * @return String, filename
     * @since 0.2.0
     */
    protected String getTrainingSetFilename() {
        return
                // Fully-qualified class name
                this.oTrainingSet.getClass().getName() + "." +

                        // Global cluster: <PR>.<FE>.<FVS>
                        // For the same FE method we may have different feature vector sizes
                        MARF.getPreprocessingMethod() + "." +
                        MARF.getFeatureExtractionMethod() + "." +
                        this.oFeatureExtraction.getFeaturesArray().length + "." +

                        // Extension depending on the dump type
                        getDefaultExtension();
    }

    /**
     * Retrieves the features source.
     *
     * @return returns the FeatureExtraction reference
     * @since 0.3.0.4
     */
    public IFeatureExtraction getFeatureExtraction() {
        return this.oFeatureExtraction;
    }

    /**
     * Allows setting the features surce.
     *
     * @param poFeatureExtraction the FeatureExtraction object to set
     * @since 0.3.0.4
     */
    public void setFeatureExtraction(IFeatureExtraction poFeatureExtraction) {
        this.oFeatureExtraction = poFeatureExtraction;
    }

    /**
     * Implementes Cloneable interface for the Classification object.
     * The contained FeatureExtraction isn't cloned at this point,
     * and is just assigned to the clone.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        Classification oClone = (Classification) super.clone();
        oClone.oResultSet = (ResultSet) this.oResultSet.clone();
        oClone.oTrainingSet = (TrainingSet) this.oTrainingSet.clone();
        oClone.oFeatureExtraction = this.oFeatureExtraction;
        return oClone;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.41 $";
    }
}

// EOF
