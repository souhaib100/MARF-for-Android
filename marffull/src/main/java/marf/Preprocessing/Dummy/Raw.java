package marf.Preprocessing.Dummy;

import marf.Preprocessing.IPreprocessing;
import marf.Preprocessing.PreprocessingException;
import marf.Storage.Sample;
import marf.util.Debug;


/**
 * <p>Implements raw preprocessing module for testing purposes
 * that does <b>not</b> do any preprocessing.</p>
 * <p>
 * <p>$Id: Raw.java,v 1.13 2005/12/31 23:17:37 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.13 $
 * @since 0.3.0.2
 */
public class Raw
        extends Dummy {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 6915809043983583380L;

    /**
     * Default constructor for reflective creation of Preprocessing
     * clones. Typically should not be used unless really necessary
     * for the frameworked modules.
     *
     * @since 0.3.0.5
     */
    public Raw() {
        super();
    }

    /**
     * Implementation of the preprocessing pipeline.
     *
     * @param poPreprocessing successor preprocessing module
     * @throws PreprocessingException
     */
    public Raw(IPreprocessing poPreprocessing)
            throws PreprocessingException {
        super(poPreprocessing);
        //Debug.debug("Raw constructed with preprocessing [" + poPreprocessing + "].");
    }

    /**
     * Raw Constructor.
     *
     * @param poSample incoming sample
     * @throws PreprocessingException
     */
    public Raw(Sample poSample)
            throws PreprocessingException {
        super(poSample);
        //Debug.debug("Raw constructed with sample [" + poSample + "].");
    }

    /**
     * Raw implementation of <code>preprocess()</code> for testing.
     * Does not do any preprocessing.
     *
     * @return <code>true</code>
     * @throws PreprocessingException never thrown
     */
    public boolean preprocess()
            throws PreprocessingException {
        Debug.debug("Raw preprocess() done.");
        return true;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.13 $";
    }
}

// EOF
