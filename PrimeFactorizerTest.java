package test.rice;

import main.rice.PrimeFactorizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Arrays;

/**
 * A test suite for the PrimeFactorizer class. Every method with the annotation "@Test"
 * will be called when running the test with JUnit.
 */
public class PrimeFactorizerTest {

    /**
     * A prime factorizer with an upper bound of 100.
     */
    private static final PrimeFactorizer factorizer100 = new PrimeFactorizer(100);

    /**
     * Tests that attempting factorization of a negative number rightfully returns null.
     */
    @Test
    void testFactorizeNegative() {
        int[] actual = factorizer100.computePrimeFactorization(-1);
        assertNull(actual);
    }

    /**
     * Tests factorization of a prime that can be factorized.
     */
    @Test
    void testFactorize7() {
        int[] actual = factorizer100.computePrimeFactorization(7);
        int[] expected = new int[]{7};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize1() {
        int[] actual = factorizer100.computePrimeFactorization(1);
        int[] expected = new int[]{1};
        //System.out.println("Factors for input 1: " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize4() {
        //more than 1 factor but the same primes
        int[] actual = factorizer100.computePrimeFactorization(4);
        int[] expected = new int[]{2,2};
        //System.out.println("Factors for input 4: " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize6() {
        int[] actual = factorizer100.computePrimeFactorization(6);
        int[] expected = new int[]{2,3};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize0() {
        int[] actual = factorizer100.computePrimeFactorization(0);
        int[] expected = null;;
        //System.out.println("Factors for input 0: " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize48() {
        int[] actual = factorizer100.computePrimeFactorization(48);
        int[] expected = new int[]{2, 2, 2, 2, 3};
        //System.out.println("Factors for input 48: " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize150() {
        int[] actual = factorizer100.computePrimeFactorization(150);
        int[] expected = null;
        //System.out.println("Factors for input 150: " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize113() {
        int[] actual = factorizer100.computePrimeFactorization(113);
        int[] expected = null;;
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize23269() {
        int[] actual = factorizer100.computePrimeFactorization(23269);
        int[] expected = null;;
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize100000() {
        int[] actual = factorizer100.computePrimeFactorization(100000);
        int[] expected = null;;
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize15() {
        int[] actual = factorizer100.computePrimeFactorization(15);
        int[] expected = new int[]{3,5};
        //System.out.println("Factors for input 15: " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }

    @Test
    void testFactorize85() {
        int[] actual = factorizer100.computePrimeFactorization(85);
        int[] expected = new int[]{5,17};
        // System.out.println("Factors for input 85: " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }
    @Test
    void testFactorize74() {
        int[] actual = factorizer100.computePrimeFactorization(74);
        int[] expected = new int[]{2,37};
        //System.out.println("Factors for input 74: " + Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }
    //
}