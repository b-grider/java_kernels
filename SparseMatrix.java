/*************************************************************************
 * 
 *  
  *  A sparse, square matrix, implemented using compressed sparse column format
 *	Author: Ben
 *	Date: 3/3/2015
 *
inspired by: http://introcs.cs.princeton.edu/java/44st/SparseMatrix.java.html

 *************************************************************************/
import java.util.Date;
import java.util.Random;
import java.util.Arrays;


public class SparseMatrix 
{
    
    private final int N;                // N-by-N matrix, final size once initialized
    private double[][] matrix;          // 2D double array to store real numbers, mutable
    private double percentFill;
    private double[] values;			//stores the actual double nonzero values
    private int[] indices;				//stores the row indices of each nonzero item
    private int[] indptr;				//stores the indices of the ith column starting nonzero value (values correspond to indices in indices, values)
    int nonZero;
    private int maxColumn;				//the index in indptr of the last nonzero column in the sparse matrix.
    int indptr_empty;
    int indices_empty;					//these are for tracking the empty indices in the backing array when we first use the empty constructor
    int values_empty;
    


    public SparseMatrix(int N, double percentFill) 
    {
        this.maxColumn = -1;
        this.N  = N;
        this.percentFill=percentFill;                           				//initialize fields
        this.nonZero = (int)(N*N*(percentFill/100));
        this.values = new double[nonZero];
        this.indices = new int[nonZero];
        this.indptr = new int[N+1];
        
        Arrays.fill(indptr, nonZero);													//default initialize to nonZero, meaning every column represented by indptr[] is void of nonzero elements (indices that don't exist in indices[] or values[])
        
        
        int delta;
        if(nonZero != 0)
        {
        	delta = (N*N)/nonZero;
        }
        else {
        	delta = (N*N);
        }
        
       
        Random ng = new Random(new Date().getTime());
        int randomNumber;

       for(int i=0; i<nonZero; i++) 
       {
            randomNumber = ((int)(ng.nextDouble()*delta) + i*delta); 		/*(i*delta + (int)ng.nextDouble()*delta);*/
            indices[i] = randomNumber%N;                        				//mod by n to get a random row number
            values[i] = 42*ng.nextDouble();
            
            if((randomNumber/N) > maxColumn) 						//if this is the greatest column number yet, it will be the start of a new column
            {                        
                maxColumn = randomNumber/N;
                indptr[randomNumber/N] = i;                         //add the index (i) to the list, as it will be the index of a new column start.
            }
            
            
        }
       
        

    }
    
    
    //empty init constructor for construction by SparseAccumulator during multiplication
    public SparseMatrix(int N, int nonZero)
    {
        this.N  = N;                          			
        
        this.nonZero = nonZero;
        values = new double[nonZero];
        indices = new int[nonZero];
        indptr = new int[N+1];
        Arrays.fill(indptr, nonZero);										//default initialize to N, meaning every column is void of nonzero elements
        values_empty=0;
        indices_empty=0;
        indptr_empty=0;
    }

    
    
    public CompressedSparseVector getColumn(int columnIndex)		//this will only be called on columns containing nonZero elements
    {
    																//first get a count of how large the vector arrays should be
    	int start=indptr[columnIndex];								//i gets the index that is the start row of the column
    	int end;
    	int size;
    	
    	if(columnIndex == maxColumn)
    	{
    		end = indices.length;
    	}
    	else
    	{	
    		columnIndex++;
    		while(indptr[columnIndex] == nonZero)
    		{
    			columnIndex++;
    		}
    		
    		end=indptr[columnIndex];
    	}
    	
    	size=end-start;
    	CompressedSparseVector vector = new CompressedSparseVector(size);
    	
    	while(start != end) 
    	{
    		vector.addPair(this.indices[start], this.values[start]);
    		start++;
    	}
    	
    	return vector;
    }
    
    
    
    
    
