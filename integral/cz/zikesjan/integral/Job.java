package cz.zikesjan.integral;

/**
 *
 * @author Jan Zikes
 */

/*
 * interface that covers jobs used in CalculatorImpl
 */
public interface Job {

    /*
     * returns 1 if instance is FunctionJob, 2 if instance is ConstantJob and 3
     * if instance is ConstantJob
     */
    public int getType();

}
