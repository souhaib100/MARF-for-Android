package marf.nlp.Parsing;


/**
 * <p>ClassSymTabEntry represents an entry in the symbol
 * table corresponding to a class definition.</p>
 * <p>
 * $Id: ClassSymTabEntry.java,v 1.8 2005/12/30 18:36:54 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.8 $
 * @since 0.3.0.2
 */
public class ClassSymTabEntry
        extends SymTabEntry {
    /**
     * Default Constructor.
     */
    public ClassSymTabEntry() {
    }

    /**
     * Can return only CLASS.
     *
     * @return SymDataType of CLASS
     */
    public SymDataType getDataType() {
        return new SymDataType(CLASS);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.8 $";
    }
}

// EOF
