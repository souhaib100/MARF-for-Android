package marf.Classification;

import marf.FeatureExtraction.IFeatureExtraction;
import marf.Storage.Result;
import marf.Storage.ResultSet;


/**
 * <p>Classification Interface.</p>
 * <p>
 * <p>$Id: IClassification.java,v 1.4 2005/08/05 22:19:53 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.4 $
 * @since 0.3.0.3
 */
public interface IClassification {
    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.4 $";

	/* Classification API */

    /**
     * Generic classification routine.
     *
     * @return <code>true</code> if classification was successful; <code>false</code> otherwise
     * @throws ClassificationException if there was an error while classifying
     */
    boolean classify()
            throws ClassificationException;

    /**
     * Generic training routine for building/updating
     * mean vectors in the training set.
     *
     * @return <code>true</code> if training was successful; <code>false</code> otherwise
     * @throws ClassificationException if there was a problem while training
     */
    boolean train()
            throws ClassificationException;

    /**
     * Retrieves the likely classification result.
     * If there were many, this will return the result with the
     * highest statistical score or probability. The decision
     * of whether to retrieve a maximum result (with maximum probability)
     * or minimum result (with minimum distance) from the sample is
     * left to be made by concrete implementations.
     *
     * @return Result object
     */
    Result getResult();

    /**
     * Retrieves the enclosed result set.
     *
     * @return the enclosed ResultSet object
     */
    ResultSet getResultSet();

    /**
     * Retrieves the features source.
     *
     * @return returns the FeatureExtraction reference
     * @since 0.3.0.4
     */
    IFeatureExtraction getFeatureExtraction();

    /**
     * Allows setting the features surce.
     *
     * @param poFeatureExtraction the FeatureExtraction object to set
     * @since 0.3.0.4
     */
    void setFeatureExtraction(IFeatureExtraction poFeatureExtraction);
}

// EOF
