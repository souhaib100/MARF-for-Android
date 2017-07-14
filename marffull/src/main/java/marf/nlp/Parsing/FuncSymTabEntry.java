package marf.nlp.Parsing;

import java.util.Vector;


/**
 * <p>FuncSymTabEntry represents a symbol table entry
 * for function definition.
 * </p>
 * <p>
 * $Id: FuncSymTabEntry.java,v 1.10 2005/12/30 18:36:54 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.10 $
 * @since 0.3.0.2
 */
public class FuncSymTabEntry
        extends SymTabEntry {
    /**
     * Return Value Type.
     */
    protected SymDataType oRetValType = null;

    /**
     * Parameter List.
     * Parameters are VarSymTabEntries.
     */
    protected Vector oParams = null;

    /**
     * Constructor.
     */
    public FuncSymTabEntry() {
        this.oParams = new Vector();
    }

    /**
     * Add a new parameter.
     *
     * @param poParam a parameter to add
     */
    public void addParam(VarSymTabEntry poParam) {
        this.oParams.addElement(poParam);
    }

    /**
     * Allows retrieval of parameter list as Vector.
     *
     * @return a parameter list
     */
    public Vector getParams() {
        return this.oParams;
    }

    /**
     * Cannot be anything other than FUNCTION.
     *
     * @return SymDataType correspoinding to the FUNCTION identifier.
     * @see #FUNCTION
     */
    public SymDataType getDataType() {
        return new SymDataType(FUNCTION);
    }

    /**
     * Allows setting the return data type.
     *
     * @param poSymDataType type of the return value
     */
    public void setRetValDataType(SymDataType poSymDataType) {
        this.oRetValType = poSymDataType;
    }

    /**
     * Allows getting the datatype of the function return value.
     *
     * @return SymDataType corresponding to the return value
     * @see SymDataType
     */
    public SymDataType getRetValDataType() {
        return this.oRetValType;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.10 $";
    }
}

// EOF
