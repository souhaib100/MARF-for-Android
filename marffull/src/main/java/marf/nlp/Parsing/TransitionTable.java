package marf.nlp.Parsing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import marf.nlp.Parsing.GrammarCompiler.GrammarElement;
import marf.nlp.Parsing.GrammarCompiler.NonTerminal;
import marf.nlp.Parsing.GrammarCompiler.Rule;
import marf.nlp.Parsing.GrammarCompiler.Terminal;
import marf.util.Debug;


/**
 * <p>TransitionTable class
 * stores transition table for the main parser.
 * Can be serialized and deserialized as a binary
 * gzipped object.</p>
 * <p>
 * $Id: TransitionTable.java,v 1.14 2006/01/19 04:51:41 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.14 $
 * @since 0.3.0.2
 */
public class TransitionTable
        implements Serializable {
    /**
     * Transition Table (TT) is a Vector
     * of Vectors of Objects, such as Rules
     * and SyntaxErrors.
     *
     * @see Rule
     * @see SyntaxError
     */
    protected Vector oTT = null;

    /**
     * Vector of NonTerminals as "row-pointers"
     * to TT.
     */
    protected Vector oNonTerminals = null;

    /**
     * Vector of Terminals as "column-pointers"
     * to TT.
     */
    protected Vector oTerminals = null;

    /**
     *
     */
    protected int iEOFTerminalID = -1;

    /**
     *
     */
    protected int iStartNonTerminalID = -1;

    /**
     * Prepared file name of a file with the saved table
     * (if available).
     * Should not be serialized.
     */
    protected transient String strTableFile;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 7463126736067849797L;

    /**
     * Default Cosntructor.
     */
    public TransitionTable() {
        this("marf.table.dat");
        Debug.debug("TransitionTable::TransitionTable()");
    }

    /**
     * Constructor with params.
     *
     * @param pstrTableFileName
     */
    public TransitionTable(String pstrTableFileName) {
        this.strTableFile = pstrTableFileName;
        Debug.debug("TransitionTable::TransitionTable(" + pstrTableFileName + ")");
    }

    /**
     * This is a standard attempt to initilaize.
     * Must be called only by <code>GrammarCompiler</code> or when
     * dimensions of the table are well known.
     *
     * @param piRows rows number in the table
     * @param piCols columns number in the table
     * @return <code>true</code> if initialization was successful
     * @see marf.nlp.Parsing.GrammarCompiler.GrammarCompiler
     */
    public boolean init(int piRows, int piCols) {
        if (this.oTT == null) {
            this.oTT = new Vector(piRows);
            this.oTT.setSize(piRows);

            int r = -1;

            try {
                for (r = 0; r < piRows; r++) {
                    Vector oHorizontalVector = new Vector(piCols);
                    oHorizontalVector.setSize(piCols);
                    this.oTT.setElementAt(oHorizontalVector, r);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("TransitionTable::init() - index out of bounds");
                System.err.println("piRows=" + piRows + ", piCols=" + piCols + ", r=" + r);
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Saves current state of the TT (itself).
     *
     * @return <code>true</code> if serialization was successful
     */
    public boolean save() {
        try {
            // Save to file
            FileOutputStream oFOS = new FileOutputStream(this.strTableFile);

            // Compressed
            GZIPOutputStream oGZOS = new GZIPOutputStream(oFOS);

            // Save objects
            ObjectOutputStream oOOS = new ObjectOutputStream(oGZOS);

            oOOS.writeObject(this);
            oOOS.flush();
            oOOS.close();

            return true;
        } catch (IOException e) {
            System.err.println("TransitionTable::save() - WARNING: " + e.getMessage());
            e.printStackTrace(System.err);
            return false;
        }
    }

    /**
     * Serialize in text mode. Loading is not implemented.
     * TODO: migrate to MARF's dump/store mechanism.
     *
     * @param piOperation - 0 - LOAD, 1 - SAVE
     * @return <code>true</code> if serialization was successful
     */
    public boolean serialize(int piOperation) {
        if (piOperation == 0) {
            System.err.println("TransitionTable::serialize(LOAD) - Not implemented.");
            return false;
        } else {
            // A row of terminals
            int iTermNum = this.oTerminals.size();

            System.out.print("   Terminals(" + iTermNum + "): \t");

            for (int t = 0; t < iTermNum; t++) {
                Terminal oTerminal = (Terminal) this.oTerminals.elementAt(t);
                System.out.print("(" + oTerminal.getID() + ")" + oTerminal.getName() + "\t");
            }

            System.out.println();

            // Non terminals and rules...
            int iNonTemNum = this.oNonTerminals.size();

            System.out.println("NonTerminals(" + iNonTemNum + "): \t");

            for (int n = 0; n < iNonTemNum; n++) {
                System.out.print(((NonTerminal) this.oNonTerminals.elementAt(n)).getName() + "\t");

                for (int t = 0; t < iTermNum; t++) {
                    Vector oRow = (Vector) this.oTT.elementAt(n);

                    Object oEntry = oRow.elementAt(t);

                    if (oEntry instanceof Rule) {
                        Rule oRule = (Rule) oRow.elementAt(t);
                        System.out.print(oRule.toAbbrString() + "\t");
                    } else {
                        SyntaxError oSyntaxError = (SyntaxError) oEntry;
                        System.out.print("e\t");
                        Debug.debug(oSyntaxError.getMessage() + "\t");
                    }
                }

                System.out.println();
            }

            return true;
        }
    }

    /**
     * Terminals property accessor.
     *
     * @return Terminals list
     */
    public Vector getTerminals() {
        return this.oTerminals;
    }

    /**
     * NonTerminals property accessor.
     *
     * @return NonTerminals list
     */
    public Vector getNonTerminals() {
        return this.oNonTerminals;
    }

    /**
     * Sets Terminals property.
     *
     * @param poTerminalList Vector with terminals
     */
    public void setTerminals(Vector poTerminalList) {
        this.oTerminals = poTerminalList;
    }

    /**
     * Sets NonTerminals property.
     *
     * @param poNonTerminalList Vector with non-terminals
     */
    public void setNonTerminals(Vector poNonTerminalList) {
        this.oNonTerminals = poNonTerminalList;
    }

    /**
     * Gets a table entry at a given position (NonTerminal, Terminal).
     *
     * @param poNonTerminal non-terminal as a line number
     * @param poTerminal    terminal as a column
     * @return the entry at the specified position
     */
    public Object getEntryAt(final NonTerminal poNonTerminal, final Terminal poTerminal) {
        Vector oRow = (Vector) this.oTT.elementAt(poNonTerminal.getID());
        return oRow.elementAt(poTerminal.getID());
    }

    /**
     * Gets a table entry at a given position (NonTerminal, Token).
     *
     * @param poNonTerminal non-terminal as a line number
     * @param poToken       token corresponding to a terminal as a column
     * @return the entry at the specified position
     */
    public Object getEntryAt(NonTerminal poNonTerminal, Token poToken) {
        for (int t = 0; t < this.oTerminals.size(); t++) {
            if
                    (
                // Any other terminal
                    ((poToken.getLexeme().equals(((Terminal) this.oTerminals.elementAt(t)).getName()))

                            ||

                            // ID
                            (
                                    (poToken.getTokenType().getType() == TokenType.ID)
                                            &&
                                            ((Terminal) this.oTerminals.elementAt(t)).getName().equals("ID"))
                    )

                            ||

                            // NUM
                            (
                                    (poToken.getTokenType().getType() == TokenType.NUM)
                                            &&
                                            (((Terminal) this.oTerminals.elementAt(t)).getName().equals("NUM"))
                            )
/*
                ||

				// INTEGER
				(
					(poToken.getTokenType().getSubtype() == GrammarTokenType.INTEGER)
					&&
					(((Terminal)this.oTerminals.elementAt(t)).getName().equals("INTEGER"))
				)
*/
                    ) {
                Vector oRow = (Vector) this.oTT.elementAt(poNonTerminal.getID());

                System.out.println("Now: [" + poNonTerminal.getID() + "," + t + "]");

                return oRow.elementAt(t);
            }
        }

        Debug.debug("TT::getEntryAt() - Was looking for " + poToken.getLexeme() + " and didn't find it!");

        return null;
    }

    /**
     * Sets the entry of a table.
     *
     * @param poNonTerminal the non-terminal as a row index
     * @param poTerminal    the terminal as a column index
     * @param poEntry       the entry to set
     * @throws ArrayIndexOutOfBoundsException if either index does not exist
     */
    public void setEntryAt(final NonTerminal poNonTerminal, final Terminal poTerminal, Object poEntry) {
        try {
            Vector oRow = (Vector) this.oTT.elementAt(poNonTerminal.getID());
            oRow.setElementAt(poEntry, poTerminal.getID());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException
                    (
                            "TransitinTable::setEntryAt() - Out of boundaries.\n"
                                    + "TerminalID=" + poTerminal.getID()
                                    + ", NonTerminalID=" + poNonTerminal.getID()
                    );
        }
    }

    /**
     * TableFile property accessor.
     *
     * @return table file name
     */
    public String getTableFile() {
        return this.strTableFile;
    }

    /**
     * Sets TableFile property.
     *
     * @param pstrTableFileName the new filename to set
     */
    public void setTableFile(String pstrTableFileName) {
        this.strTableFile = pstrTableFileName;
    }

    /**
     * Gets grammar element by its name (lexeme).
     *
     * @param pstrName a name to search
     * @return GrammarElement, if found; null, if not found
     */
    public GrammarElement getGrammarElement(String pstrName) {
        GrammarElement oGrammarElement = null;

        for (int i = 0; i < this.oNonTerminals.size(); i++) {
            oGrammarElement = (GrammarElement) this.oNonTerminals.elementAt(i);

            if (oGrammarElement.getName().equals(pstrName)) {
                return oGrammarElement;
            }
        }

        for (int i = 0; i < this.oTerminals.size(); i++) {
            oGrammarElement = (GrammarElement) this.oTerminals.elementAt(i);

            if (oGrammarElement.getName().equals(pstrName)) {
                return oGrammarElement;
            }
        }

        Debug.debug
                (
                        "TransitionTable::getGrammarElement() - WARNING: No element \"" +
                                pstrName +
                                "\" was found!"
                );

        return oGrammarElement;
    }

    /**
     * Allows setting the ID of an EOF terminal.
     *
     * @param piID the ID corresponding to the end-of-file marker
     */
    public void setEOFTerminalID(int piID) {
        this.iEOFTerminalID = piID;
    }

    /**
     * Allows setting the ID of the Start non-terminal.
     *
     * @param piID the ID corresponding to the Start non-terminal
     */
    public void setStartNonTerminalID(int piID) {
        this.iStartNonTerminalID = piID;
    }

    /**
     * Allows querying for the EOF terminal. If the token
     * currently unset, attempts to locate the default by
     * trying to lookup the "$" entry.
     *
     * @return the terminal object acting as an indicator of end-of-file
     */
    public Terminal getEOFTerminal() {
        if (this.iEOFTerminalID == -1) {
            Terminal oEOF = (Terminal) getGrammarElement("$");
            this.iEOFTerminalID = oEOF.getID();
            Debug.debug("TransitionTable::getEOFTerminal() - WARNING: EOF wasn't serialized. Updated locally.");
            return oEOF;
        }

        return (Terminal) this.oTerminals.elementAt(this.iEOFTerminalID);
    }

    /**
     * Allows querying for the starting non-terminal. If the token
     * currently unset, attempts to locate the default by
     * trying to lookup the "&lt;prog&gt;" entry.
     *
     * @return the non-terminal object acting as an indicator of the beginning of the parse
     */
    public NonTerminal getStartNonTerminal() {
        if (this.iStartNonTerminalID == -1) {
            NonTerminal oStart = (NonTerminal) getGrammarElement("<prog>");
            this.iStartNonTerminalID = oStart.getID();
            Debug.debug("TransitionTable::getStartNonTerminal() - WARNING: START wasn't serialized. Updated locally.");
            return oStart;
        }

        return (NonTerminal) this.oNonTerminals.elementAt(this.iStartNonTerminalID);
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.14 $";
    }
}

// EOF
