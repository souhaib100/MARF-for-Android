package marf.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * <p>Command-Line Option Processing Facilitating Utility.
 * Helps to maintain and validate command-line options and their arguments.
 * The class is properly synchronized as of 0.3.0.4.</p>
 * <p>
 * <p>$Id: OptionProcessor.java,v 1.36 2006/01/27 19:15:54 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.36 $
 * @since 0.3.0.2
 */
public class OptionProcessor {
    /**
     * Indicates undefined option (-1).
     */
    public static final int UNDEF = -1;

    /**
     * A hash-table that contains valid string options map.
     */
    protected Hashtable oValidOptionsStrings = new OptionsHashtable();

    /**
     * A hash-table that contains valid numerical options map.
     *
     * @since 0.3.0.3
     */
    protected Hashtable oValidOptionsNumbers = new OptionsHashtable();

    /**
     * A hash-table that contains active string options map.
     * A proper subset of valid options.
     */
    protected Hashtable oActiveOptionsStrings = new OptionsHashtable();

    /**
     * A hash-table that contains active numerical options amp.
     * A proper subset of valid options.
     *
     * @since 0.3.0.3
     */
    protected Hashtable oActiveOptionsNumbers = new OptionsHashtable();

    /**
     * A vector that contains invalid options for
     * error reporting.
     */
    protected Vector oInvalidOptions = new Vector();


    /**
     * Constructs options-free OptionProcessor.
     */
    public OptionProcessor() {
    }

    /**
     * Constructs <code>OptionProcessor</code> with known list of
     * options from a <code>Hashtable</code> by converting its
     * contents to internal data structures.
     * This expects the <code>Hashtable</code> to contain
     * <code>String</code>:<code>Integer</code> pairs;
     * the pairs that do not conform to this requirement are added to
     * the invalid options collection.
     *
     * @param poValidOptions a collection of valid options; if null then
     *                       the internal hashtable remains empty and is <b>not</b> set to null.
     */
    public OptionProcessor(final Hashtable poValidOptions) {
        // Avoid setting null options
        if (poValidOptions != null) {
            Enumeration oKeys = poValidOptions.keys();

            while (oKeys.hasMoreElements()) {
                Object oStringKey = oKeys.nextElement();
                Object oIntegerValue = poValidOptions.get(oStringKey);

                if (oStringKey instanceof String) {
                    if (oIntegerValue instanceof Integer) {
                        addValidOption(new Option((Integer) oIntegerValue, oStringKey.toString()));
                        continue;
                    }
                }

                // This will only happen if the Hashtable contains
                // something else than String:Integer
                this.oInvalidOptions.add(oStringKey + ":" + oIntegerValue);
            }
        }
    }

    /**
     * Allows adding a valid option to the set of valid options.
     * If the entry with that name was already in the set, it
     * gets replaced.
     *
     * @param piOptionCode     option's numerical code to use, a value
     * @param pstrOptionString option's lexical representation, a key
     */
    public synchronized final void addValidOption(final int piOptionCode, final String pstrOptionString) {
        addValidOption(new Option(piOptionCode, pstrOptionString));
    }

    /**
     * Allows adding a valid option to the set of valid options indicating
     * the necessity of argument to it. If the entry with that name was
     * already in the set, it gets replaced.
     *
     * @param piOptionCode       option's numerical code to use, a value
     * @param pstrOptionString   option's lexical representation, a key
     * @param pbRequiresArgument set to <code>true</code> if the option
     *                           requires argument. An exception will be thrown during parsing
     *                           if an option that requires an argument doesn't get one.
     * @see #parse(String[])
     * @since 0.3.0.3
     */
    public synchronized final void addValidOption(final int piOptionCode, final String pstrOptionString, boolean pbRequiresArgument) {
        addValidOption(new Option(piOptionCode, pstrOptionString, pbRequiresArgument));
    }

