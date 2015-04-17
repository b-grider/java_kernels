/*
 *Simple container class to encapsulate the information in a compressed sparse vector.
 * Author: Ben
 * Date: 3/2/2015
 */

import java.lang.Exception;


public class CompressedSparseVector {

		public int[] indices;		//this will correspond to the row index, since we're storing compressed sparse columns
		public double[] values;		//the values at each row index for this column
		public int nonZero;
		int emptyIndex;
		
		public CompressedSparseVector(int nonZero)
		{
			this.nonZero=nonZero;
			indices=new int[nonZero];
			values=new double[nonZero];
			emptyIndex=0;
		}
		
		public void addPair(int index, double value)
		{
			indices[emptyIndex]=index;
			values[emptyIndex]=value;
			emptyIndex++;	
			
		}
		
}
