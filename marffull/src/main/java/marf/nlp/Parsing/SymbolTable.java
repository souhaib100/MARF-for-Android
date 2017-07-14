/*
 * SymbolTable Class
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing;

import java.util.Enumeration;
import java.util.Hashtable;

import marf.util.NotImplementedException;


/**
 * <p>Symbol Table data structure.
 * TODO: complete implementation.
 * </p>
 * <p>
 * $Id: SymbolTable.java,v 1.15 2006/01/16 21:43:44 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.15 $
 * @since 0.3.0.2
 */
public class SymbolTable {
    /**
     * This is a container for SymTabEnries.
     */
    private Hashtable oSymTabEntries = null;

    /**
     * Global Index 'pointing' to all SymbolTables
     * to speedup search.
     */
    protected Hashtable oSymTabIndex = new Hashtable();

    /**
     * Backward reference.
     */
    protected SymbolTable oParentSymTab = null;

    /**
     * Name of the table, must be unique.
     */
    protected String strName = "";

    /**
     * Default Constructor.
     */
    public SymbolTable() {
        this("_GLOBAL_");
    }

    /**
     * Accepts scope name.
     *
     * @param pstrName name of the scope
     */
    public SymbolTable(String pstrName) {
        this.strName = pstrName;
        this.oSymTabEntries = new Hashtable();
    }

    /**
     * Accepts scope name and the reference to the parent.
     *
     * @param pstrName       scope name
     * @param poParentSymTab parent symbol table reference
     */
    public SymbolTable(String pstrName, SymbolTable poParentSymTab) {
        this(pstrName);
        this.oParentSymTab = poParentSymTab;
    }

    /**
     * Adds a symbol token to the table. If the symbol is
     * already there, its additional location is recorded.
     * Else a new entry is created.
     *
     * @param poToken symbol token to add
     * @return 0 on success
     */
    public int addSymbol(Token poToken) {
        if (this.oSymTabEntries.contains(poToken.getLexeme())) {
            SymTabEntry oSymTabEntry = (SymTabEntry) oSymTabEntries.get(poToken.getLexeme());
            oSymTabEntry.addLocation(poToken.getPosition());
        } else {
            SymTabEntry oSymTabEntry = new SymTabEntry(poToken);
            oSymTabEntry.addLocation(poToken.getPosition());
            this.oSymTabEntries.put(poToken.getLexeme(), oSymTabEntry);
        }

        return 0;
    }

    /**
     * Not implemented.
     * The same as above <code>addSymbol(Token)</code>, just has
     * an extra parameter for convenience
     * if on the time of insertion the symbol
     * is already defined.
     *
     * @param poToken
     * @param pbResolved
     * @return nothing
     * @throws NotImplementedException
     * @see #addSymbol(Token)
     */
    public int addSymbol(Token poToken, boolean pbResolved) {
        throw new NotImplementedException();
    }

    /**
     * Not implemented.
     * This method adds also a type along
     * with the token. This as well assumes
     * that the token is defined because
     * invokation o fthe method can happen
     * only during the declaration of a symbol
     * in the source code.
     *
     * @param oToken
     * @param piSymDataType
     * @return nothing
     * @throws NotImplementedException
     */
    public int addSymbol(Token oToken, int piSymDataType) {
        throw new NotImplementedException();
    }

    /**
     * Not implemented.
     *
     * @param piEntryType
     * @param poToken
     * @throws NotImplementedException
     */
    public void addEntry(int piEntryType, Token poToken) {
        throw new NotImplementedException();
    }

    /**
     * Not implemented.
     *
     * @param pstrScope
     * @param pstrName
     * @param pbDeclared
     * @throws NotImplementedException
     */
    public void updateEntry(String pstrScope, String pstrName, boolean pbDeclared) {
        throw new NotImplementedException();
    }

    /**
     * Creates child symbol table with a given name.
     *
     * @param pstrNewTableName child's symbol table name
     */
    public void create(String pstrNewTableName) {
        // Does it already exist?

        // Is it us?
        if (this.strName.equals(pstrNewTableName)) {
            System.err.println("SymbolTable::create() - ERROR: Same name [" + pstrNewTableName + "] as a parent table!");
            System.err.println("Possibly multiply-declared symbol in the source program.");
        }

        // Is it in Global index?
        // Possible conflict
        if (this.oSymTabIndex.containsKey(pstrNewTableName) == true) {
            System.err.println("SymbolTable::create() - WARNING: Same name [" + pstrNewTableName + "] was found in the global index.");
            System.err.println("Will change it to: " + pstrNewTableName + "_");
            pstrNewTableName += "_";
        }

        // Creating new
        SymbolTable oNewSymTab = new SymbolTable(pstrNewTableName, this);

        // Indexing
        this.oSymTabIndex.put(pstrNewTableName, oNewSymTab);
    }