    /**
     * Adds valid option. This is an Option object helper method.
     *
     * @param poOption option object to add to valid options
     * @since 0.3.0.3
     */
    protected synchronized final void addValidOption(final Option poOption) {
        this.oValidOptionsStrings.put(poOption.getOptionName(), poOption);
        this.oValidOptionsNumbers.put(new Integer(poOption.getOptionEnumeration()), poOption);
    }

    /**
     * Allows adding an active option to the set of active options directly.
     * If the entry with that name was already in the set, it
     * gets replaced.
     * Also makes sure that the active option is also added to the
     * valid options set.
     *
     * @param piOptionCode     option's numerical code to use, a value
     * @param pstrOptionString option's lexical representation, a key
     */
    public synchronized final void addActiveOption(final int piOptionCode, final String pstrOptionString) {
        addActiveOption(new Option(piOptionCode, pstrOptionString));
    }

    /**
     * Adds active option. This is an Option object helper method.
     *
     * @param poOption option object to add to active options
     * @since 0.3.0.3
     */
    protected synchronized final void addActiveOption(final Option poOption) {
        this.oActiveOptionsStrings.put(poOption.getOptionName(), poOption);
        this.oActiveOptionsNumbers.put(new Integer(poOption.getOptionEnumeration()), poOption);
        addValidOption(poOption);
    }

    /**
     * Clears out all the option lists of this option processor.
     *
     * @since 0.3.0.3
     */
    public synchronized void clear() {
        this.oActiveOptionsStrings.clear();
        this.oActiveOptionsNumbers.clear();
        this.oValidOptionsStrings.clear();
        this.oValidOptionsNumbers.clear();
        this.oInvalidOptions.clear();
    }

    /**
     * Retrieves the option count currently known to the option
     * processor as a sum of active and invalid options. Valid
     * options are omitted from the count because they are more
     * of a static state comparted to the currently active
     * subset of them as well as possibly invalid options, which
     * represent more of a dynamic state.
     *
     * @return active plus invalid options count
     * @since 0.3.0.4
     */
    public synchronized int size() {
        return
                this.oActiveOptionsStrings.size() +
                        this.oInvalidOptions.size();
    }

    /**
     * Returns active and invalid options (in that order) in a form
     * suitable to feed back as an argument vector <code>argv</code>
     * to another application's <code>main()</code>. Essentially,
     * this allows reconstruct the original <code>argv</code> except
     * that the only ordering guaranteed is that all active are
     * followed by all invalid options.
     *
     * @return array of strings of active and invalid options
     * @since 0.3.0.4
     */
    public synchronized String[] getArgumentVector() {
        String[] astrActiveOptions = new String[this.oActiveOptionsStrings.size()];
        Arrays.copy(astrActiveOptions, 0, this.oActiveOptionsStrings.keySet().toArray());

        String[] astrInvalidOptions = new String[this.oInvalidOptions.size()];
        Arrays.copy(astrInvalidOptions, 0, this.oInvalidOptions.toArray());

        return Arrays.concatenate(astrActiveOptions, astrInvalidOptions);
    }

    /**
     * Returns active and invalid options (in that order) in a form
     * suitable to feed back as an argument string to an external
     * application.
     *
     * @return a space-delimited String of active and invalid options
     * @since 0.3.0.5
     */
    public synchronized String getArgumentString() {
        return Arrays.arrayToString(getArgumentVector());
    }

