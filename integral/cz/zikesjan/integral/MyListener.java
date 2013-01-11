package cz.zikesjan.integral;

/*
 * Listener which can calculate duration time also the part of the speed
 * competition (for more see class Main).
 */
public class MyListener implements ResultListener {

	public MyListener( long startTime )
	{
		this.startTime = startTime;
	}

	private long startTime;
        public double r=0;
	@Override
	public void reportResult(double result) {

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
                r=result;
		System.out.print("integral result is " + result);
		System.out.println("\nRun time: " + runTime);
                
	}

}