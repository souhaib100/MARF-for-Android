package marf.Classification.NeuralNetwork;

import java.io.BufferedWriter;
import java.util.ArrayList;

import marf.util.BaseThread;


/**
 * <p>Class Neuron -- a basic element of a neural network.</p>
 * <p>
 * <p>$Id: Neuron.java,v 1.16 2006/01/23 00:03:34 mokhov Exp $</p>
 *
 * @author Ian Clement
 * @author Serguei Mokhov
 * @version $Revision: 1.16 $
 * @since 0.0.1
 */
public class Neuron
        extends BaseThread {
    /*
     * ----------------------
	 * Enumeration
	 * ----------------------
	 */

    /**
     * Indicates undefined neuron type.
     *
     * @since 0.1.2
     */
    public static final int UNDEF = -1;

    /**
     * Indicates input neuron.
     *
     * @since 0.1.2
     */
    public static final int INPUT = 0;

    /**
     * Indicates middle (hidden) neuron.
     *
     * @since 0.1.2
     */
    public static final int HIDDEN = 1;

    /**
     * Indicates output neuron.
     *
     * @since 0.1.2
     */
    public static final int OUTPUT = 2;


	/*
     * ----------------------
	 * Data Members
	 * ----------------------
	 */

    /**
     * Neuron's name.
     */
    protected String strName;

    /**
     * Current neuron type.
     */
    protected int iType = UNDEF;

    /**
     * Inputs of the current Neuron.
     */
    private ArrayList oInputs = new ArrayList();

    /**
     * Inputs' weights.
     */
    private ArrayList oWeights = new ArrayList();

    /**
     * Buffered weights to be comitted.
     */
    private ArrayList oWeightsBuffer = new ArrayList();

    /**
     * Outputs of the current Neuron.
     */
    private ArrayList oOutputs = new ArrayList();

    /**
     * Used in error calculation.
     */
    protected double dDelta = 0.0;

    /**
     * Activation threshhold.
     */
    protected double dThreshold = 0.0;

    /**
     * Current Neuron's result.
     */
    protected double dResult = 0.0;


	/*
	 * ----------------------
	 * Methods
	 * ----------------------
	 */

    /**
     * Neuron's Constructor.
     *
     * @param pstrName Neuron's name
     * @param piType   Neuron's type
     */
    public Neuron(String pstrName, int piType) {
        this.strName = new String(pstrName);
        this.iType = piType;
    }

    /**
     * Adds an input neuron and its associated weight.
     *
     * @param poInputNeuron Neuron to be added
     * @param pdWeight      associated weight
     * @return <code>true</code> if add was successful
     */
    public final boolean addInput(Neuron poInputNeuron, double pdWeight) {
        // XXX: Ian, did you really mean '&&' and not '&' in here?
        // because if inputs.add(in) returns false,
        // other parts don't get executed at all
/*		return
			inputs.add(in) &&
			weights.add(new Double(weight)) &&
			weightsBuf.add(new Double(weight));
*/
        return
                oInputs.add(poInputNeuron) &
                        oWeights.add(new Double(pdWeight)) &
                        oWeightsBuffer.add(new Double(pdWeight));

    }

    /**
     * Adds an output neuron.
     *
     * @param poOutputNeuron the Neuron to add
     * @return true if the neuron was added
     */
    public final boolean addOutput(Neuron poOutputNeuron) {
        return this.oOutputs.add(poOutputNeuron);
    }

    /**
     * Evaluates current neuron's value.
     */
    public final void eval() {
        if (this.iType == INPUT) {
            // Assumes that the result of an input neuron is == the input
            return;
        }

        // Nothing to evaluate if there are no inputs.
        if (this.oInputs.isEmpty()) {
            return;
        }

        double dCount = 0;

        for (int i = 0; i < this.oInputs.size(); i++) {
            dCount +=
                    ((Neuron) oInputs.get(i)).dResult *
                            ((Double) oWeights.get(i)).doubleValue();
        }

        dCount -= this.dThreshold;

        this.dResult = 1.0 / (1.0 + Math.exp(-dCount));

        //Debug.debug("Neuron: " + this.strName + ", Sum: " + dCount + ", Result: " + this.dResult);
    }

    /**
     * Retrieves specific neuron's weight.
     *
     * @param poNeuron Neuron to work on
     * @return weight (double) or -1.0 if not found.
     */
    private final double getWeight(final Neuron poNeuron) {
        int iIndex = this.oInputs.indexOf(poNeuron);

        if (iIndex >= 0) {
            return ((Double) oWeights.get(iIndex)).doubleValue();
        }

        //System.out.println("There is no pointer n in neuron");

        return -1.0;
    }

    /**
     * Neuron training.
     *
     * @param pdExpected expected value
     * @param pdAlpha    used in error calculation (training constant)
     * @param pdBeta     used in error calculation
     */
    public final void train(final double pdExpected, final double pdAlpha, final double pdBeta) {
        switch (this.iType) {
            // output nodes calculate delta differently based on expected result...
            case OUTPUT:
                this.dDelta = (pdExpected - this.dResult) * this.dResult * (1.0 - this.dResult);
                break;

            case HIDDEN: {
                double dSum = 0.0;

                for (int i = 0; i < this.oOutputs.size(); i++) {
                    dSum +=
                            ((Neuron) this.oOutputs.get(i)).dDelta *
                                    ((Neuron) this.oOutputs.get(i)).getWeight(this);
                }

                this.dDelta = this.dResult * (1.0 - this.dResult) * dSum;
                break;
            }

            // No need to train INPUT-type neurons
            case INPUT:
            default:
                return;
        }

        //Debug.debug("Neuron: " + this.strName + ", Delta: " + this.dDelta);

        // Buffer the new weights to commit them later
        for (int i = 0; i < this.oInputs.size(); i++) {
            this.oWeightsBuffer.set
                    (
                            i,
                            new Double
                                    (
                                            pdBeta * ((Double) this.oWeights.get(i)).doubleValue() +
                                                    pdAlpha * this.dDelta * ((Neuron) this.oInputs.get(i)).dResult
                                    )
                    );

            //double dArg = ((Double)this.oWeights.get(i)).doubleValue();
            //Debug.debug("\tNew weight " + i + ": " + dArg);
        }
    }

    /**
     * Applies weight changes.
     */
    public final void commit() {
        for (int i = 0; i < this.oWeights.size(); i++) {
            this.oWeights.set(i, new Double(((Double) this.oWeightsBuffer.get(i)).doubleValue()));
        }
    }

    /**
     * Dumps XML of the current Neuron.
     *
     * @param poWriter  Writer object to write output to
     * @param piTabsNum tabulation needed
     * @throws java.io.IOException if there is any problem to output to the writer
     */
    public final void printXML(BufferedWriter poWriter, final int piTabsNum)
            throws java.io.IOException {
        NeuralNetwork.indent(poWriter, piTabsNum);
        StringBuffer oBuffer = new StringBuffer();

        poWriter.write
                (
                        oBuffer
                                .append("<neuron index=\"")
                                .append(this.strName)
                                .append("\" thresh=\"")
                                .append(this.dThreshold)
                                .append("\">")
                                .toString()
                );

        poWriter.newLine();

        for (int i = 0; i < this.oInputs.size(); i++) {
            oBuffer = new StringBuffer();
            NeuralNetwork.indent(poWriter, piTabsNum + 1);

            poWriter.write
                    (
                            oBuffer
                                    .append("<input ref=\"")
                                    .append(((Neuron) this.oInputs.get(i)).strName)
                                    .append("\" weight=\"")
                                    .append(this.oWeights.get(i))
                                    .append("\"/>")
                                    .toString()
                    );

            poWriter.newLine();
        }

        for (int i = 0; i < this.oOutputs.size(); i++) {
            oBuffer = new StringBuffer();
            NeuralNetwork.indent(poWriter, piTabsNum + 1);

            poWriter.write
                    (
                            oBuffer
                                    .append("<output ref=\"")
                                    .append(((Neuron) this.oOutputs.get(i)).strName)
                                    .append("\"/>").toString()
                    );

            poWriter.newLine();
        }

        NeuralNetwork.indent(poWriter, piTabsNum);
        poWriter.write("</neuron>");
        poWriter.newLine();
    }

    /**
     * The body of a Neuron thread.
     * TODO: complete.
     *
     * @since 0.3.0.2
     */
    public void run() {
        eval();
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.16 $";
    }
}

// EOF
