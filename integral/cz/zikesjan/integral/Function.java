package cz.zikesjan.integral;

import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author Jan Zikes
 */

/*
 * class for storing data about function
 */
public final class Function {
    String name;
    String expression;
    List<Character> varList = new LinkedList<Character>();
    AbstractNode top;
    List<Function> l;

    public Function(String name, String expression, List<Character> varList){
        this.name=name;
        this.expression=expression;
        this.varList=varList;
    }

    /*
     * equals that helps to find out if there is some function that has exact
     * the same name
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Function other = (Function) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    
}
