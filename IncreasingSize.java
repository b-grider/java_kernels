
public class IncreasingSize {

	// test client
    public static void main(String[] args) 
    {
    	double sTime=0, eTime=0, overallTime=0;
    	
    	System.out.println("size    1.0%    5.0%    10.0%    15.0%    20.0%");
    	
    	double[] fillStats={1.0, 5.0, 10.0, 15.0, 20.0};
    	int[] sizeStats={2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384};
    	
    	for(int k : sizeStats)
    	{
    		System.out.print(k);
    		
    		for(double j : fillStats)
    		{
    			for(int i=0; i<100; i++)
            	{
            		//create random sparse matrices
            		SparseMatrix A = new SparseMatrix(k, j);
            		SparseMatrix B = new SparseMatrix(k, j);
            		
            		//perform timing trial, += to overall time
            		sTime = System.currentTimeMillis();		//currentTimeMillis();
            		SparseMatrix C = A.multiply(B);
            		eTime = System.currentTimeMillis();
            		
            		overallTime+=(eTime-sTime);
            	}
            	
            	System.out.print("    " + (overallTime/100));
            	
            	//reset timer between trials
            	overallTime=0;
    		}
    	
    		System.out.println();
    	}
    	
    }
}