    /**
     * <p>Parses option strings from the argument vector.
     * Does not alter the argument vector itself, but initializes
     * internal active and invalid options. If the same options occurs
     * more than once, the last occurrence only takes effect.</p>
     * <p>
     * <p><b>NOTICE</b>, since 0.3.0.3 '=' has a special meaning
     * as a separator bewteen options and values, and, therefore,
     * cannot appear inside option strings or by itself on the
     * command line.</p>
     *
     * @param argv argument vector (array of command-line options)
     * @return number of valid options found
     * @throws RuntimeException if some valid option that requires an argument
     *                          doesn't have one.
     */
    public synchronized final int parse(final String[] argv) {
        int iValidOptions = argv.length;

        for (int i = 0; i < argv.length; i++) {
            Debug.debug("[" + argv[i] + "]");

            Option oOption = (Option) oValidOptionsStrings.get(argv[i]);

            // Possinbly an invalid option
            if (oOption == null) {
                // Check for "=" on its own
                if (argv[i].equals("=")) {
                    throw new RuntimeException
                            (
                                    "Invalid option '=' found.\n" +
                                            "HINT: did you intend an option argument? If so, please use\n" +
                                            "      the form of 'option=argument' with no blanks around '='"
                            );
                }

                // See if we got "option=value" case
                String[] astrOptValue = argv[i].split("=");

                if (astrOptValue.length == 2) {
                    Debug.debug(astrOptValue[0] + " -> " + astrOptValue[1]);

                    // Check if the "option" part is valid
                    oOption = (Option) oValidOptionsStrings.get(astrOptValue[0]);

                    // If so, set its argument
                    if (oOption != null) {
                        oOption.setOptionArgument(astrOptValue[1]);
                        addActiveOption(oOption);
                        continue;
                    }
                }

				/*
                 * Only count the option as invalid if it's not blank.
				 * This infrequent scenario is possible when argv is
				 * generated programmatically and not from a command line,
				 * and as such should simply be ignored. This still
				 * however means that we have one valid option less, so
				 * the counting is outside the if().
				 */
                if (argv[i].equals("") == false) {
                    this.oInvalidOptions.add(argv[i]);
                }

                iValidOptions--;
            }

            // Update existing entry to be active
            else {
                if
                        (
                        oOption.requiresArgument() &&
                                (
                                        oOption.getOptionArgument() == null ||
                                                oOption.getOptionArgument().equals("")
                                )
                        ) {
                    throw new RuntimeException("Option " + oOption + " requires an argument.");
                }

                addActiveOption(oOption);
            }
        }

        return iValidOptions;
    }

    /**
     * Returns numerical equivalent of an option string.
     *
     * @param pstrOption option string to look the integer equivalent for
     * @return int, the option enumeration value
     * @throws RuntimeException if the named option is not found to be active/valid
     */
    public synchronized final int getOption(final String pstrOption) {
        return getOption(pstrOption, false);
    }

    /**
     * Returns numerical equivalent of an option string.
     *
     * @param pstrOption      option string to look the integer equivalent for
     * @param pbReturnOnError tells getOption to return UNDEF instead of throwing an
     *                        exception if set to <code>true</>
     * @return int, the option enumeration value
     * @throws RuntimeException if the named option is not found to be active/valid
     */
    public synchronized final int getOption(final String pstrOption, boolean pbReturnOnError) {
        Debug.debug
                (
                        "Active Option requested: " + pstrOption +
                                " opts: " + this.oActiveOptionsStrings
                );

        Option oOption = (Option) oActiveOptionsStrings.get(pstrOption);

        // Attempt to see of the parameter option has an argument.
        if (oOption == null) {
            oOption = (Option) oActiveOptionsStrings.get(pstrOption.split("=")[0]);
        }

        // General processing
        if (oOption == null) {
            if (pbReturnOnError == false) {
                String strCommonHint =
                        "HINT: make sure you call OptionProcessor.parse() first or\n" +
                                "explicitly add the option as active using OptionProcessor.addActiveOption().";

                if (isInvalidOption(pstrOption)) {
                    throw new RuntimeException
                            (
                                    "Option [" + pstrOption + "] is listed as invalid."
                            );
                } else if (isValidOption(pstrOption)) {
                    throw new RuntimeException
                            (
                                    "Option [" + pstrOption + "] is listed as valid, but " +
                                            "is not listed as active.\n" + strCommonHint
                            );
                }

                // Deals with the case when the option isn't listed anywhere gracefully
                else {
                    throw new RuntimeException
                            (
                                    "Nothing's known about the [" + pstrOption + "] option.\n" +
                                            strCommonHint
                            );
                }
            } else {
                return UNDEF;
            }
        }

        return oOption.getOptionEnumeration();
    }

