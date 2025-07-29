package main.rice.node;
import main.rice.obj.PyFloatObj;
import java.util.*;
// TODO: implement the PyFloatNode class here

/**
 *
 *  The class generates  simple Python Float objects.
 */
public class PyFloatNode extends APyNode<PyFloatObj> {
    /**
     * The method is a zero args constructor for Python Float node objects
     */
    public PyFloatNode() {}

    /**
     * The method generates and returns one valid Python object of type PyFloatObj,
     * selected from the random domain.
     *
     * @return a randomly selected PyFloatObj object
     */
    @Override
    public PyFloatObj genRandVal() {
        //randomly generate an index within range of random domain
        int randIdx = new Random().nextInt(getRanDomain().size());
        double value = getRanDomain().get(randIdx).doubleValue();
        return new PyFloatObj(value);
    }

    /**
     * The method generates and returns all valid Python objects of type PyFloatObj within the exhaustive domain.
     *
     * @return all valid PyFloatObj objects
     */
    @Override
    public Set<PyFloatObj> genExVals() {
        //Each Java Float in the domain should be converted to a PyFloatObj
        // with the same value.
        Set<PyFloatObj> floatObjs = new HashSet<>();
        for (Number num : getExDomain()) {
            double pyFloat= num.doubleValue();
            floatObjs.add(new PyFloatObj(pyFloat));
        }
        return floatObjs;
    }

}
