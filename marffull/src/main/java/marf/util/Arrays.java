package marf.util;

import java.util.Comparator;
import java.util.Vector;


/**
 * <p><code>marf.util.Arrays</code> is an extension of <code>java.util.Arrays</code>
 * to group a lot of commonly used arrays-related functionality in one place. This class
 * can do whatever <code>java.util.Arrays</code> can, plus allows copying array portions,
 * including cases when the source and destination arrays are of different types, and providing
 * array-to-Vector and array-to-delimited-String conversions. For the type-conversion
 * routines a proper casting to the destination type is performed when needed. It also
 * allows inheritance from the class, so that anyone wishing to extend it is welcome to
 * do so without the pain of re-wrapping the methods.</p>
 * <p>
 * TODO: optimize.
 * <p>
 * <p>NOTE: the <code>java.util.Arrays</code> compliance is true as of JDK 1.4.</p>
 * <p>
 * <p>NOTE: it does not actually inherit (extend) from <code>java.util.Arrays</code>, but rather wraps
 * existing methods, plus adds the <code>copy()</code> wrappers of <code>System.arraycopy()</code>,
 * and <code>arrayToVector()</code> methods.</p>
 * <p>
 * <p>$Id: Arrays.java,v 1.35 2005/12/05 03:13:02 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @author Shuxin Fan
 * @version $Revision: 1.35 $
 * @see java.util.Arrays
 * @see System#arraycopy(Object, int, Object, int, int)
 * @since 0.3.0.1
 */
public class Arrays {
    /**
     * The protected default constructor is provided
     * to allow making extension of this class if
     * developers desire to do so. Normally, you would
     * not need to instantiate this class, but in order
     * not to re-wrap our calls in possible extensions
     * this constructor is available.
     */
    protected Arrays() {
    }

	/*
     * -------
	 * Copying
	 * -------
	 */

