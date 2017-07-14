package marf.Stats.StatisticalEstimators.Smoothing;

import marf.MARF;
import marf.Stats.Ngram;


/**
 * <p>Good-Turing Smoothing Estimator.
 * TODO: complete.
 * </p>
 * <p>
 * $Id: GoodTuring.java,v 1.20 2006/01/29 22:28:22 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.20 $
 * @since 0.3.0.2
 */
public class GoodTuring
        extends Smoothing {
    /**
     * The default threshold value of 5.
     *
     * @since 0.3.0.5
     */
    public static final int DEFAULT_GOOD_TURING_THRESHOLD = 5;

    /**
     * Good-Turing threshold value.
     */
    private int iThreshold;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -2336791420487311348L;

    /**
     * Constructs the default estimator with
     * the default value of threshold of 5.
     */
    public GoodTuring() {
        this(DEFAULT_GOOD_TURING_THRESHOLD);
    }

    /**
     * Constructs the estimator with the specified threshold.
     *
     * @param piThreshold the desired threshold value
     */
    public GoodTuring(final int piThreshold) {
        super();
        this.iThreshold = piThreshold;
    }

    /**
     * Implements Good-Turing smoothing algorithm for uni-, bi-,
     * and tri-grams.
     *
     * @see marf.Stats.StatisticalEstimators.Smoothing.ISmoothing#smooth()
     */
    public boolean smooth() {
        try {
            System.out.println(getClass().getName() + " smoothing has begun.");

            // c <= Threshold = k; N0, N1, ..., Nc, N(c+1) = N(k + 1)
            long alN[] = new long[this.iThreshold + 2];
            int iV = this.oProbabilityTable.size();

            switch (MARF.NLP.getNgramModel()) {
                case MARF.ENgramModels.UNIGRAM: {
                    for (int j = 0; j < iV; j++) {
                        int iOccurence = (int) this.oProbabilityTable.getOccurence(j);

                        if (iOccurence <= this.iThreshold + 1) {
                            alN[iOccurence]++;
                        }
                    }

                    for (int j = 0; j < iV; j++) {
                        double dNewOccurence;

                        int iOccurence = (int) this.oProbabilityTable.getOccurence(j);

                        if (iOccurence > this.iThreshold) {
                            continue;
                        } else {
                            dNewOccurence =
                                    (
                                            (iOccurence + 1) * (alN[iOccurence + 1] / (double) alN[iOccurence])
                                                    -
                                                    (iOccurence * (this.iThreshold + 1) * alN[this.iThreshold + 1] / (double) alN[1])
                                    )
                                            /
                                            (1 - (this.iThreshold + 1) * alN[this.iThreshold + 1] / (double) alN[1]);
                        }

                        this.oProbabilityTable.setOccurence(j, dNewOccurence);
                    }

                    break;
                }

                case MARF.ENgramModels.BIGRAM: {
//					Debug.debug("V=" + iV);

                    // Compute N0 ... N(k+1)
                    for (int i = 0; i < iV; i++) {
                        for (int j = 0; j < iV; j++) {
                            int iOccurence = (int) this.oProbabilityTable.getOccurence(j, i);

                            if (iOccurence <= this.iThreshold + 1) {
                                alN[iOccurence]++;
                            }
                        }

                        //Debug.debug("N["+i+"]=" + N[i]);
                    }

//					Debug.debug("N\\["+N+"]/");

                    for (int i = 0; i < iV; i++) {
                        for (int j = 0; j < iV; j++) {
                            double dNewOccurence;

                            int iOccurence = (int) this.oProbabilityTable.getOccurence(j, i);

                            if (iOccurence > this.iThreshold) {
                                continue;
                            } else {
                                dNewOccurence =
                                        (
                                                (iOccurence + 1) * (alN[iOccurence + 1] / (double) alN[iOccurence])
                                                        -
                                                        (iOccurence * (this.iThreshold + 1) * alN[this.iThreshold + 1] / (double) alN[1])
                                        )
                                                /
                                                (1 - (this.iThreshold + 1) * alN[this.iThreshold + 1] / (double) alN[1]);
                            }

/*
                            Debug.debug
							(
								"Old Occurence: " + lOccurence + "\n" +
								"New Occurence: " + dNewOccurence
							);
*/
                            this.oProbabilityTable.setOccurence(j, i, dNewOccurence);
                        }
                    }

                    break;
                }

                case MARF.ENgramModels.TRIGRAM: {
                    // Compute N0 ... N(k+1)
                    for (int k = 0; k < iV; k++) {
                        for (int i = 0; i < iV; i++) {
                            for (int j = 0; j < iV; j++) {
                                int iOccurence = (int) this.oProbabilityTable.getOccurence(j, i, k);

                                if (iOccurence <= this.iThreshold + 1) {
                                    alN[iOccurence]++;
                                }
                            }
                        }
                    }

                    for (int k = 0; k < iV; k++)
                        for (int i = 0; i < iV; i++)
                            for (int j = 0; j < iV; j++) {
                                double dNewOccurence;

                                int lOccurence = (int) this.oProbabilityTable.getOccurence(j, i, k);

                                if (lOccurence > this.iThreshold)
                                    continue;
                                else
                                    dNewOccurence =
                                            (
                                                    (lOccurence + 1) * (alN[lOccurence + 1] / (double) alN[lOccurence])
                                                            -
                                                            (lOccurence * (this.iThreshold + 1) * alN[this.iThreshold + 1] / (double) alN[1])
                                            )
                                                    /
                                                    (1 - (this.iThreshold + 1) * alN[this.iThreshold + 1] / (double) alN[1]);

                                this.oProbabilityTable.setOccurence(j, i, k, dNewOccurence);
                            }

                    break;
                }

                default: {
                    return false;
                }
            }

            System.out.println(getClass().getName() + " smoothing has completed.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            return false;
        }

        return true;
    }

    /**
     * Not implemented.
     *
     * @param poNgram
     * @return 0.0
     */
    public double p(Ngram poNgram) {
        return 0.0;
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.20 $";
    }
}

// EOF
