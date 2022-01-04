/* MULTITHREADING <MyClass.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Sathvik Gujja
 * srg3394
 * 76000
 * Slip days used: <3>
 * Summer 2021
 */
package assignment6;

import java.util.Map;

import assignment6.Theater.Seat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.Thread;

public class BookingClient {

    /**
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
	
	public Map<String, Integer> office;
	public Theater theater;
	
	public ArrayList<String> boxOffices;
	public ArrayList<Integer> customerNumbers;
	
	public int numId;
	
    public BookingClient(Map<String, Integer> office, Theater theater) {
        // TODO: Implement this constructor
    	this.office = office;
    	this.theater = theater;
    	boxOffices = new ArrayList<String>();
    	customerNumbers = new ArrayList<Integer>();
    	numId = 1;
    	
    	for(String s : office.keySet()) {
    		boxOffices.add(s);
    	}
    }

    /**
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate() {
        List<Thread> threadList = new ArrayList<Thread>();
        for(int i = 0; i< office.size(); i++) {
        	String boxOfficeId = boxOffices.get(i);
        	Thread thread = new Thread() {
        		@Override
        		public void run() {
        			int numCustomers = office.get(boxOfficeId); // number of customers at the box office;
        			
        			for(int n = numCustomers; n > 0; n--) {
        				synchronized(theater.seatQueue) {				
        					int customerId = numId;
        					numId++;
        					
        					theater.printTicket(boxOfficeId, theater.bestAvailableSeat(), customerId);					
        					office.replace(boxOfficeId, n - 1);
        				}
        				try {
        					Thread.sleep(theater.getPrintDelay());
        				}
        				catch(InterruptedException e) {
        					
        				}
        			}
        		}
        	};
        	
        	threadList.add(thread);
        	thread.start();
        }
        
    	
        return threadList;
    }

    public static void main(String[] args) {
		
    }
}