    /**
     * Generic <code>copy()</code> routine is based on <code>System.arraycopy()</code>.
     *
     * @param poDestination           destination array of copy
     * @param piDestinationStartIndex where in the destination array start placing the values
     * @param poSource                source of elements
     * @param piSourceStartIndex      where in the source array start copying the values from
     * @param piHowMany               how many elements should be copied from the source to destination
     */
    public static void copy
    (
            Object poDestination,
            final int piDestinationStartIndex,
            Object poSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        poSource,
                        piSourceStartIndex,
                        poDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Generic <code>copy()</code> routine is based on <code>System.arraycopy()</code>
     * for Object arrays.
     *
     * @param paoDestination          destination array of objects to copy to
     * @param piDestinationStartIndex where in the destination array start placing the values
     * @param paoSource               source of Object elements
     * @param piSourceStartIndex      where in the source array start copying the values from
     * @param piHowMany               how many elements should be copied from the source to destination
     */
    public static void copy
    (
            Object[] paoDestination,
            final int piDestinationStartIndex,
            Object[] paoSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        paoSource,
                        piSourceStartIndex,
                        paoDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Copies N boolean elements from source to destination starting at certain index in the <b>destination</b>.
     * A wrapper call to <code>System.arraycopy()</code>.
     *
     * @param pabDestination          array to copy to
     * @param piDestinationStartIndex starting index in the destination to start copying to
     * @param pabSource               array of booleans to copy from
     * @param piSourceStartIndex      starting index in the source to start copying from
     * @param piHowMany               N; the number of elements to copy from the source to the destination
     */
    public static void copy
    (
            boolean[] pabDestination,
            final int piDestinationStartIndex,
            boolean[] pabSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        pabSource,
                        piSourceStartIndex,
                        pabDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Copies N byte elements from source to destination starting at certain index in the <b>destination</b>.
     * A wrapper call to <code>System.arraycopy()</code>.
     *
     * @param patDestination          array to copy to
     * @param piDestinationStartIndex starting index in the destination to start copying to
     * @param patSource               array of bytes to copy from
     * @param piSourceStartIndex      starting index in the source to start copying from
     * @param piHowMany               N; the number of elements to copy from the source to the destination
     */
    public static void copy
    (
            byte[] patDestination,
            final int piDestinationStartIndex,
            byte[] patSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        patSource,
                        piSourceStartIndex,
                        patDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Copies N character elements from source to destination starting at certain index in the <b>destination</b>.
     * A wrapper call to <code>System.arraycopy()</code>.
     *
     * @param pacDestination          array to copy to
     * @param piDestinationStartIndex starting index in the destination to start copying to
     * @param pacSource               array of characters to copy from
     * @param piSourceStartIndex      starting index in the source to start copying from
     * @param piHowMany               N; the number of elements to copy from the source to the destination
     */
    public static void copy
    (
            char[] pacDestination,
            final int piDestinationStartIndex,
            char[] pacSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        pacSource,
                        piSourceStartIndex,
                        pacDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Copies N integer elements from source to destination starting at certain index in the <b>destination</b>.
     * A wrapper call to <code>System.arraycopy()</code>.
     *
     * @param paiDestination          array to copy to
     * @param piDestinationStartIndex starting index in the destination to start copying to
     * @param paiSource               array of integers to copy from
     * @param piSourceStartIndex      starting index in the source to start copying from
     * @param piHowMany               N; the number of elements to copy from the source to the destination
     */
    public static void copy
    (
            int[] paiDestination,
            final int piDestinationStartIndex,
            int[] paiSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        paiSource,
                        piSourceStartIndex,
                        paiDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Copies N short elements from source to destination starting at certain index in the <b>destination</b>.
     * A wrapper call to <code>System.arraycopy()</code>.
     *
     * @param pasDestination          array to copy to
     * @param piDestinationStartIndex starting index in the destination to start copying to
     * @param pasSource               array of shorts to copy from
     * @param piSourceStartIndex      starting index in the source to start copying from
     * @param piHowMany               N; the number of elements to copy from the source to the destination
     */
    public static void copy
    (
            short[] pasDestination,
            final int piDestinationStartIndex,
            short[] pasSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        pasSource,
                        piSourceStartIndex,
                        pasDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Copies N long elements from source to destination starting at certain index in the <b>destination</b>.
     * A wrapper call to <code>System.arraycopy()</code>.
     *
     * @param palDestination          array to copy to
     * @param piDestinationStartIndex starting index in the destination to start copying to
     * @param palSource               array of longs to copy from
     * @param piSourceStartIndex      starting index in the source to start copying from
     * @param piHowMany               N; the number of elements to copy from the source to the destination
     */
    public static void copy
    (
            long[] palDestination,
            final int piDestinationStartIndex,
            long[] palSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        palSource,
                        piSourceStartIndex,
                        palDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Copies N float elements from source to destination starting at certain index in the <b>destination</b>.
     * A wrapper call to <code>System.arraycopy()</code>.
     *
     * @param pafDestination          array to copy to
     * @param piDestinationStartIndex starting index in the destination to start copying to
     * @param pafSource               array of float to copy from
     * @param piSourceStartIndex      starting index in the source to start copying from
     * @param piHowMany               N; the number of elements to copy from the source to the destination
     */
    public static void copy
    (
            float[] pafDestination,
            final int piDestinationStartIndex,
            float[] pafSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        pafSource,
                        piSourceStartIndex,
                        pafDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Copies N double elements from source to destination starting at certain index in the <b>destination</b>.
     * A wrapper call to <code>System.arraycopy()</code>.
     *
     * @param padDestination          array to copy to
     * @param piDestinationStartIndex starting index in the destination to start copying to
     * @param padSource               array of doubles to copy from
     * @param piSourceStartIndex      starting index in the source to start copying from
     * @param piHowMany               N; the number of elements to copy from the source to the destination
     * @throws ArrayIndexOutOfBoundsException if one of the indices is out of range
     */
    public static void copy
    (
            double[] padDestination,
            final int piDestinationStartIndex,
            double[] padSource,
            final int piSourceStartIndex,
            final int piHowMany
    ) {
        System.arraycopy
                (
                        padSource,
                        piSourceStartIndex,
                        padDestination,
                        piDestinationStartIndex,
                        piHowMany
                );
    }

    /**
     * Generic <code>copy()</code> routine is based on <code>System.arraycopy()</code>.
     *
     * @param poDestination destination array of copy
     * @param piStartIndex  where in the destination array start placing the values
     * @param poSource      source of elements
     * @param piHowMany     how many elements should be copied from the source to destination
     */
    public static void copy(Object poDestination, final int piStartIndex, Object poSource, final int piHowMany) {
        copy(poDestination, piStartIndex, poSource, 0, piHowMany);
    }

    /**
     * Generic <code>copy()</code> routine is based on <code>System.arraycopy()</code>
     * for Object arrays.
     *
     * @param paoDestination destination array of objects to copy to
     * @param piStartIndex   where in the destination array start placing the values
     * @param paoSource      source of Object elements
     * @param piHowMany      how many elements should be copied from the source to destination
     */
    public static void copy(Object[] paoDestination, final int piStartIndex, Object[] paoSource, final int piHowMany) {
        copy(paoDestination, piStartIndex, paoSource, 0, piHowMany);
    }

    /**
     * Copies N character elements from source to destination starting at certain index in the <b>destination</b>.
     *
     * @param pacDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param pacSource      array of characters to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(char[] pacDestination, final int piStartIndex, char[] pacSource, final int piHowMany) {
        copy(pacDestination, piStartIndex, pacSource, 0, piHowMany);
    }

    /**
     * Copies N boolean elements from source to destination starting at certain index in the <b>destination</b>.
     *
     * @param pabDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param pabSource      array of booleans to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(boolean[] pabDestination, final int piStartIndex, boolean[] pabSource, final int piHowMany) {
        copy(pabDestination, piStartIndex, pabSource, 0, piHowMany);
    }

    /**
     * Copies N byte elements from source to destination starting at certain index in the <b>destination</b>.
     *
     * @param patDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param patSource      array of bytes to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(byte[] patDestination, final int piStartIndex, byte[] patSource, final int piHowMany) {
        copy(patDestination, piStartIndex, patSource, 0, piHowMany);
    }

    /**
     * Copies N integer elements from source to destination starting at certain index in the <b>destination</b>.
     *
     * @param paiDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param paiSource      array of integers to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(int[] paiDestination, final int piStartIndex, int[] paiSource, final int piHowMany) {
        copy(paiDestination, piStartIndex, paiSource, 0, piHowMany);
    }

    /**
     * Copies N short elements from source to destination starting at certain index in the <b>destination</b>.
     *
     * @param pasDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param pasSource      array of shorts to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(short[] pasDestination, final int piStartIndex, short[] pasSource, final int piHowMany) {
        copy(pasDestination, piStartIndex, pasSource, 0, piHowMany);
    }

    /**
     * Copies N long elements from source to destination starting at certain index in the <b>destination</b>.
     *
     * @param palDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param palSource      array of longs to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(long[] palDestination, final int piStartIndex, long[] palSource, final int piHowMany) {
        copy(palDestination, piStartIndex, palSource, 0, piHowMany);
    }

    /**
     * Copies N float elements from source to destination starting at certain index in the <b>destination</b>.
     *
     * @param pafDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param pafSource      array of floats to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(float[] pafDestination, final int piStartIndex, float[] pafSource, final int piHowMany) {
        copy(pafDestination, piStartIndex, pafSource, 0, piHowMany);
    }

    /**
     * Copies N double elements from source to destination starting at certain index in the <b>destination</b>.
     *
     * @param padDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param padSource      array of doubles to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(double[] padDestination, final int piStartIndex, double[] padSource, final int piHowMany) {
        copy(padDestination, piStartIndex, padSource, 0, piHowMany);
    }

    /**
     * Generic <code>copy()</code> routine is based on <code>System.arraycopy()</code>
     * for Object arrays.
     *
     * @param paoDestination destination array of objects to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param paoSource      source of object elements
     */
    public static void copy(Object[] paoDestination, final int piStartIndex, Object[] paoSource) {
        copy(paoDestination, piStartIndex, paoSource, paoSource.length);
    }

    /**
     * Copies source to destination starting at certain index in the <b>destination</b>.
     *
     * @param pabDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param pabSource      array of booleans to copy from
     */
    public static void copy(boolean[] pabDestination, final int piStartIndex, boolean[] pabSource) {
        copy(pabDestination, piStartIndex, pabSource, pabSource.length);
    }

    /**
     * Copies source to destination starting at certain index in the <b>destination</b>.
     *
     * @param patDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param patSource      array of bytes to copy from
     */
    public static void copy(byte[] patDestination, final int piStartIndex, byte[] patSource) {
        copy(patDestination, piStartIndex, patSource, patSource.length);
    }

    /**
     * Copies source to destination starting at certain index in the <b>destination</b>.
     *
     * @param pacDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param pacSource      array of characters to copy from
     */
    public static void copy(char[] pacDestination, final int piStartIndex, char[] pacSource) {
        copy(pacDestination, piStartIndex, pacSource, pacSource.length);
    }

    /**
     * Copies source to destination starting at certain index in the <b>destination</b>.
     *
     * @param paiDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param paiSource      array of integers to copy from
     */
    public static void copy(int[] paiDestination, final int piStartIndex, int[] paiSource) {
        copy(paiDestination, piStartIndex, paiSource, paiSource.length);
    }

    /**
     * Copies source to destination starting at certain index in the <b>destination</b>.
     *
     * @param pasDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param pasSource      array of shorts to copy from
     */
    public static void copy(short[] pasDestination, final int piStartIndex, short[] pasSource) {
        copy(pasDestination, piStartIndex, pasSource, pasSource.length);
    }

    /**
     * Copies source to destination starting at certain index in the <b>destination</b>.
     *
     * @param palDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param palSource      array of longs to copy from
     */
    public static void copy(long[] palDestination, final int piStartIndex, long[] palSource) {
        copy(palDestination, piStartIndex, palSource, palSource.length);
    }

    /**
     * Copies source to destination starting at certain index in the <b>destination</b>.
     *
     * @param pafDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param pafSource      array of floats to copy from
     */
    public static void copy(float[] pafDestination, final int piStartIndex, float[] pafSource) {
        copy(pafDestination, piStartIndex, pafSource, pafSource.length);
    }

    /**
     * Copies source to destination starting at certain index in the <b>destination</b>.
     *
     * @param padDestination array to copy to
     * @param piStartIndex   starting index in the destination to start copying to
     * @param padSource      array of doubles to copy from
     */
    public static void copy(double[] padDestination, final int piStartIndex, double[] padSource) {
        copy(padDestination, piStartIndex, padSource, padSource.length);
    }

    /**
     * Generic <code>copy()</code> routine is based on <code>System.arraycopy()</code>.
     *
     * @param poDestination destination array of copy
     * @param poSource      source of elements
     * @param piHowMany     N; the number of elements to copy from the source to the destination
     */
    public static void copy(Object poDestination, Object poSource, final int piHowMany) {
        copy(poDestination, 0, poSource, piHowMany);
    }

    /**
     * Generic <code>copy()</code> routine is based on <code>System.arraycopy()</code>
     * for Object arrays.
     *
     * @param paoDestination array to copy to
     * @param paoSource      source of object elements
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(Object[] paoDestination, Object[] paoSource, final int piHowMany) {
        copy(paoDestination, 0, paoSource, piHowMany);
    }

    /**
     * Copies N character elements from source to destination.
     *
     * @param pacDestination array to copy to
     * @param pacSource      array of characters to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(char[] pacDestination, char[] pacSource, final int piHowMany) {
        copy(pacDestination, 0, pacSource, piHowMany);
    }

    /**
     * Copies N boolean elements from source to destination.
     *
     * @param pabDestination array to copy to
     * @param pabSource      array of boolean to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(boolean[] pabDestination, boolean[] pabSource, final int piHowMany) {
        copy(pabDestination, 0, pabSource, piHowMany);
    }

    /**
     * Copies N byte elements from source to destination.
     *
     * @param patDestination array to copy to
     * @param patSource      array of bytes to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(byte[] patDestination, byte[] patSource, final int piHowMany) {
        copy(patDestination, 0, patSource, piHowMany);
    }

    /**
     * Copies N short elements from source to destination.
     *
     * @param pasDestination array to copy to
     * @param pasSource      array of shorts to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(short[] pasDestination, short[] pasSource, final int piHowMany) {
        copy(pasDestination, 0, pasSource, piHowMany);
    }

    /**
     * Copies N long elements from source to destination.
     *
     * @param palDestination array to copy to
     * @param palSource      array of longs to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(long[] palDestination, long[] palSource, final int piHowMany) {
        copy(palDestination, 0, palSource, piHowMany);
    }

    /**
     * Copies N float elements from source to destination.
     *
     * @param pafDestination array to copy to
     * @param pafSource      array of floats to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(float[] pafDestination, float[] pafSource, final int piHowMany) {
        copy(pafDestination, 0, pafSource, piHowMany);
    }

    /**
     * Copies N double elements from source to destination.
     *
     * @param padDestination array to copy to
     * @param padSource      array of doubles to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(double[] padDestination, double[] padSource, final int piHowMany) {
        copy(padDestination, 0, padSource, piHowMany);
    }

    /**
     * Copies N int elements from source to destination.
     *
     * @param paiDestination array to copy to
     * @param paiSource      array of ints to copy from
     * @param piHowMany      N; the number of elements to copy from the source to the destination
     */
    public static void copy(int[] paiDestination, int[] paiSource, final int piHowMany) {
        copy(paiDestination, 0, paiSource, 0, piHowMany);
    }

	/*
     * -------------
	 * Concatenation
	 * -------------
	 */

    /**
     * Concatenates two arrays of objects and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param paoLHS first array piece
     * @param paoRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static Object[] concatenate(final Object[] paoLHS, final Object[] paoRHS) {
        Object[] aoConcatenated = new Object[paoLHS.length + paoRHS.length];
        System.arraycopy(paoLHS, 0, aoConcatenated, 0, paoLHS.length);
        System.arraycopy(paoRHS, 0, aoConcatenated, paoLHS.length, paoRHS.length);
        return aoConcatenated;
    }

    /**
     * Concatenates two arrays of Strings and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param pastrLHS first array piece
     * @param pastrRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static String[] concatenate(final String[] pastrLHS, final String[] pastrRHS) {
        String[] astrConcatenated = new String[pastrLHS.length + pastrRHS.length];
        System.arraycopy(pastrLHS, 0, astrConcatenated, 0, pastrLHS.length);
        System.arraycopy(pastrRHS, 0, astrConcatenated, pastrLHS.length, pastrRHS.length);
        return astrConcatenated;
    }

    /**
     * Concatenates two arrays of doubles and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param padLHS first array piece
     * @param padRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static double[] concatenate(final double[] padLHS, final double[] padRHS) {
        double[] adConcatenated = new double[padLHS.length + padRHS.length];
        System.arraycopy(padLHS, 0, adConcatenated, 0, padLHS.length);
        System.arraycopy(padRHS, 0, adConcatenated, padLHS.length, padRHS.length);
        return adConcatenated;
    }

    /**
     * Concatenates two arrays of booleans and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param pabLHS first array piece
     * @param pabRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static boolean[] concatenate(final boolean[] pabLHS, final boolean[] pabRHS) {
        boolean[] abConcatenated = new boolean[pabLHS.length + pabRHS.length];
        System.arraycopy(pabLHS, 0, abConcatenated, 0, pabLHS.length);
        System.arraycopy(pabRHS, 0, abConcatenated, pabLHS.length, pabRHS.length);
        return abConcatenated;
    }

    /**
     * Concatenates two arrays of bytes and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param patLHS first array piece
     * @param patRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static byte[] concatenate(final byte[] patLHS, final byte[] patRHS) {
        byte[] atConcatenated = new byte[patLHS.length + patRHS.length];
        System.arraycopy(patLHS, 0, atConcatenated, 0, patLHS.length);
        System.arraycopy(patRHS, 0, atConcatenated, patLHS.length, patRHS.length);
        return atConcatenated;
    }

    /**
     * Concatenates two arrays of characters and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param pacLHS first array piece
     * @param pacRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static char[] concatenate(final char[] pacLHS, final char[] pacRHS) {
        char[] acConcatenated = new char[pacLHS.length + pacRHS.length];
        System.arraycopy(pacLHS, 0, acConcatenated, 0, pacLHS.length);
        System.arraycopy(pacRHS, 0, acConcatenated, pacLHS.length, pacRHS.length);
        return acConcatenated;
    }

    /**
     * Concatenates two arrays of integers and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param paiLHS first array piece
     * @param paiRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static int[] concatenate(final int[] paiLHS, final int[] paiRHS) {
        int[] aiConcatenated = new int[paiLHS.length + paiRHS.length];
        System.arraycopy(paiLHS, 0, aiConcatenated, 0, paiLHS.length);
        System.arraycopy(paiRHS, 0, aiConcatenated, paiLHS.length, paiRHS.length);
        return aiConcatenated;
    }

    /**
     * Concatenates two arrays of shorts and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param pasLHS first array piece
     * @param pasRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static short[] concatenate(final short[] pasLHS, final short[] pasRHS) {
        short[] asConcatenated = new short[pasLHS.length + pasRHS.length];
        System.arraycopy(pasLHS, 0, asConcatenated, 0, pasLHS.length);
        System.arraycopy(pasRHS, 0, asConcatenated, pasLHS.length, pasRHS.length);
        return asConcatenated;
    }

    /**
     * Concatenates two arrays of floats and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param pafLHS first array piece
     * @param pafRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static float[] concatenate(final float[] pafLHS, final float[] pafRHS) {
        float[] afConcatenated = new float[pafLHS.length + pafRHS.length];
        System.arraycopy(pafLHS, 0, afConcatenated, 0, pafLHS.length);
        System.arraycopy(pafRHS, 0, afConcatenated, pafLHS.length, pafRHS.length);
        return afConcatenated;
    }

    /**
     * Concatenates two arrays of longs and returns a newly
     * allocated array of the concatenated pieces.
     *
     * @param palLHS first array piece
     * @param palRHS second array piece
     * @return new combined array
     * @since 0.3.0.4
     */
    public static long[] concatenate(final long[] palLHS, final long[] palRHS) {
        long[] alConcatenated = new long[palLHS.length + palRHS.length];
        System.arraycopy(palLHS, 0, alConcatenated, 0, palLHS.length);
        System.arraycopy(palRHS, 0, alConcatenated, palLHS.length, palRHS.length);
        return alConcatenated;
    }

	/*
     * --------
	 * Equality
	 * --------
	 */

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param pabArray1 the first array of booleans to be compared for equality
     * @param pabArray2 the second array of booleans to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(boolean[] pabArray1, boolean[] pabArray2) {
        return java.util.Arrays.equals(pabArray1, pabArray2);
    }

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param patArray1 the first array of bytes to be compared for equality
     * @param patArray2 the second array of bytes to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(byte[] patArray1, byte[] patArray2) {
        return java.util.Arrays.equals(patArray1, patArray2);
    }

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param pacArray1 the first array of characters to be compared for equality
     * @param pacArray2 the second array of characters to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(char[] pacArray1, char[] pacArray2) {
        return java.util.Arrays.equals(pacArray1, pacArray2);
    }

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param padArray1 the first array of doubles to be compared for equality
     * @param padArray2 the second array of doubles to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(double[] padArray1, double[] padArray2) {
        return java.util.Arrays.equals(padArray1, padArray2);
    }

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param pafArray1 the first array of floats to be compared for equality
     * @param pafArray2 the second array of floats to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(float[] pafArray1, float[] pafArray2) {
        return java.util.Arrays.equals(pafArray1, pafArray2);
    }

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param paiArray1 the first array of integers to be compared for equality
     * @param paiArray2 the second array of integers to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(int[] paiArray1, int[] paiArray2) {
        return java.util.Arrays.equals(paiArray1, paiArray2);
    }

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param palArray1 the first array of longs to be compared for equality
     * @param palArray2 the second array of longs to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(long[] palArray1, long[] palArray2) {
        return java.util.Arrays.equals(palArray1, palArray2);
    }

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param paoArray1 the first array of Objects to be compared for equality
     * @param paoArray2 the second array of Objects to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(Object[] paoArray1, Object[] paoArray2) {
        return java.util.Arrays.equals(paoArray1, paoArray2);
    }

    /**
     * The <code>equals()</code> routine is based on <code>java.util.Arrays.equals()</code>.
     *
     * @param pasArray1 the first array of shorts to be compared for equality
     * @param pasArray2 the second array of shorts to be compared for equality
     * @return a boolean value <code> true </code> if the two arrays are equal
     */
    public static boolean equals(short[] pasArray1, short[] pasArray2) {
        return java.util.Arrays.equals(pasArray1, pasArray2);
    }

	/*
	 * -------------
	 * Array filling
	 * -------------
	 */

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param pabArray the array of booleans to be filled
     * @param pbValue  the value of boolean  to fill into the array of booleans
     */
    public static void fill(boolean[] pabArray, boolean pbValue) {
        java.util.Arrays.fill(pabArray, pbValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param pabArray    the array of booleans to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param pbValue     the value of boolean  to fill into the array of booleans
     */
    public static void fill(boolean[] pabArray, int piFromIndex, int piToIndex, boolean pbValue) {
        java.util.Arrays.fill(pabArray, piFromIndex, piToIndex, pbValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param patArray the array of bytes to be filled
     * @param ptValue  the value of byte to fill into the array of bytes
     */
    public static void fill(byte[] patArray, byte ptValue) {
        java.util.Arrays.fill(patArray, ptValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param patArray    the array of bytes to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param ptValue     the value of byte to fill into the array of bytes
     */
    public static void fill(byte[] patArray, int piFromIndex, int piToIndex, byte ptValue) {
        java.util.Arrays.fill(patArray, piFromIndex, piToIndex, ptValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param pacArray the array of characters to be filled
     * @param pcValue  the value of character to fill into the array of characters
     */
    public static void fill(char[] pacArray, char pcValue) {
        java.util.Arrays.fill(pacArray, pcValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param pacArray    the array of characters to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param pcValue     the value of character to fill into the array of characters
     */
    public static void fill(char[] pacArray, int piFromIndex, int piToIndex, char pcValue) {
        java.util.Arrays.fill(pacArray, piFromIndex, piToIndex, pcValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param padArray the array of doubles to be filled
     * @param pdValue  the value of double to fill into the array of doubles
     */
    public static void fill(double[] padArray, double pdValue) {
        java.util.Arrays.fill(padArray, pdValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param padArray    the array of doubles to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param pdValue     the value of double to fill into the array of doubles
     */
    public static void fill(double[] padArray, int piFromIndex, int piToIndex, double pdValue) {
        java.util.Arrays.fill(padArray, piFromIndex, piToIndex, pdValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param pafArray the array of floats to be filled
     * @param pfValue  the value of float to fill into the array of floats
     */
    public static void fill(float[] pafArray, float pfValue) {
        java.util.Arrays.fill(pafArray, pfValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param pafArray    the array of floats to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param pfValue     the value of float to fill into the array of floats
     */
    public static void fill(float[] pafArray, int piFromIndex, int piToIndex, float pfValue) {
        java.util.Arrays.fill(pafArray, piFromIndex, piToIndex, pfValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param paiArray the array of integers to be filled
     * @param piValue  the value of integer to fill into the array of integers
     */
    public static void fill(int[] paiArray, int piValue) {
        java.util.Arrays.fill(paiArray, piValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param paiArray    the array of integers to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param piValue     the value of integer to fill into the array of integers
     */
    public static void fill(int[] paiArray, int piFromIndex, int piToIndex, int piValue) {
        java.util.Arrays.fill(paiArray, piFromIndex, piToIndex, piValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param palArray    the array of longs to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param plValue     the value of long to fill into the array of longs
     */
    public static void fill(long[] palArray, int piFromIndex, int piToIndex, long plValue) {
        java.util.Arrays.fill(palArray, piFromIndex, piToIndex, plValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param palArray the array of longs to be filled
     * @param plValue  the value of long to fill into the array of longs
     */
    public static void fill(long[] palArray, long plValue) {
        java.util.Arrays.fill(palArray, plValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param paoArray    the array to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param paValue     the value to fill into the array
     */
    public static void fill(Object[] paoArray, int piFromIndex, int piToIndex, Object paValue) {
        java.util.Arrays.fill(paoArray, piFromIndex, piToIndex, paValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param paoArray the array to be filled
     * @param poValue  the value to fill into the array
     */
    public static void fill(Object[] paoArray, Object poValue) {
        java.util.Arrays.fill(paoArray, poValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param pasArray    the array of shorts to be filled
     * @param piFromIndex the index of the array from here the value to be filled
     * @param piToIndex   the index one after the last element of the array to be filled
     * @param psValue     the value of short  to fill into the array of shorts
     */
    public static void fill(short[] pasArray, int piFromIndex, int piToIndex, short psValue) {
        java.util.Arrays.fill(pasArray, piFromIndex, piToIndex, psValue);
    }

    /**
     * The <code>fill()</code> is based on <code>java.util.Arrays.fill()</code>.
     *
     * @param pasArray the array of shorts to be filled
     * @param psValue  the value of short  to fill into the array of shorts
     */
    public static void fill(short[] pasArray, short psValue) {
        java.util.Arrays.fill(pasArray, psValue);
    }

	/*
	 * -------
	 * Sorting
	 * -------
	 */

    /**
     * A wrapper of java.util.Arrays.sort(Object[], Comparator).
     *
     * @param paoArrayToSort array of objects to sort
     * @param poComparator   comparator object to use while sorting
     */
    public static void sort(Object[] paoArrayToSort, Comparator poComparator) {
        java.util.Arrays.sort(paoArrayToSort, poComparator);
    }

    /**
     * A wrapper of java.util.Arrays.sort(double[]).
     *
     * @param padArrayToSort array of doubles to sort
     */
    public static void sort(double[] padArrayToSort) {
        java.util.Arrays.sort(padArrayToSort);
    }

    /**
     * A wrapper of java.util.Arrays.sort(float[]).
     *
     * @param pafArrayToSort array of float to sort
     */
    public static void sort(float[] pafArrayToSort) {
        java.util.Arrays.sort(pafArrayToSort);
    }

    /**
     * A wrapper of java.util.Arrays.sort(int[]).
     *
     * @param paiArrayToSort array of integers to sort
     */
    public static void sort(int[] paiArrayToSort) {
        java.util.Arrays.sort(paiArrayToSort);
    }

    /**
     * A wrapper of java.util.Arrays.sort(byte[]).
     *
     * @param patArrayToSort array of bytes to sort
     */
    public static void sort(byte[] patArrayToSort) {
        java.util.Arrays.sort(patArrayToSort);
    }

    /**
     * A wrapper of java.util.Arrays.sort(char[]).
     *
     * @param pacArrayToSort array of characters to sort
     */
    public static void sort(char[] pacArrayToSort) {
        java.util.Arrays.sort(pacArrayToSort);
    }

    /**
     * A wrapper of java.util.Arrays.sort(double[]).
     *
     * @param palArrayToSort array of longs to sort
     */
    public static void sort(long[] palArrayToSort) {
        java.util.Arrays.sort(palArrayToSort);
    }

    /**
     * A wrapper of java.util.Arrays.sort(short[]).
     *
     * @param pasArrayToSort array of shorts to sort
     */
    public static void sort(short[] pasArrayToSort) {
        java.util.Arrays.sort(pasArrayToSort);
    }

    /**
     * A wrapper of java.util.Arrays.sort(object []).
     *
     * @param paoArrayToSort array of objects to sort
     */
    public static void sort(Object[] paoArrayToSort) {
        java.util.Arrays.sort(paoArrayToSort);
    }

	/*
	 * ----------
	 * Conversion
	 * ----------
	 */

    /**
     * Converts array of doubles to Vector.
     *
     * @param padData array of double data
     * @return equivalent collection of Double objects
     * @since 0.3.0.3
     */
    public static Vector arrayToVector(final double[] padData) {
        Vector oVector = new Vector(padData.length);

        for (int i = 0; i < padData.length; i++)
            oVector.add(new Double(padData[i]));

        return oVector;
    }

    /**
     * Converts array of ints to Vector.
     *
     * @param paiData array of int data
     * @return equivalent collection of Integer objects
     * @since 0.3.0.3
     */
    public static Vector arrayToVector(final int[] paiData) {
        Vector oVector = new Vector(paiData.length);

        for (int i = 0; i < paiData.length; i++)
            oVector.add(new Integer(paiData[i]));

        return oVector;
    }

    /**
     * Converts array of floats to Vector.
     *
     * @param pafData array of float data
     * @return equivalent collection of Float objects
     * @since 0.3.0.3
     */
    public static Vector arrayToVector(final float[] pafData) {
        Vector oVector = new Vector(pafData.length);

        for (int i = 0; i < pafData.length; i++)
            oVector.add(new Float(pafData[i]));

        return oVector;
    }

    /**
     * Converts array of shorts to Vector.
     *
     * @param pasData array of short data
     * @return equivalent collection of Short objects
     * @since 0.3.0.3
     */
    public static Vector arrayToVector(final short[] pasData) {
        Vector oVector = new Vector(pasData.length);

        for (int i = 0; i < pasData.length; i++)
            oVector.add(new Short(pasData[i]));

        return oVector;
    }

    /**
     * Converts array of longs to Vector.
     *
     * @param palData array of long data
     * @return equivalent collection of Long objects
     * @since 0.3.0.3
     */
    public static Vector arrayToVector(final long[] palData) {
        Vector oVector = new Vector(palData.length);

        for (int i = 0; i < palData.length; i++)
            oVector.add(new Long(palData[i]));

        return oVector;
    }

    /**
     * Converts array of characters to Vector.
     *
     * @param pacData array of character data
     * @return equivalent collection of Character objects
     * @since 0.3.0.3
     */
    public static Vector arrayToVector(final char[] pacData) {
        Vector oVector = new Vector(pacData.length);

        for (int i = 0; i < pacData.length; i++)
            oVector.add(new Character(pacData[i]));

        return oVector;
    }

    /**
     * Converts array of bytes to Vector.
     *
     * @param patData array of byte data
     * @return equivalent collection of Byte objects
     * @since 0.3.0.3
     */
    public static Vector arrayToVector(final byte[] patData) {
        Vector oVector = new Vector(patData.length);

        for (int i = 0; i < patData.length; i++)
            oVector.add(new Byte(patData[i]));

        return oVector;
    }

    /**
     * Converts array of Strings to Vector.
     *
     * @param pastrData array of String data
     * @return equivalent collection of String objects
     * @since 0.3.0.3
     */
    public static Vector arrayToVector(final String[] pastrData) {
        Vector oVector = new Vector(pastrData.length);

        for (int i = 0; i < pastrData.length; i++)
            oVector.add(new String(pastrData[i]));

        return oVector;
    }

    /**
     * Converts array of Strings to a single string separated by
     * the specified delimeter.
     *
     * @param pastrData     string data to concatenate
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final String[] pastrData, final String pstrDelimeter) {
        String strRetVal = pastrData[0];

        for (int i = 1; i < pastrData.length; i++) {
            strRetVal += pstrDelimeter + pastrData[i];
        }

        return strRetVal;
    }

    /**
     * Converts array of integers to a single string separated by
     * the specified delimeter.
     *
     * @param paiData       int data to append
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final int[] paiData, final String pstrDelimeter) {
        StringBuffer oRetVal = new StringBuffer();

        oRetVal.append(paiData[0]);

        for (int i = 1; i < paiData.length; i++) {
            oRetVal.append(pstrDelimeter).append(paiData[i]);
        }

        return oRetVal.toString();
    }

    /**
     * Converts array of Objects to a single string separated by
     * the specified delimeter.
     *
     * @param paoData       Object data to append
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final Object[] paoData, final String pstrDelimeter) {
        StringBuffer oRetVal = new StringBuffer();

        oRetVal.append(paoData[0]);

        for (int i = 1; i < paoData.length; i++) {
            oRetVal.append(pstrDelimeter).append(paoData[i]);
        }

        return oRetVal.toString();
    }

    /**
     * Converts array of longs to a single string separated by
     * the specified delimeter.
     *
     * @param palData       long data to append
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final long[] palData, final String pstrDelimeter) {
        StringBuffer oRetVal = new StringBuffer();

        oRetVal.append(palData[0]);

        for (int i = 1; i < palData.length; i++) {
            oRetVal.append(pstrDelimeter).append(palData[i]);
        }

        return oRetVal.toString();
    }

    /**
     * Converts array of floats to a single string separated by
     * the specified delimeter.
     *
     * @param pafData       float data to append
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final float[] pafData, final String pstrDelimeter) {
        StringBuffer oRetVal = new StringBuffer();

        oRetVal.append(pafData[0]);

        for (int i = 1; i < pafData.length; i++) {
            oRetVal.append(pstrDelimeter).append(pafData[i]);
        }

        return oRetVal.toString();
    }

    /**
     * Converts array of doubles to a single string separated by
     * the specified delimeter.
     *
     * @param padData       double data to append
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final double[] padData, final String pstrDelimeter) {
        StringBuffer oRetVal = new StringBuffer();

        oRetVal.append(padData[0]);

        for (int i = 1; i < padData.length; i++) {
            oRetVal.append(pstrDelimeter).append(padData[i]);
        }

        return oRetVal.toString();
    }

    /**
     * Converts array of bytes to a single string separated by
     * the specified delimeter.
     *
     * @param patData       byte data to append
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final byte[] patData, final String pstrDelimeter) {
        StringBuffer oRetVal = new StringBuffer();

        oRetVal.append(patData[0]);

        for (int i = 1; i < patData.length; i++) {
            oRetVal.append(pstrDelimeter).append(patData[i]);
        }

        return oRetVal.toString();
    }

    /**
     * Converts array of booleans to a single string separated by
     * the specified delimeter.
     *
     * @param pabData       boolean data to append
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final boolean[] pabData, final String pstrDelimeter) {
        StringBuffer oRetVal = new StringBuffer();

        oRetVal.append(pabData[0]);

        for (int i = 1; i < pabData.length; i++) {
            oRetVal.append(pstrDelimeter).append(pabData[i]);
        }

        return oRetVal.toString();
    }

    /**
     * Converts array of characters to a single string separated by
     * the specified delimeter.
     *
     * @param pacData       charater data to append
     * @param pstrDelimeter data elements separator
     * @return the resulting combined string
     * @since 0.3.0.5
     */
    public static String arrayToDelimitedString(final char[] pacData, final String pstrDelimeter) {
        StringBuffer oRetVal = new StringBuffer();

        oRetVal.append(pacData[0]);

        for (int i = 1; i < pacData.length; i++) {
            oRetVal.append(pstrDelimeter).append(pacData[i]);
        }

        return oRetVal.toString();
    }

    /**
     * Converts array of Strings to a single space-separated String.
     *
     * @param pastrData array of String data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final String[] pastrData) {
        return arrayToDelimitedString(pastrData, " ");
    }

    /**
     * Converts array of Objects to a single space-separated String.
     *
     * @param paoData array of Object data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final Object[] paoData) {
        return arrayToDelimitedString(paoData, " ");
    }

    /**
     * Converts array of integers to a single space-separated String.
     *
     * @param paiData array of integer data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final int[] paiData) {
        return arrayToDelimitedString(paiData, " ");
    }

    /**
     * Converts array of longs to a single space-separated String.
     *
     * @param palData array of long data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final long[] palData) {
        return arrayToDelimitedString(palData, " ");
    }

    /**
     * Converts array of floats to a single space-separated String.
     *
     * @param pafData array of float data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final float[] pafData) {
        return arrayToDelimitedString(pafData, " ");
    }

    /**
     * Converts array of doubles to a single space-separated String.
     *
     * @param padData array of double data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final double[] padData) {
        return arrayToDelimitedString(padData, " ");
    }

    /**
     * Converts array of bytes to a single space-separated String.
     *
     * @param patData array of byte data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final byte[] patData) {
        return arrayToDelimitedString(patData, " ");
    }

    /**
     * Converts array of booleans to a single space-separated String.
     *
     * @param pabData array of boolean data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final boolean[] pabData) {
        return arrayToDelimitedString(pabData, " ");
    }

    /**
     * Converts array of characters to a single space-separated String.
     *
     * @param pacData array of charater data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToString(final char[] pacData) {
        return arrayToDelimitedString(pacData, " ");
    }

    /**
     * Converts array of Strings to a single comma-separated String.
     *
     * @param pastrData array of String data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final String[] pastrData) {
        return arrayToDelimitedString(pastrData, ",");
    }

    /**
     * Converts array of Objects to a single comma-separated String.
     *
     * @param paoData array of Object data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final Object[] paoData) {
        return arrayToDelimitedString(paoData, ",");
    }

    /**
     * Converts array of integers to a single comma-separated String.
     *
     * @param paiData array of integer data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final int[] paiData) {
        return arrayToDelimitedString(paiData, ",");
    }

    /**
     * Converts array of longs to a single comma-separated String.
     *
     * @param palData array of long data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final long[] palData) {
        return arrayToDelimitedString(palData, ",");
    }

    /**
     * Converts array of floats to a single comma-separated String.
     *
     * @param pafData array of float data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final float[] pafData) {
        return arrayToDelimitedString(pafData, ",");
    }

    /**
     * Converts array of doubles to a single comma-separated String.
     *
     * @param padData array of double data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final double[] padData) {
        return arrayToDelimitedString(padData, ",");
    }

    /**
     * Converts array of bytes to a single comma-separated String.
     *
     * @param patData array of byte data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final byte[] patData) {
        return arrayToDelimitedString(patData, ",");
    }

    /**
     * Converts array of booleans to a single comma-separated String.
     *
     * @param pabData array of boolean data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final boolean[] pabData) {
        return arrayToDelimitedString(pabData, ",");
    }

    /**
     * Converts array of charaters to a single comma-separated String.
     *
     * @param pacData array of charater data
     * @return equivalent combined string
     * @since 0.3.0.5
     */
    public static String arrayToCSV(final char[] pacData) {
        return arrayToDelimitedString(pacData, ",");
    }

    /**
     * Provides an array-of-objects-to-List bridge.
     * Wraps <code>java.util.Arrays.asList()</code>.
     *
     * @param paoObjects array of objects
     * @return corresponding List collection
     * @since 0.3.0.3
     */
    public static java.util.List asList(Object[] paoObjects) {
        return java.util.Arrays.asList(paoObjects);
    }

    /**
     * General copy-conversion method that copies N int elements
     * from an array of ints into array of doubles.
     *
     * @param padDestination    destination array of doubles
     * @param piDestinationFrom index in the destination to start place elements at
     * @param paiSource         sourc earray of ints
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, int piDestinationFrom, int[] paiSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            padDestination[piDestinationFrom + i] = paiSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N int elements
     * from an array of ints into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param paiSource      sourc earray of ints
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, int[] paiSource, int piHowMany) {
        copy(padDestination, 0, paiSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all int elements
     * from an array of ints into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param paiSource      source array of ints
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, int[] paiSource) {
        copy(padDestination, 0, paiSource, 0, paiSource.length);
    }

    /**
     * General copy-conversion method that copies N int elements
     * from an array of ints into array of floats.
     *
     * @param pafDestination    destination array of floats
     * @param piDestinationFrom index in the destination to start place elements at
     * @param paiSource         source array of ints
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, int piDestinationFrom, int[] paiSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pafDestination[piDestinationFrom + i] = paiSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N int elements
     * from an array of ints into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param paiSource      source array of ints
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, int[] paiSource, int piHowMany) {
        copy(pafDestination, 0, paiSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all int elements
     * from an array of ints into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param paiSource      source array of ints
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, int[] paiSource) {
        copy(pafDestination, 0, paiSource, 0, paiSource.length);
    }

    /**
     * General copy-conversion method that copies N int elements
     * from an array of ints into array of longs.
     *
     * @param palDestination    destination array of longs
     * @param piDestinationFrom index in the destination to start place elements at
     * @param paiSource         source array of ints
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int piDestinationFrom, int[] paiSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            palDestination[piDestinationFrom + i] = paiSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N int elements
     * from an array of ints into array of longs.
     *
     * @param palDestination destination array of longs
     * @param paiSource      source array of ints
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int[] paiSource, int piHowMany) {
        copy(palDestination, 0, paiSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all int elements
     * from an array of ints into array of longs.
     *
     * @param palDestination destination array of longs
     * @param paiSource      source array of ints
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int[] paiSource) {
        copy(palDestination, 0, paiSource, 0, paiSource.length);
    }

    /**
     * General copy-conversion method that copies N int elements
     * from an array of ints into array of shorts.
     *
     * @param pasDestination    destination array of shorts
     * @param piDestinationFrom index in the destination to start place elements at
     * @param paiSource         source array of ints
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, int piDestinationFrom, int[] paiSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pasDestination[piDestinationFrom + i] = (short) paiSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N int elements
     * from an array of ints into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param paiSource      source array of ints
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, int[] paiSource, int piHowMany) {
        copy(pasDestination, 0, paiSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all int elements
     * from an array of ints into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param paiSource      source array of ints
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, int[] paiSource) {
        copy(pasDestination, 0, paiSource, 0, paiSource.length);
    }

    /**
     * General copy-conversion method that copies N int elements
     * from an array of ints into array of bytes.
     *
     * @param patDestination    destination array of bytes
     * @param piDestinationFrom index in the destination to start place elements at
     * @param paiSource         source array of ints
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, int piDestinationFrom, int[] paiSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            patDestination[piDestinationFrom + i] = (byte) (paiSource[piSourceFrom + i]);
    }

    /**
     * Copy-conversion method that copies N int elements
     * from an array of ints into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param paiSource      source array of ints
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, int[] paiSource, int piHowMany) {
        copy(patDestination, 0, paiSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all int elements
     * from an array of ints into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param paiSource      sourc earray of ints
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, int[] paiSource) {
        copy(patDestination, 0, paiSource, 0, paiSource.length);
    }

    /**
     * General copy-conversion method that copies N int elements
     * from an array of ints into array of characters.
     *
     * @param pacDestination    destination array of characters
     * @param piDestinationFrom index in the destination to start place elements at
     * @param paiSource         source array of ints
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, int piDestinationFrom, int[] paiSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pacDestination[piDestinationFrom + i] = (char) paiSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N int elements
     * from an array of ints into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param paiSource      sourc earray of ints
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, int[] paiSource, int piHowMany) {
        copy(pacDestination, 0, paiSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all int elements
     * from an array of ints into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param paiSource      sourc earray of ints
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, int[] paiSource) {
        copy(pacDestination, 0, paiSource, 0, paiSource.length);
    }

    /**
     * General copy-conversion method that copies N double elements
     * from an array of doubles into array of integers.
     *
     * @param paiDestination    destination array of integers
     * @param piDestinationFrom index in the destination to start place elements at
     * @param padSource         source array of doubles
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, int piDestinationFrom, double[] padSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            paiDestination[piDestinationFrom + i] = (int) padSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N double elements
     * from an array of doubles into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param padSource      source array of doubles
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, double[] padSource, int piHowMany) {
        copy(paiDestination, 0, padSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all double elements
     * from an array of doubles into array of ints.
     *
     * @param paiDestination destination array of ints
     * @param padSource      source array of doubles
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, double[] padSource) {
        copy(paiDestination, 0, padSource, 0, padSource.length);
    }

    /**
     * General copy-conversion method that copies N double elements
     * from an array of doubles into array of characters.
     *
     * @param pacDestination    destination array of characters
     * @param piDestinationFrom index in the destination to start place elements at
     * @param padSource         source array of doubles
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, int piDestinationFrom, double[] padSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pacDestination[piDestinationFrom + i] = (char) padSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N double elements
     * from an array of doubles into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param padSource      source array of doubles
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, double[] padSource, int piHowMany) {
        copy(pacDestination, 0, padSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all double elements
     * from an array of doubles into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param padSource      source array of doubles
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, double[] padSource) {
        copy(pacDestination, 0, padSource, 0, padSource.length);
    }

    /**
     * General copy-conversion method that copies N double elements
     * from an array of doubles into array of bytes.
     *
     * @param patDestination    destination array of bytes
     * @param piDestinationFrom index in the destination to start place elements at
     * @param padSource         source array of doubles
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, int piDestinationFrom, double[] padSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            patDestination[piDestinationFrom + i] = (byte) padSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N double elements
     * from an array of doubles into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param padSource      source array of doubles
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, double[] padSource, int piHowMany) {
        copy(patDestination, 0, padSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all double elements
     * from an array of doubles into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param padSource      source array of doubles
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, double[] padSource) {
        copy(patDestination, 0, padSource, 0, padSource.length);
    }

    /**
     * General copy-conversion method that copies N double elements
     * from an array of doubles into array of floats.
     *
     * @param pafDestination    destination array of floats
     * @param piDestinationFrom index in the destination to start place elements at
     * @param padSource         source array of doubles
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, int piDestinationFrom, double[] padSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pafDestination[piDestinationFrom + i] = (float) padSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N double elements
     * from an array of doubles into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param padSource      source array of doubles
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, double[] padSource, int piHowMany) {
        copy(pafDestination, 0, padSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all double elements
     * from an array of doubles into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param padSource      source array of doubles
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, double[] padSource) {
        copy(pafDestination, 0, padSource, 0, padSource.length);
    }

    /**
     * General copy-conversion method that copies N double elements
     * from an array of doubles into array of shorts.
     *
     * @param pasDestination    destination array of shorts
     * @param piDestinationFrom index in the destination to start place elements at
     * @param padSource         source array of doubles
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, int piDestinationFrom, double[] padSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pasDestination[piDestinationFrom + i] = (short) padSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N double elements
     * from an array of doubles into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param padSource      source array of doubles
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, double[] padSource, int piHowMany) {
        copy(pasDestination, 0, padSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all double elements
     * from an array of doubles into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param padSource      source array of doubles
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, double[] padSource) {
        copy(pasDestination, 0, padSource, 0, padSource.length);
    }

    /**
     * General copy-conversion method that copies N double elements
     * from an array of doubles into array of longs.
     *
     * @param palDestination    destination array of longs
     * @param piDestinationFrom index in the destination to start place elements at
     * @param padSource         source array of doubles
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int piDestinationFrom, double[] padSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            palDestination[piDestinationFrom + i] = (long) padSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N double elements
     * from an array of doubles into array of longs.
     *
     * @param palDestination destination array of longs
     * @param padSource      source array of doubles
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, double[] padSource, int piHowMany) {
        copy(palDestination, 0, padSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all double elements
     * from an array of doubles into array of longs.
     *
     * @param palDestination destination array of longs
     * @param padSource      source array of doubles
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, double[] padSource) {
        copy(palDestination, 0, padSource, 0, padSource.length);
    }


    /**
     * General copy-conversion method that copies N float elements
     * from an array of floats into array of characters.
     *
     * @param pacDestination    destination array of characters
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pafSource         source array of floats
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, int piDestinationFrom, float[] pafSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pacDestination[piDestinationFrom + i] = (char) pafSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N float elements
     * from an array of floats into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param pafSource      source array of floats
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, float[] pafSource, int piHowMany) {
        copy(pacDestination, 0, pafSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all float elements
     * from an array of floats into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param pafSource      source array of floats
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, float[] pafSource) {
        copy(pacDestination, 0, pafSource, 0, pafSource.length);
    }

    /**
     * General copy-conversion method that copies N float elements
     * from an array of floats into array of bytes.
     *
     * @param patDestination    destination array of bytes
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pafSource         source array of floats
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, int piDestinationFrom, float[] pafSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            patDestination[piDestinationFrom + i] = (byte) pafSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N float elements
     * from an array of floats into array of bytes.
     *
     * @param patDestination destination array of characters
     * @param pafSource      source array of bytes
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, float[] pafSource, int piHowMany) {
        copy(patDestination, 0, pafSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all float elements
     * from an array of floats into array of characters.
     *
     * @param patDestination destination array of bytes
     * @param pafSource      source array of floats
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, float[] pafSource) {
        copy(patDestination, 0, pafSource, 0, pafSource.length);
    }

    /**
     * General copy-conversion method that copies N float elements
     * from an array of floats into array of integers.
     *
     * @param paiDestination    destination array of integers
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pafSource         source array of floats
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, int piDestinationFrom, float[] pafSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            paiDestination[piDestinationFrom + i] = (int) pafSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N float elements
     * from an array of floats into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param pafSource      source array of floats
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, float[] pafSource, int piHowMany) {
        copy(paiDestination, 0, pafSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all float elements
     * from an array of floats into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param pafSource      source array of floats
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, float[] pafSource) {
        copy(paiDestination, 0, pafSource, 0, pafSource.length);
    }

    /**
     * General copy-conversion method that copies N float elements
     * from an array of floats into array of shorts.
     *
     * @param pasDestination    destination array of shorts
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pafSource         source array of floats
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, int piDestinationFrom, float[] pafSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pasDestination[piDestinationFrom + i] = (short) pafSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N float elements
     * from an array of floats into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param pafSource      source array of floats
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, float[] pafSource, int piHowMany) {
        copy(pasDestination, 0, pafSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all float elements
     * from an array of floats into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param pafSource      source array of floats
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, float[] pafSource) {
        copy(pasDestination, 0, pafSource, 0, pafSource.length);
    }

    /**
     * General copy-conversion method that copies N float elements
     * from an array of floats into array of longs.
     *
     * @param palDestination    destination array of longs
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pafSource         source array of floats
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int piDestinationFrom, float[] pafSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            palDestination[piDestinationFrom + i] = (long) pafSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N float elements
     * from an array of floats into array of longs.
     *
     * @param palDestination destination array of longs
     * @param pafSource      source array of floats
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, float[] pafSource, int piHowMany) {
        copy(palDestination, 0, pafSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all float elements
     * from an array of floats into array of longs.
     *
     * @param palDestination destination array of longs
     * @param pafSource      source array of floats
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, float[] pafSource) {
        copy(palDestination, 0, pafSource, 0, pafSource.length);
    }

    /**
     * General copy-conversion method that copies N float elements
     * from an array of floats into array of doubles.
     *
     * @param padDestination    destination array of doubles
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pafSource         source array of floats
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, int piDestinationFrom, float[] pafSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            padDestination[piDestinationFrom + i] = pafSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N float elements
     * from an array of floats into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param pafSource      source array of floats
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, float[] pafSource, int piHowMany) {
        copy(padDestination, 0, pafSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all float elements
     * from an array of floats into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param pafSource      source array of floats
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, float[] pafSource) {
        copy(padDestination, 0, pafSource, 0, pafSource.length);
    }

    /**
     * General copy-conversion method that copies N short elements
     * from an array of shorts into array of doubles.
     *
     * @param padDestination    destination array of doubles
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pasSource         source array of shorts
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, int piDestinationFrom, short[] pasSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            padDestination[piDestinationFrom + i] = pasSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N short elements
     * from an array of shorts into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param pasSource      source array of shorts
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, short[] pasSource, int piHowMany) {
        copy(padDestination, 0, pasSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all short elements
     * from an array of shorts into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param pasSource      source array of shorts
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, short[] pasSource) {
        copy(padDestination, 0, pasSource, 0, pasSource.length);
    }

    /**
     * General copy-conversion method that copies N short elements
     * from an array of shorts into array of characters.
     *
     * @param pacDestination    destination array of characters
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pasSource         source array of shorts
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, int piDestinationFrom, short[] pasSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pacDestination[piDestinationFrom + i] = (char) pasSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N short elements
     * from an array of shorts into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param pasSource      source array of shorts
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, short[] pasSource, int piHowMany) {
        copy(pacDestination, 0, pasSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all short elements
     * from an array of shorts into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param pasSource      source array of shorts
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, short[] pasSource) {
        copy(pacDestination, 0, pasSource, 0, pasSource.length);
    }

    /**
     * General copy-conversion method that copies N short elements
     * from an array of shorts into array of bytes.
     *
     * @param patDestination    destination array of bytes
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pasSource         source array of shorts
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, int piDestinationFrom, short[] pasSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            patDestination[piDestinationFrom + i] = (byte) pasSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N short elements
     * from an array of shorts into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param pasSource      source array of shorts
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, short[] pasSource, int piHowMany) {
        copy(patDestination, 0, pasSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all short elements
     * from an array of shorts into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param pasSource      source array of shorts
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, short[] pasSource) {
        copy(patDestination, 0, pasSource, 0, pasSource.length);
    }

    /**
     * General copy-conversion method that copies N short elements
     * from an array of shorts into array of integers.
     *
     * @param paiDestination    destination array of integers
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pasSource         source array of shorts
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, int piDestinationFrom, short[] pasSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            paiDestination[piDestinationFrom + i] = pasSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N short elements
     * from an array of shorts into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param pasSource      source array of shorts
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, short[] pasSource, int piHowMany) {
        copy(paiDestination, 0, pasSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all short elements
     * from an array of shorts into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param pasSource      source array of shorts
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, short[] pasSource) {
        copy(paiDestination, 0, pasSource, 0, pasSource.length);
    }

    /**
     * General copy-conversion method that copies N short elements
     * from an array of shorts into array of floats.
     *
     * @param pafDestination    destination array of floats
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pasSource         source array of shorts
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, int piDestinationFrom, short[] pasSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pafDestination[piDestinationFrom + i] = pasSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N short elements
     * from an array of shorts into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param pasSource      source array of shorts
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, short[] pasSource, int piHowMany) {
        copy(pafDestination, 0, pasSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all short elements
     * from an array of shorts into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param pasSource      source array of shorts
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, short[] pasSource) {
        copy(pafDestination, 0, pasSource, 0, pasSource.length);
    }

    /**
     * General copy-conversion method that copies N short elements
     * from an array of shorts into array of longs.
     *
     * @param palDestination    destination array of longs
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pasSource         source array of shorts
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int piDestinationFrom, short[] pasSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            palDestination[piDestinationFrom + i] = pasSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N short elements
     * from an array of shorts into array of longs.
     *
     * @param palDestination destination array of longs
     * @param pasSource      source array of shorts
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, short[] pasSource, int piHowMany) {
        copy(palDestination, 0, pasSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all short elements
     * from an array of shorts into array of longs.
     *
     * @param palDestination destination array of longs
     * @param pasSource      source array of shorts
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, short[] pasSource) {
        copy(palDestination, 0, pasSource, 0, pasSource.length);
    }

    /**
     * General copy-conversion method that copies N byte elements
     * from an array of bytes into array of longs.
     *
     * @param palDestination    destination array of longs
     * @param piDestinationFrom index in the destination to start place elements at
     * @param patSource         source array of bytes
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int piDestinationFrom, byte[] patSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            palDestination[piDestinationFrom + i] = patSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N byte elements
     * from an array of bytes into array of longs.
     *
     * @param palDestination destination array of longs
     * @param patSource      source array of bytes
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, byte[] patSource, int piHowMany) {
        copy(palDestination, 0, patSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all byte elements
     * from an array of bytes into array of longs.
     *
     * @param palDestination destination array of longs
     * @param patSource      source array of bytes
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, byte[] patSource) {
        copy(palDestination, 0, patSource, 0, patSource.length);
    }

    /**
     * General copy-conversion method that copies N byte elements
     * from an array of bytes into array of doubles.
     *
     * @param padDestination    destination array of doubles
     * @param piDestinationFrom index in the destination to start place elements at
     * @param patSource         source array of bytes
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, int piDestinationFrom, byte[] patSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            padDestination[piDestinationFrom + i] = patSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N byte elements
     * from an array of bytes into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param patSource      source array of bytes
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, byte[] patSource, int piHowMany) {
        copy(padDestination, 0, patSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all byte elements
     * from an array of bytes into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param patSource      source array of bytes
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, byte[] patSource) {
        copy(padDestination, 0, patSource, 0, patSource.length);
    }

    /**
     * General copy-conversion method that copies N byte elements
     * from an array of bytes into array of shorts.
     *
     * @param pasDestination    destination array of shorts
     * @param piDestinationFrom index in the destination to start place elements at
     * @param patSource         source array of bytes
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, int piDestinationFrom, byte[] patSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pasDestination[piDestinationFrom + i] = patSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N byte elements
     * from an array of bytes into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param patSource      source array of bytes
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, byte[] patSource, int piHowMany) {
        copy(pasDestination, 0, patSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all byte elements
     * from an array of bytes into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param patSource      source array of bytes
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, byte[] patSource) {
        copy(pasDestination, 0, patSource, 0, patSource.length);
    }

    /**
     * General copy-conversion method that copies N byte elements
     * from an array of bytes into array of floats.
     *
     * @param pafDestination    destination array of floats
     * @param piDestinationFrom index in the destination to start place elements at
     * @param patSource         source array of bytes
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, int piDestinationFrom, byte[] patSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pafDestination[piDestinationFrom + i] = patSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N byte elements
     * from an array of bytes into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param patSource      source array of bytes
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, byte[] patSource, int piHowMany) {
        copy(pafDestination, 0, patSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all byte elements
     * from an array of bytes into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param patSource      source array of bytes
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, byte[] patSource) {
        copy(pafDestination, 0, patSource, 0, patSource.length);
    }

    /**
     * General copy-conversion method that copies N byte elements
     * from an array of bytes into array of characters.
     *
     * @param pacDestination    destination array of characters
     * @param piDestinationFrom index in the destination to start place elements at
     * @param patSource         source array of bytes
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, int piDestinationFrom, byte[] patSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pacDestination[piDestinationFrom + i] = (char) patSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N byte elements
     * from an array of bytes into array of characters.
     *
     * @param pacDestination destination array of characters
     * @param patSource      source array of bytes
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, byte[] patSource, int piHowMany) {
        copy(pacDestination, 0, patSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all byte elements
     * from an array of bytes into array of bytes.
     *
     * @param pacDestination destination array of characters
     * @param patSource      source array of bytes
     * @since 0.3.0.3
     */
    public static void copy(char[] pacDestination, byte[] patSource) {
        copy(pacDestination, 0, patSource, 0, patSource.length);
    }

    /**
     * General copy-conversion method that copies N byte elements
     * from an array of bytes into array of integers.
     *
     * @param paiDestination    destination array of integers
     * @param piDestinationFrom index in the destination to start place elements at
     * @param patSource         source array of bytes
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, int piDestinationFrom, byte[] patSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            paiDestination[piDestinationFrom + i] = patSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N byte elements
     * from an array of bytes into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param patSource      source array of bytes
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, byte[] patSource, int piHowMany) {
        copy(paiDestination, 0, patSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all byte elements
     * from an array of bytes into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param patSource      source array of bytes
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, byte[] patSource) {
        copy(paiDestination, 0, patSource, 0, patSource.length);
    }

    /**
     * General copy-conversion method that copies N character elements
     * from an array of characters into array of integers.
     *
     * @param paiDestination    destination array of integers
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pacSource         source array of characters
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, int piDestinationFrom, char[] pacSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            paiDestination[piDestinationFrom + i] = pacSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N character elements
     * from an array of characters into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param pacSource      source array of characters
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, char[] pacSource, int piHowMany) {
        copy(paiDestination, 0, pacSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all character elements
     * from an array of characters into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param pacSource      source array of characters
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, char[] pacSource) {
        copy(paiDestination, 0, pacSource, 0, pacSource.length);
    }

    /**
     * General copy-conversion method that copies N character elements
     * from an array of characters into array of bytes.
     *
     * @param patDestination    destination array of bytes
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pacSource         source array of characters
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, int piDestinationFrom, char[] pacSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            patDestination[piDestinationFrom + i] = (byte) pacSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N character elements
     * from an array of characters into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param pacSource      source array of characters
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, char[] pacSource, int piHowMany) {
        copy(patDestination, 0, pacSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all character elements
     * from an array of characters into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param pacSource      source array of characters
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, char[] pacSource) {
        copy(patDestination, 0, pacSource, 0, pacSource.length);
    }

    /**
     * General copy-conversion method that copies N character elements
     * from an array of characters into array of shorts.
     *
     * @param pasDestination    destination array of shorts
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pacSource         source array of characters
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, int piDestinationFrom, char[] pacSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pasDestination[piDestinationFrom + i] = (short) pacSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N character elements
     * from an array of characters into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param pacSource      source array of characters
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, char[] pacSource, int piHowMany) {
        copy(pasDestination, 0, pacSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all character elements
     * from an array of characters into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param pacSource      source array of characters
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, char[] pacSource) {
        copy(pasDestination, 0, pacSource, 0, pacSource.length);
    }

    /**
     * General copy-conversion method that copies N character elements
     * from an array of characters into array of longs.
     *
     * @param palDestination    destination array of longs
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pacSource         source array of characters
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int piDestinationFrom, char[] pacSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            palDestination[piDestinationFrom + i] = pacSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N character elements
     * from an array of characters into array of longs.
     *
     * @param palDestination destination array of longs
     * @param pacSource      source array of characters
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, char[] pacSource, int piHowMany) {
        copy(palDestination, 0, pacSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all character elements
     * from an array of characters into array of longs.
     *
     * @param palDestination destination array of longs
     * @param pacSource      source array of characters
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, char[] pacSource) {
        copy(palDestination, 0, pacSource, 0, pacSource.length);
    }

    /**
     * General copy-conversion method that copies N character elements
     * from an array of characters into array of floats.
     *
     * @param pafDestination    destination array of floats
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pacSource         source array of characters
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, int piDestinationFrom, char[] pacSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pafDestination[piDestinationFrom + i] = pacSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N character elements
     * from an array of characters into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param pacSource      source array of characters
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, char[] pacSource, int piHowMany) {
        copy(pafDestination, 0, pacSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all character elements
     * from an array of characters into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param pacSource      source array of characters
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, char[] pacSource) {
        copy(pafDestination, 0, pacSource, 0, pacSource.length);
    }

    /**
     * General copy-conversion method that copies N character elements
     * from an array of characters into array of doubles.
     *
     * @param padDestination    destination array of doubles
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pacSource         source array of characters
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, int piDestinationFrom, char[] pacSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            padDestination[piDestinationFrom + i] = pacSource[piSourceFrom + i];
    }

    /**
     * Copy-conversion method that copies N character elements
     * from an array of characters into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param pacSource      source array of characters
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, char[] pacSource, int piHowMany) {
        copy(padDestination, 0, pacSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all character elements
     * from an array of characters into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param pacSource      source array of characters
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, char[] pacSource) {
        copy(padDestination, 0, pacSource, 0, pacSource.length);
    }

    /**
     * General copy-conversion method that copies N int elements
     * from an array of ints into array of Strings.
     *
     * @param pstrDestination   destination array of Strings
     * @param piDestinationFrom index in the destination to start place elements at
     * @param paiSource         source array of ints
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int piDestinationFrom, int[] paiSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pstrDestination[piDestinationFrom + i] = paiSource[piSourceFrom + i] + "";
    }

    /**
     * Copy-conversion method that copies N int elements
     * from an array of ints into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param paiSource       source array of ints
     * @param piHowMany       N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int[] paiSource, int piHowMany) {
        copy(pstrDestination, 0, paiSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all int elements
     * from an array of ints into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param paiSource       source array of ints
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int[] paiSource) {
        copy(pstrDestination, 0, paiSource, 0, paiSource.length);
    }

    /**
     * General copy-conversion method that copies N char elements
     * from an array of chars into array of Strings.
     *
     * @param pstrDestination   destination array of Strings
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pacSource         source array of chars
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int piDestinationFrom, char[] pacSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pstrDestination[piDestinationFrom + i] = pacSource[piSourceFrom + i] + "";
    }

    /**
     * Copy-conversion method that copies N char elements
     * from an array of chars into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param pacSource       source array of chars
     * @param piHowMany       N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, char[] pacSource, int piHowMany) {
        copy(pstrDestination, 0, pacSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all char elements
     * from an array of chars into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param pacSource       source array of chars
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, char[] pacSource) {
        copy(pstrDestination, 0, pacSource, 0, pacSource.length);
    }

    /**
     * General copy-conversion method that copies N byte elements
     * from an array of bytes into array of Strings.
     *
     * @param pstrDestination   destination array of Strings
     * @param piDestinationFrom index in the destination to start place elements at
     * @param patSource         source array of bytes
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int piDestinationFrom, byte[] patSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pstrDestination[piDestinationFrom + i] = patSource[piSourceFrom + i] + "";
    }

    /**
     * Copy-conversion method that copies N byte elements
     * from an array of bytes into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param patSource       source array of bytes
     * @param piHowMany       N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, byte[] patSource, int piHowMany) {
        copy(pstrDestination, 0, patSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all byte elements
     * from an array of ints into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param patSource       source array of bytes
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, byte[] patSource) {
        copy(pstrDestination, 0, patSource, 0, patSource.length);
    }

    /**
     * General copy-conversion method that copies N short elements
     * from an array of shorts into array of Strings.
     *
     * @param pstrDestination   destination array of Strings
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pasSource         source array of shorts
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int piDestinationFrom, short[] pasSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pstrDestination[piDestinationFrom + i] = pasSource[piSourceFrom + i] + "";
    }

    /**
     * Copy-conversion method that copies N short elements
     * from an array of shorts into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param patSource       source array of shorts
     * @param piHowMany       N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, short[] patSource, int piHowMany) {
        copy(pstrDestination, 0, patSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all short elements
     * from an array of shorts into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param pasSource       source array of shorts
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, short[] pasSource) {
        copy(pstrDestination, 0, pasSource, 0, pasSource.length);
    }

    /**
     * General copy-conversion method that copies N long elements
     * from an array of longs into array of Strings.
     *
     * @param pstrDestination   destination array of Strings
     * @param piDestinationFrom index in the destination to start place elements at
     * @param palSource         source array of longs
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int piDestinationFrom, long[] palSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pstrDestination[piDestinationFrom + i] = palSource[piSourceFrom + i] + "";
    }

    /**
     * Copy-conversion method that copies N long elements
     * from an array of longs into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param palSource       source array of longs
     * @param piHowMany       N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, long[] palSource, int piHowMany) {
        copy(pstrDestination, 0, palSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all long elements
     * from an array of longs into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param palSource       source array of longs
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, long[] palSource) {
        copy(pstrDestination, 0, palSource, 0, palSource.length);
    }

    /**
     * General copy-conversion method that copies N float elements
     * from an array of floats into array of Strings.
     *
     * @param pstrDestination   destination array of Strings
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pafSource         source array of floats
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int piDestinationFrom, float[] pafSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pstrDestination[piDestinationFrom + i] = pafSource[piSourceFrom + i] + "";
    }

    /**
     * Copy-conversion method that copies N float elements
     * from an array of floats into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param pafSource       source array of floats
     * @param piHowMany       N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, float[] pafSource, int piHowMany) {
        copy(pstrDestination, 0, pafSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all float elements
     * from an array of floats into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param pafSource       source array of floats
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, float[] pafSource) {
        copy(pstrDestination, 0, pafSource, 0, pafSource.length);
    }

    /**
     * General copy-conversion method that copies N double elements
     * from an array of doubles into array of Strings.
     *
     * @param pstrDestination   destination array of Strings
     * @param piDestinationFrom index in the destination to start place elements at
     * @param padSource         source array of doubles
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, int piDestinationFrom, double[] padSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pstrDestination[piDestinationFrom + i] = padSource[piSourceFrom + i] + "";
    }

    /**
     * Copy-conversion method that copies N double elements
     * from an array of doubles into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param padSource       source array of doubles
     * @param piHowMany       N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, double[] padSource, int piHowMany) {
        copy(pstrDestination, 0, padSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all double elements
     * from an array of doubles into array of Strings.
     *
     * @param pstrDestination destination array of Strings
     * @param padSource       source array of doubles
     * @since 0.3.0.3
     */
    public static void copy(String[] pstrDestination, double[] padSource) {
        copy(pstrDestination, 0, padSource, 0, padSource.length);
    }

    /**
     * General copy-conversion method that copies N String elements
     * from an array of Strings into array of doubles.
     *
     * @param padDestination    destination array of doubles
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pastrSource       source array of Strings
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @throws NumberFormatException if one of the Strings doesn't have a properly formatted number
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, int piDestinationFrom, String[] pastrSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            padDestination[piDestinationFrom + i] = Double.parseDouble(pastrSource[piSourceFrom + i]);
    }

    /**
     * Copy-conversion method that copies N String elements
     * from an array of Strings into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param pastrSource    source array of Strings
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, String[] pastrSource, int piHowMany) {
        copy(padDestination, 0, pastrSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all String elements
     * from an array of Strings into array of doubles.
     *
     * @param padDestination destination array of doubles
     * @param pastrSource    source array of Strings
     * @since 0.3.0.3
     */
    public static void copy(double[] padDestination, String[] pastrSource) {
        copy(padDestination, 0, pastrSource, 0, pastrSource.length);
    }

    /**
     * General copy-conversion method that copies N String elements
     * from an array of Strings into array of floats.
     *
     * @param pafDestination    destination array of floats
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pastrSource       source array of Strings
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @throws NumberFormatException if one of the Strings doesn't have a properly formatted number
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, int piDestinationFrom, String[] pastrSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pafDestination[piDestinationFrom + i] = Float.parseFloat(pastrSource[piSourceFrom + i]);
    }

    /**
     * Copy-conversion method that copies N String elements
     * from an array of Strings into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param pastrSource    source array of Strings
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(float[] pafDestination, String[] pastrSource, int piHowMany) {
        copy(pafDestination, 0, pastrSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all String elements
     * from an array of Strings into array of floats.
     *
     * @param pafDestination destination array of floats
     * @param pastrSource    source array of Strings
     */
    public static void copy(float[] pafDestination, String[] pastrSource) {
        copy(pafDestination, 0, pastrSource, 0, pastrSource.length);
    }

    /**
     * General copy-conversion method that copies N String elements
     * from an array of Strings into array of longs.
     *
     * @param palDestination    destination array of longs
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pastrSource       source array of Strings
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @throws NumberFormatException if one of the Strings doesn't have a properly formatted number
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, int piDestinationFrom, String[] pastrSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            palDestination[piDestinationFrom + i] = Long.parseLong(pastrSource[piSourceFrom + i]);
    }

    /**
     * Copy-conversion method that copies N String elements
     * from an array of Strings into array of longs.
     *
     * @param palDestination destination array of longs
     * @param pastrSource    source array of Strings
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, String[] pastrSource, int piHowMany) {
        copy(palDestination, 0, pastrSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all String elements
     * from an array of Strings into array of longs.
     *
     * @param palDestination destination array of longs
     * @param pastrSource    source array of Strings
     * @since 0.3.0.3
     */
    public static void copy(long[] palDestination, String[] pastrSource) {
        copy(palDestination, 0, pastrSource, 0, pastrSource.length);
    }

    /**
     * General copy-conversion method that copies N String elements
     * from an array of Strings into array of integers.
     *
     * @param paiDestination    destination array of integers
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pastrSource       source array of Strings
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @throws NumberFormatException if one of the Strings doesn't have a properly formatted number
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, int piDestinationFrom, String[] pastrSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            paiDestination[piDestinationFrom + i] = Integer.parseInt(pastrSource[piSourceFrom + i]);
    }

    /**
     * Copy-conversion method that copies N String elements
     * from an array of Strings into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param pastrSource    source array of Strings
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, String[] pastrSource, int piHowMany) {
        copy(paiDestination, 0, pastrSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all String elements
     * from an array of Strings into array of integers.
     *
     * @param paiDestination destination array of integers
     * @param pastrSource    source array of Strings
     * @since 0.3.0.3
     */
    public static void copy(int[] paiDestination, String[] pastrSource) {
        copy(paiDestination, 0, pastrSource, 0, pastrSource.length);
    }

    /**
     * General copy-conversion method that copies N String elements
     * from an array of Strings into array of shorts.
     *
     * @param pasDestination    destination array of shorts
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pastrSource       source array of Strings
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @throws NumberFormatException if one of the Strings doesn't have a properly formatted number
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, int piDestinationFrom, String[] pastrSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            pasDestination[piDestinationFrom + i] = Short.parseShort(pastrSource[piSourceFrom + i]);
    }

    /**
     * Copy-conversion method that copies N String elements
     * from an array of Strings into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param pastrSource    source array of Strings
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, String[] pastrSource, int piHowMany) {
        copy(pasDestination, 0, pastrSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all String elements
     * from an array of Strings into array of shorts.
     *
     * @param pasDestination destination array of shorts
     * @param pastrSource    source array of Strings
     * @since 0.3.0.3
     */
    public static void copy(short[] pasDestination, String[] pastrSource) {
        copy(pasDestination, 0, pastrSource, 0, pastrSource.length);
    }

    /**
     * General copy-conversion method that copies N String elements
     * from an array of Strings into array of bytes.
     *
     * @param patDestination    destination array of bytes
     * @param piDestinationFrom index in the destination to start place elements at
     * @param pastrSource       source array of Strings
     * @param piSourceFrom      index in the source to start take elements from
     * @param piHowMany         N; how many elements to copy
     * @throws NumberFormatException if one of the Strings doesn't have a properly formatted number
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, int piDestinationFrom, String[] pastrSource, int piSourceFrom, int piHowMany) {
        for (int i = 0; i < piHowMany; i++)
            patDestination[piDestinationFrom + i] = Byte.parseByte(pastrSource[piSourceFrom + i]);
    }

    /**
     * Copy-conversion method that copies N String elements
     * from an array of Strings into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param pastrSource    source array of Strings
     * @param piHowMany      N; how many elements to copy
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, String[] pastrSource, int piHowMany) {
        copy(patDestination, 0, pastrSource, 0, piHowMany);
    }

    /**
     * Copy-conversion method that copies all String elements
     * from an array of Strings into array of bytes.
     *
     * @param patDestination destination array of bytes
     * @param pastrSource    source array of Strings
     * @since 0.3.0.3
     */
    public static void copy(byte[] patDestination, String[] pastrSource) {
        copy(patDestination, 0, pastrSource, 0, pastrSource.length);
    }

	/*
	 * -------------
	 * Binary Search
	 * -------------
	 */

    /**
     * The <code>binarySearch()</code> routine is based on <code>java.util.Arrays.binarySearch()</code>.
     *
     * @param pafArray the array of floats to be searched
     * @param pfValue  the value of float to be searched in the array of floats
     * @return index of the searched value if it is in the array of floats
     * @since 0.3.0.3
     */
    public static int binarySearch(float[] pafArray, float pfValue) {
        return java.util.Arrays.binarySearch(pafArray, pfValue);
    }

    /**
     * The <code>binarySearch()</code> routine is based on <code>java.util.Arrays.binarySearch()</code>.
     *
     * @param padArray the array of doubles to be searched
     * @param pdValue  the value of double to be searched in the array of doubles
     * @return index of the searched value if it is in the array of doubles
     * @since 0.3.0.3
     */
    public static int binarySearch(double[] padArray, double pdValue) {
        return java.util.Arrays.binarySearch(padArray, pdValue);
    }

    /**
     * The <code>binarySearch()</code> routine is based on <code>java.util.Arrays.binarySearch()</code>.
     *
     * @param pasArray the array of shorts to be searched
     * @param psValue  the value of shorts to be searched in the array of shorts
     * @return index of the searched value if it is in the array of shorts
     * @since 0.3.0.3
     */
    public static int binarySearch(short[] pasArray, short psValue) {
        return java.util.Arrays.binarySearch(pasArray, psValue);
    }

    /**
     * The <code>binarySearch()</code> routine is based on <code>java.util.Arrays.binarySearch()</code>.
     *
     * @param paiArray the array of integers to be searched
     * @param piValue  the value of integer to be searched in the array of integers
     * @return index of the searched value if it is in the array of integers
     * @since 0.3.0.3
     */
    public static int binarySearch(int[] paiArray, int piValue) {
        return java.util.Arrays.binarySearch(paiArray, piValue);
    }

    /**
     * The <code>binarySearch()</code> routine is based on <code>java.util.Arrays.binarySearch()</code>.
     *
     * @param patArray the array of bytes to be searched
     * @param ptValue  the value of byte to be searched in the array of bytes
     * @return index of the searched value if it is in the array of bytes
     * @since 0.3.0.3
     */
    public static int binarySearch(byte[] patArray, byte ptValue) {
        return java.util.Arrays.binarySearch(patArray, ptValue);
    }

    /**
     * The <code>binarySearch()</code> routine is based on <code>java.util.Arrays.binarySearch()</code>.
     *
     * @param pacArray the array of characters to be searched
     * @param pcValue  the value of character to be searched in the array of characters
     * @return index of the searched value if it is in the array of characters
     * @since 0.3.0.3
     */
    public static int binarySearch(char[] pacArray, char pcValue) {
        return java.util.Arrays.binarySearch(pacArray, pcValue);
    }

    /**
     * The <code>binarySearch()</code> routine is based on <code>java.util.Arrays.binarySearch()</code>.
     *
     * @param paoArray the array of objects to be searched
     * @param poValue  the value of object to be searched in the array of objects
     * @return index of the searched value if it is in the array of objects
     * @throws ClassCastException if the type of the searched value is not match the type of the array
     * @since 0.3.0.3
     */
    public static int binarySearch(Object[] paoArray, Object poValue) {
        return java.util.Arrays.binarySearch(paoArray, poValue);
    }

    /**
     * The <code>binarySearch()</code> routine is based on <code>java.util.Arrays.binarySearch()</code>.
     *
     * @param paoArray     the array of objects to be searched
     * @param poValue      the value of object to be searched in the array of objects
     * @param poComparator the comparator to decide the order of the array
     * @return index of the searched value if it is in the array of objects
     * @throws ClassCastException if the type of the searched value is not match the type of the array
     * @since 0.3.0.3
     */
    public static int binarySearch(Object[] paoArray, Object poValue, Comparator poComparator) {
        return java.util.Arrays.binarySearch(paoArray, poValue, poComparator);
    }

	/*
	 * ----
	 * Misc
	 * ----
	 */

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.35 $";
    }
}

// EOF
