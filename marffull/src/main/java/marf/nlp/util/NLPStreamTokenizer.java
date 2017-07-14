package marf.nlp.util;

import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Stack;

import marf.nlp.Parsing.Token;


/**
 * <p>NLP Stream Tokenizer. Allows pushing back multiple
 * tokens and has a reader reverence to be able to reset it.
 * </p>
 * <p>
 * $Id: NLPStreamTokenizer.java,v 1.18 2006/01/19 04:13:18 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.18 $
 * @since 0.3.0.2
 */
public class NLPStreamTokenizer
        extends StreamTokenizer {
    /**
     * Keep a reference to the Reader ourselves to be able
     * to <code>reset()</code>.
     *
     * @see #reset()
     */
    protected Reader oReader = null;

    /**
     * A stack to push back tokens.
     */
    protected Stack oPushBackup = new Stack();

    /**
     * Default push backup level of 2.
     */
    protected int iPushBackupLevel = 2;

    /**
     * Reference to the token on the top of
     * our own stack.
     *
     * @since 0.3.0.5
     */
    protected Token oTopToken = null;

    /**
     * NLP Stream Tokenizer based on a reader.
     *
     * @param poReader reader to use to read tokens/lexemes
     */
    public NLPStreamTokenizer(Reader poReader) {
        super(poReader);
        this.oReader = poReader;
        this.oPushBackup.ensureCapacity(this.iPushBackupLevel);
    }

    /**
     * Returns a next token from the NLP stream or the stack if
     * any were pushed back.
     *
     * @return next lexeme token if there is any or null
     * @throws java.io.IOException
     */
    public String getNextToken()
            throws java.io.IOException {
/*		String strNewToken =
            (nextToken() == TT_EOF) ?
			null : new String((char)ttype + "");
*/
        String strNewToken = null;

        while (nextToken() != TT_EOF) {
            if
                    (
//				(this.ttype <= '\u0020')
                    (this.ttype < '\u0020')
/*				||
                !(
					(this.ttype >= 'A' && this.ttype <= 'Z')
					||
					(this.ttype >= 'a' && this.ttype <= 'z')
					||
					(this.ttype >= '0' && this.ttype <= '9')
//					||
//					(this.ttype >= '\u00A0' && this.ttype <= '\u00FF')
				)
*/)
                continue;

//			strNewToken = new String(Character.toLowerCase((char)ttype) + "");
            strNewToken = new String((char) ttype + "");
            break;
        }

        return strNewToken;
    }

    /**
     * Resets the internal reader's stream.
     *
     * @throws java.io.IOException
     */
    public void reset()
            throws java.io.IOException {
        if (this.oReader == null) {
            throw new NullPointerException("NLPStreamTokenizer.reset() - Reader is null.");
        }

        this.oReader.reset();
    }

    /**
     * Retrieves the next token from the stream or local
     * stack. The tokens retrieved from the local stack
     * always if the stack is not empty. When the stack
     * is empty the <code>nextToken()</code> of the parent
     * is called.
     *
     * @return the value of the token type (ttype field)
     * @throws java.io.IOException
     * @see #ttype
     * @since 0.3.0.5
     */
    public int nextToken()
            throws java.io.IOException {
        if (this.oPushBackup.empty()) {
            return super.nextToken();
        } else {
            this.oTopToken = (Token) this.oPushBackup.pop();

            this.sval = this.oTopToken.getLexeme();
            this.ttype = this.oTopToken.getTokenType().getType();
            this.nval = this.oTopToken.getNumericalValue();

            return this.ttype;
        }
    }

    /**
     * Overridden to place tokens back onto to stack, virtually
     * of any number of tokens.
     *
     * @since 0.3.0.5
     */
    public void pushBack() {
        this.oTopToken = new Token(this.sval, super.lineno(), this.ttype, this.nval);
        this.oPushBackup.push(this.oTopToken);
        //super.pushBack();
    }

    /**
     * Returns the current line number (of the latest/top token).
     * Overridden to account for tokens stored in the local stack.
     *
     * @return the current line number of this tokenizer
     * @since 0.3.0.5
     */
    public int lineno() {
        if (this.oTopToken != null) {
            return this.oTopToken.getLineNumber();
        } else {
            return super.lineno();
        }
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.18 $";
    }
}

// EOF
