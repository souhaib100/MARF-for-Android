package marf.util;

/**
 * <p>Class BaseThread is customized base class for many of our own threads.</p>
 * <p>
 * <p>It provides an attempt to maintain an automatic unique TID (thread ID)
 * among all the derivatives and allow setting your own if needed, integrates
 * with ExpandedThreadGroup, and maintains a local reference for the Runnable
 * target if clients need it.</p>
 * <p>
 * <p>Java 1.5 NOTE: In this Java version they finally managed to provide
 * a method similar to our <code>getTID()</code>, called <code>getId()</code>
 * and this class was created prior that. And the functionality we offer
 * seems to be superior anyway.</p>
 * <p>
 * $Id: BaseThread.java,v 1.16 2005/06/20 05:36:43 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.16 $
 * @see ExpandedThreadGroup
 * @see Runnable
 * @see #iTID
 * @see #getNextTID()
 * @see #getTID()
 * @since 0.3.0
 */
public class BaseThread
        extends Thread {
    /*
     * ------------
	 * Data members
	 * ------------
	 */

    /**
     * Next TID to be assigned.
     * Preserves value across all instances.
     */
    protected static int siNextTID = 1;

    /**
     * Our Thread ID.
     */
    protected int iTID;

    /**
     * Local references to the target, in case clients
     * need it.
     */
    protected Runnable oTarget = null;

	/*
     * ------------
	 * Constructors
	 * ------------
	 */

    /**
     * Default constructor.
     * Calls Thread's constructor and setTID().
     */
    public BaseThread() {
        super();
        setTID();
    }

    /**
     * Constructor with Runnable.
     * Calls Thread's constructor with the Runnable argument and setTID().
     *
     * @param poTarget runnable Thread-like object.
     */
    public BaseThread(Runnable poTarget) {
        super(poTarget);
        setTID();
    }

    /**
     * Constructor with Runnable and thread name.
     * Equivalent to this(null, poTarget, pstrName).
     *
     * @param poTarget runnable Thread-like object.
     * @param pstrName name of the thread.
     */
    public BaseThread(Runnable poTarget, String pstrName) {
        this(null, poTarget, pstrName);
    }

    /**
     * Constructor with ThreadGroup, Runnable, and thread name.
     * Equivalent to super(poThreadGroup, poTarget, pstrName), but
     * overridden to call setTID().
     *
     * @param poThreadGroup ThreadGroup to add this thread to
     * @param poTarget      runnable Thread-like object.
     * @param pstrName      name of the thread.
     */
    public BaseThread(ThreadGroup poThreadGroup, Runnable poTarget, String pstrName) {
        super(poThreadGroup, poTarget, pstrName);
        this.oTarget = poTarget;
        setTID();
    }

    /**
     * Assigns name to the thread and places it to the specified group.
     * Equivalent to BaseThread(poThreadGroup, null, pstrName).
     *
     * @param poThreadGroup ThreadGroup to add this thread to
     * @param pstrName      A string indicating human-readable thread's name
     */
    public BaseThread(ThreadGroup poThreadGroup, String pstrName) {
        this(poThreadGroup, null, pstrName);
    }

    /**
     * Assigns name to the thread.
     * Equivalent to BaseThread(null, null, pstrName).
     *
     * @param pstrName A string indicating human-readable thread's name
     */
    public BaseThread(String pstrName) {
        this(null, null, pstrName);
    }

    /**
     * Assigns name to the thread and places it to the specified expanded group.
     * Calls this(poThreadGroup, null, pstrName), but also keeps a local reference
     * to this thread inside the group.
     *
     * @param poThreadGroup ExpandedThreadGroup to add this thread to
     * @param pstrName      A string indicating human-readable thread's name
     */
    public BaseThread(ExpandedThreadGroup poThreadGroup, String pstrName) {
        this(poThreadGroup, null, pstrName);
    }

    /**
     * Assigns name to the thread and places it to the specified expanded group.
     * Calls this(poThreadGroup, poTarget, null), but also keeps a local reference
     * to this thread inside the group.
     *
     * @param poThreadGroup ExpandedThreadGroup to add this thread to
     * @param poTarget      runnable Thread-like object.
     */
    public BaseThread(ExpandedThreadGroup poThreadGroup, Runnable poTarget) {
        this(poThreadGroup, poTarget, null);
    }

    /**
     * Assigns name to the thread and places it to the specified expanded group.
     * Does super(poThreadGroup, poTarget, pstrName), but also keeps a local reference
     * to this thread inside the group.
     *
     * @param poThreadGroup ExpandedThreadGroup to add this thread to
     * @param poTarget      runnable Thread-like object.
     * @param pstrName      A string indicating human-readable thread's name
     */
    public BaseThread(ExpandedThreadGroup poThreadGroup, Runnable poTarget, String pstrName) {
		/*
		 * It is important not to have a null name in here,
		 * as Thread always assumes some kind of default, and
		 * does some operations on it during initialization.
		 */
        super
                (
                        poThreadGroup,
                        poTarget,
                        pstrName == null ?
                                BaseThread.class.getName() + ", " + getMARFSourceCodeRevision() + ", TID=" + getNextTID() :
                                pstrName
                );

		/*
		 * This scenario is very possible when using convenience
		 * constructos with this() call as this() chooses as the
		 * base the constructor we are in, instead of
		 * BaseThread(ThreadGroup, Runnable, String).
		 */
        if (poThreadGroup != null)
            poThreadGroup.addThread(this);

        this.oTarget = poTarget;
        setTID();
    }

    /**
     * Sets user-specified thread ID and resets the sequencing from it.
     *
     * @param piTID starting TID
     */
    public BaseThread(final int piTID) {
        siNextTID = piTID;
        setTID();
    }

    /**
     * Retrieves our TID.
     *
     * @return TID, integer
     */
    public final int getTID() {
        return this.iTID;
    }

    /**
     * Sets internal TID and updates next TID on contruction time, so it's private.
     */
    private synchronized final void setTID() {
        this.iTID = siNextTID++;
    }

    /**
     * Lets to examine the next TID to be assigned.
     *
     * @return next TID, integer
     */
    public synchronized static final int getNextTID() {
        return siNextTID;
    }

    /**
     * Retrieves the thread's target.
     * Proved to be usefult in some caes, e.g. in the GIPSY,
     * needed to know who was actually the target posmortem.
     *
     * @return Returns the Runnable target set during construction
     */
    public Runnable getTarget() {
        return this.oTarget;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.16 $";
    }
}

// EOF
