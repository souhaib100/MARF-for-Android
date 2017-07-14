package marf.nlp.Stemming;

/**
 * <p>General Stemming Interface.</p>
 * <p>
 * $Id: IStemming.java,v 1.2 2006/01/06 22:20:13 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.2 $
 * @since 0.3.0.3
 */
public interface IStemming {
    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.2 $";

    /**
     * General stem method the derivatives must implement.
     *
     * @return number of stems produced
     */
    int stem();
}

// EOF
