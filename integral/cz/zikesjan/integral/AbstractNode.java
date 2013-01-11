package cz.zikesjan.integral;

import java.util.Set;

/**
 *
 * @author Jan Zikes
 */

/*
 *Interface for Node and Leaf (enables composite design pattern)
 */

public interface AbstractNode {
    /*
     * returns double value of Abstract node
     *
     * @param variable
     *                integrating variable
     * @param delta
     *             integrating delta
     * @param l
     *         set of defined constants
     */
    public double evaluate(char variable, double delta, Set<Constant> l);

    /*
     * returns name of the AbstractNode
     */
    public String getName();
}
