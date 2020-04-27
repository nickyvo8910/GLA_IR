package uk.ac.gla.dcs.dsms;

import org.terrier.structures.postings.BlockPosting;
import org.terrier.structures.postings.Posting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.terrier.matching.dsms.DependenceScoreModifier;

/**
 * You should use this sample class to implement a proximity feature in Exercise
 * 2. TODO: Describe the function that your class implements
 * <p>
 * You can add your feature into a learned model by appending
 * DSM:uk.ac.gla.IRcourse.SampleProxFeatureDSM to the features.list file.
 * 
 * @author 2253715v
 */
public class NhutMVoAvgDistProxFeatureDSM extends DependenceScoreModifier {

	/**
	 * This class is passed the postings of the current document, and should return
	 * a score to represent that document.
	 */
	@Override
	protected double calculateDependence(Posting[] ips, // postings for this document (these are actually
														// IterablePosting[])
			boolean[] okToUse, // is each posting on the correct document?
			double[] phraseTermWeights, boolean SD // not needed
	) {

		final int numberOfQueryTerms = okToUse.length;

		// ***
		// TODO: in this part, write your code that inspects
		// the positions of query terms, to make a proximity function
		// NB: you can cast each Posting to org.terrier.structures.postings.BlockPosting
		// and use the getPositions() method to obtain the positions.
		List<BlockPosting> okToUseIps = new ArrayList<>();

		List<Double> aggreList = new ArrayList<>();

		double result = 0;

		// Getting okay to use Postings and cast to Block Posting
		for (int i = 0; i < numberOfQueryTerms; i++) {
			if (okToUse[i])
				okToUseIps.add((BlockPosting) ips[i]);
		}

		for (int i = 0; i < okToUseIps.size(); i++) {
			for (int j = i + 1; j < okToUseIps.size(); j++) {
				aggreList.add(getAvgDist(okToUseIps.get(i), okToUseIps.get(j)));
			}
		}

		for (Double num : aggreList) {
			result += num;
		}
		result = (double) result / (double) aggreList.size();

		return result;
	}

	private double getAvgDist(BlockPosting a, BlockPosting b) {

		int[] aPos = a.getPositions();
		int[] bPos = b.getPositions();
		
		if(aPos.length ==0 || bPos.length==0 ) {
			return 0;
		}else {

		double sum =0;

		for (int i = 0; i < aPos.length; i++) {
			for (int j = 0; j < bPos.length; j++) {
					sum += Math.abs(aPos[i] - bPos[j]);
		}}
		
		sum = (double) sum/ (double)(aPos.length*bPos.length);
		
	
		return sum;}
	}

	/** You do NOT need to implement this method */
	@Override
	protected double scoreFDSD(int matchingNGrams, int docLength) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return "AvgDistProxFeatureDSM";
	}

}
