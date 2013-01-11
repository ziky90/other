package ass.semestralka;

import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author Jan Zikes
 */

/*
 * class that cares about whole parsing stuff
 */
public final class Parser {

    AbstractNode top;
    Set<Function> functList = new HashSet<Function>();

    public Parser(String expression, Set<Function> l) {
        this.functList = l;
        this.parse(expression);
    }

    /*
     * method to parse expression
     *
     * @param expression
     *                  expression to be parsed
     */
    public void parse(String expression) {
        Scanner scan = new Scanner(expression);
        String temp;
        char ch;
        Stack<Character> signStack = new Stack<Character>();
        Stack<AbstractNode> leafStack = new Stack<AbstractNode>();
        Stack<Integer> priorities = new Stack<Integer>();
        int priority = 0;
        int bracketCounter = 0;
        boolean opened = false;
        while (scan.hasNext()) {    //loop that scans whole expression
            temp = scan.next();
            ch = temp.charAt(0);
            if (temp.length() == 1) {   //assurance it will be no function
                if (ch == '+' || ch == '-') {   //if lowest priority check
                    if (priority < 1) { //there was no operation with higher or same priority before
                        signStack.push(ch);
                    } else {
                        char ch2 = signStack.peek();
                        while (ch2 == '*' || ch2 == '/' || ch2 == '^' || ch2 == '+' || ch2 == '-') {    //there was, so do shounting yard algorithm
                            signStack.pop();
                            Node n = new Node(ch2);
                            n.right = leafStack.pop();
                            n.left = leafStack.pop();
                            leafStack.push(n);
                            if (!signStack.isEmpty()) {
                                ch2 = signStack.peek();
                            } else {
                                break;
                            }
                        }
                        signStack.push(ch);
                    }
                    priority = 1;
                } else if (ch == '*' || ch == '/') {    //check for operations of priority 2
                    if (priority < 2) {     //there was no operation with higher or same priority before
                        signStack.push(ch);
                    } else {
                        char ch2 = signStack.peek();
                        while (ch2 == '^' || ch2 == '*' || ch2 == '/') {    //there was, so do shounting yard algorithm 
                            signStack.pop();
                            Node n = new Node(ch2);
                            n.right = leafStack.pop();
                            n.left = leafStack.pop();
                            leafStack.push(n);
                            if (!signStack.isEmpty()) {
                                ch2 = signStack.peek();
                            } else {
                                break;
                            }
                        }
                        signStack.push(ch);
                    }
                    priority = 2;
                } else if (ch == '^') { //check for operations of priority 3
                    signStack.push(ch);
                    priority = 3;
                } else if (ch == '(') { //check for open bracket
                    signStack.push(ch);
                    priorities.add(priority);
                    priority = 0;
                    bracketCounter++;
                    opened = true;
                } else if (ch == ')') { //check for closing bracket
                    if (opened == false) {
                        throw new IllegalArgumentException();
                    }
                    while (signStack.peek() != '(') {   //work as before until opening bracket is on the sign stack
                        char ch2 = signStack.pop();
                        Node n = new Node(ch2);
                        n.right = leafStack.pop();
                        n.left = leafStack.pop();
                        leafStack.push(n);
                    }
                    signStack.pop();
                    bracketCounter--;
                    priority = priorities.pop();
                } else {    //here it muss be leaf
                    Leaf l = new Leaf(temp);
                    leafStack.push(l);
                }
            } 
            //not interesting for regexes
            else {    //longer than char
                if (temp.charAt(temp.length() - 1) == '(') {    //check for function
                    String fcExpression = null;
                    List<String> varlInstead = new LinkedList<String>();
                    List<Character> varList = new LinkedList<Character>();
                    String fcname = temp.substring(0, temp.length() - 1);
                    StringBuilder buf = new StringBuilder();
                    int inpos = 1;  //recursive function level
                    for (Function fc : functList) { //findin the function for expression in the list
                        if (fc.name.equalsIgnoreCase(fcname)) {
                            fcExpression = fc.expression;
                            varList = fc.varList;
                            break;
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("( ");
                    while (inpos != 0) {    //building inner function string
                        if (!scan.hasNext()) {
                            throw new IllegalArgumentException();
                        } else {
                            temp = scan.next();
                            if (temp.charAt(0) == ',' && inpos == 1) {    //check for ',' in function
                                sb.append(")");
                                varlInstead.add(sb.toString());
                                sb.delete(0, sb.length());
                                sb.append("( ");

                            } else if (temp.charAt(0) == ')') { //check for closing bracket
                                sb.append(temp).append(" ");
                                inpos--;
                            } else if (temp.charAt(temp.length() - 1) == '(') { //check for inner function
                                sb.append(temp).append(" ");
                                inpos++;
                            } else {
                                sb.append(temp).append(" ");
                            }
                        }
                    }
                    varlInstead.add(sb.toString());
                    if (fcExpression == null) { //check for function with no expression
                        throw new IllegalArgumentException();
                    } else {    //prepare and recursively parse function expression
                        Scanner localScan = new Scanner(fcExpression);
                        String localTemp;
                        while (localScan.hasNext()) {
                            localTemp = localScan.next();
                            int counter = 0;
                            boolean was = false;
                            char tmpchr = localTemp.charAt(0);
                            for (Character tempChar : varList) {
                                if (tempChar == tmpchr && localTemp.length() == 1) { //if the variable was found
                                    buf.append(varlInstead.get(counter)).append(" ");
                                    was = true;
                                    break;
                                } else if (tmpchr == '-' && localTemp.length() > 1) {  //if the -variable was found
                                    if (localTemp.charAt(1) == tempChar) {
                                        buf.append("-1 * ").append(varlInstead.get(counter)).append(" ");
                                        was = true;
                                        break;
                                    }
                                }
                                counter++;
                            }
                            if (was == false) {
                                buf.append(localTemp).append(" ");
                            }
                        }
                        Parser p = new Parser(buf.toString(), functList);
                        leafStack.push(p.top);
                    }
                } else {    //else it must be leaf
                    Leaf l = new Leaf(temp);
                    leafStack.push(l);
                }
            }
        }
        while (!signStack.isEmpty()) {  //meke all the data left on the stacks
            char ch2 = signStack.pop();
            Node n = new Node(ch2);
            try {
                n.right = leafStack.pop();
                n.left = leafStack.pop();
            } catch (EmptyStackException ese) {
                throw new IllegalArgumentException();
            }
            leafStack.push(n);
        }
        if (bracketCounter != 0) {  //check if all the brackets was closed
            throw new IllegalArgumentException();
        }
        if (leafStack.size() != 0) {    //check if there are no leafs remaining on the stack
            top = leafStack.peek();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
