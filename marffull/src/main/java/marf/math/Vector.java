package marf.math;

import marf.util.Debug;


/**
 * <p>Algebraic operations on vectors.</p>
 * <p><b>NOTE:</b> this class has the same considerations as
 * <code>marf.math.Matrix</code>.</p>
 * <p>
 * <p>$Id: Vector.java,v 1.12 2005/08/13 23:09:39 susan_fan Exp $</p>
 *
 * @author Serguei A. Mokhov
 * @version $Revision: 1.12 $
 * @see Matrix
 * @since 0.3.0
 */
public class Vector
        extends Matrix {
    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -220324533429950897L;

    /**
     * Default vector's length is 3 elements.
     */
    public static final int DEFAULT_VECTOR_LENGTH = 3;

    // Object Live Cycle

    /**
     * Constructs a vector of default length of 3.
     */
    public Vector() {
        this(DEFAULT_VECTOR_LENGTH);
    }

    /**
     * Constructs a vector with the specified length.
     *
     * @param piVectorLength require vector length
     */
    public Vector(final int piVectorLength) {
        super(piVectorLength, 1);
    }

    /**
     * Constructs a vector from another vector; a-la copy conctructor.
     *
     * @param poVector a vector to make a copy of
     */
    public Vector(final Vector poVector) {
        super(poVector);
    }

    /**
     * Constructs a vector out of a matrix and forces the rows
     * dimension to 1 and "flattens" the matrix to have the number
     * of columns as the same number of elements in the original
     * matrix.
     *
     * @param poMatrix base matrix to construct the vector from
     */
    public Vector(final Matrix poMatrix) {
        super(poMatrix);
        this.iRows = 1;
        this.iCols = poMatrix.getElements();
    }

    /**
     * Constructs a vector out of a raw array of doubles.
     *
     * @param padVectorData source data for the vector
     */
    public Vector(final double[] padVectorData) {
        super(padVectorData);
    }

    /**
     * Allows having a transposed vector upon consuctrion.
     * Useful for example before doing linear operations.
     *
     * @param pad1DMatrix  vector data
     * @param pbTransposed <code>true</code> if the transposed vector is desired
     * @since 0.3.0.3
     */
    public Vector(double[] pad1DMatrix, boolean pbTransposed) {
        super(pad1DMatrix, pbTransposed);
    }

    // Vector Specific Operations

    /**
     * Allows getting the value of vector's element at specified position.
     *
     * @param piPosition index to get the element from
     * @return vector element's value
     */
    public final double getElement(final int piPosition) {
        return this.iRows == 1 ?
                super.getElement(0, piPosition) :
                super.getElement(piPosition, 0);
    }

    /**
     * Allows setting the value of vector's element at specified position.
     *
     * @param piPosition index to det the element at
     * @param pdValue    the element's value
     */
    public void setElement(final int piPosition, final double pdValue) {
        if (this.iRows == 1)
            super.setElement(0, piPosition, pdValue);
        else
            super.setElement(piPosition, 0, pdValue);
    }

    /**
     * Retrieves the mathematical length of the vector.
     *
     * @return the length value
     */
    public double getLength() {
        double dSqSum = 0.0;

        for (int i = 0; i < getElements(); i++)
            dSqSum += this.adMatrix[i] * this.adMatrix[i];

        return Math.sqrt(dSqSum);
    }

    /**
     * V1 = this + V.
     *
     * @param poVector V
     * @return V1
     * @since 0.3.0.3
     */
    public Vector add(final Vector poVector) {
        return add(this, poVector);
    }

    /**
     * V3 = V1 + V2.
     *
     * @param poLHSVector V1
     * @param poRHSVector V2
     * @return V3
     * @since 0.3.0.3
     */
    public static Vector add(final Vector poLHSVector, final Vector poRHSVector) {
        return new Vector(Matrix.add(poLHSVector, poRHSVector));
    }

    /**
     * Returns a unit-vector representation of this vector.
     * This vector remains intact, instead a "unitized" copy is returned.
     *
     * @return corresponding unit vector
     */
    public final Vector getUnitVector() {
        Vector oUnitVector = new Vector(this);
        oUnitVector.normalize();
        return oUnitVector;
    }

    /**
     * Calculates an inner product of two vectors, V3 = V1 * V2.
     *
     * @param poLHSVector first vector, V1
     * @param poRHSVector second vector, V2
     * @return the inner product vector of the two, V3
     */
    public static Vector getInnerProduct(final Vector poLHSVector, final Vector poRHSVector) {
        Vector oXVector = new Vector();

        oXVector.setElement(0, (poLHSVector.getElement(1) * poRHSVector.getElement(2) - poLHSVector.getElement(2) * poRHSVector.getElement(1)));
        oXVector.setElement(1, (poLHSVector.getElement(2) * poRHSVector.getElement(0) - poLHSVector.getElement(0) * poRHSVector.getElement(2)));
        oXVector.setElement(2, (poLHSVector.getElement(0) * poRHSVector.getElement(1) - poLHSVector.getElement(1) * poRHSVector.getElement(0)));

        return oXVector;
    }

    /**
     * Calculates the cross product of two vectors, V3 = V1 x V2.
     * TODO: complete all cases.
     *
     * @param poLHSVector first vector, V1
     * @param poRHSVector second vector, V2
     * @return the cross product, V3
     */
    public static Vector getCrossProduct(final Vector poLHSVector, final Vector poRHSVector) {
        Debug.debug("Vector.getInnerProduct() - WARNING: Implementation is incomplete!\n");

        if (poLHSVector.getElements() != poRHSVector.getElements())
            return new Vector(0);

        Vector oVectorIP = new Vector(poLHSVector.getElements());

        for (int i = 0; i < oVectorIP.getElements(); i++)
            oVectorIP.setElement(i, poLHSVector.getElement(i) * poRHSVector.getElement(i));

        return oVectorIP;
    }

    /**
     * Calculates the dot product of two vectors, d = V1 * V2.
     *
     * @param poLHSVector first vector, V1
     * @param poRHSVector second vector, V2
     * @return the dot product, d
     */
    public static double getDotProduct(final Vector poLHSVector, final Vector poRHSVector) {
        double dDotProduct = 0.0;

        if (poLHSVector.getElements() != poRHSVector.getElements()) {
            Debug.debug
                    (
                            "Vector.getDotProduct() - WARNING: Number of elements in vectors do not match: lhs=" +
                                    poLHSVector.getElements() + ", rhs=" + poRHSVector.getElements() + "\n"
                    );

            return -1.0;
        }

        for (int i = 0; i < poLHSVector.getElements(); i++)
            dDotProduct += poLHSVector.getElement(i) * poRHSVector.getElement(i);

        return dDotProduct;
    }

    /**
     * Tells whether this and the parameter vectors are orthogonal or not.
     *
     * @param poVector another vector to compare to
     * @return <code>true</code> if the vectors are orthogonal
     */
    public boolean isOrthogonal(final Vector poVector) {
        return (getDotProduct(this, poVector) == 0);
    }

    /**
     * Normalizes the vector's components by its length.
     */
    public void normalize() {
        double dLength = getLength();

        // If fraction is too small avoid division by zero
        // and just set all elements to zero...
        if (Math.abs(dLength) < Double.MIN_VALUE)
            setAll();

            // ... or divide by the length otherwise
        else {
            for (int i = 0; i < this.iCols * this.iRows; i++)
                this.adMatrix[i] /= dLength;
        }
    }

    /**
     * Creates a unit-vector "i".
     *
     * @return a new vector instante of {1, 0, 0}
     */
    public final static Vector i() {
        return new Vector(new double[]{1, 0, 0});
    }

    /**
     * Creates a unit-vector "j".
     *
     * @return a new vector instante of {0, 1, 0}
     */
    public final static Vector j() {
        return new Vector(new double[]{0, 1, 0});
    }

    /**
     * Creates a unit-vector "k".
     *
     * @return a new vector instante of {0, 0, 1}
     */
    public final static Vector k() {
        return new Vector(new double[]{0, 0, 1});
    }

    /**
     * Implements Clonable interface.
     * Creates a deep copy of the object by cloning the internal matrix array.
     *
     * @return object copy of this vector
     * @see java.lang.Object#clone()
     * @since 0.3.0.3
     */
    public Object clone() {
        return new Vector(this);
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.12 $";
    }
}

// EOF
