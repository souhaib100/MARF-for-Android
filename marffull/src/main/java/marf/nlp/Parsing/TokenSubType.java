/*
 * TokenSubType Class
 * (C) 2001 - 2006 Serguei Mokhov, <mailto:mokhov@cs.concordia.ca>
 */

package marf.nlp.Parsing;

import java.util.Hashtable;


/**
 * <p>MARF Specific Tokens Types.</p>
 * <p>
 * $Id: TokenSubType.java,v 1.14 2006/01/19 04:13:16 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.14 $
 * @since 0.3.0.2
 */
public class TokenSubType
        extends TokenType {
    /**
     * Token subtype; default is UNKNOWN.
     *
     * @see TokenType#UNKNOWN
     */
    protected int iSubtype = UNKNOWN;

	/*
     * Constants for operators, brackets,
	 * and punctuation.
	 */

    /**
     * Constant for operator "equals".
     */
    public static final int OP_EQ = 1;

    /**
     * Constant for operator "not equal".
     */
    public static final int OP_NE = 2;

    /**
     * Constant for operator "less than".
     */
    public static final int OP_LT = 3;

    /**
     * Constant for operator "greater than".
     */
    public static final int OP_GT = 4;

    /**
     * Constant for operator "less than or equal to".
     */
    public static final int OP_LE = 5;

    /**
     * Constant for operator "greater than or equal to".
     */
    public static final int OP_GE = 6;

    /**
     * Constant for operator "plus".
     */
    public static final int OP_PLUS = 7;

    /**
     * Constant for operator "minus".
     */
    public static final int OP_MINUS = 8;

    /**
     * Constant for operator "multiply".
     */
    public static final int OP_MULT = 9;

    /**
     * Constant for operator "divide".
     */
    public static final int OP_DIV = 10;

    /**
     * Constant for operator "assign".
     */
    public static final int OP_ASIGN = 11;

    /**
     * Constant for operator "object membership".
     */
    public static final int OP_MEMB = 12;

    /**
     * Constant for "comma".
     */
    public static final int COMMA = 13;

    /**
     * Constant for "semicolon".
     */
    public static final int SEMICOLON = 14;

    /**
     * Constant for "open parenthesis".
     */
    public static final int PAREN_OP = 15;

    /**
     * Constant for "closed parenthesis".
     */
    public static final int PAREN_CL = 16;

    /**
     * Constant for "open curly brace".
     */
    public static final int CURLY_OP = 17;

    /**
     * Constant for "closed curly brace".
     */
    public static final int CURLY_CL = 18;

    /**
     * Constant for "open square bracket".
     */
    public static final int SQR_OP = 19;

    /**
     * Constant for "close square bracket".
     */
    public static final int SQR_CL = 20;

    // Keyword codes

    /**
     * Constant for keyword "do".
     */
    public static final int DO = 21;

    /**
     * Constant for keyword "else".
     */
    public static final int ELSE = 22;

    /**
     * Constant for keyword "if".
     */
    public static final int IF = 23;

    /**
     * Constant for keyword "integer".
     */
    public static final int INTEGER = 24;

    /**
     * Constant for keyword "class".
     */
    public static final int CLASS = 25;

    /**
     * Constant for keyword "read".
     */
    public static final int READ = 26;

    /**
     * Constant for keyword "real".
     */
    public static final int REAL = 27;

    /**
     * Constant for keyword "return".
     */
    public static final int RETURN = 28;

    /**
     * Constant for keyword "then".
     */
    public static final int THEN = 29;

    /**
     * Constant for keyword "while".
     */
    public static final int WHILE = 30;

    /**
     * Constant for keyword "write".
     */
    public static final int WRITE = 31;

    /**
     * Constant for keyword "program".
     */
    public static final int PROGRAM = 32;

    /**
     * Constant for keyword "and".
     */
    public static final int AND = 33;

    /**
     * Constant for keyword "not".
     */
    public static final int NOT = 34;

    /**
     * Constant for keyword "or".
     */
    public static final int OR = 35;

    /**
     * Constant for keyword "this".
     */
    public static final int THIS = 36;

    // Mappings

    /**
     * Maps constants and keyword spellings.
     *
     * @since October 2, 2001
     */
    public static Hashtable soKeywords = new Hashtable();

    /**
     * Maps constants and punctuation.
     *
     * @since October 2, 2001
     */
    public static Hashtable soValidPunctuation = new Hashtable();

    /**
     * Maps constants and operators.
     *
     * @since October 2, 2001
     */
    public static Hashtable soOperators = new Hashtable();

    /**
     * Maps constants and brackets, parentheses, and braces.
     *
     * @since October 2, 2001
     */
    public static Hashtable soBrackets = new Hashtable();

    /**
     * Matches numbers to literals for output purposes.
     *
     * @since October 2, 2001
     */
    public static Hashtable soTokenSubTypes = new Hashtable();

    static {
        // Init keywords match hashtable
        soKeywords.put(new String("do"), new Integer(DO));
        soKeywords.put(new String("else"), new Integer(ELSE));
        soKeywords.put(new String("if"), new Integer(IF));
        soKeywords.put(new String("integer"), new Integer(INTEGER));
        soKeywords.put(new String("class"), new Integer(CLASS));
        soKeywords.put(new String("read"), new Integer(READ));
        soKeywords.put(new String("real"), new Integer(REAL));
        soKeywords.put(new String("return"), new Integer(RETURN));
        soKeywords.put(new String("then"), new Integer(THEN));
        soKeywords.put(new String("while"), new Integer(WHILE));
        soKeywords.put(new String("write"), new Integer(WRITE));
        soKeywords.put(new String("program"), new Integer(PROGRAM));
        soKeywords.put(new String("and"), new Integer(AND));
        soKeywords.put(new String("not"), new Integer(NOT));
        soKeywords.put(new String("or"), new Integer(OR));
        soKeywords.put(new String("this"), new Integer(THIS));

        // Init punctuation match hashtable
        soValidPunctuation.put(new String(";"), new Integer(SEMICOLON));
        soValidPunctuation.put(new String(","), new Integer(COMMA));

        // Init operators match hashtable
        soOperators.put(new String("=="), new Integer(OP_EQ));
        soOperators.put(new String("<>"), new Integer(OP_NE));
        soOperators.put(new String("<"), new Integer(OP_LT));
        soOperators.put(new String(">"), new Integer(OP_GT));
        soOperators.put(new String("<="), new Integer(OP_LE));
        soOperators.put(new String(">="), new Integer(OP_GE));
        soOperators.put(new String("+"), new Integer(OP_PLUS));
        soOperators.put(new String("-"), new Integer(OP_MINUS));
        soOperators.put(new String("*"), new Integer(OP_MULT));
        soOperators.put(new String("/"), new Integer(OP_DIV));
        soOperators.put(new String("="), new Integer(OP_ASIGN));
        soOperators.put(new String("."), new Integer(OP_MEMB));

        // Init bracketing
        soBrackets.put(new String("("), new Integer(PAREN_OP));
        soBrackets.put(new String(")"), new Integer(PAREN_CL));
        soBrackets.put(new String("{"), new Integer(CURLY_OP));
        soBrackets.put(new String("}"), new Integer(CURLY_CL));
        soBrackets.put(new String("["), new Integer(SQR_OP));
        soBrackets.put(new String("]"), new Integer(SQR_CL));

        // Init token subtypes for output
        soTokenSubTypes.put(new Integer(COMMA), new String("COMMA"));
        soTokenSubTypes.put(new Integer(SEMICOLON), new String("SEMICOLON"));
        soTokenSubTypes.put(new Integer(OP_EQ), new String("OP_EQ"));
        soTokenSubTypes.put(new Integer(OP_NE), new String("OP_NE"));
        soTokenSubTypes.put(new Integer(OP_LT), new String("OP_LT"));
        soTokenSubTypes.put(new Integer(OP_GT), new String("OP_GT"));
        soTokenSubTypes.put(new Integer(OP_LE), new String("OP_LE"));
        soTokenSubTypes.put(new Integer(OP_GE), new String("OP_GE"));
        soTokenSubTypes.put(new Integer(OP_PLUS), new String("OP_PLUS"));
        soTokenSubTypes.put(new Integer(OP_MINUS), new String("OP_MINUS"));
        soTokenSubTypes.put(new Integer(OP_MULT), new String("OP_MULT"));
        soTokenSubTypes.put(new Integer(OP_DIV), new String("OP_DIV"));
        soTokenSubTypes.put(new Integer(OP_ASIGN), new String("OP_ASIGN"));
        soTokenSubTypes.put(new Integer(OP_MEMB), new String("OP_MEMB"));
        soTokenSubTypes.put(new Integer(PAREN_OP), new String("PAREN_OP"));
        soTokenSubTypes.put(new Integer(PAREN_CL), new String("PAREN_CL"));
        soTokenSubTypes.put(new Integer(CURLY_OP), new String("CURLY_OP"));
        soTokenSubTypes.put(new Integer(CURLY_CL), new String("CURLY_CL"));
        soTokenSubTypes.put(new Integer(SQR_OP), new String("SQR_OP"));
        soTokenSubTypes.put(new Integer(SQR_CL), new String("SQR_CL"));
        soTokenSubTypes.put(new Integer(UNKNOWN), new String("UNKNOWN"));
        soTokenSubTypes.put(new Integer(DO), new String("DO"));
        soTokenSubTypes.put(new Integer(ELSE), new String("ELSE"));
        soTokenSubTypes.put(new Integer(IF), new String("IF"));
        soTokenSubTypes.put(new Integer(INTEGER), new String("INTEGER"));
        soTokenSubTypes.put(new Integer(CLASS), new String("CLASS"));
        soTokenSubTypes.put(new Integer(READ), new String("READ"));
        soTokenSubTypes.put(new Integer(REAL), new String("REAL"));
        soTokenSubTypes.put(new Integer(RETURN), new String("RETURN"));
        soTokenSubTypes.put(new Integer(THEN), new String("THEN"));
        soTokenSubTypes.put(new Integer(WHILE), new String("WHILE"));
        soTokenSubTypes.put(new Integer(WRITE), new String("WRITE"));
        soTokenSubTypes.put(new Integer(PROGRAM), new String("PROGRAM"));
        soTokenSubTypes.put(new Integer(AND), new String("AND"));
        soTokenSubTypes.put(new Integer(NOT), new String("NOT"));
        soTokenSubTypes.put(new Integer(OR), new String("OR"));
        soTokenSubTypes.put(new Integer(THIS), new String("THIS"));
    }

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 8570456006441229360L;

    /**
     * Default Constructor.
     */
    public TokenSubType() {
        super();
    }

    /**
     * Sets token sub-type given its lexeme.
     *
     * @param pstrLexeme the lexeme to determine the token sub-type
     */
    public void setSubTypeByLexeme(final String pstrLexeme) {
        if (soKeywords.get(pstrLexeme) != null) {
            this.iSubtype = ((Integer) soKeywords.get(pstrLexeme)).intValue();
        } else if (soValidPunctuation.get(pstrLexeme) != null) {
            this.iSubtype = ((Integer) soValidPunctuation.get(pstrLexeme)).intValue();
        } else if (soBrackets.get(pstrLexeme) != null) {
            this.iSubtype = ((Integer) soBrackets.get(pstrLexeme)).intValue();
        } else if (soOperators.get(pstrLexeme) != null) {
            this.iSubtype = ((Integer) soOperators.get(pstrLexeme)).intValue();
        } else {
            this.iSubtype = UNKNOWN;
        }
    }

    /**
     * Allows querying for the current token sub-type.
     *
     * @return the current token subtype
     * @since 0.3.0.5
     */
    public int getSubtype() {
        return this.iSubtype;
    }

    /**
     * Allows setting the current token sub-type.
     *
     * @param piSubtype the new token subtype
     * @since 0.3.0.5
     */
    public void setSubtype(int piSubtype) {
        this.iSubtype = piSubtype;
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
