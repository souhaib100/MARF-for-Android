package marf.Preprocessing;


/**
 * An interface all filters must comply with.
 *
 * @author Serguei Mokhov
 * @since 0.3.0.4
 */
public interface IFilter {
    /**
     * Interface source code revision.
     */
    String MARF_INTERFACE_CODE_REVISION = "$Revision: 1.1 $";

    /**
     * Applies filtering to the sample array and buffers
     * the filtered data.
     *
     * @param padSample   original sample to apply filtering to; should not be altered
     * @param padFiltered buffer for filtered data
     * @return <code>true<code> if filtering was successful
     * @throws PreprocessingException if any error happened during filtering
     */
    public boolean filter(final double[] padSample, double[] padFiltered)
            throws PreprocessingException;
}

// EOF
