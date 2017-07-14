package marf.nlp.Parsing;

import java.util.Stack;
import java.util.Vector;


/**
 * <p>Semantic Analyzer.
 * TODO: complete implementation.
 * </p>
 * <p>
 * $Id: SemanticAnalyzer.java,v 1.7 2006/01/08 01:16:49 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.7 $
 * @since 0.3.0.2
 */
public class SemanticAnalyzer {
    /**
     * A collection of semantic errors found.
     */
    protected Vector oSematicErrors = new Vector();

    /**
     * Semantic stack for semantic checks.
     */
    protected Stack oSematicStack = new Stack();

    /**
     * Internal reference to the symbol table for
     * type and definition checks.
     */
    public SymbolTable oSymTab = null;

    /**
     * Not implemented. Checks whether a given identifier was defined.
     *
     * @param pstrScopeSymbol
     * @param pstrSymbolToCheck
     * @return <code>false</code>
     */
    public boolean isDefinedID(String pstrScopeSymbol, String pstrSymbolToCheck) {
        return false;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.7 $";
    }
}

// EOF
