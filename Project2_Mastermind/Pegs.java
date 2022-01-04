package assignment2;

import java.util.*;
import java.util.ArrayList;

public class Pegs {
	
	int bPegs;
	int wPegs;
	String[] userGuess;
	String[] code;
	int sem;
	int sem2;
	ArrayList<Integer> temp;
	
	public Pegs(String guess, String genCode) {
		userGuess = guess.split("");
		code = genCode.split("");
		bPegs = 0;
		wPegs = 0;
		sem = 0;
		sem2 = 0;
	}

	
	public void checkBlackPegs() { 
		ArrayList<Integer> blackList = new ArrayList<Integer>();
		ArrayList<Integer> whiteList = new ArrayList<Integer>();
		
		for(int i = 0; i < userGuess.length; i++) {
			if(userGuess[i].equals(code[i])) {
				bPegs += 1;
				blackList.add(i);
				sem = 1;
			}
		}
		for(int i = 0; i<userGuess.length;i++) {
			sem = 0;
			if(blackList.contains(i) == false && sem == 0) {
				for(int j = 0; j<userGuess.length;j++){
					if(blackList.size()>=0) {
						if(code[j].equals(userGuess[i])){
							if(sem == 0 && blackList.contains(j) == false && whiteList.contains(j) == false) {
								sem = 1;
								wPegs++;
								whiteList.add(j);
							}
						}
					}
					
				}
			}
		}			
	}	
	
	public void printPegs(){
		System.out.println(bPegs + "b_" + wPegs + "w\n" );
	}
	
	public int getBlackPegs() {
		return this.bPegs;
	}
	
	public int getWhitePegs() {
		return this.wPegs;
	}
}
