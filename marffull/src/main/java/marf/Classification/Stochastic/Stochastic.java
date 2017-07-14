package marf.Classification.Stochastic;

import marf.Classification.Classification;
import marf.Classification.ClassificationException;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.Storage.Result;
import marf.util.NotImplementedException;


/**
 * <p>Generic Stochastic Classification Module.</p>
 * <p>TODO: partially implemented.</p>
 * <p>
 * <p>$Id: Stochastic.java,v 1.24 2006/02/12 23:57:58 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.24 $
 * @since 0.0.1
 */
public class Stochastic
        extends Classification {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -415255678695729045L;

    /**
     * Stochastic Constructor.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    public Stochastic(IFeatureExtraction poFeatureExtraction) {
        super(poFeatureExtraction);
    }

    /**
     * Not Implemented.
     *
     * @return nothing
     * @throws NotImplementedException
     * @throws ClassificationException never thrown
     */
    public boolean classify()
            throws ClassificationException {
        throw new NotImplementedException(this, "classify()");
    }

    /**
     * Not Implemented.
     *
     * @return nothing
     * @throws NotImplementedException
     * @throws ClassificationException never thrown
     */
    public boolean train()
            throws ClassificationException {
        throw new NotImplementedException(this, "train()");
    }

    /**
     * Retrieves the maximum-probability classification result.
     *
     * @return Result object
     * @since 0.3.0.2
     */
    public Result getResult() {
        return this.oResultSet.getMaximumResult();
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.24 $";
    }
}

// EOF
