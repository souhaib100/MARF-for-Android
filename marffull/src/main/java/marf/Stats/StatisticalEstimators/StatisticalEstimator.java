package marf.Stats.StatisticalEstimators;

import java.util.Vector;

import marf.MARF;
import marf.MARF.NLP;
import marf.Stats.ProbabilityTable;
import marf.Storage.StorageException;
import marf.Storage.StorageManager;
import marf.nlp.util.NLPStreamTokenizer;
import marf.util.NotImplementedException;


/**
 * <p>Implements generic Statistical Estimator routines.
 * Must be subclasses by concrete implemenations of statistical estimators.</p>
 * <p>
 * $Id: StatisticalEstimator.java,v 1.29 2006/02/11 20:36:56 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.29 $
 * @since 0.3.0.2
 */
public abstract class StatisticalEstimator
        extends StorageManager
        implements IStatisticalEstimator {
    /**
     * Probabilities table to perform estimation on.
     */
    protected ProbabilityTable oProbabilityTable = null;

    /**
     * Stream tokenizer for NLP processing to get word
     * and non-word tokens from.
     */
    protected NLPStreamTokenizer oStreamTokenizer = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = -7023903602882315275L;

    /**
     * Default constructor creates a new probability table
     * based on language with the default filename.
     *
     * @see NLP#getLanguage()
     * @see #getFilename()
     */
    public StatisticalEstimator() {
        //XXX: super(oProbabilityTable, getFilename());
        this.oProbabilityTable = new ProbabilityTable(NLP.getLanguage());
        this.strFilename = getFilename();
        this.oObjectToSerialize = this.oProbabilityTable;
        //XXX: Debug.debug("StatisticalEstimator()");
    }

    /**
     * N-gram-based probability classification.
     *
     * @return calculated probability value
     */
    //XXX: public abstract double p(String pstrSentence);
    //XXX: public abstract double p(Ngram poNgram);
    public final double p() {
        double dProbability = 0.0;

        try {
            // P(EOS,first-char)
//			boolean bBeginning = true;

            restore();

            String strToken = null;

//			while(this.oStreamTokenizer.nextToken() != StreamTokenizer.TT_EOF)
            while ((strToken = this.oStreamTokenizer.getNextToken()) != null) {
//				String strToken = getNextToken();

                // Something which what we didn't ask for (not a string)
//				if(strToken == null)
//				{
//					System.out.println("WARNING: Null token!");
//					continue;
//				}

                Vector oNgram = new Vector();

                switch (MARF.NLP.getNgramModel()) {
                    case MARF.ENgramModels.UNIGRAM: {
                        oNgram.add(strToken);
                        dProbability += Math.log(this.oProbabilityTable.p(oNgram));
                        break;
                    }

                    case MARF.ENgramModels.BIGRAM: {
                        oNgram.add(strToken);

//						if(oStreamTokenizer.nextToken() == StreamTokenizer.TT_EOF)
//							break;

//						String strToken2 = getNextToken();

                        String strToken2 = oStreamTokenizer.getNextToken();

                        if (strToken2 == null) {
                            break;
                        }

                        oNgram.add(strToken2);

                        this.oStreamTokenizer.pushBack();

                        dProbability += Math.log(oProbabilityTable.p(oNgram));

                        break;
                    }

                    case MARF.ENgramModels.TRIGRAM: {
                        oNgram.add(strToken);

//						if(oStreamTokenizer.nextToken() == StreamTokenizer.TT_EOF)
//							break;

                        String strToken2 = oStreamTokenizer.getNextToken();

                        if (strToken2 == null) {
                            break;
                        }

                        oNgram.add(strToken2);

                        int iTokenType2 = this.oStreamTokenizer.ttype;
                        double dNumericValue2 = this.oStreamTokenizer.nval;

//						if(oStreamTokenizer.nextToken() == StreamTokenizer.TT_EOF)
//							break;

//						String strToken3 = getNextToken();

                        String strToken3 = this.oStreamTokenizer.getNextToken();

                        if (strToken3 == null) {
                            break;
                        }

                        oNgram.add(strToken3);

                        int iTokenType3 = this.oStreamTokenizer.ttype;
                        double dNumericValue3 = this.oStreamTokenizer.nval;

                        this.oStreamTokenizer.ttype = iTokenType2;
                        this.oStreamTokenizer.nval = dNumericValue2;
                        this.oStreamTokenizer.sval = strToken2;

                        this.oStreamTokenizer.pushBack();

                        this.oStreamTokenizer.ttype = iTokenType3;
                        this.oStreamTokenizer.nval = dNumericValue3;
                        this.oStreamTokenizer.sval = strToken3;

                        this.oStreamTokenizer.pushBack();

                        dProbability += Math.log(this.oProbabilityTable.p(oNgram));
                        break;
                    }

                    default: {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }

        return dProbability;
    }

    /**
     * Every estimator needs to implement its specific training method.
     *
     * @return <code>true</code> if training was successful
     */
    public boolean train() {
        try {
            restore();

            String strToken = null;

            // Collect Stats
            //XXX: while(this.oStreamTokenizer.nextToken() != StreamTokenizer.TT_EOF)
            while ((strToken = this.oStreamTokenizer.getNextToken()) != null) {
                //String strToken = getNextToken();

                // Something which what we didn't ask for (not a string)
                if (strToken == null) {
                    System.err.println
                            (
                                    "WARNING: Null token! st:[" + this.oStreamTokenizer.toString() + "]" +
                                            this.oStreamTokenizer.sval + this.oStreamTokenizer.nval + this.oStreamTokenizer.ttype
                            );

                    continue;
                }

                Vector oNgram = new Vector();

                switch (MARF.NLP.getNgramModel()) {
                    case MARF.ENgramModels.UNIGRAM: {
                        oNgram.add(strToken);
//						Debug.debug("oNgram["+oUnigram+"]");
                        this.oProbabilityTable.incFrequency(oNgram);

                        break;
                    }

                    case MARF.ENgramModels.BIGRAM: {
                        oNgram.add(strToken);

//						if(this.oStreamTokenizer.nextToken() == StreamTokenizer.TT_EOF)
//							break;

                        String strToken2 = this.oStreamTokenizer.getNextToken();

                        if (strToken2 == null) {
                            break;
                        }

                        oNgram.add(strToken2);
                        this.oStreamTokenizer.pushBack();

//						Debug.debug("oNgram["+oNgram+"]");
                        this.oProbabilityTable.incFrequency(oNgram);

                        break;
                    }

                    case MARF.ENgramModels.TRIGRAM: {
                        oNgram.add(strToken);

//						if(this.oStreamTokenizer.nextToken() == StreamTokenizer.TT_EOF)
//							break;

                        String strToken2 = this.oStreamTokenizer.getNextToken();

                        if (strToken2 == null) {
                            break;
                        }

                        oNgram.add(strToken2);

                        int iTokenType2 = this.oStreamTokenizer.ttype;
                        double dNumericValue2 = this.oStreamTokenizer.nval;

                        //if(oStreamTokenizer.nextToken() == StreamTokenizer.TT_EOF)
                        //	break;

                        String strToken3 = this.oStreamTokenizer.getNextToken();

                        if (strToken3 == null) {
                            break;
                        }

                        oNgram.add(strToken3);

                        int iTokenType3 = this.oStreamTokenizer.ttype;
                        double dNumericValue3 = this.oStreamTokenizer.nval;

                        this.oStreamTokenizer.ttype = iTokenType2;
                        this.oStreamTokenizer.nval = dNumericValue2;
                        this.oStreamTokenizer.sval = strToken2;

                        this.oStreamTokenizer.pushBack();

                        this.oStreamTokenizer.ttype = iTokenType3;
                        this.oStreamTokenizer.nval = dNumericValue3;
                        this.oStreamTokenizer.sval = strToken3;

                        this.oStreamTokenizer.pushBack();

//						Debug.debug("oNgram["+oNgram+"]");
                        this.oProbabilityTable.incFrequency(oNgram);

                        break;
                    }

                    default: {
                        break;
                    }
                }
            }

            dump();
/*
            smooth();

			dump();

			this.oProbabilityTable.dumpCSV();
*/
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }

        return true;
    }

/*
    protected String getNextToken()
	{
		return new String((char)oStreamTokenizer.ttype + "");
	}
*/

    /**
     * Updates the reference to the probabilities table
     * for future serialization after restoration. A part
     * of the StorageManager interface.
     *
     * @see marf.Storage.StorageManager#backSynchronizeObject()
     * @since 0.3.0.5
     */
    public void backSynchronizeObject() {
        this.oProbabilityTable = (ProbabilityTable) this.oObjectToSerialize;
    }

    /**
     * Not implemented.
     *
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public void dumpCSV()
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Not implemented.
     *
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public void dumpXML()
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Not implemented.
     *
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public void restoreCSV()
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * Not implemented.
     *
     * @throws NotImplementedException
     * @throws StorageException        never thrown
     */
    public void restoreXML()
            throws StorageException {
        throw new NotImplementedException();
    }

    /**
     * @see marf.Stats.StatisticalEstimators.IStatisticalEstimator#setStreamTokenizer(marf.nlp.util.NLPStreamTokenizer)
     */
    public final void setStreamTokenizer(NLPStreamTokenizer poStreamTokenizer) {
        this.oStreamTokenizer = poStreamTokenizer;
    }

    /**
     * @see marf.Stats.StatisticalEstimators.IStatisticalEstimator#getStreamTokenizer()
     */
    public NLPStreamTokenizer getStreamTokenizer() {
        return this.oStreamTokenizer;
    }

    /**
     * @see marf.Stats.StatisticalEstimators.IStatisticalEstimator#getProbabilityTable()
     */
    public ProbabilityTable getProbabilityTable() {
        return this.oProbabilityTable;
    }

    /**
     * @see marf.Stats.StatisticalEstimators.IStatisticalEstimator#setLanguage(java.lang.String)
     */
    public final void setLanguage(final String pstrLang) {
        this.oProbabilityTable.setLang(pstrLang);
    }

    /**
     * @see marf.Stats.StatisticalEstimators.IStatisticalEstimator#getLanguage()
     */
    public final String getLanguage() {
        return this.oProbabilityTable.getLang();
    }

    /**
     * Resets the internal filename to the default
     * and returns it.
     *
     * @return the reset filename
     * @see #getFilename()
     */
    public final String resetFilename() {
        return (this.strFilename = getFilename());
    }

    /**
     * Sets the default filename for dumps as e.g.
     * <code>nlp.StatisticalEstimators.Smoothing.WittenBell.1.en.gzbin</code>.
     * More generally, <code>&lth;estimator/smoothing&gth;.&lth;ngram-model&gth;.&lth;lang&gth;.gzbin</code>
     *
     * @see marf.Storage.StorageManager#getFilename()
     */
    public final String getFilename() {
        return
                getClass().getName() +
                        "." + MARF.NLP.getNgramModel() +
                        "." + MARF.NLP.getLanguage() +
                        ".gzbin";
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.29 $";
    }
}

// EOF
