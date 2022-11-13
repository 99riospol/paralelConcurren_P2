package ex_02;

import java.util.concurrent.locks.*;

public class SushiMonitor_02 {
	
	 	private Lock lock = new ReentrantLock();
	    private Condition canEat = lock.newCondition();
	    private volatile int freeSeats=5;
	    private volatile boolean group=false;
	    private final int MAX_CAPACITY=5;
	    private volatile int nextTicket =0;
	    private volatile int nowServing=0;

	    public void enter (int i) {
	        lock.lock();
	        int myTicket=nextTicket;
	        boolean firstWait=true;
	    	nextTicket++;
	        System.out.println("----> Entering C("+i+")");
	        while(freeSeats==0 || group || myTicket!=nowServing) {
	            if(!group && freeSeats==0) {
	                group=true;
	                System.out.println(" Possible group detected. I wait C("+i+") ");
	            }
	            else if (group && firstWait){
	                System.out.println(" I'm told to wait for all free C("+i+") ");
	            }
	            if(freeSeats!=0 && !group) {
	            	canEat.signal();
	            }
	            	
	            try {		    
	            	firstWait=false;
	            	canEat.await();	            	
				}
				catch(InterruptedException e) {}
	        }
	        System.out.println("+++ [free: "+freeSeats+"] I sit down C("+i+")");
	        nowServing++;
	        freeSeats--;
	       

	        lock.unlock();
	    }

	    public void exit (int i) {
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
