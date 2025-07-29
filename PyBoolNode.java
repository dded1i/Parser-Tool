package main.rice.node;
import main.rice.obj.*;
import java.util.*;
// TODO: implement the PyBoolNode class here

/**
 *  The class generates  simple Python Boolean objects.
 */
public class PyBoolNode extends APyNode<PyBoolObj> {
    //zero args constructor

    /**
     * The method is a default zero-args constructor for python Boolean object.
     */
    public PyBoolNode() {
    }
    /**
     * The method generates and returns all valid Python objects of type PyBoolObj within the exhaustive domain.
     * @return  all valid PyBoolObj objects
     */

    @Override
    public Set<PyBoolObj> genExVals() {
        // exhaustive domain will be non-null.
        //0 represents False; 1 represents True.
        Set<PyBoolObj> booleanObjs = new HashSet<>();
        for (Number num : getExDomain()) {
            boolean pyValue = (num.intValue() == 1);
            booleanObjs.add(new PyBoolObj(pyValue));
        }
        return booleanObjs;
    }

    /**
     * The method generates and returns one valid Python object of type PyBoolObj,
     * selected from the random domain.
     * @return a randomly selected PyBoolObj object
     */
    @Override
    public PyBoolObj genRandVal() {
        //randomly generate an index within range of random domain
        int randIdx = new Random().nextInt(getRanDomain().size());
        int value = getRanDomain().get(randIdx).intValue();
        return new PyBoolObj(value == 1);
    }
}