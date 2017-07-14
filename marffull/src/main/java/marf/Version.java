package marf;

import marf.util.MARFException;


/**
 * <p>Responsible for providing and validating version information of MARF.<p>
 * <p>
 * $Id: Version.java,v 1.1 2006/01/14 19:06:19 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @since 0.3.0.5
 */
public class Version {
    /**
     * Indicates major MARF version, like <b>1</b>.x.x.
     */
    public static final int MAJOR_VERSION = 0;

    /**
     * Indicates minor MARF version, like 1.<b>1</b>.x.
     */
    public static final int MINOR_VERSION = 3;

    /**
     * Indicates MARF revision, like 1.1.<b>1</b>.
     */
    public static final int REVISION = 0;

    /**
     * Indicates MARF minor development revision, like 1.1.1.<b>1</b>.
     * This is primarily for development releases. On the final release
     * the counting stops and is reset to 0 every minor version.
     *
     * @see #MINOR_VERSION
     */
    public static final int MINOR_REVISION = 5;

    /**
     * Should be automatically replaced through scripting
     * when compiling smaller binaries of MARF, e.g.
     * marf-util, marf-math, or marf-nlp, etc. packages,
     * the corresponding suffix (i.e. "util", "nlp", ...)
     * should end up being in this constant and then compiled.
     * This suffix is appended all string varians of the version.
     * The default fat MARF should not contain anything.
     */
    private static final String PACKAGE = "";

    /**
     * Returns a string representation of the MARF version.
     *
     * @return version String
     * @see #MINOR_REVISION
     */
    public static final String getStringVersion() {
        return
                MAJOR_VERSION + "." +
                        MINOR_VERSION + "." +
                        REVISION + "." +
                        MINOR_REVISION +

                        // Will only be activated through scripting PACKAGE
                        // at the distro time.
                        (PACKAGE.equals("") ? "" : "-" + PACKAGE);
    }

    /**
     * Returns a string representation of the MARF version
     * given its floating point equivalent. E.g. 30.5 becomes "0.3.0.5".
     *
     * @param pdDoubleVersion the floating point equivalent of the version
     * @return version String
     */
    public static final String getStringVersion(double pdDoubleVersion) {
        int iMinorRevision = (int) (Math.abs((int) pdDoubleVersion - pdDoubleVersion) * 10);
        int iMajorVersion = (int) ((pdDoubleVersion - iMinorRevision * 0.1) / 100);
        int iMinorVersion = (int) ((pdDoubleVersion - iMajorVersion * 100 - iMinorRevision * 0.1) / 10);
        int iRevision = (int) ((pdDoubleVersion - iMajorVersion * 100 - iMinorVersion * 10 - iMinorRevision * 0.1));

        return
                iMajorVersion + "." +
                        iMinorVersion + "." +
                        iRevision + "." +
                        iMinorRevision +

                        // Will only be activated through scripting PACKAGE
                        // at the distro time.
                        (PACKAGE.equals("") ? "" : "-" + PACKAGE);
    }

    /**
     * Returns an integer representation of the MARF version.
     * As of 0.3.0.3, MINOR_REVISION is included into calculations
     * and the formula changed to begin with 1000 as a MAJOR_VERSION
     * coefficient.
     *
     * @return integer version as <code>MAJOR_VERSION * 1000 + MINOR_VERSION * 100 + REVISION * 10 + MINOR_REVISION</code>
     * @see #MAJOR_VERSION
     * @see #MINOR_VERSION
     * @see #REVISION
     * @see #MINOR_REVISION
     */
    public static final int getIntVersion() {
        return
                MAJOR_VERSION * 1000 +
                        MINOR_VERSION * 100 +
                        REVISION * 10 +
                        MINOR_REVISION;
    }

    /**
     * Retrieves double version of MARF. Unlike the integer version, the double
     * one begins with 100 and the minor revision is returned after the point,
     * e.g. 123.4 for 1.2.3.4.
     *
     * @return double version as <code>MAJOR_VERSION * 100 + MINOR_VERSION * 10 + REVISION + MINOR_REVISION / 10</code>
     * @see #MAJOR_VERSION
     * @see #MINOR_VERSION
     * @see #REVISION
     * @see #MINOR_REVISION
     */
    public static final double getDoubleVersion() {
        return
                MAJOR_VERSION * 100 +
                        MINOR_VERSION * 10 +
                        REVISION +
                        (double) MINOR_REVISION / 10;
    }

    /**
     * Makes sure the applications isn't run against older MARF version.
     *
     * @param pdDoubleVersion floating point version representation to validate
     * @throws MARFException if the MARF's version is too old
     */
    public static final void validateVersions(double pdDoubleVersion)
            throws MARFException {
        if (getDoubleVersion() < pdDoubleVersion) {
            throw new MARFException
                    (
                            "Your MARF version (" + getStringVersion()
                                    + ") is too old. This application requires "
                                    + getStringVersion(pdDoubleVersion) + " or above."
                    );
        }
    }

    /**
     * Makes sure the applications isn't run against older MARF version.
     *
     * @param pstrStringVersion String representation of the required version
     * @throws MARFException        if the MARF's version is too old
     * @throws NullPointerException if the parameter is null
     */
    public static final void validateVersions(final String pstrStringVersion)
            throws MARFException {
        if (getStringVersion().compareTo(pstrStringVersion) < 0) {
            throw new MARFException
                    (
                            "Your MARF version (" + getStringVersion()
                                    + ") is too old. This application requires "
                                    + pstrStringVersion + " or above."
                    );
        }
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.1 $";
    }
}

// EOF