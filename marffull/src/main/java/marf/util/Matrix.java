package marf.util;


/**
 * <p>Free Matrix - a multidimensional extension of FreeVector.
 * The class is properly synchronized.</p>
 * <p>
 * TODO: complete
 * <p>
 * $Id: Matrix.java,v 1.14 2005/08/11 00:44:50 mokhov Exp $
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.14 $
 * @see FreeVector
 * @since 0.3.0.1
 */
public class Matrix
        extends FreeVector {
    //public static final int DEFAULT_CARDINALITY = 2;

    /**
     * Matrix dimensions.
     */
    protected int[] aiDimensions = null;

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = 3533845945234893934L;

    /**
     * Creates free matrix with certain cardinality.
     *
     * @param piCardinality number of dimensions of the matrix.
     */
    public Matrix(int piCardinality) {
        this.aiDimensions = new int[piCardinality];
    }

    /**
     * Creates a free matrix with user-defined dimensions.
     * The dimensions array is properly clonned internally.
     *
     * @param paiDimensions the desired dimensions; if null, a 1-by-1 matrix created
     */
    public Matrix(int[] paiDimensions) {
        if (paiDimensions == null) {
            // 1x1 point
            this.aiDimensions = new int[1];
            this.aiDimensions[0] = 1;
        } else
            this.aiDimensions = (int[]) paiDimensions.clone();

        ensureIndexCapacity(this.aiDimensions[0] - 1);
    }

    /**
     * Creates an 1-by-1 matrix.
     */
    public Matrix() {
        this(null);
    }

    /**
     * Retrieves current number of dimensions of the matrix.
     *
     * @return current cardinality
     */
    public final synchronized int getCardinality() {
        return this.aiDimensions.length;
    }

    /**
     * Retrieves a particular dimension value.
     *
     * @param piDimensionNumber dimension number to the the value off
     * @return the dimension value
     * @throws ArrayIndexOutOfBoundsException if the parameter is out of range
     */
    public final synchronized int getDimention(final int piDimensionNumber) {
        return this.aiDimensions[piDimensionNumber];
    }

    /**
     * Allows to retrieve the object from a multidimensional matrix.
     *
     * @param paiPoint dimension coordinates
     * @return object at the specified point coordinates
     */
    public synchronized Object elementAt(final int[] paiPoint) {
        if (paiPoint == null || paiPoint.length == 0)
            return elementAt(0);

        Object oObject = elementAt(paiPoint[0]);

        if (paiPoint.length == 1)
            return oObject;

        int[] aiReducedPoint = new int[paiPoint.length - 1];

        for (int i = 0; i < aiReducedPoint.length; i++)
            aiReducedPoint[i] = paiPoint[i];

        return elementAt(aiReducedPoint);
    }

    /**
     * Not implemented.
     * Should insert an element at the specified dimensions.
     *
     * @param paiPointIndex dimension coordinates to insert the element at
     * @param poElement     the element to insert
     * @throws NotImplementedException
     */
    public synchronized void add(int[] paiPointIndex, Object poElement) {
        throw new NotImplementedException(this, "add()");
    }

    /**
     * Retrieves class' revision.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.14 $";
    }
}

// EOF
