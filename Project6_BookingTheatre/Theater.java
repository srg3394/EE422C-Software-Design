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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Theater {

    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 50; // 50 ms. Use it to fix the delay time between prints.
    private SalesLogs log = new SalesLogs(); // Field in Theater class.

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Represents a seat in the theater A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;

        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }
    }

    // end of class Seat

    /**
     * Represents a paper ticket purchased by a client
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;

        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol + showLine + eol + boxLine + eol + seatLine + eol + clientLine + eol + dashLine;

            return result;
        }
    }

    /**
     * SalesLogs are security wrappers around an ArrayList of Seats and one of
     * Tickets that cannot be altered, except for adding to them. getSeatLog returns
     * a copy of the internal ArrayList of Seats. getTicketLog returns a copy of the
     * internal ArrayList of Tickets.
     */
    static class SalesLogs {
        private ArrayList<Seat> seatLog;
        private ArrayList<Ticket> ticketLog;

        private SalesLogs() {
            seatLog = new ArrayList<Seat>();
            ticketLog = new ArrayList<Ticket>();
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Seat> getSeatLog() {
            return (ArrayList<Seat>) seatLog.clone();
        }

        @SuppressWarnings("unchecked")
        public ArrayList<Ticket> getTicketLog() {
            return (ArrayList<Ticket>) ticketLog.clone();
        }

        public void addSeat(Seat s) { // call when seat is allocated
            seatLog.add(s);
        }

        public void addTicket(Ticket t) { // call when ticket is printed
            ticketLog.add(t);
        }

    } // end of class SeatLog
    
    public Queue<Seat> seatQueue;
    public boolean allSold;
    public String showName;
    SalesLogs s;
    
    
    public Theater(int numRows, int seatsPerRow, String show) {
        seatQueue = new LinkedList<Seat>();
        this.allSold = false;
        showName = show;
        s = new SalesLogs();
        createTickets(numRows, seatsPerRow);
        
    }
    
    public void createTickets(int numRows, int seatsPerRow) {
    	for(int i = 0; i <= numRows; i++) { // <= because we need to create tickets for the last row too
    		for(int j = 0; j < seatsPerRow; j++) {
    			Seat newSeat = new Seat(i, j + 1);
    			seatQueue.add(newSeat); // seats already in order by best avaliable
    		}
    	}
    }

    /**
     * Calculates the best seat not yet reserved
     *
     * @return the best seat or null if theater is full
     */
    public Seat bestAvailableSeat() {
        
    	if(!seatQueue.isEmpty()) { // check if theres any seats left in queue
    		Seat polled = seatQueue.poll();
    		s.seatLog.add(polled);
    		return polled;
    	}
    	else {
    		return null;
    	}
    	
    }

    /**
     * Prints a ticket to the console for the client after they reserve a seat.
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
        
    	if(boxOfficeId == null) {
    		return null;
    	}
    	
    	if(allSold) {
    		if(seatQueue.peek() == null) {
    			System.out.println("Sorry, we are sold out!");
    		} 		
    	}
    	else {
    		if(seat != null) {
    			Ticket newTicket = new Ticket(showName, boxOfficeId, seat, client);
    			s.ticketLog.add(newTicket);
    			System.out.println(newTicket.toString());
    			return newTicket;
    		}
    	}
    	
    	
    	
    	
        return null;
    }

    /**
     * Lists all seats sold for this theater in order of purchase.
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        return s.getSeatLog();
    }

    /**
     * Lists all tickets sold for this theater in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        return s.getTicketLog();
        		
    }
}
