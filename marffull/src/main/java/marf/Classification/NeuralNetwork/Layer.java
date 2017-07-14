package marf.Classification.NeuralNetwork;

import java.io.Serializable;
import java.util.ArrayList;

import marf.util.BaseThread;


/**
 * <p>Represents a Neural Network Layer.
 * <p>
 * Being itself a thread, encapsulates a group of
 * Neuron thread comprising a neron layer of the network.
 * The actual threading is almost unimplemented yet, but the
 * class itself is properly synchronized.
 * </p>
 * <p>
 * $Id: Layer.java,v 1.8 2006/01/22 23:47:26 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.8 $
 * @since 0.3.0.2
 */
public class Layer
        extends BaseThread
        implements Serializable {
    /**
     * Layer's data (a collection of neurons).
     *
     * @since 0.3.0.5
     */
    private ArrayList oLayerData = new ArrayList();

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.5
     */
    private static final long serialVersionUID = 6204412694265536336L;


    /**
     * Default constructor.
     */
    public Layer() {
        super();
    }

    /**
     * Evaluates the layer by evaluating each neuron in it.
     *
     * @since 0.3.0.5
     */
    public synchronized void eval() {
        for (int j = 0; j < size(); j++) {
            ((Neuron) this.oLayerData.get(j)).eval();
        }
    }

    /**
     * Performs training on each neuron in this layer given
     * the training constant between 0.0 and 1.0.
     *
     * @param pdTrainConst training constant to use for neurons
     * @since 0.3.0.5
     */
    public synchronized void train(final double pdTrainConst) {
        for (int j = 0; j < size(); j++) {
            get(j).train(0.0, pdTrainConst, 1.0);
        }
    }

    /**
     * Applies changes made to neurons on this layer.
     *
     * @since 0.3.0.5
     */
    public synchronized final void commit() {
        for (int j = 0; j < size(); j++) {
            get(j).commit();
        }
    }

    /**
     * For threading does evaluation.
     * TODO: complete.
     *
     * @since 0.3.0.5
     */
    public void run() {
        eval();
    }

    /**
     * Allows getting a neuron object given index.
     *
     * @param piIndex the index to fetch the neuron from.
     * @return the neuron object corresponding to the index if found
     * @since 0.3.0.5
     */
    public synchronized Neuron get(int piIndex) {
        return (Neuron) this.oLayerData.get(piIndex);
    }

    /**
     * Allows adding a neuron to the layer.
     *
     * @param poNeuron the neuron to add
     * @return <code>true</code> if the neuron was added
     * @since 0.3.0.5
     */
    public synchronized boolean add(Neuron poNeuron) {
        return this.oLayerData.add(poNeuron);
    }

    /**
     * Allows querying for layer size in terms of number of neurons.
     *
     * @return the layer size
     * @since 0.3.0.5
     */
    public synchronized int size() {
        return this.oLayerData.size();
    }

    /**
     * Returns the Neuron called by its name string.
     *
     * @param pstrName the neuron name to search for
     * @return the name Neuron object if found
     * @since 0.3.0.5
     */
    public Neuron getNeuron(final String pstrName) {
        Neuron oNeuron = null;

        for (int i = 0; i < size(); i++) {
            oNeuron = (Neuron) this.oLayerData.get(i);

            if (oNeuron.strName.equals(pstrName)) {
                break;
            }
        }

        return oNeuron;
    }

    /**
     * Allows querying raw layer data collection.
     *
     * @return the collection of neurons
     * @since 0.3.0.5
     */
    public synchronized ArrayList getLayerData() {
        return this.oLayerData;
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.8 $";
    }
}

// EOF
