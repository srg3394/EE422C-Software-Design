/*
 * This file is how you might test out your code.  Don't submit this, and don't
 * have a main method in SortTools.java.
 */

package assignment1;
public class Main {
    public static void main(String [] args) {
        // call your test methods here
        // SortTools.isSorted() etc.
    	int[] x = new int[]{2, -4, -3, -2, -1, 0};
    	int[]y = SortTools.copyAndInsert(x, 4, 2);
    	//SortTools.isSorted(x, 3);
    	System.out.println(SortTools.isSorted(x, 3));
    	System.out.println(SortTools.find(x, 4, -4));
    	
    	for(int i = 0;i<y.length;i++) {
    		System.out.println(y[i]);
    	}
    	
    	System.out.print("hello world");
    }
}
