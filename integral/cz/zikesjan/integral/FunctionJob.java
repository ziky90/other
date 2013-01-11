package cz.zikesjan.integral;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jan Zikes
 */

/*
 *class for storing data about FunctionJob
 */
public class FunctionJob implements Job{

    String name;
    String expression;
    List<Character> varList = new LinkedList<Character>();

    public FunctionJob(String name, String expression, char... variables) {
        this.name=name;
        this.expression=expression;
        for (char ch:variables){
            varList.add(ch);
        }
    }

    /*
     * returns 1 if instance is FunctionJob, 2 if instance is ConstantJob and 3
     * if instance is ConstantJob
     */
    public int getType() {
        return 1;
    }

}
