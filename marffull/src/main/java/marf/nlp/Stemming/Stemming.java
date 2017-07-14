package marf.nlp.Stemming;

/**
 * <p>General Stemmer. Must be subclassed by a language-specific stemmers.
 * If they can't, they must implement IStemming.
 * </p>
 * <p>
 * <p>TODO: implement.</p>
 * <p>
 * $Id: Stemming.java,v 1.12 2006/01/06 22:20:13 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.12 $
 * @since 0.3.0.2
 */
public abstract class Stemming
        implements IStemming {
    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.12 $";
    }
}

// EOF
