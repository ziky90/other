package cz.zikesjan.integral;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Jan Zikes
 */

/*
 * pool for storing of Threads (ThreadPool design pattern, multiton design pattern)
 */
public class ThreadPool<ThreadPrototype extends Clone<ThreadPrototype>> {

    private Queue<ThreadPrototype> q = new ConcurrentLinkedQueue<ThreadPrototype>();
    private final ThreadPrototype o;
    private int size;

    public ThreadPool(int size, final ThreadPrototype t) {
        this.size = size;
        this.o = t;
        this.makeThreads();
    }

    /*
     * method that creates maximal allowed number of threads
     */
    private void makeThreads() {
        for (int i = 0; i < size; i++) {
            q.add(o.clone());            
        }
    }

    /*
     * because of the multitont getInstance()
     */


    /*
     * method for returning of the Thread into the pool
     *
     * @param t
     *         thread to be returned
     */
    public void returnObject(ThreadPrototype t) {
        q.add(t);
    }

    /*
     * method which returns thread that is borowed (It will never happend that
     * the q will be empety and it will also be called borrowObject() because
     * of the check in CalculatorImpl)
     */
    public ThreadPrototype borrowObject() {
        ThreadPrototype t = q.poll();
        return t;


    }
}
