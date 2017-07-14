package marf.Stats.StatisticalEstimators.Smoothing;

import marf.MARF;
import marf.Stats.Ngram;


/**
 * <p>Represents Witten-Bell Smoothing Algorithm.
 * </p>
 * <p>
 * $Id: WittenBell.java,v 1.20 2006/01/29 22:28:22 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.20 $
 * @since 0.3.0.2
 */
public class WittenBell
        extends Smoothing {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 4056635859068758735L;

    /**
     * Default constructor with a call to the parent.
     */
    public WittenBell() {
        super();
    }

    /**
     * Implements the Witten-Bell smoothing algorithm for
     * uni-, bi, and tri-gram models.
     *
     * @see marf.Stats.StatisticalEstimators.Smoothing.ISmoothing#smooth()
     */
    public boolean smooth() {
        try {
            System.out.println(getClass().getName() + " smoothing has begun.");

            int iV = this.oProbabilityTable.size();

            switch (MARF.NLP.getNgramModel()) {
                case MARF.ENgramModels.UNIGRAM: {
                    long lN = 0;
                    long lZ = 0;
                    long lT = 0;

                    for (int j = 0; j < iV; j++) {
                        long lOccurence = (long) this.oProbabilityTable.getOccurence(j);

                        lN += lOccurence;
                        lZ += lOccurence == 0 ? 1 : 0;
                    }

                    lT = iV - lZ;

                    for (int j = 0; j < iV; j++) {
                        double dNewOccurence;

                        long lOccurence = (long) this.oProbabilityTable.getOccurence(j);

                        double dAdjustment = (double) lN / (double) (lN + lT);

                        // Unseen
                        if (lOccurence == 0) {
                            dNewOccurence = ((double) lT / (double) lZ) * dAdjustment;
                        }

                        // Seen
                        else {
                            dNewOccurence = lOccurence * dAdjustment;
                        }

                        this.oProbabilityTable.setOccurence(j, dNewOccurence);
                    }

                    break;
                }

                case MARF.ENgramModels.BIGRAM: {
                    long alN[] = new long[iV];
                    long alZ[] = new long[iV];
                    long alT[] = new long[iV];

//					Debug.debug("V=" + iV);

                    for (int i = 0; i < iV; i++) {
                        for (int j = 0; j < iV; j++) {
                            long lOccurence = (long) this.oProbabilityTable.getOccurence(j, i);

                            alN[i] += lOccurence;
                            alZ[i] += lOccurence == 0 ? 1 : 0;
                        }

                        alT[i] = iV - alZ[i];
/*
                        Debug.debug
						(
							"N["+i+"]=" + alN[i] + ", " +
							"Z["+i+"]=" + alZ[i] + ", " +
							"T["+i+"]=" + alT[i]
						);
*/
                    }

                    for (int i = 0; i < iV; i++) {
                        for (int j = 0; j < iV; j++) {
                            double dNewOccurence;

                            long lOccurence = (long) this.oProbabilityTable.getOccurence(j, i);

                            double dAdjustment = (double) alN[i] / (double) (alN[i] + alT[i]);

                            // Unseen
                            if (lOccurence == 0) {
                                dNewOccurence = ((double) alT[i] / (double) alZ[i]) * dAdjustment;
                            }

                            // Seen
                            else {
                                dNewOccurence = lOccurence * dAdjustment;
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
                    long alN[][] = new long[iV][iV];
                    long alZ[][] = new long[iV][iV];
                    long alT[][] = new long[iV][iV];

                    for (int k = 0; k < iV; k++) {
                        for (int i = 0; i < iV; i++) {
                            for (int j = 0; j < iV; j++) {
                                long lOccurence = (long) this.oProbabilityTable.getOccurence(j, i, k);

                                alN[k][i] += lOccurence;
                                alZ[k][i] += lOccurence == 0 ? 1 : 0;
                            }

                            alT[k][i] = iV - alZ[k][i];
                        }
                    }

                    for (int k = 0; k < iV; k++) {
                        for (int i = 0; i < iV; i++) {
                            for (int j = 0; j < iV; j++) {
                                double dNewOccurence;

                                long lOccurence = (long) this.oProbabilityTable.getOccurence(j, i, k);

                                double dAdjustment = (double) alN[k][i] / (double) (alN[k][i] + alT[k][i]);

                                // Unseen
                                if (lOccurence == 0) {
                                    dNewOccurence = ((double) alT[k][i] / (double) alZ[k][i]) * dAdjustment;
                                }

                                // Seen
                                else {
                                    dNewOccurence = lOccurence * dAdjustment;
                                }

                                this.oProbabilityTable.setOccurence(j, i, k, dNewOccurence);
                            }
                        }
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
