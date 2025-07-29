package test.rice.obj;

import main.rice.obj.PyBoolObj;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test cases for the PyBoolObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyBoolObjTest {

    /**
     * Two distinct PyBoolObjs representing the Python value True.
     */
    private static final PyBoolObj true1 = new PyBoolObj(true);
    private static final PyBoolObj true2 = new PyBoolObj(true);

    /**
     * Two distinct PyBoolObjs representing the Python value False.
     */
    private static final PyBoolObj false1 = new PyBoolObj(false);
    private static final PyBoolObj false2 = new PyBoolObj(false);

    /**
     * Tests that getValue() returns the expected result for a PyBoolObj representing
     * True.
     */
    @Test
    @Order(1)
    void testGetValueTrue() {
        assertEquals(true, true1.getValue());
    }

    /**
     * Tests that getValue() returns the expected result for a PyBoolObj representing
     * False.
     */
    @Test
    @Order(2)
    void testGetValueFalse() {
        assertEquals(false, false1.getValue());
    }

    /**
     * Tests that toString() returns the expected result for a PyBoolObj representing
     * True.
     */
    @Test
    @Order(3)
    void testToStringTrue() {
        assertEquals("True", true1.toString());
    }

    /**
     * Tests that toString() returns the expected result for a PyBoolObj representing
     * False.
     */
    @Test
    @Order(4)
    void testToStringFalse() {
        assertEquals("False", false1.toString());
    }

    /**
     * Tests that two distinct PyBoolObjs with the same value (true) are considered
     * equivalent by equals().
     */
    @Test
    @Order(5)
    void testEqualsTrue() {
        assertEquals(true1, true2);
    }

    /**
     * Tests that two distinct PyBoolObjs with the same value (false) are considered
     * equivalent by equals().
     */
    @Test
    @Order(6)
    void testEqualsFalse() {
        assertEquals(false1, false2);
    }

    /**
     * Tests that a PyBoolObj is not considered to be equivalent to a non-APyObj.
     */
    @Test
    @Order(7)
    void testNotEqualNonAPyObj() {
        assertNotEquals(true1, true);
    }

    /**
     * Tests that two PyBoolObjs with different values are not considered equivalent by
     * equals().
     */
    @Test
    @Order(8)
    void testNotEqual() {
        assertNotEquals(true1, false1);
    }

    /**
     * Tests that two distinct PyBoolObjs with the same value (true) return the same value
     * for hashCode().
     */
    @Test
    @Order(9)
    void testHashCodeTrue() {
        assertEquals(true1.hashCode(), true2.hashCode());
    }

    /**
     * Tests that two distinct PyBoolObjs with the same value (false) return the same
     * value for hashCode().
     */
    @Test
    @Order(10)
    void testHashCodeFalse() {
        assertEquals(false1.hashCode(), false2.hashCode());
    }

    /**
     * Tests that two distinct PyBoolObjs with different values return different values
     * for hashCode().
     */
    @Test
    @Order(11)
    void testHashCodeNotEqual() {
        assertNotEquals(true1.hashCode(), false1.hashCode());
    }
}