    /**
     * Returns string equivalent of a numerical option.
     *
     * @param piOption option to look the String equivalent for
     * @return the option string
     * @throws RuntimeException if the named option is not found to be active/valid
     */
    public synchronized final String getOption(int piOption) {
        return getOption(piOption, false);
    }

    /**
     * Returns string equivalent of a numerical option.
     *
     * @param piOption        option to look the String equivalent for
     * @param pbReturnOnError tells getOption to return an empty string instead of throwing an
     *                        exception if set to <code>true</>
     * @return the option string
     * @throws RuntimeException if the named option is not found to be active/valid
     */
    public synchronized final String getOption(int piOption, boolean pbReturnOnError) {
        Debug.debug
                (
                        "Active Option requested: " + piOption +
                                " opts: " + this.oActiveOptionsStrings
                );

        Integer oIntegerOption = new Integer(piOption);

        if (this.oActiveOptionsNumbers.containsKey(oIntegerOption)) {
            Option oOption = (Option) this.oActiveOptionsNumbers.get(oIntegerOption);

            if (oOption != null) {
                return oOption.getOptionName();
            }

            if (pbReturnOnError == false) {
                throw new RuntimeException
                        (
                                "Internal error: Corresponding key was not found for " +
                                        "option " + piOption + ".\n"
                        );
            }
        } else {
            if (pbReturnOnError == false) {
                throw new RuntimeException
                        (
                                "There is no key associated with this option value (" + piOption + ")."
                        );
            }
        }

        return "";
    }

    /**
     * Allows querying for option argument by option name.
     *
     * @param pstrOption option name
     * @return option argument
     * @since 0.3.0.3
     */
    public synchronized String getOptionArgument(final String pstrOption) {
        return getOptionArgument(pstrOption, false);
    }

    /**
     * Allows querying for option argument by option name.
     *
     * @param pstrOption      option name
     * @param pbReturnOnError tells to return an empty string instead of throwing an
     *                        exception if set to <code>true</>
     * @return option argument
     * @since 0.3.0.3
     */
    public synchronized String getOptionArgument(final String pstrOption, boolean pbReturnOnError) {
        Option oOption =
                (Option) this.oActiveOptionsNumbers.get
                        (
                                new Integer(getOption(pstrOption, pbReturnOnError))
                        );

        if (oOption == null) {
            return "";
        }

        return oOption.getOptionArgument();
    }

    /**
     * Allows querying for option argument by option number.
     *
     * @param piOption option number
     * @return option argument
     * @since 0.3.0.3
     */
    public synchronized String getOptionArgument(final int piOption) {
        return getOptionArgument(piOption, false);
    }

    /**
     * Allows querying for option argument by option number.
     *
     * @param piOption        option number
     * @param pbReturnOnError tells to return an empty string instead of throwing an
     *                        exception if set to <code>true</>
     * @return option argument
     * @since 0.3.0.3
     */
    public synchronized String getOptionArgument(final int piOption, boolean pbReturnOnError) {
        Option oOption =
                (Option) this.oActiveOptionsStrings.get
                        (
                                getOption(piOption, pbReturnOnError)
                        );

        if (oOption == null) {
            return "";
        }

        return oOption.getOptionArgument();
    }

    /**
     * Allows querying for the set of invalid options.
     *
     * @return Vector, the list of rejected options
     */
    public synchronized final Vector getInvalidOptions() {
        return this.oInvalidOptions;
    }

    /**
     * Allows querying for the set of active options.
     *
     * @return Hashtable, containing the mapping of active options
     */
    public synchronized final Hashtable getActiveOptions() {
        return this.oActiveOptionsStrings;
    }

