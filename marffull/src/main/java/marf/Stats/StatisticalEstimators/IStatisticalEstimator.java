package marf.Stats.StatisticalEstimators;

import marf.Stats.ProbabilityTable;
import marf.nlp.util.NLPStreamTokenizer;


/**
 * <p>Represents Statistical Estimator interface.
 * If concrete statistical estimators cannot inherit
 * from the generic <code>StatisticalEstimator</code> class
 * (which provides most generic implementation), they must
 * implement this interface then.
 * </p>
 * <p>
 * $Id: IStatisticalEstimator.java,v 1.2 2006/01/15 01:50:57 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.2 $
 * @since 0.3.0.3
 */
public interface IStatisticalEstimator {
    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.2 $";

    /**
     * Classification via calculation of a probability <i>p</i>.
     *
     * @return calculated probability value
     */
    double p();

    /**
     * Every estimator needs to implement its specific training method.
     *
     * @return <code>true</code> if training was successful
     */
    boolean train();

    /**
     * Sets desired stream tokenizer.
     *
     * @param poStreamTokenizer NLPStreamTokenizer or a derivative to use for tokens
     * @see marf.nlp.util.NLPStreamTokenizer
     */
    void setStreamTokenizer(NLPStreamTokenizer poStreamTokenizer);

    /**
     * Retrieves current stream tokenizer.
     *
     * @return the stream tokenizer beeing used
     */
    NLPStreamTokenizer getStreamTokenizer();

    /**
     * Retrieves current probabilities table.
     *
     * @return probabilities table beeing used
     * @see ProbabilityTable
     */
    ProbabilityTable getProbabilityTable();

    /**
     * Allows alteration of the current language being processed.
     *
     * @param pstrLang desired language
     */
    void setLanguage(String pstrLang);

    /**
     * Retrieves current language.
     *
     * @return language name of language being processed
     */
    String getLanguage();
}

// EOF
