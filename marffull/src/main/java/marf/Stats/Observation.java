package marf.Stats;


/**
 * <p>Generic Observation of an occurce of something (like a word or an n-gram).</p>
 * <p>
 * $Id: Observation.java,v 1.12 2006/01/02 22:24:00 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.12 $
 * @since 0.3.0.2
 */
public class Observation
        extends WordStats {
    /**
     * Probability of the observation of happening prior
     * it actually being observed.
     */
    protected double dPriorProbability = 0.0;

    /**
     * Probability of the observation after
     * it was observed.
     */
    protected double dPosteriorProbability = 0.0;

    /**
     * Indicates a fact that this observation has been "seen".
     */
    protected boolean bSeen = false;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 608350003332106275L;

    /**
     * Constructs default unseen observation with zero probabilities.
     *
     * @since 0.3.0.5
     */
    public Observation() {
        super();
    }

    /**
     * Copy-constructor.
     *
     * @param poObservation the Observation object to copy properties of
     * @since 0.3.0.5
     */
    public Observation(final Observation poObservation) {
        // This will copy StatisticalObject and WordStats members.
        super(poObservation);

        // Copy ours
        this.dPriorProbability = poObservation.getPriorProbability();
        this.dPosteriorProbability = poObservation.getPosteriorProbability();
        this.bSeen = poObservation.wasSeen();
    }

    /**
     * Indicates whether this observation was actually seen.
     *
     * @return <code>true</code> if it was seen
     * @since 0.3.0.5
     */
    public boolean wasSeen() {
        return this.bSeen;
    }

    /**
     * Marks this observation as seen.
     *
     * @since 0.3.0.5
     */
    public void markAsSeen() {
        this.bSeen = true;
    }

    /**
     * Allows querying for the current value of the posterior probability.
     *
     * @return the current posterior probability of this observation
     * @since 0.3.0.5
     */
    public double getPosteriorProbability() {
        return this.dPosteriorProbability;
    }

    /**
     * Allows setting the new value of the posterior probability.
     *
     * @param pdPosteriorProbability the posterior probability to set
     * @since 0.3.0.5
     */
    public void setPosteriorProbability(double pdPosteriorProbability) {
        this.dPosteriorProbability = pdPosteriorProbability;
    }

    /**
     * Allows querying for the current value of the prior probability.
     *
     * @return the current prior probability of this observation
     * @since 0.3.0.5
     */
    public double getPriorProbability() {
        return this.dPriorProbability;
    }

    /**
     * Allows setting the new value of the prior probability.
     *
     * @param pdPriorProbability the prior probability to set
     * @since 0.3.0.5
     */
    public void setPriorProbability(double pdPriorProbability) {
        this.dPriorProbability = pdPriorProbability;
    }

    /**
     * Implements Cloneable interface for the Observation object.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        return new Observation(this);
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.12 $";
    }
}

// EOF
