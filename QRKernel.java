import java.util.Random;

import org.ejml.alg.dense.decomposition.qr.QRDecompositionHouseholderColumn_D64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.QRDecomposition;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;


public class QRKernel {
	
	
	public static double[][] getRandomMatrix(int dimension, double percentFill)
	{
		//create and initialize 2d double array
		double[][] matrix = new double[dimension][];
		
		for(int i=0; i<dimension; i++)
		{
			matrix[i]=new double[dimension];
		}
		
		
		//randomly generate the appropriate number of random array contents
		int numberOfRandoms = (int)(dimension*dimension*(percentFill/100));
		Random ng = new Random();
		for(int i=0; i<numberOfRandoms; i++)
		{
			int x = ng.nextInt(dimension);
			int y = ng.nextInt(dimension);
			matrix[x][y]=42*ng.nextDouble();
		}
		
		return matrix;
	}
	
	

	public static void main(String[] args)
	{
	
		
		double sTime=0, eTime=0, overallTime=0;
    	
    	System.out.println("size    1.0%    25.0%    50.0%    75.0%    100.0%");
    	
    	double[] fillStats={1.0, 25.0, 50.0, 75.0, 100.0};
    	int[] sizeStats={1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384};
    	
    	for(int k : sizeStats)
    	{
    		System.out.print(k);
    		
    		for(double j : fillStats)
    		{
    			for(int i=0; i<100; i++)
            	{
            		//create random sparse matrices
    				DenseMatrix64F a = new DenseMatrix64F(getRandomMatrix(k,j));
            		
            		//perform timing trial, += to overall time
            		sTime = System.currentTimeMillis();
            		QRDecomposition<DenseMatrix64F> decomp = DecompositionFactory.qr(k, k);
            		decomp.decompose(a);
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
