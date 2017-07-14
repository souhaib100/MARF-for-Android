/*
 * SymTabEntry Class
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing;

import net.windward.android.awt.Point;

import java.util.Vector;


/**
 * <p>This class denotes one entry per user-defined symbol
 * in the Symbol Table.
 * </p>
 * <p>
 * $Id: SymTabEntry.java,v 1.11 2006/01/19 04:13:16 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.11 $
 * @since 0.3.0.2
 */
public class SymTabEntry {
    /**
     * Variable entry type.
     */
    public static final int VARIABLE = 0;

    /**
     * Function entry type.
     */
    public static final int FUNCTION = 1;

    /**
     * Class Entry Type.
     */
    public static final int CLASS = 2;

    /**
     * Numeric ID of the entry.
     * Not really used.
     *
     * @since September 2001
     */
    protected int iID;

    /**
     * A flag indicating that the symbol was (true)
     * or was not (false) resolved.
     *
     * @since September 2001
     */
    protected boolean bDeclared = false;

    /**
     * A flag indicating that the symbol was (true)
     * or was not (false) reserved.
     *
     * @since September 2001
     */
    protected boolean bReserved = false;

    /**
     * A flag indicating that the symbol was (true)
     * or was not (false) multiply defined.
     *
     * @since December 21, 2001
     */
    protected boolean bMultilplyDefined = false;

    /**
     * Entry symbol, encapsulated as Token.
     */
    protected Token oUID = null;

    /**
     * Data type of this symbol (e.g. integer, real,
     * class).
     *
     * @since September 2001
     */
    protected SymDataType oDataType = null;

    /**
     * List of locations where the given ID
     * appears in the source code.
     *
     * @since September 2001
     */
    protected Vector oLocationsList = null;

    /**
     * Locations in generated code
     * (point, x - base, y - offset).
     */
    protected Vector oGenCodeLocations = null;

    /**
     * Local scope symbol table (if applicable).
     */
    protected SymbolTable oLocalSymTab = null;

    /**
     * Current Entry type.
     */
    public int iEntryType = VARIABLE;

    /**
     * Name of the symbol table entry, must be unique.
     */
    protected String strName;

    /**
     * Default Constructor.
     */
    public SymTabEntry() {
    }

    /**
     * Constructs object with the name of the symbol table entry.
     *
     * @param pstrName name of the scope
     */
    public SymTabEntry(String pstrName) {
        this.strName = pstrName;
    }

    /**
     * Constructs object with the entry symbol, encapsulated as Token.
     *
     * @param poUID entry symbol, encapsulated as Token
     */
    public SymTabEntry(Token poUID) {
        this.oUID = poUID;
        this.oLocationsList = new Vector();
    }

    /**
     * Constructs object with the entry symbol, encapsulated as Token,
     * and a flag to indicate whether the symbol was resolved.
     *
     * @param poUID      entry symbol, encapsulated as Token
     * @param pbDeclared a flag indicating whether the symbol was resolved.
     */
    public SymTabEntry(Token poUID, boolean pbDeclared) {
        this(poUID);
        this.bDeclared = pbDeclared;
    }

    /**
     * Constructs object with the entry symbol, encapsulated as Token,
     * and entry symbol's data type; set the symbol as resolved.
     *
     * @param poUID         entry symbol, encapsulated as Token
     * @param poSymDataType data type of the symbol.
     */
    public SymTabEntry(Token poUID, SymDataType poSymDataType) {
        this.bDeclared = true;
        this.oUID = poUID;
        this.oDataType = poSymDataType;
        this.oLocationsList = new Vector();
    }

    /**
     * Allows querying current data type of the entry.
     *
     * @return the data type of the current symbol.
     */
    public SymDataType getDataType() {
        return this.oDataType;
    }

    /**
     * Adds location to every occurrence
     * of a token in the source code.
     *
     * @param poPosition position (line and column) of the symbol's occurence
     */
    public void addLocation(Point poPosition) {
        if (this.oLocationsList.contains(poPosition) == false) {
            this.oLocationsList.addElement(poPosition);
        }
    }

    /**
     * Get list of occurrences of this token
     * entry.
     *
     * @return the locations list
     */
    public Vector getLocationsList() {
        return this.oLocationsList;
    }

    /**
     * Returns UID.
     *
     * @return Entry symbol, encapsulated as Token
     */
    public Token getUID() {
        return this.oUID;
    }

    /**
     * Sets UID.
     *
     * @param poToken entry symbol encapsulated as Token
     */
    public void setUID(Token poToken) {
        this.oUID = poToken;
    }

    /**
     * Determines if the resolved property is true.
     *
     * @return <code>true<code> if the Resolved property is true
     */
    public boolean isDeclared() {
        return this.bDeclared;
    }

    /**
     * Sets the value of the declared property.
     *
     * @param pbDeclared the new value of the Declared property
     */
    public void setDeclared(boolean pbDeclared) {
        this.bDeclared = pbDeclared;
    }

    /**
     * Determines if the MultiplyDefined property is true.
     *
     * @return <code>true<code> if the MultilplyDefined property is true
     */
    public boolean isMultiplyDefined() {
        return this.bMultilplyDefined;
    }

    /**
     * Sets the value of the MultilplyDefined property.
     *
     * @param pbFlag the new value of the MultilplyDefined property
     */
    public void setMultiplyDefined(boolean pbFlag) {
        this.bMultilplyDefined = pbFlag;
    }

    /**
     * Sets the value of the MultilplyDefined property as true.
     */
    public void setMultiplyDefined() {
        this.setMultiplyDefined(true);
    }

    /**
     * Allows querying for the local scope symbol table (if applicable).
     *
     * @return the local scope symbol table.
     */
    public SymbolTable getLocalSymTab() {
        return this.oLocalSymTab;
    }

    /**
     * Gets Numeric ID of the entry.
     *
     * @return numeric ID of the entry
     */
    public int getID() {
        return this.iID;
    }

    /**
     * Sets numeric ID of the entry.
     *
     * @param piID the new value of the symbol table entry's ID
     */
    public void setID(int piID) {
        this.iID = piID;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.11 $";
    }
}

// EOF
