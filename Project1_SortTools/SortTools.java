// SortTools.java
/*
 * EE422C Project 1 submission by
 * Sathvik Gujja
 * srg3394
 * <76000>
 * Summer 2021
 * Slip days used: 0
 */

package assignment1;
public class SortTools {
    
	
	/**
      * Return whether the first n elements of x are sorted in non-decreasing
      * order.
      * @param x is the array
      * @param n is the size of the input to be checked
      * @return true if array is sorted
      */
    public static boolean isSorted(int[] x, int n) {
        if(x.length==0) {
        	return false;
        }
        if((n <= 0) || (n > x.length)) {
        	return false;
        }
        if(x.length == 1) {
        	return true;
        }
        
        for(int i = 0; i< n-1; i++) {
        	if(x[i] > x[i+1]) {
        		return false;
        	}
        }
        
        return true;
    }

    /**
     * Return an index of value v within the first n elements of x.
     * @param x is the array
     * @param n is the size of the input to be checked
     * @param v is the value to be searched for
     * @return any index k such that k < n and x[k] == v, or -1 if no such k exists
     */
    public static int find(int[] x, int n, int v) {
        
    	int start = 0; 
		int finish = n - 1;
		int middle;
		
		while (start <= finish) {
			 middle = (start + finish) / 2; 
			
			if (x[middle] == v) {
				return middle;
			}
			else if(x[middle] < v) {
				start = middle + 1;
			}
			else {
				finish = middle - 1;
			}
		}
		
        return -1;
    }

    /**
     * Return a sorted, newly created array containing the first n elements of x
     * and ensuring that v is in the new array.
     * @param x is the array
     * @param n is the number of elements to be copied from x
     * @param v is the value to be added to the new array if necessary
     * @return a new array containing the first n elements of x as well as v
     */
    public static int[] copyAndInsert(int[] x, int n, int v) {
        // stub only, you write this!
        // TODO: complete it
    	int[] copyArray;
    	if(find(x,n,v) != -1) {
    		copyArray = new int[n];
    		
    		for(int i = 0; i<n; i++) {
    			copyArray[i] = x[i];
    		}
    		
    		return copyArray;
    	}
    	else {
    		copyArray = new int[n+1];   		
    	
    	
    		int j = 0;
    		while(j < n && x[j] < v) {
    			copyArray[j] = x[j];
    			j++;
    		}
    		copyArray[j] = v;
    		j++;
    		while(j < copyArray.length) {
    			copyArray[j] = x[j-1];
    			j++;
    		}
    		return copyArray;
    	}
    	
    }

    /**
     * Insert the value v in the first n elements of x if it is not already
     * there, ensuring those elements are still sorted.
     * @param x is the array
     * @param n is the number of elements in the array
     * @param v is the value to be added
     * @return n if v is already in x, otherwise returns n+1
     */
    public static int insertInPlace(int[] x, int n, int v) {
        // stub only, you write this!
        // TODO: complete it
    	
    	if(find(x,n,v) == -1) {
    		int start = 0;
    		while(v > x[start] && n>start) {
    			start++;
    		}
    		int found = start;
    		for(int j = n; j > start; j--) {
    			x[j] = x[j - 1];
    		}
    		x[found] = v;    		
    		return n + 1;
    	}
    	else {
    		return n;
    	}
    }

    /*
     * Sort the first n elements of x in-place in non-decreasing order using
     * insertion sort.
     * @param x is the array to be sorted
     * @param n is the number of elements of the array to be sorted
     */
    public static void insertSort(int[] x, int n) {
        // stub only, you write this!
        // TODO: complete it
    	int k;
    	int y;
    	for(int i = 1; i< n;i++) {
    		y = i-1;
    		k = x[i];
    		
    		while(x[y] > k && y>=0) {
    			x[y+1] = x[y];
    			y--;
    			if(y < 0) {
    				break;
    			}
    		}
    		
    		x[y+1] = k;
    	}
    	
    }
}
