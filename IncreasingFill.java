
public class IncreasingFill {

	// test client
    public static void main(String[] args) 
    {
    	double sTime=0, eTime=0, overallTime=0;
    	
    	System.out.println("%fill    128    256    512    1024    2048");
    	
    	double[] fillStats={1.0, 5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0};
    	int[] sizeStats={128, 256, 512, 1024, 2048};
    	
    	for(double j : fillStats)
    	{
    		System.out.print(j);
    		
    		for(int k : sizeStats)
    		{
    			for(int i=0; i<100; i++)
            	{
            		//create random sparse matrices
            		SparseMatrix A = new SparseMatrix(k, j);
            		SparseMatrix B = new SparseMatrix(k, j);
            		
            		//perform timing trial, += to overall time
            		sTime = System.currentTimeMillis();
            		SparseMatrix C = A.multiply(B);
            		eTime = System.currentTimeMillis();
            		
            		overallTime+=(eTime-sTime);
            	}
            	
            	System.out.print("    " + (overallTime/100));
            	
            	
            	overallTime=0;
    		}
    	
    		System.out.println();
    	}
    	
    }
}
