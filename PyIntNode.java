package main.rice.node;
import main.rice.obj.PyIntObj;
import java.util.*;

/**
 * The class generates  simple Python Integer objects.
 */
// TODO: implement the PyIntNode class here
public class PyIntNode extends APyNode<PyIntObj>{
    /**
     * The method is a zero args constructor for Python Integer node objects
     */
    public PyIntNode(){}
    /**
     * The method generates and returns one valid Python object of type PyIntObj,
     * selected from the random domain.
     *
     * @return a randomly selected PyIntObj object
     */
    @Override
    public PyIntObj genRandVal() {
        //randomly generate an index within range of random domain
        int randIdx = new Random().nextInt(getRanDomain().size());
        int value = getRanDomain().get(randIdx).intValue();
        return new PyIntObj(value);
    }
    /**
     * The method generates and returns all valid Python objects of type PyIntObj within the exhaustive domain.
     *
     * @return all valid PyIntObj objects
     */
    @Override
    public Set<PyIntObj> genExVals() {
        //Each Java Integer in the domain should be converted to a PyIntObj
        // with the same value.
        Set<PyIntObj> intObjs = new HashSet<>();
        for (Number num : getExDomain()) {
            int pyInt= num.intValue();
            intObjs.add(new PyIntObj(pyInt));
        }
        return intObjs;
    }
}
