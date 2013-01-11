package cz.zikesjan.integral;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jan Zikes
 */
public class CalculatorImpl implements Calculator {

    Set<Function> functList = new HashSet<Function>();
    Set<Constant> constList = new HashSet<Constant>();
    private ConcurrentLinkedQueue<Job> jobQueue = new ConcurrentLinkedQueue<Job>();
    int threads;
    volatile double value;
    volatile int progress;
    volatile boolean done = false;
    boolean setUp = true;
    ThreadPool<ThreadPrototype> pool;
    MasterThread mt;

    public CalculatorImpl(int threadsNum) {
        this.threads = threadsNum;
        mt = new MasterThread();
        Thread t = new Thread(mt);
        t.start();
    }

    /*
     * adding FunctionJob into the ConcurrentLinkedQueue (enables calling from more than one thread)
     */
    public void addFunction(String name, String expression, char... variables) throws IllegalArgumentException {
        jobQueue.add(new FunctionJob(name, expression, variables));
        if (mt.sleeped) {
            mt.refuse();
        }
    }

    /*
     * adding ConstantJob into the ConcurrentLinkedQueue (enables calling from more than one thread)
     */
    public void setConstant(char constaName, double value) throws IllegalArgumentException {
        jobQueue.add(new ConstantJob(constaName, value));
        if (mt.sleeped) {
            mt.refuse();
        }
    }

    /*
     * adding IntegrateJob into the ConcurrentLinkedQueue (enables calling from more than one thread)
     */
    public void integrate(String expression, char variable, double min, double max, double delta, ResultListener rl) throws ParseException, IllegalArgumentException {
        jobQueue.add(new IntegrateJob(expression, variable, min, max, delta, rl));
        if (mt.sleeped) {
            mt.refuse();
        }
    }

    /*
     * returns functions already saved
     */
    public String[] getSupportedFunctions() {
        String[] s = new String[functList.size()];
        int i = 0;
        for (Function f : functList) {
            s[i] = f.name;
            i++;
        }
        return s;
    }

    /*
     * sets value of entaire Integral
     *
     * @param addValue
     *                partial Inegral result for threads area
     */
    public synchronized void setValue(double addValue) {

        value = value + addValue;
        progress++;
        if (progress == threads) {
            done = true;
        }


    }

    /*
     * returns thread into the pool
     *
     * @param tp
     *          thread that should be returned
     */
    public synchronized void returnToPool(ThreadPrototype tp) {

        pool.returnObject(tp);

    }

    /*
     * main thread of the application ehich runs ever and all the time long
     */
    private class MasterThread extends Thread {

        Thread[] thredArray = new Thread[threads - 1];
        boolean sleeped = false;

        @Override
        public void run() {

            if (CalculatorImpl.this.threads > 1) { //creating ThreadPool if there is more than one thread allowed
                ThreadPrototype tp = new ThreadPrototype();
                CalculatorImpl.this.pool = new ThreadPool<ThreadPrototype>(threads - 1, tp);

            }
            while (true) {  //infinite loop of the MasterThread

                if (!CalculatorImpl.this.jobQueue.isEmpty()) {  //if there is something to do just do it
                    sleeped = false;
                    Job j = CalculatorImpl.this.jobQueue.poll();

                    if (j.getType() == 1) { //is it FunctionJob?

                        FunctionJob fj = (FunctionJob) j;
                        CalculatorImpl.this.functList.add(new Function(fj.name, fj.expression, fj.varList));
                    } else if (j.getType() == 2) {  //is it ConstantJob?

                        ConstantJob cj = (ConstantJob) j;
                        CalculatorImpl.this.constList.add(new Constant(cj.constaName, cj.value));
                    } else {    //else it must be IntegrateJob
                        IntegrateJob ij = (IntegrateJob) j;
                        Parser p = new Parser(ij.expression, CalculatorImpl.this.functList);    //create parser and parse

                        if (CalculatorImpl.this.threads == 1) { //if there is just one thread allowed than do it easy (helps when testing)
                            double intValue = 0d;
                            if (ij.min < ij.max) {
                                for (double i = ij.min; i < ij.max; i = i + ij.delta) {
                                    intValue = intValue + (p.top.evaluate(ij.variable, i, constList) * ij.delta);
                                }
                            } else if (ij.max < ij.min) {
                                for (double i = ij.max; i < ij.min; i = i + ij.delta) {
                                    intValue = intValue + (p.top.evaluate(ij.variable, i, constList) * ij.delta);
                                }
                            } else {
                                intValue = 0;
                            }
                            ij.rl.reportResult(intValue);   //report calculated value

                        } else {    //else do it multithread
                            double intValue = 0d;
                            double interval;
                            if(ij.max>ij.min){
                                interval = (ij.max - ij.min) / (CalculatorImpl.this.threads);
                            }
                            else{
                                interval = (ij.min - ij.max) / (CalculatorImpl.this.threads);
                            }

                            for (int k = 0; k < threads - 1; k++) { //getting threads from pool and assign them their jobs
                                ThreadPrototype t = CalculatorImpl.this.pool.borrowObject();
                                if (ij.min < ij.max) {
                                    t.setValues(ij.min + (k * interval), (ij.min + (k * interval)) + interval, ij.delta, p, ij.variable, constList, CalculatorImpl.this);
                                    t.pooled = false;
                                } else if (ij.max < ij.min) {
                                    t.setValues(ij.max + (k * interval), (ij.max + (k * interval)) + interval, ij.delta, p, ij.variable, constList, CalculatorImpl.this);
                                    t.pooled = false;
                                } else {
                                    t.pooled = true;

                                }

                                if (setUp) {    //if it is first time
                                    Thread th = new Thread(t);
                                    th.start();
                                    thredArray[k] = th;
                                } else {
                                    t.refuse();
                                }

                            }

                            setUp = false;
                            if (ij.min < ij.max) {
                                for (double i = ij.min + (interval * (CalculatorImpl.this.threads - 1)); i < ij.max; i = i + ij.delta) {    //do master threads own job
                                    intValue = intValue + (p.top.evaluate(ij.variable, i, constList) * ij.delta);
                                }
                                CalculatorImpl.this.setValue(intValue);

                                while (!done) { //wait for threads that has not finished yet
                                }
                            } else if (ij.max < ij.min) {
                                for (double i = ij.max + (interval * (CalculatorImpl.this.threads - 1)); i < ij.min; i = i + ij.delta) {    //do master threads own job
                                    intValue = intValue + (p.top.evaluate(ij.variable, i, constList) * ij.delta);
                                }
                                CalculatorImpl.this.setValue(intValue);

                                while (!done) { //wait for threads that has not finished yet
                                }
                            }   else{
                                CalculatorImpl.this.setValue(0);
                                
                            }
                            CalculatorImpl.this.progress = 0;
                            ij.rl.reportResult(CalculatorImpl.this.value);
                            CalculatorImpl.this.value = 0d;
                            done = false;

                        }
                    }

                } else {
                    sleeped = true;
                    close();
                }
            }

        }

        public synchronized void close() {  //method for making thread waiting
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadPrototype.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public synchronized void refuse() { //method for Thread Waking up
            notifyAll();

        }
    }
}
