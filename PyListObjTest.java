package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyListObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyListObjTest {
    // TODO: you are encouraged to add additional tests for edge cases here

    /**
     * A non-empty List.
     */
    private static List<PyFloatObj> val;

    /**
     * Two identical PyListObjs containing floats.
     */
    private static PyListObj<PyFloatObj> floatList;
    private static PyListObj<PyFloatObj> floatList2;

    /**
     * A different PyListObj containing floats.
     */
    private static PyListObj<PyFloatObj> floatList3;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        // Create two identical lists of floats, and a third distinct list of floats.
        val = new ArrayList<>();
        val.add(new PyFloatObj(1.5));
        val.add(new PyFloatObj(-17.0));
        val.add(new PyFloatObj(2.0));
        val.add(new PyFloatObj(4.888));
        floatList = new PyListObj<>(val);

        val = List.of(new PyFloatObj(1.5), new PyFloatObj(-17.0),
                new PyFloatObj(2.0), new PyFloatObj(4.888));
        floatList2 = new PyListObj<>(val);

        val = List.of(new PyFloatObj(-5.0), new PyFloatObj(3.125));
        floatList3 = new PyListObj<>(val);
    }

    /**
     * Tests getValue().
     */
    @Test
    @Order(1)
    void testGetValue() {
        assertEquals(val, floatList3.getValue());
    }

    /**
     * Tests toString().
     */
    @Test
    @Order(2)
    void testToString() {
        assertEquals("[-5.0, 3.125]", floatList3.toString());
    }

    /**
     * Tests equals() on two identical PyListObjs.
     */
    @Test
    @Order(3)
    void testEquals() {
        assertEquals(floatList, floatList2);
    }

    /**
     * Tests equals() on two non-equivalent PyListObjs.
     */
    @Test
    @Order(4)
    void testNotEqual() {
        assertNotEquals(floatList, floatList3);
    }

    /**
     * Tests that hashCode() returns the same value for two identical PyListObjs.
     */
    @Test
    @Order(5)
    void testHashCodeEqual() {
        assertEquals(floatList.hashCode(), floatList2.hashCode());
    }

    /**
     * Tests that hashCode() returns different values for two non-equivalent PyListObjs.
     */
    @Test
    @Order(6)
    void testHashCodeNotEqual() {
        assertNotEquals(floatList.hashCode(), floatList3.hashCode());
    }
}