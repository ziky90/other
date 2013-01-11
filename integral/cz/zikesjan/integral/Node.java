package cz.zikesjan.integral;

import java.util.Set;

/**
 *
 * @author Jan Zikes
 */

/*
 * Class for store data about expression tree's Nodes
 */
public class Node implements AbstractNode {

    char name;
    AbstractNode left;
    AbstractNode right;

    public Node(char name) {
        this.name = name;
    }

    /*
     * returns and calculates value of the Node
     *
     * @param variable
     *                integration variable
     *
     * @param lower
     *             lower border value of current delta (it can be also any other
     *             poit in this delta interval)
     *
     * @param l
     *         set of already defined constants
     */
    public double evaluate(char variable, double lower, Set<Constant> l) {
        if (this.name == '+') {
            return left.evaluate(variable, lower, l) + right.evaluate(variable, lower, l);
        } else if (this.name == '-') {
            return left.evaluate(variable, lower, l) - right.evaluate(variable, lower, l);
        } else if (this.name == '*') {
            return left.evaluate(variable, lower, l) * right.evaluate(variable, lower, l);
        } else if (this.name == '/') {
            return left.evaluate(variable, lower, l) / right.evaluate(variable, lower, l);
        } else {
            return java.lang.Math.pow(this.left.evaluate(variable, lower, l),this.right.evaluate(variable, lower, l));
        }
    }

     /*
     * returns name of the leaf
     */
    public String getName() {
        return Character.toString(name);
    }

    
}
