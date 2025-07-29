package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyDictObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyDictObjTest {
    // TODO: you are encouraged to add additional tests for edge cases here

    /**
     * A sample Map.
     */
    private static Map<PyIntObj, PyIntObj> val;

    /**
     * Two identical PyDictObjs.
     */
    private static PyDictObj<PyIntObj, PyIntObj> dict1;
    private static PyDictObj<PyIntObj, PyIntObj> dict2;

    /**
     * A different PyDictObj.
     */
    private static PyDictObj<PyIntObj, PyIntObj> dict3;

    /**
     * Sets up all static fields for use in test cases.
     */
    @BeforeAll
    static void setUp() {
        // Create two dicts with identical contents
        val = Map.of(new PyIntObj(1), new PyIntObj(1), new PyIntObj(2),
                new PyIntObj(4), new PyIntObj(3), new PyIntObj(9));
        dict1 = new PyDictObj<>(val);

        val = new HashMap<PyIntObj, PyIntObj>();
        val.put(new PyIntObj(1), new PyIntObj(1));
        val.put(new PyIntObj(2), new PyIntObj(4));
        val.put(new PyIntObj(3), new PyIntObj(9));
        dict2 = new PyDictObj<>(val);

        // Create another dict with different contents
       val = Map.of(new PyIntObj(5), new PyIntObj(6));
       dict3 = new PyDictObj<>(val);
    }

    /**
     * Tests getValue().
     */
    @Test
    @Order(1)
    void testGetValueNested() {
        assertEquals(val, dict3.getValue());
    }

    /**
     * Tests toString().
     */
    @Test
    @Order(2)
    void testToStringMultiple() {
        assertEquals("{5: 6}", dict3.toString());
    }

    /**
     * Tests equals() on two identical PyDictObjs.
     */
    @Test
    @Order(3)
    void testEquals() {
        assertEquals(dict1, dict2);
    }

    /**
     * Tests equals() on two non-equivalent PyDictObjs.
     */
    @Test
    @Order(4)
    void testNotEqual() {
        assertNotEquals(dict1, dict3);
    }

    /**
     * Tests that hashCode() returns the same value for two identical PyDictObjs.
     */
    @Test
    @Order(5)
    void testHashCodeEqual() {
        assertEquals(dict1.hashCode(), dict2.hashCode());
    }

    /**
     * Tests that hashCode() returns different values for two non-equivalent PyDictObjs.
     */
    @Test
    @Order(6)
    void testHashCodeNotEqual() {
        assertNotEquals(dict1.hashCode(), dict3.hashCode());
    }
}