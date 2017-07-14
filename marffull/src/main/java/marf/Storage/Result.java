package marf.Storage;

import java.io.Serializable;


/**
 * <p>Represents a single classification result - ID and some value
 * indicating either certain distance from the sample being recognized
 * or a probability.</p>
 * <p>
 * <p>$Id: Result.java,v 1.22 2006/01/02 22:24:00 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.22 $
 * @since 0.0.1
 */
public class Result
        implements Serializable, Cloneable {
    /**
     * Identified subject's ID.
     */
    protected int iID = 0;

    /**
     * Textual result description.
     *
     * @since 0.3.0
     */
    protected String strDescription = "";

    /**
     * Distance/probability.
     *
     * @since 0.3.0
     */
    protected double dOutcome = 0.0;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 5289013996325438809L;

    /**
     * Default Constructor.
     * Equivalent to <code>Result(0)</code>
     */
    public Result() {
        this(0);
    }

    /**
     * ID Constructor. Equivalent to <code>Result(piID, 0, "")</code>.
     *
     * @param piID integer ID of the speaker
     */
    public Result(final int piID) {
        this(piID, 0, "");
    }

    /**
     * ID/outcome Constructor. Equivalent to <code>Result(piID, pdOutcome, "")</code>.
     *
     * @param piID      integer ID of the subject
     * @param pdOutcome distance/probability of the result
     * @since 0.3.0
     */
    public Result(final int piID, final double pdOutcome) {
        this(piID, pdOutcome, "");
    }

    /**
     * ID/description Constructor.
     * Equivalent to <code>Result(piID, 0, pstrDescription)</code>.
     *
     * @param piID            integer ID of the subject
     * @param pstrDescription textual description of the result
     * @since 0.3.0
     */
    public Result(final int piID, final String pstrDescription) {
        this(piID, 0, pstrDescription);
    }

    /**
     * Outcome/description Constructor.
     * Equivalent to <code>Result(0, pdOutcome, pstrDescription)</code>.
     *
     * @param pdOutcome       distance/probability of the result
     * @param pstrDescription textual description of the result
     * @since 0.3.0
     */
    public Result(final double pdOutcome, final String pstrDescription) {
        this(0, pdOutcome, pstrDescription);
    }

    /**
     * General ID/outcome/description Constructor.
     *
     * @param piID            integer ID of the subject
     * @param pdOutcome       distance/probability of the result
     * @param pstrDescription textual description of the result
     * @since 0.3.0
     */
    public Result(final int piID, final double pdOutcome, final String pstrDescription) {
        this.iID = piID;
        this.dOutcome = pdOutcome;
        this.strDescription = pstrDescription;
    }

    /**
     * Copy-constructor.
     *
     * @param poResult Result object to copy data from
     * @since 0.3.0.5
     */
    public Result(final Result poResult) {
        this(poResult.getID(), poResult.getOutcome(), new String(poResult.getDescription()));
    }

    /**
     * Returns result's ID.
     *
     * @return ID of an entity (speaker, instrument, language, etc)
     */
    public final int getID() {
        return this.iID;
    }

    /**
     * Retrieves the outcome value.
     *
     * @return distance/probability of the result
     * @since 0.3.0
     */
    public final double getOutcome() {
        return this.dOutcome;
    }

    /**
     * Retrieves textual description of the result.
     *
     * @return string describing the result
     * @since 0.3.0
     */
    public final String getDescription() {
        return this.strDescription;
    }

    /**
     * Retrieves the result values in a form of string, which is
     * of the following form: [ID:outcome:description].
     *
     * @return string representation of the Result object
     * @since 0.3.0
     */
    public String toString() {
        StringBuffer oBuffer = new StringBuffer();

        oBuffer
                .append("[").append(this.iID).append(":")
                .append(this.dOutcome).append(":")
                .append(this.strDescription).append("]");

        return oBuffer.toString();
    }

    /**
     * Sets ID, should only be called by a Classification module.
     *
     * @param piID ID of the subject classified
     */
    public final void setID(final int piID) {
        this.iID = piID;
    }

    /**
     * Sets outcome value.
     *
     * @param pdOutcome resulting outcome
     * @since 0.3.0.2
     */
    public final void setOutcome(double pdOutcome) {
        this.dOutcome = pdOutcome;
    }

    /**
     * Sets description of the result.
     *
     * @param pstrDescription description string
     * @since 0.3.0.2
     */
    public final void setDescription(String pstrDescription) {
        this.strDescription = pstrDescription;
    }

    /**
     * Implements Cloneable interface for the Result object.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new Result(this);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.22 $";
    }
}

// EOF
