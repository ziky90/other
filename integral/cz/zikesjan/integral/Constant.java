package cz.zikesjan.integral;

/**
 *
 * @author Jan Zikes
 */

/*
 * Class for storing data about constant
 */
public class Constant {

    char constaname;
    double value;

    public Constant(char constaName, double value){
        constaname=constaName;
        this.value=value;
    }

    /*
     * equals for checking if there is no constant with same name
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Constant other = (Constant) obj;
        if (this.constaname != other.constaname) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.constaname;
        return hash;
    }
    
}
