package uk.ac.gla.dcs.dsms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.terrier.matching.dsms.DependenceScoreModifier;
import org.terrier.structures.postings.BlockPosting;
import org.terrier.structures.postings.Posting;

/**
 * You should use this sample class to implement a proximity feature in Exercise
 * 2. TODO: Describe the function that your class implements
 * <p>
 * You can add your feature into a learned model by appending
 * DSM:uk.ac.gla.IRcourse.SampleProxFeatureDSM to the features.list file.
 * 
 * @author 2253715v
 */
public class NhutMVoMinDistProxFeatureDSM extends DependenceScoreModifier {

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
		// ***
		// ok to use ips
		List<BlockPosting> okToUseIps = new ArrayList<>();

		List<Integer> aggreList = new ArrayList<>();

		double result = 0;

		// Getting okay to use Postings and cast to Block Posting
		for (int i = 0; i < numberOfQueryTerms; i++) {
			if (okToUse[i])
				okToUseIps.add((BlockPosting) ips[i]);
		}

		for (int i = 0; i < okToUseIps.size(); i++) {
			for (int j = i + 1; j < okToUseIps.size(); j++) {
				aggreList.add(getMinDistInArray(okToUseIps.get(i), okToUseIps.get(j)));
			}
		}

		for (Integer integer : aggreList) {
			result += integer;
		}
		result = (double) result / (double) aggreList.size();

		return result;
	}

	private int getMinDistInArray(BlockPosting a, BlockPosting b) {

		int[] aPos = a.getPositions();
		int[] bPos = b.getPositions();
		if(aPos.length ==0 || bPos.length==0 ) {
			return 0;
		}else {

			int minDist = Integer.MAX_VALUE;

			for (int i = 0; i < aPos.length; i++) {
				for (int j = 0; j < bPos.length; j++) {
					//System.out.println(Math.abs(aPos[i] - bPos[j]));
					if (Math.abs(aPos[i] - bPos[j]) < minDist)
						minDist = Math.abs(aPos[i] - bPos[j]);
				}
			}
//			System.out.println("Min Val:"+minDist);
			
			if (minDist == Integer.MAX_VALUE)
				return 0;
			else
				return minDist;
		}
	}

	/** You do NOT need to implement this method */
	@Override
	protected double scoreFDSD(int matchingNGrams, int docLength) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return "MinDistProxFeatureDSM";
	}

}
