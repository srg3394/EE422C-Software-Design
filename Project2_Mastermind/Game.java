package assignment2;

import java.util.Scanner;


public class Game{
	
	String toPlay = "Y";
	private String secretCode;
	private boolean isTesting;
	private boolean isPlaying = true;
	Scanner input;
	private int numGuesses;
	private String colors[];
	private int pegNumber;
	public History h = new History(numGuesses);
	
	public Game(boolean isTesting, Scanner input, SecretCodeGenerator generator, GameConfiguration config) {
		this.secretCode = generator.getNewSecretCode();
		this.isTesting = isTesting;
		this.input = input;
		this.numGuesses = config.guessNumber;
		this.colors = config.colors;
		this.pegNumber = config.pegNumber;
		
		
		if(isTesting) { 
			System.out.println("Secret code: " + secretCode);
			System.out.println();
		}
	}
	
	public String runGame() {
		
		History h = new History(numGuesses);
		
		while(isPlaying) {
			
			System.out.println("You have " + numGuesses + " guess(es) left.");
			System.out.println("Enter guess:");
			String guess = input.nextLine(); // get guess
			
			int checkGuess = checkGuess(guess);
		
			if(checkGuess == -1 || checkGuess == -2 || checkGuess == -3) {
				System.out.println("INVALID_GUESS");
				System.out.println("");
				
			}
			if(checkGuess == -4) {
				numGuesses -= 1;
				h.updateGuess(guess);
				Pegs check = new Pegs(guess,secretCode);
				check.checkBlackPegs();
				h.updatePegs(check.getBlackPegs(),check.getWhitePegs());
				h.updateRound();
				System.out.print(guess + " -> ");
				check.printPegs();
				
				
				if(check.getBlackPegs() == secretCode.length()) {
					numGuesses = 0;
				}
				
				
			}
			if(checkGuess == 0) { //HISTORY
				h.print();
				System.out.println("");
				
				
				
			}
			if(numGuesses <= 0) {
				isPlaying = false;
				int blackPegs;
				Pegs p = new Pegs(guess,secretCode);
				p.checkBlackPegs();
				blackPegs = p.getBlackPegs();
				if(blackPegs == secretCode.length()) {
					System.out.println("You win!");
					System.out.println("");
				}
				else {
					System.out.println("You lose! The pattern was " + secretCode);		
				}
				
				System.out.println("Do you want to play a new game? (Y/N): ");
				String toPlay = input.nextLine();
				
				if(toPlay.equals("N")) {
					isPlaying = false;
					return toPlay;
					
				}
			}
			
		}
		return toPlay;
	}
	
	public int checkGuess(String guess) {
		char guessSplit;
		String guessString;
	
		if(guess.equals("HISTORY")) {
			return 0; // history guess
		}
		if(guess.length() != secretCode.length()) {
			return -1; // wrong size
		}
		
		
		for(int i = 0; i<guess.length();i++) {
			guessSplit = guess.charAt(i);
			guessString = Character.toString(guessSplit);
			if(Character.isLowerCase(guessSplit)) {
				return -2;
			}			
			
		}	
		
		for(int j = 0; j<guess.length();j++) {
			guessSplit = guess.charAt(j);
			guessString = Character.toString(guessSplit);
			if(colorCheck(guessString) == -3) {
				return -3;
			}
		}
		
		return -4;
	}
	
	public int colorCheck(String guess) {
		for(int i = 0; i< colors.length;i++) {
			if(guess.equals(colors[i])) {
				return 0;
			}
		}
		return -3;
	}
}