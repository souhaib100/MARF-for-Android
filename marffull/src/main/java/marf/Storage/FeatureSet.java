package marf.Storage;

import java.io.Serializable;
import java.util.Vector;

import marf.util.NotImplementedException;


/**
 * <p>FeatureSet -- Encapsulates subject's feature vectors.</p>
 * TODO: complete implementation.
 * <p>
 * <p>$Id: FeatureSet.java,v 1.14 2006/01/02 22:24:00 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.14 $
 * @since 0.3.0.1
 */
public class FeatureSet
        implements Serializable, Cloneable {
    /**
     * A Vector of TrainingSamples.
     */
    protected Vector oFeatureVectors = new Vector();

    /**
     * For serialization versioning.
     * When adding new members or make other structural
     * changes regenerate this number with the
     * <code>serialver</code> tool that comes with JDK.
     *
     * @since 0.3.0.4
     */
    private static final long serialVersionUID = -1931299425905008139L;

    /**
     * Construct a training set object.
     */
    public FeatureSet() {
    }

    /**
     * Retrieves training samples.
     *
     * @return vector of training samples.
     */
    public Vector getFeatureVectors() {
        return this.oFeatureVectors;
    }

    /**
     * Appends yet another feature vector.
     * TODO: implement.
     *
     * @param padFeatures
     * @param piSubjectID
     * @param pstrFileName
     * @param piFeatureExtractionMethod
     * @param piPreprocessingMethod
     * @throws NotImplementedException
     */
    public void addFeatureVector
    (
            double[] padFeatures,
            int piSubjectID,
            String pstrFileName,
            int piFeatureExtractionMethod,
            int piPreprocessingMethod
    ) {
        throw new NotImplementedException();

		/*
         * check if this sample is already in the training set
		 * for this feature type & preprocessing mode
		 */
/*		TrainingSample samp = null;
        boolean newSamp = true;

		for(int i = 0; (i < oFeatureVectors.size()) && (newSamp); i++)
		{
			samp = (TrainingSample)(oFeatureVectors.get(i));

			if
			(
				(iFeatureExtractionMethod == samp.iFeatureExtractionMethod)
				&&
				(strFileName.compareTo(samp.strFileName) == 0)
				&&
				(iPreprocessingMethod == samp.iPreprocessingMethod)
			)
				newSamp = false;
		}

		if(newSamp)
			samp = new TrainingSample();

		samp.adFeatures = adFeatures;
		samp.iSubjectID = iSubjectID;
		samp.strFileName = strFileName;
		samp.iFeatureExtractionMethod = iFeatureExtractionMethod;
		samp.iPreprocessingMethod = iPreprocessingMethod;

		if(newSamp)
		{
			this.oFeatureVectors.add(samp);

			Debug.debug
			(
				"Added feature vector for subject " + iSubjectID +
				", feature extraction method " + iFeatureExtractionMethod +
				" (\"" + strFileName + "\")"
			);
		}
		else
		{
			Debug.debug
			(
				"Updated feature vector for subject " + iSubjectID +
				", feature type " + iFeatureExtractionMethod
				+ " (\"" + strFileName + "\")"
			);
		}
*/
    }

    /**
     * Sizes of the feature vectors set.
     *
     * @return number of training samples in the set
     */
    public int size() {
        return this.oFeatureVectors.size();
    }

    /**
     * Retrieve the current training set from disk.
     * TODO: implement.
     *
     * @throws StorageException
     * @throws NotImplementedException
     * @since 0.3.0.5
     */
    public void restore()
            throws StorageException {
        throw new NotImplementedException();
/*	
        try
		{
			BufferedReader br = new BufferedReader(new FileReader(this.strFeatureSetFile));

			int num = Integer.parseInt(br.readLine());

			for(int i = 0; i < num; i++)
			{
				TrainingSample ts = new TrainingSample();

				ts.restore(br);
				oFeatureVectors.add(ts);
			}

			br.close();

			Debug.debug("Training set loaded successfully: " + oFeatureVectors.size() + " feature vectors.");
		}
		catch (FileNotFoundException e)
		{
			Debug.debug("FileNotFoundException in FeatureSet.restore()!");
		}
		catch (NumberFormatException e)
		{
			Debug.debug("Number format exception in FeatureSet.restore()!");
		}
*/
    }


    /**
     * Dump the current training set to disk.
     * TODO: implement.
     *
     * @throws StorageException
     * @throws NotImplementedException
     * @since 0.3.0.5
     */
    public void dump()
            throws StorageException {
        throw new NotImplementedException();
/*	
		BufferedWriter bw = new BufferedWriter(new FileWriter(this.strFeatureSetFile));

		bw.write(Integer.toString(oFeatureVectors.size()));
		bw.newLine();

		Debug.debug
		(
			"Wrote " + Integer.toString(oFeatureVectors.size()) +
			" to file " + this.strFeatureSetFile
		);

		for(int i = 0; i < oFeatureVectors.size(); i++)
			((TrainingSample)oFeatureVectors.get(i)).dump(bw);

		bw.close();
*/
    }

    /**
     * Implementes Cloneable interface for the FeatureSet object.
     * Performs a "deep" copy of this object including all the feature vectors.
     *
     * @see java.lang.Object#clone()
     * @since 0.3.0.5
     */
    public Object clone() {
        try {
            FeatureSet oClone = (FeatureSet) super.clone();

            oClone.oFeatureVectors =
                    this.oFeatureVectors == null ?
                            null : (Vector) this.oFeatureVectors.clone();

            return oClone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.getMessage());
        }
    }

    /**
     * Returns source code revision information.
     *
     * @return revision string
     * @since 0.3.0.2
     */
    public static String getMARFSourceCodeRevision() {
        return "$Revision: 1.14 $";
    }
}

// EOF
