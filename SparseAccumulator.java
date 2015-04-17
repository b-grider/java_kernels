/*
 * A class designed to keep track of accumulated values during column-wise sparse matrix multiplication
 * Keeps a boolean isNonzero array and double values array to collect values, then put in a compressed sparse column matrix after collection
 * 
 * Author: Ben
 * Date: 3/2/2015
 */

import java.util.Arrays;
import java.util.LinkedList;

public class SparseAccumulator {
	
	int N;									//column size
	double[] values;						//actual values
	boolean[] isNonzero;					//tracks which indices are in use
	LinkedList<Integer> nonZeroIndices;		//list of all nonZero indices

	public SparseAccumulator(int N) 
	{
		this.N=N;
		values = new double[N];				
		isNonzero=new boolean[N];			//defaults to false
		nonZeroIndices=new LinkedList<Integer>();
	}
	
	public void insert(double value, int index)
	{
		//put the value in the values array
		values[index]+=value;
		
		//mark it as nonZero
		isNonzero[index]=true;
		
		//Add the index to the nonZeroIndices list
		if(!nonZeroIndices.contains(index))
		{
			nonZeroIndices.add(index);
		}
	}
	
	
	
	public int[] getIndices()				//no need to sort, the values will always be inserted in row index order due to columnwise multiplication
	{
		int[] resultIndices=new int[nonZeroIndices.size()];
		for(int i=0; i<nonZeroIndices.size(); i++)
		{
			resultIndices[i]=nonZeroIndices.get(i);
		}
		
		return resultIndices;
	}
	
	
	
	public double[] getValues()
	{
		
		double[] returnValues = new double[nonZeroIndices.size()];
		
		for(int i=0; i<nonZeroIndices.size(); i++)
		{
			returnValues[i]=values[nonZeroIndices.get(i)];
		}
		
		return returnValues;
	}
	
}
