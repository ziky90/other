package cz.zikesjan.integral;

/**
 *
 * @author Jan Zikes
 */

/*
 *class for storing data about ConstantJob
 */
public class ConstantJob implements Job{

    char constaName;
    double value;

    public ConstantJob(char constaName, double value) {
          this.constaName=constaName;
          this.value=value;
    }

    /*
     * returns 1 if instance is FunctionJob, 2 if instance is ConstantJob and 3
     * if instance is ConstantJob
     */
    public int getType() {
        return 2;
    }



}