    /**
     * Allows querying for the set of active options.
     *
     * @return Hashtable, containing the mapping of active options
     */
    public synchronized final Hashtable getValidOptions() {
        return this.oValidOptionsStrings;
    }

    /**
     * Checks whether supplied string option is active.
     *
     * @param pstrOption option to verify
     * @return <code>true</code> if the option is active, <code>false</code> otherwise
     */
    public synchronized boolean isActiveOption(final String pstrOption) {
        boolean bActive = this.oActiveOptionsStrings.containsKey(pstrOption);

        // Check for options with arguments
        if (bActive == false) {
            String[] astrOptValue = pstrOption.split("=");

            if (astrOptValue.length == 2) {
                bActive = this.oActiveOptionsStrings.containsKey(astrOptValue[0]);
            }
        }

        return bActive;
    }

    /**
     * Checks whether supplied integer option is active.
     *
     * @param piOption option to verify
     * @return <code>true</code> if the option is active, <code>false</code> otherwise
     */
    public synchronized boolean isActiveOption(int piOption) {
        return this.oActiveOptionsNumbers.containsKey(new Integer(piOption));
    }

    /**
     * Checks whether supplied string option is valid.
     *
     * @param pstrOption option to verify
     * @return <code>true</code> if the option is valid, <code>false</code> otherwise
     */
    public synchronized boolean isValidOption(String pstrOption) {
        return this.oValidOptionsStrings.containsKey(pstrOption);
    }

    /**
     * Checks whether supplied integer option is valid.
     *
     * @param piOption option to verify
     * @return <code>true</code> if the option is active, <code>false</code> otherwise
     */
    public synchronized boolean isValidOption(int piOption) {
        return this.oValidOptionsNumbers.containsKey(new Integer(piOption));
    }

    /**
     * Checks whether supplied string option is invalid.
     *
     * @param pstrOption option to verify
     * @return <code>true</code> if the option is invalid, <code>false</code> otherwise
     */
    public synchronized boolean isInvalidOption(String pstrOption) {
        return this.oInvalidOptions.contains(pstrOption);
    }

    /**
     * Converts the internals of the OptionProcessor to a string
     * and returns it. Very useful for debugging.
     *
     * @return String representation of an OptionProcessor object
     */
    public synchronized String toString() {
        StringBuffer oBuffer = new StringBuffer();

        oBuffer
                .append("Valid options: ").append(this.oValidOptionsStrings).append("\n")
                .append("Active options: ").append(this.oActiveOptionsStrings).append("\n")
                .append("Invalid options: ").append(this.oInvalidOptions).append("\n");

        return oBuffer.toString();
    }

    /**
     * Main Option placeholder.
     *
     * @author Serguei Mokhov
     * @since 0.3.0.3
     */
    protected class Option {
        /**
         * Internal container for option name; default is "UNDEFINED".
         */
        protected String strOptionName = "UNDEFINED";

        /**
         * Internal container for option argument; default is "<UNDEFINED>".
         */
        protected String strOptionArgument = "<UNDEFINED>";

        /**
         * Internal container for integer equivalent; default is UNDEF.
         *
         * @see OptionProcessor#UNDEF
         */
        protected int iOptionEnum = UNDEF;

        /**
         * A flag is set to <code>true</code> if the option
         * has a mandatory argument.
         */
        protected boolean bRequiresArgument = false;

        /**
         * Vanilla integer/string option constructor.
         * Equivalent to <code>Option(piOption, pstrOption, false)</code>.
         *
         * @param piOption   integer option equivalent
         * @param pstrOption String option equivalent
         */
        public Option(int piOption, String pstrOption) {
            this(piOption, pstrOption, false);
        }

