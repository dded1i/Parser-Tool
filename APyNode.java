package main.rice.node;
import main.rice.obj.*;
import java.util.*;

/**
 * the class is used for abstract representation of PyNode and serves as a generator of Python objects. It takes a
 * single type parameter, ObjType, representing the type of object that it generates.
 * @param <ObjType> the type of object that the class generates bounded by APyObj
 */
public abstract class APyNode<ObjType extends APyObj<?>> {
   //We will store the domain information  as fields
    // Your code for the abstract generator of Python objects
    // The ObjType parameter represents the type of APyObj it generates
    // You can have abstract methods or other logic here

    /**
     * The method gets left child of the object.
     * @return the left child, null if no left child exists.
     */
   public APyNode<ObjType> getLeftChild() {
       return null; // Implement in concrete iterable classes
   }
    /**
     * The method gets right child of the object.
     * @return the right child, null if no left child exists.
     */
    public APyNode<ObjType> getRightChild() {
        return null;
    }

    /**
     * This is a declaration of the field of exhaustive domain
     */
    private List<? extends Number> ranDomain;
    /**
     * This is a declaration of the field of random domain
     */
    public List<? extends Number> exDomain;

    /**
     * The method sets the random domain to the input list of numbers.
     * @param ranDomain random domain object
     */
    public void setRanDomain(List<? extends Number> ranDomain){ this.ranDomain = ranDomain;}
    /**
     * The method sets the random domain to the input list of numbers.
     * @param exDomain random domain object
     */
    public void setExDomain(List<? extends Number> exDomain){this.exDomain = exDomain;}

    /**
     * The method returns the random domain; returns null if it has not been set yet. do not need to account for null explicitly
     * because it's a default value
     * @return the random domain
     */
    public List<? extends Number> getRanDomain(){return ranDomain;}

    /**
     *The method returns the exhaustive domain; returns null if it has not been set yet.
     * @return the exhaustive domain
     */

    public List<? extends Number> getExDomain(){return exDomain;}
    /**
     * the method generates and returns one valid Python object of type ObjType, selected from the random domain
     * @return  one valid Python object of type ObjType
     */
    public abstract ObjType genRandVal();
    /**
     * the method generates and returns all valid Python objects of type ObjType within the exhaustive domain.
     * @return  one valid Python object of type ObjType
     */
    public abstract Set<ObjType> genExVals();

}