    /**
     * Looks up an entry in either this table or in index
     * given the table name an the identifier. Equivalent
     * to <code>search(pstrTable, pstrID, false)</code>.
     *
     * @param pstrTable the table name
     * @param pstrID    the symbol identifier
     * @return the object entry
     * @see #search(String, String, boolean)
     */
    public Object search(String pstrTable, String pstrID) {
        return search(pstrTable, pstrID, false);
    }

    /**
     * Looks up a symbol table entry in either this table or in index.
     * Can create a reserved entry in the table if the flag is set and
     * the entry isn't there.
     *
     * @param pstrTable      the table name
     * @param pstrID         the symbol identifier
     * @param pbReserveEntry mark the symbol table entry reserved or not
     * @return symbol table entry object
     */
    public Object search(String pstrTable, String pstrID, boolean pbReserveEntry) {
        SymTabEntry oEntry = null;

        // Us first
        if (this.strName.equals(pstrID)) {
            if (this.oSymTabEntries.containsKey(pstrID) == true) {
                oEntry = (SymTabEntry) this.oSymTabEntries.get(pstrID);
                return oEntry;
            }
        }

        // Cascade down the tables
        Enumeration oEnum = this.oSymTabEntries.elements();

        while (oEnum.hasMoreElements() && oEntry == null) {
            oEntry = (SymTabEntry) ((SymTabEntry) oEnum.nextElement())
                    .getLocalSymTab()
                    .search(pstrTable, pstrID, pbReserveEntry);
        }

        if (pbReserveEntry == true && oEntry == null) {
            oEntry = new SymTabEntry(pstrID);
            oEntry.bReserved = true;
            this.oSymTabEntries.put(pstrID, oEntry);
        }

        return oEntry;
    }

    /**
     * Finds a reserved entry ginen table and ID and
     * marks it unreserved. This completes the insertion
     * (and definition) of the entry in the table.
     *
     * @param pstrTable symbol table name to search for
     * @param pstrID    identifier to search for
     * @return symbol table entry object if found; null otherwise
     */
    public Object insert(final String pstrTable, final String pstrID) {
        SymTabEntry oEntry = null;

        oEntry = (SymTabEntry) search(pstrTable, pstrID, true);

        if (oEntry.bReserved == true) {
            oEntry.bReserved = false;
            return oEntry;
        }

        return null;
    }

    /**
     * Not implemented.
     * TODO: implement.
     *
     * @param pstrTable table name to delete
     */
    public void delete(String pstrTable) {
        // It will die itself when time comes
    }

    /**
     * Serializes the named table into STDOUT.
     *
     * @param pstrTable the symbol table to dump
     */
    public void print(String pstrTable) {
        if (this.strName.equals(pstrTable)) {
            serialize(1);
        } else {
            if (this.oSymTabIndex.containsKey(pstrTable)) {
                ((SymbolTable) this.oSymTabIndex.get(pstrTable)).serialize(1);
            }
        }
    }

    /**
     * Allows querying for the contained entries.
     *
     * @return the hashtable with SymTab entries
     */
    public Hashtable getSymTabEntries() {
        return this.oSymTabEntries;
    }

    /**
     * Not implemented.
     * Returns a symbol table entry by
     * an entry ID. 'null' means there is
     * no entry with such an ID.
     *
     * @param piID
     * @return null
     */
    public SymTabEntry getSymTabEntry(int piID) {
        return null;
    }

    /**
     * This method returns the entry by
     * token's lexeme.
     *
     * @param pstrLexeme the lexeme of the entry to look up
     * @return SymTabEntry reference corresponding to the lexeme
     */
    public SymTabEntry getSymTabEntry(String pstrLexeme) {
        return (SymTabEntry) this.oSymTabEntries.get(pstrLexeme);
    }

    /**
     * Not implemented. By ID.
     *
     * @param piID
     * @param pbResolved
     * @return false
     */
    public boolean setResolved(int piID, boolean pbResolved) {
        return false;
    }

    /**
     * Marks a symbol table entry as resolved/declared (or not)
     * given lexeme.
     *
     * @param pstrLexeme the lexeme to look up
     * @param pbResolved flag to set whether the given entry is resolved or not
     * @return <code>true</code> if the entry was updated (read found and updated)
     */
    public boolean setResolved(String pstrLexeme, boolean pbResolved) {
        if (this.oSymTabEntries.containsKey(pstrLexeme)) {
            ((SymTabEntry) this.oSymTabEntries.get(pstrLexeme)).setDeclared(pbResolved);
            return true;
        }

        return false;
    }

    /**
     * Not implemented.
     *
     * @param piID
     * @param piType
     * @return false
     */
    public boolean setType(int piID, int piType) {
        return false;
    }

    /**
     * Not implemented.
     *
     * @param pstrLexeme
     * @param piType
     * @return false
     */
    public boolean setType(String pstrLexeme, int piType) {
        return false;
    }

    /**
     * Not implemented.
     *
     * @param piOpertation
     * @return false
     */
    public boolean serialize(int piOpertation) {
        return false;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.15 $";
    }
}

// EOF
