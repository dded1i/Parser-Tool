package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyTupleObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyTupleObjTest {
    // TODO: you are encouraged to add additional tests for edge cases here

    /**
     * A non-empty List.
     */
    private static List<PyFloatObj> val;

    /**
     * Two identical PyTupleObjs containing floats.
     */
    private static PyTupleObj<PyFloatObj> floatTup;
    private static PyTupleObj<PyFloatObj> floatTup2;

    /**
     * A different PyTupleObj containing floats.
     */
    private static PyTupleObj<PyFloatObj> floatTup3;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        // Create two identical tuples of floats, and a third distinct tuple of floats.
        val = new ArrayList<>();
        val.add(new PyFloatObj(1.5));
        val.add(new PyFloatObj(-17.0));
        val.add(new PyFloatObj(2.0));
        val.add(new PyFloatObj(4.888));
        floatTup = new PyTupleObj<>(val);

        val = List.of(new PyFloatObj(1.5), new PyFloatObj(-17.0),
                new PyFloatObj(2.0), new PyFloatObj(4.888));
        floatTup2 = new PyTupleObj<>(val);

        val = List.of(new PyFloatObj(-5.0), new PyFloatObj(3.125));
        floatTup3 = new PyTupleObj<>(val);
    }

    /**
     * Tests getValue().
     */
    @Test
    @Order(1)
    void testGetValue() {
        assertEquals(val, floatTup3.getValue());
    }

    /**
     * Tests toString().
     */
    @Test
    @Order(2)
    void testToString() {
        assertEquals("(-5.0, 3.125)", floatTup3.toString());
    }

    /**
     * Tests equals() on two identical PyTupleObjs.
     */
    @Test
    @Order(3)
    void testEquals() {
        assertEquals(floatTup, floatTup2);
    }

    /**
     * Tests equals() on two non-equivalent PyTupleObjs.
     */
    @Test
    @Order(4)
    void testNotEqual() {
        assertNotEquals(floatTup, floatTup3);
    }

    /**
     * Tests that hashCode() returns the same value for two identical PyTupleObjs.
     */
    @Test
    @Order(5)
    void testHashCodeEqual() {
        assertEquals(floatTup.hashCode(), floatTup2.hashCode());
    }

    /**
     * Tests that hashCode() returns different values for two non-equivalent PyTupleObjs.
     */
    @Test
    @Order(6)
    void testHashCodeNotEqual() {
        assertNotEquals(floatTup.hashCode(), floatTup3.hashCode());
    }
}