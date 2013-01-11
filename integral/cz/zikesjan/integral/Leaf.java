package cz.zikesjan.integral;

import java.util.Set;

/**
 *
 * @author Jan Zikes
 */

/*
 * class that store data about leaf (composite design pattern)
 */
public class Leaf implements AbstractNode {

    String name;

    public Leaf(String name) {
        this.name = name;
    }

    /*
     * returns value of leaf
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

        if (name.charAt(0) == variable) {
            return lower;
        } else if (name.charAt(0) == '-' && name.charAt(1) == variable) {
            return -lower;
        } else {
            if (l != null) {
                double val = 0;
                boolean init = false;
                for (Constant c : l) {
                    if (name.charAt(0) == c.constaname) {
                        val = c.value;
                        init = true;
                        break;
                    } else if (name.charAt(0) == '-' && name.charAt(1) == c.constaname) {
                        val = -c.value;
                        init = true;
                        break;
                    }
                }
                if (init == true) {
                    return val;
                } else {
                    try {
                        return Double.valueOf(name);
                    } catch (NumberFormatException n) {
                        throw new IllegalArgumentException();
                    }
                }
            } else {
                try {
                    return Double.valueOf(name);
                } catch (NumberFormatException n) {
                    throw new IllegalArgumentException();
                }
            }

        }
    }

    /*
     * returns name of the leaf
     */
    public String getName() {
        return this.name;
    }
}
