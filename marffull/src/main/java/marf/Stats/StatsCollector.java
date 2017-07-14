package marf.Stats;

import marf.util.NotImplementedException;

/**
 * <p>TODO: Implement.
 * <p>
 * Ideally we'd want to measure.
 * How long it takes for a particular module to do the thing.
 * How long it takes to run whole pipeline.
 * How many features...
 * Amount of noise and silence removed...
 * ...</p>
 * <p>
 * $Id: StatsCollector.java,v 1.8 2006/01/06 22:20:13 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.8 $
 * @since 0.0.1
 */
public class StatsCollector {
    /**
     * @throws NotImplementedException
     */
    public StatsCollector() {
        throw new NotImplementedException("StatsCollector.StatsCollector()");
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.8 $";
    }
}

// EOF