        /**
         * Vanilla integer/string option constructor.
         * Equivalent to <code>Option(piOption, pstrOption, false)</code>.
         *
         * @param poOption   integer option equivalent
         * @param pstrOption String option equivalent
         * @since 0.3.0.5
         */
        public Option(Integer poOption, String pstrOption) {
            this(poOption.intValue(), pstrOption, false);
        }

        /**
         * Integer/string option constructor with required argument indication.
         *
         * @param piOption           integer option equivalent
         * @param pstrOption         String option equivalent
         * @param pbRequiresArgument <code>true</code> if option requires an argument
         */
        public Option(int piOption, String pstrOption, boolean pbRequiresArgument) {
            this.iOptionEnum = piOption;
            this.strOptionName = pstrOption;
            this.bRequiresArgument = pbRequiresArgument;
        }

        /**
         * Integer/string option constructor with required argument.
         *
         * @param piOption             integer option equivalent
         * @param pstrOption           String option equivalent
         * @param pstrRequiredArgument the option argument
         */
        public Option(int piOption, String pstrOption, String pstrRequiredArgument) {
            this(piOption, pstrOption, true);
            this.strOptionArgument = pstrRequiredArgument;
        }

        /**
         * Tells if this option requires an argument.
         *
         * @return <code>true</code> if the argument is required
         */
        public synchronized boolean requiresArgument() {
            return this.bRequiresArgument;
        }

        /**
         * Allows to specify whether this option requires an argument or not.
         *
         * @param pbRequiresArgument <code>true</code> if the argument is required
         */
        public synchronized void requiresArgument(boolean pbRequiresArgument) {
            this.bRequiresArgument = pbRequiresArgument;
        }

        /**
         * Retrieves the option number.
         *
         * @return the enumeration option value
         */
        public synchronized int getOptionEnumeration() {
            return this.iOptionEnum;
        }

        /**
         * Allows setting the option number.
         *
         * @param piOptionEnum the option number to set
         */
        public synchronized void setOptionEnumeration(int piOptionEnum) {
            this.iOptionEnum = piOptionEnum;
        }

        /**
         * Retrieves the option argument string.
         *
         * @return the argument.
         */
        public synchronized String getOptionArgument() {
            return this.strOptionArgument;
        }

        /**
         * Allows setting the option argument.
         *
         * @param pstrOptionArgument the option argument to set.
         */
        public synchronized void setOptionArgument(String pstrOptionArgument) {
            this.strOptionArgument = pstrOptionArgument;
        }

        /**
         * Rerieves the option name.
         *
         * @return the name
         */
        public synchronized String getOptionName() {
            return this.strOptionName;
        }

        /**
         * Allows setting the option name.
         *
         * @param pstrOptionName the option name to set
         */
        public synchronized void setStrOptionName(String pstrOptionName) {
            this.strOptionName = pstrOptionName;
        }

        /**
         * String representation of the option.
         *
         * @return String containing option name, its numeric value, and its argument
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return new StringBuffer()
                    .append(this.strOptionName).append(" (").append(this.iOptionEnum).append(")")
                    .append((this.bRequiresArgument ? " = '" + this.strOptionArgument + "'" : ""))
                    .toString();
        }
    }

    /**
     * <p>A hashtable designed for to hold options.
     * <p>
     * TODO: when we drop support for JDK1.4, this should be typechecked
     * for Option type only.
     * </p>
     *
     * @author Serguei Mokhov
     * @see OptionProcessor.Option
     * @since 0.3.0.3
     */
    protected class OptionsHashtable
            extends Hashtable {
        /**
         * For serialization versioning.
         * When adding new members or make other structural
         * changes regenerate this number with the
         * <code>serialver</code> tool that comes with JDK.
         *
         * @since 0.3.0.4
         */
        private static final long serialVersionUID = 8686199546957797968L;

        /**
         * Merely calls the parent's constructor.
         */
        public OptionsHashtable() {
            super();
        }
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.36 $";
    }
}

// EOF
