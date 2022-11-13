package ex_01;

import java.util.concurrent.locks.*;

public class SushiMonitor_01 {
	
	
	/* COMPLETE */
	private Lock lock = new ReentrantLock();
	private Condition canEat = lock.newCondition();
	private volatile int freeSeats=5;
	private volatile boolean group=false;
	private static final int MAX_CAPACITY=5;
	
	public void enter (int i) {
		/* COMPLETE */
		lock.lock();
		System.out.println("----> Entering C("+i+")");
		
		while(freeSeats==0 || group) {
			if(!group) {
				group=true;
				System.out.println("*** Possible group detected. I wait C("+i+")");
			}
			else {
				System.out.println("*** I'm told to wait for all free C("+i+")");
			}
			try {
				canEat.await();
			}
			catch(InterruptedException e) {}
		}
		System.out.println("+++ [free: "+freeSeats+"] I sit down C("+i+")");
		freeSeats--;
		lock.unlock();
	}
	
	public void exit (int i) {
		/* COMPLETE */
		lock.lock();
		freeSeats++;
		if (freeSeats==MAX_CAPACITY && group) {
			group=false;
		}
		System.out.println("---> now leaving [free: "+freeSeats+"] C("+i+")");
		canEat.signal();
		lock.unlock();
	}
}
