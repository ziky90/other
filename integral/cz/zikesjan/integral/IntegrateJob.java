package cz.zikesjan.integral;

/**
 *
 * @author Jan Zikes
 */

/*
 *class for storing data about IntegrateJob
 */
public class IntegrateJob implements Job{

    String expression;
    char variable;
    double min;
    double max;
    double delta;
    ResultListener rl;

    public IntegrateJob(String expression, char variable, double min, double max, double delta, ResultListener rl) {
        this.expression = expression;
        this.variable = variable;
        this.min = min;
        this.max = max;
        this.delta = delta;
        this.rl = rl;
    }

    /*
     * returns 1 if instance is FunctionJob, 2 if instance is ConstantJob and 3
     * if instance is ConstantJob
     */
    public int getType() {
        return 3;
    }

}