    public SparseMatrix multiply(SparseMatrix B)
    {
    	SparseMatrix result;
    	//SparseAccumulator accumulator=new SparseAccumulator(B.getSize());
    	CompressedSparseVector vector;
    	int bSize=B.getSize();
    	int counter=0;
    	
    	//Simulate multiplication to find the right space allocation for result matrix
    
    	//check for overlapping non-zero entries, increment counter:::
    	for(int i=0; i<bSize; i++) 
		{
    		//get a CompressedSparseVector from the matrix B
    		if(B.columnIsNonZero(i)) 
    		{
    			vector = B.getColumn(i);
    			
    			for(int j=0; j<vector.indices.length;j++)
    			{
    																//now for the non-zero entries of B's rows of column i, find the overlapping non zero entries of A's columns
    				if(indptr[vector.indices[j]] != nonZero)				//if there are nonzero entries in the column of interest of A.. 
    				{
    																//count the number of non zero entries, increment counter for each one
    					int start=indptr[vector.indices[j]];	    //start is a column number, find the start index of the row entries by using indptr[vector.indices[j]]
    					int end;
    					if(vector.indices[j] == maxColumn)			//if this aligns with the final non-zero column
    					{
    						end=indices.length;						//make the end the final index of the indices array
    					}
    					else
    					{
    						int t=1;
    						//end=indptr[vector.indices[j]+t];
    						
	    						while(indptr[vector.indices[j]+t] == nonZero)
	    						{
	    							t++;								//if it's not the final column, go to the next nonzero column.
	    						}
	    						
    						end=indptr[vector.indices[j]+t];			//make end the starting row index of the next nonZero column.
    					}
    					
    					counter+=(end-start);							//subtract start from it to get the number
    				
    				}
    			}
    		}
			
		}
    	
    	
    	//declare and initialize our result matrix now that we know how many nonzero elements
    	result=new SparseMatrix(N, counter);
    	
    	
    	//Perform actual multiplication using SparseAccumulator
    	for(int i=0; i<bSize; i++) 
		{
    		//get a CompressedSparseVector from the matrix B if column is non-zero
    		if(B.columnIsNonZero(i)) 
    		{
    			vector = B.getColumn(i);
    			
    			for(int j=0; j<vector.indices.length;j++)				//iterate over only the nonzero elements of the vector
    			{
    					
    				SparseAccumulator accumulator=new SparseAccumulator(bSize);  //now for the non zero entries of B's rows of column i, find the overlapping non zero entries of A's columns
    				
    				if(indptr[vector.indices[j]] != nonZero)			//if there are nonzero entries in the column of interest of A.. 
    				{
    																	//multiply the vector element by all nonZero elements in this matrix's column j
    					int start=indptr[vector.indices[j]];			//match the column with the nonzero index of the vector (indptr[vector.indices[j]])
    					int end;										//get the row index of first nonzero element ^^ with indices[]
    					if(vector.indices[j] == maxColumn)
    					{
    						end=indices.length;							//if there are no nonzero columns after this, just go till the end of indices
    					}
    					else
    					{
    																	//else end will be the next start of the most immediate nonzero column
    						int t=1;
    						//end=indptr[vector.indices[j]];
    						
    						while(indptr[vector.indices[j]+t] == nonZero)
    						{
    							t++;
    						}
    						
    						end=indptr[vector.indices[j]+t];
    					}
    					
    					//end=indptr[vector.indices[j]+t];
    					double value;
    					int index;
    					while(start!=end)
    					{
    						value=vector.values[j]*values[start];		//multiply the row index values by the value in the vector
    						index=indices[start];
    						accumulator.insert(value, index);
    						start++;
    					}
    				}
    				
    				result.put(accumulator, i);
    			}
    		}
			
		}
    	
    	
    	
    	return result;
    }
    
   
    
    

    public boolean columnIsNonZero(int i) 
    {
		if(indptr[i] == nonZero) 
		{
			return false;
		}
		return true;
	}


																	// return a string representation
    public String printString() 
    {

        															//put sparse matrix in a 2D array for now, maybe look into Super CSV for printing straight from CSC later

        matrix = new double[N][];

        for(int i=0; i<N; i++) 
        {
        	matrix[i] = new double[N];                				//all zeros to start
        }

        columnLoop:
        for(int i=0; i<N+1; i++) 									//initialize the nonzero elements
        {
        	if(indptr[i] == nonZero) 
        	{
        		continue;
        	}
        	else
        	{
        		int column = i;
        		int j = indptr[i];									//this is the row index/value (in arrays: indices/values) of the first row index in column i
        		int row = indices[j];
        		int c=j+1;
        		boolean remainingZero = false;
        		
        															//now find k, the first item in indptr !=N after indptr[i]
        			int t=1;
        			while (indptr[i+t]==nonZero) 							//iterate from the current i to the next i that is not == N, (the index of the first column with a nonzero item)
        			{
        				if((i+t) == (indptr.length-1)) 				//if we've checked everything till the end of indptr and they all ==N, the remaining items are all 0.0 in matrix
        				{
        					remainingZero = true;
        					break;
        				}
        				t++;
        			}
        			
        			
        		if(remainingZero) 									//if the rest of the columns are all zero, put the remaining indices[] and values[] in place, then break out of the column loop, matrix is ready to print
        		{
        			while(j<indices.length)							//put the rest of the elements of this column into the matrix.
            		{
            			matrix[indices[j]][i] = values[j];
            			j++;
            		}
        			break columnLoop;
        		}
        		
        		else 
        		{
        			int k = indptr[i+t];	
        			
        			while((j != k) && (j<indices.length))			//put the rest of the elements of this column into the matrix.
            		{
            			matrix[indices[j]][i] = values[j];
            			j++;
            		}
        		}
        		
        	}
        }

        
        StringBuilder builder = new StringBuilder("N = " + N + ", nonzeros = " + nonZero + "\n");
        builder.append("indices: " + indices.length + " values: " + values.length + " indptr: " + indptr.length + "\n");

            for (int i = 0; i < N; i++) 
            {
                for(int j=0; j<N; j++) 
                {
                    builder.append(matrix[i][j] + " ");
                }
                builder.append("\n");
            }

        return builder.toString();
    }



    public void put(SparseAccumulator accumulator, int columnIndex) 
    {
    	//This will put the results held by the SPA into the compressed column of this SparseMatrix
    	
    	indptr[columnIndex]=indices_empty;								//the column index we are inserting on gets the most immediate empty index in indices[]/values[]
    	
    	for(int i : accumulator.getIndices())
    	{
    		indices[indices_empty]=i;									//copy over the indices, tracking the free index
    		indices_empty++;
    	}
    	
    	for(double d : accumulator.getValues())
    	{
    		values[values_empty]=d;										//copy over the values, tracking the free index
    		values_empty++;
    	}
    	
    }



    public double get(int i, int j) 
    {
        if (i < 0 || i >= N) throw new RuntimeException("Illegal index");
        if (j < 0 || j >= N) throw new RuntimeException("Illegal index");
        return matrix[i][j];
    }

    
    public int getSize() 
    {
    	return N;
    }

}