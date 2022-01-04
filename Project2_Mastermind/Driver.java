/*
 * Mastermind
 * Jan 20, 2020
 */

package assignment2;

import java.util.Scanner;


public class Driver {
    public static void main(String[] args) {
    	String colors[] = {"B","G","O","P","R","Y"};
    	Boolean isTesting = false;
    	GameConfiguration config = new GameConfiguration(12, colors, 4);
    	SecretCodeGenerator secret = new SecretCodeGenerator(config);
    	Driver.start(isTesting, config, secret);
    }

    public static void start(Boolean isTesting, GameConfiguration config, SecretCodeGenerator generator) {
        // TODO: complete this method
		// We will call this method from our JUnit test cases.
    	
    	Scanner input = new Scanner(System.in);
    	System.out.println("Welcome to Mastermind.");
    	System.out.println("Do you want to play a new game? (Y/N): ");
    	String answer = input.nextLine();
    	
    	while (answer.equals("Y")){
    		
    		Game newGame = new Game(isTesting,input,generator,config);
    		answer = newGame.runGame();
    	}
    }
}
