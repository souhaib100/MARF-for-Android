package marf.Classification.Markov;

import marf.Classification.ClassificationException;
import marf.Classification.Stochastic.Stochastic;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.Storage.StorageException;
import marf.util.NotImplementedException;


/**
 * Hidden Markov Models-based Classification Module.
 * <p>
 * <p>Not Implemented.</p>
 * <p>
 * <p>$Id: Markov.java,v 1.23 2005/08/14 01:15:55 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.23 $
 * @since 0.0.1
 */
public class Markov
        extends Stochastic {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 4931363558439103262L;

    /**
     * Markov Constructor.
     *
     * @param poFeatureExtraction FeatureExtraction module reference
     */
    public Markov(IFeatureExtraction poFeatureExtraction) {
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
        throw new NotImplementedException("Markov.classify()");
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
        throw new NotImplementedException("Markov.train()");
    }

	/* From Storage Manager */

    /**
     * Not Implemented.
     *
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public void dump()
            throws StorageException {
        throw new NotImplementedException("Markov.dump()");
    }

    /**
     * Not Implemented.
     *
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public void restore()
            throws StorageException {
        throw new NotImplementedException("Markov.restore()");
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.23 $";
    }
}

// EOF
