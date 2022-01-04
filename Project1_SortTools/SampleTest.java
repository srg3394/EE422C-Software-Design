// SampleTest.java
/*
 * EE422C Project 1 submission by
 * Sathvik Gujja
 * srg3394
 * <76000>
 * Summer 2021
 * Slip days used: 0
 */

package assignment1;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertArrayEquals;

public class SampleTest {
	@Rule
	public Timeout globalTimeout = Timeout.seconds(10000);
	private SortTools s = new SortTools();

    @Test
    public void sampleTest() {
        int[] x = new int[]{-5, -4, -3, -2, -1, 0};
        assertEquals(2, SortTools.find(x, x.length, -3));
        
    } 

    @Test(timeout = 2000)
    public void testFind() {
        int[] test = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

        assertEquals(0, SortTools.find(test, test.length, 1));
        assertEquals(3, SortTools.find(test, test.length, 4));
        assertEquals(6, SortTools.find(test, test.length, 7));
        assertEquals(10, SortTools.find(test, test.length, 11));
    }
    
    @Test
    public void testInsertGeneral() {
        int[] expected = {0, 1, 2, 3, 4, 5, 6};
        int[] actual = {1, 2, 3, 4, 5, 6};

        actual = s.copyAndInsert(actual, actual.length, 0);
    

        assertEquals(expected.length, actual.length);
        assertArrayEquals(expected, actual);

        int[] expected2 = {1, 2, 3, 4, 5, 6, 7};
        int[] actual2 = {1, 2, 3, 4, 5, 6};

        actual2 = s.copyAndInsert(actual2, actual2.length, 7);

        assertEquals(expected2.length, actual2.length);
        assertArrayEquals(expected2, actual2);
    }
    
    @Test(timeout = 2000)
    public void testInsertSort() {
        int[][] expected = {
                {2, 4, 5, 7, 6, 1, 9, 0, 1, 2, 5},
                {2, 4, 5, 7, 6, 1, 9, 0, 1, 2, 5},
                {1, 2, 4, 5, 6, 7, 9, 0, 1, 2, 5},
                {0, 1, 1, 2, 4, 5, 6, 7, 9, 2, 5},
                {0, 1, 1, 2, 2, 4, 5, 5, 6, 7, 9}
        };
        int[] actual = {4, 2, 5, 7, 6, 1, 9, 0, 1, 2, 5};

        SortTools.insertSort(actual, 2);
     //   System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected[0], actual);
        SortTools.insertSort(actual, 4);
     //   System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected[1], actual);
        SortTools.insertSort(actual, 6);
      //  System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected[2], actual);
        SortTools.insertSort(actual, 9);
      //  System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected[3], actual);
        SortTools.insertSort(actual, 11);
      //  System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected[4], actual);
    }
    
    @Test(timeout = 2000)
    public void testInsertInPlace() {
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] actual = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = 1; i < expected.length - 1; i++) {
            assertEquals(i + 1, SortTools.insertInPlace(actual, i, i));
            assertArrayEquals(expected, actual);
        }

        for (int i = 1; i < expected.length - 1; i++) {
            assertEquals(i, SortTools.insertInPlace(actual, i, i - 1));
            assertArrayEquals(expected, actual);
        }

        int[] expected2 = {-1, 0, 1, 2, 4, 5, 6, 7, 8, 9};
        assertEquals(4, SortTools.insertInPlace(actual, 3, -1));
       // System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected2, actual);

        int[] expected3 = {-1, 0, 1, 12, 4, 5, 6, 7, 8, 9};
        assertEquals(4, SortTools.insertInPlace(actual, 3, 12));
     //   System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected3, actual);

        assertEquals(4, SortTools.insertInPlace(actual, 3, 2));
     //   System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected2, actual);

        int[] expected4 = {-1, 0, 1, 2, 4, 5, 6, 7, 8, 14};
        assertEquals(10, SortTools.insertInPlace(actual, 9, 14));
     //   System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected4, actual);
    }
    
}
