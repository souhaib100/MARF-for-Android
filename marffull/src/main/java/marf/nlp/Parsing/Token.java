/*
 * Token Class
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing;

import net.windward.android.awt.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

/**
 * <p>This class denotes a Token data structure.</p>
 * <p>
 * $Id: Token.java,v 1.19 2006/01/19 04:13:16 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.19 $
 * @since 0.3.0.2
 */
public class Token
        implements Serializable {
    /**
     * Spelling of the token.
     */
    protected String strLexeme;

    /**
     * Line and column where the token occured.
     */
    protected Point oPosition;

    /**
     * Token type.
     */
    protected TokenSubType oTokenType;

    /**
     * Numerical value associated with the token
     * to be mapped to StreamTokenizer.nval
     *
     * @see java.io.StreamTokenizer#nval
     */
    protected double dNumericalValue = 0;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 3470336900324740404L;

    /**
     * Constructor with full parameters.
     *
     * @param pstrLexeme  token's lexeme
     * @param poPosition  position in the source
     * @param poTokenType type of a token
     */
    public Token(String pstrLexeme, Point poPosition, TokenSubType poTokenType) {
        this.strLexeme = pstrLexeme;
        this.oPosition = poPosition;
        this.oTokenType = poTokenType;
    }

    /**
     * Constructor that maps values produced by StreamTokenizer.
     *
     * @param pstrLexeme       token's lexeme
     * @param piLineNumber     line number of the oken
     * @param piTokenType      type of a token
     * @param pdNumericalValue numerical value of the token
     * @see java.io.StreamTokenizer
     * @since 0.3.0.5
     */
    public Token(String pstrLexeme, int piLineNumber, int piTokenType, double pdNumericalValue) {
        this.strLexeme = pstrLexeme;

        this.oPosition = new Point();
        this.oPosition.y = piLineNumber;

        this.oTokenType = new TokenSubType();
        this.oTokenType.iType = piTokenType;

        this.dNumericalValue = pdNumericalValue;
    }

    /**
     * Copy-constructor.
     *
     * @param poToken token to clone data members from.
     */
    public Token(final Token poToken) {
        this.strLexeme = new String(poToken.strLexeme);
        this.oPosition = (Point) poToken.getPosition().clone();

        // TODO: fix cloning
        this.oTokenType = poToken.getTokenType();
    }

    /**
     * Loads/saves the token to a text file.
     * TODO: migrate to MARF's dump/store machinery.
     *
     * @param piOperation  load or save
     * @param poFileWriter file writer object to use
     * @return true of the write is successful
     */
    public boolean serialize(int piOperation, FileWriter poFileWriter) {
        if (piOperation == 0) {
            System.err.println("Token::serialize(LOAD) - Not implemented.");
            return false;
        } else {
            try {
                /*
                poFileWriter.write
				(
					Position.y +
					": Type[" +	(String)oTokenType.TokenTypes.get(new Integer(oTokenType.type)) + "], " +
					"Lexeme[" + Lexeme
					+ "], " +
					"Subtype[" + (String)oTokenType.TokenSubTypes.get(new Integer(oTokenType.subtype)) + "]\n"
				);
				*/

                poFileWriter.write
                        (
                                this.oPosition.y +
                                        ": " + TokenType.soTokenTypes.get(new Integer(this.oTokenType.iType)) + ", [ " +
                                        this.strLexeme
                                        + " ], " +
                                        TokenSubType.soTokenSubTypes.get(new Integer(this.oTokenType.iSubtype)) + "\n"
                        );

                return true;
            } catch (IOException e) {
                System.err.println("Token::serialize() - " + e.getMessage());
                e.printStackTrace(System.err);
                return false;
            }
        }
    }

    /**
     * Allows accessing the lexeme property.
     *
     * @return the current value of the lexeme property
     */
    public String getLexeme() {
        return this.strLexeme;
    }

    /**
     * Sets the value of the lexeme property.
     *
     * @param pstrLexeme the new value of the lexeme property
     */
    public void setLexeme(String pstrLexeme) {
        this.strLexeme = pstrLexeme;
    }

    /**
     * Allows accessing position property.
     *
     * @return the current value of the position
     */
    public Point getPosition() {
        return this.oPosition;
    }

    /**
     * Sets the value of the new position of the token.
     *
     * @param poPosition the new value of the position property
     */
    public void setPosition(Point poPosition) {
        this.oPosition = poPosition;
    }

    /**
     * Gets the line number component of the token's position.
     *
     * @return the current value of line number
     * @since 0.3.0.5
     */
    public int getLineNumber() {
        return this.oPosition.y;
    }

    /**
     * Gets the numerical representation of the token.
     *
     * @return the current numerical value of the token
     * @since 0.3.0.5
     */
    public double getNumericalValue() {
        return this.dNumericalValue;
    }

    /**
     * Allows accessing the token type.
     *
     * @return the current value of the TokenType property
     */
    public TokenSubType getTokenType() {
        return this.oTokenType;
    }

    /**
     * Sets the value of the token type.
     *
     * @param poTokenType the new value of the TokenType
     */
    public void setTokenType(TokenSubType poTokenType) {
        this.oTokenType = poTokenType;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.19 $";
    }
}

// EOF
