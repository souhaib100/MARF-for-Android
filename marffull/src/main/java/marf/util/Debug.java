package marf.util;


/**
 * <p>Debugging Facility.</p>
 * <p>
 * <p>$Id: Debug.java,v 1.16 2006/01/18 19:31:14 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.16 $
 * @since 0.3.0.2
 */
public class Debug
        extends Logger {
    /**
     * Default filename to log the debug output to.
     *
     * @since 0.3.0.4
     */
    public static final String DEFAULT_DEBUG_LOG_FILENAME = "debug.log";

    /**
     * Public debug flag.
     * Notice, multithreaded clients must synchronized on this
     * themselves if they use the flag directly.
     */
    public static boolean sbDebugOn = false;

    /**
     * Needed for logger.
     * Sets the default filename to debug.log
     *
     * @throws Exception if parent does
     */
    protected Debug()
            throws Exception {
        super(DEFAULT_DEBUG_LOG_FILENAME);
    }

    /**
     * Enables or disables debugging based on the paramter.
     * This method is properly synchronized.
     *
     * @param pbEnable to set the debug flag to
     * @return old value of the debug flag
     */
    public static synchronized final boolean enableDebug(boolean pbEnable) {
        boolean bOldValue = sbDebugOn;
        sbDebugOn = pbEnable;
        return bOldValue;
    }

    /**
     * Enables debugging.
     * This method is properly synchronized.
     *
     * @return old value of the debug flag
     */
    public static synchronized final boolean enableDebug() {
        return enableDebug(true);
    }

    /**
     * Allows to atomically query the debug flag.
     *
     * @return the current state of the debug flag
     * @since 0.3.0.4
     */
    public static synchronized final boolean isDebugOn() {
        return sbDebugOn;
    }

    /**
     * Issues a debug message if the flag is on.
     * The message can have the trailing EOL or not based
     * on the 2nd argument. This method is properly synchronized.
     *
     * @param pstrMsgString desired debug message to be issued
     * @param pbEOLNeeded   <code>true</code> if the trailing EOL is desired.
     */
    public static synchronized final void debug(final String pstrMsgString, final boolean pbEOLNeeded) {
        if (sbDebugOn) {
            if (pbEOLNeeded) {
                System.err.println(pstrMsgString);
            } else {
                System.err.print(pstrMsgString);
            }
        }
    }

    /**
     * Issues a debug message if the flag is on with a trailing EOL.
     * This method is properly synchronized.
     *
     * @param pstrMsgString desired debug message to be issued
     */
    public static synchronized final void debug(final String pstrMsgString) {
        debug(pstrMsgString, true);
    }

    /**
     * Issues a debug message if the flag is on with a trailing EOL
     * by calling <code>toString()</code> of the parameter.
     * This method is properly synchronized.
     *
     * @param poObject object to dump to the debug output
     * @since 0.3.0.3
     */
    public static synchronized final void debug(final Object poObject) {
        debug(poObject.toString(), true);
    }

    /**
     * Issues a debug message if the flag is on with a trailing EOL.
     * This method is properly synchronized.
     *
     * @param poClass       add class to extract the name from as a prefix
     * @param pstrMsgString desired debug message to be issued
     */
    public static synchronized final void debug(final Class poClass, final String pstrMsgString) {
        debug
                (
                        new StringBuffer()
                                .append(poClass.getName())
                                .append(":")
                                .append(pstrMsgString)
                );
    }

    /**
     * Issues an empty debug message if the flag is on with a trailing EOL.
     * This method is properly synchronized.
     */
    public static synchronized final void debug() {
        debug("");
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
