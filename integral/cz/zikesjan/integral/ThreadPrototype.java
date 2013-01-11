package cz.zikesjan.integral;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jan Zikes
 */

/*
 * class in which is defined the prototype of thread
 */
public class ThreadPrototype extends Thread implements Clone<ThreadPrototype> {

    double min;
    double max;
    double delta;
    Parser p;
    double value;
    double prevValue;
    char variable;
    Set<Constant> constList;
    CalculatorImpl caller;
    boolean pooled = true;


    /*
     * method for setting data for task
     *
     * @param min
     *           left border of interval for which should this thread calculate
     *           the integral
     *
     * @param max
     *           right border of interval for which should this thread calculate
     *           the integral
     *
     * @param delta
     *             delta that should be used for integral evaluating
     *
     * @param p
     *         parser which has already parsed the expression
     *
     * @param variable
     *                integrate variable
     *
     * @param constList
     *                 set of all of already defined constants
     *
     * @param caller
     *              CalculatorImpl from which was Thread called
     */
    public void setValues(double min, double max, double delta, Parser p, char variable, Set<Constant> constList, CalculatorImpl caller) {
        this.min = min;
        this.max = max;
        this.delta = delta;
        this.p = p;
        this.variable = variable;
        this.constList = constList;
        this.caller = caller;

    }

    /*
     * create new ThreadPrototype instead of clone
     */
    @Override
    public ThreadPrototype clone() {
        return new ThreadPrototype();
    }

    /*
     * running cycle of the thread
     */
    @Override
    public void run() {

        while (true) {  //infinite live loop of the thread
            if (!pooled) {  //check if is in pool
                value = 0d;
                for (double d = min; d < max; d = d + delta) {  //calculate already defined part of the integral

                    value = value + (p.top.evaluate(variable, d, constList) * delta);

                }
                caller.setValue(value);
                caller.returnToPool(this);
                pooled = true;

            } else {
                close();
                    
                }
            }
        }
    public synchronized void close(){   //method for making thread waiting
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadPrototype.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public synchronized void refuse(){  //method for Thread Waking up
        notifyAll();

    }
    }

