package marf.Preprocessing.Dummy;

import marf.Preprocessing.IPreprocessing;
import marf.Preprocessing.Preprocessing;
import marf.Preprocessing.PreprocessingException;
import marf.Storage.Sample;
import marf.util.Debug;


/**
 * <p>Implements dummy preprocessing module for testing purposes
 * that does only normalization.</p>
 * <p>
 * <p>$Id: Dummy.java,v 1.25 2005/12/31 23:17:37 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.25 $
 * @since 0.0.1
 */
public class Dummy
        extends Preprocessing {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -8360158324170431628L;

    /**
     * Default constructor for reflective creation of Preprocessing
     * clones. Typically should not be used unless really necessary
     * for the frameworked modules.
     *
     * @since 0.3.0.5
     */
    public Dummy() {
        super();
    }

    /**
     * Implementation of the preprocessing pipeline.
     *
     * @param poPreprocessing successor preprocessing module
     * @throws PreprocessingException
     * @since 0.3.0.3
     */
    public Dummy(IPreprocessing poPreprocessing)
            throws PreprocessingException {
        super(poPreprocessing);
    }

    /**
     * Dummy Constructor.
     *
     * @param poSample incomping sample
     * @throws PreprocessingException
     */
    public Dummy(Sample poSample)
            throws PreprocessingException {
        super(poSample);
    }

    /**
     * Dummy implementation of <code>preprocess()</code> for testing.
     *
     * @return <code>true</code>
     * @throws PreprocessingException never thrown
     */
    public boolean preprocess()
            throws PreprocessingException {
        return normalize();
    }

    /**
     * Dummy implementation of <code>removeNoise()</code> for testing.
     *
     * @return <code>false</code>
     * @throws PreprocessingException never thrown
     */
    public final boolean removeNoise()
            throws PreprocessingException {
        Debug.debug(this.getClass().getName() + ".removeNoise()");
        return false;
    }

    /**
     * Dummy implementation of <code>removeSilence()</code> for testing.
     *
     * @return <code>false</code>
     * @throws PreprocessingException never thrown
     */
    public final boolean removeSilence()
            throws PreprocessingException {
        Debug.debug(this.getClass().getName() + ".removeSilence()");
        return false;
    }

    /**
     * Dummy implementation of <code>cropAudio()</code> for testing.
     *
     * @param pdStartingFrequency unused
     * @param pdEndFrequency      unused
     * @return <code>false</code>
     * @throws PreprocessingException never thrown
     */
    public final boolean cropAudio(double pdStartingFrequency, double pdEndFrequency)
            throws PreprocessingException {
        Debug.debug(this.getClass().getName() + ".cropAudio()");
        return false;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.25 $";
    }
}

// EOF
