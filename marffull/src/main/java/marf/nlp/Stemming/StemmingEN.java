package marf.nlp.Stemming;

import marf.util.NotImplementedException;

/**
 * <p>English Language Stemmer.
 * TODO: implement.</p>
 * <p>
 * $Id: StemmingEN.java,v 1.11 2006/01/06 22:20:13 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.11 $
 * @since 0.3.0.2
 */
public class StemmingEN
        extends Stemming {
    /**
     * Not implemented.
     *
     * @return nothing
     */
    public final int stem() {
        throw new NotImplementedException();
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.11 $";
    }
}

// EOF
