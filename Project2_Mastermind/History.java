package assignment2;

public class History {
	
	private int roundCount;
	private int numGuesses;
	private int[] bPegs;
	private int[] wPegs;
	private String[] guessList;
	
	History(int numGuesses) {
		this.numGuesses = numGuesses;
		guessList = new String[numGuesses];
		bPegs = new int[numGuesses];
		wPegs  = new int[numGuesses];
		roundCount = 0;
	}
	
	public void updateRound() {
		roundCount++;
		
	}
	
	public void updatePegs(int bPeg, int wPeg){
		bPegs[roundCount] = bPeg;
		wPegs[roundCount] = wPeg;
	}
	
	public void updateGuess(String guess){
		if(roundCount >= numGuesses) {
			System.out.print("Error");
			return;
		}
		guessList[roundCount] = guess;
		
	}
	
	public void print(){
		if(roundCount == 0) {
			System.out.println("You have not guessed yet!");
			return;
		}
		
		for(int i = 0;i < roundCount;i++) {
			System.out.println(guessList[i] + " -> " + bPegs[i] + "b_" + wPegs[i] + "w");
		}
	}
}
