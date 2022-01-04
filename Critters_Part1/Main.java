/*
 * CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Summer 2021
 */

package assignment4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

/*
 * Usage: java <pkg name>.Main <input file> test input file is
 * optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */

public class Main {

    /* Scanner connected to keyboard input, or input file */
    static Scanner kb;

    /* Input file, used instead of keyboard input if specified */
    private static String inputFile;

    /* If test specified, holds all console output */
    static ByteArrayOutputStream testOutputString;

    /* Use it or not, as you wish! */
    private static boolean DEBUG = false;

    /* if you want to restore output to console */
    static PrintStream old = System.out;

    /* Gets the package name.  The usage assumes that Critter and its
       subclasses are all in the same package. */
    private static String myPackage; // package of Critter file.

    /* Critter cannot be in default pkg. */
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     *
     * @param args args can be empty.  If not empty, provide two
     *             parameters -- the first is a file name, and the
     *             second is test (for test output, where all output
     *             to be directed to a String), or nothing.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
            }
            if (args.length >= 2) {
                /* If the word "test" is the second argument to java */
                if (args[1].equals("test")) {
                    /* Create a stream to hold the output */
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    /* Save the old System.out. */
                    old = System.out;
                    /* Tell Java to use the special stream; all
                     * console output will be redirected here from
                     * now */
                    System.setOut(ps);
                }
            }
        } else { // If no arguments to main
            kb = new Scanner(System.in); // Use keyboard and console
        }
        commandInterpreter(kb);

        System.out.flush();
    }

    /* Do not alter the code above for your submission. */

    private static void commandInterpreter(Scanner kb) 
    {
    	boolean keepPlaying = true;
    	String input;
    	//here implement parsing, calling, etc
    	while(keepPlaying)
    	{
    		System.out.print("critters>");
    		input = kb.nextLine();

    		try 
    		{
        		if(input.equals("quit"))
        		{
        			keepPlaying = false;
        			continue;
        		}
        		else  if(input.contains("stats"))
        		{
        			String[] splitInput = input.split(" ");
            		Class<?> critter_class = null;

        			if(splitInput.length > 2)
        			{
        					System.out.println("error processing: "+ input);
        					continue;
        			}
        			else
        			{
        	    		try 
        	    		{
        	    			critter_class = Class.forName(myPackage + "." + splitInput[1]);
        	    			List<Critter> critterList = Critter.getInstances(splitInput[1]);
        	    			Method stats = critter_class.getMethod("runStats", List.class);
        	    			stats.invoke(null, critterList);
        	    		}
        				catch(Exception e)
        				{
        					List<Critter> critterList = Critter.getInstances(splitInput[1]);
        					Critter.runStats(critterList);
        				}
        			}
        		}
        		else if(input.contains("seed"))
        		{
        			String[] splitInput = input.split(" ");
        			if(splitInput.length > 2)
    					System.out.println("error processing: " + input);
        			else
        			{
        				try
    					{
        					int seed = Integer.parseInt(splitInput[1]);
        					Critter.setSeed(seed);
    					}
    					catch(NumberFormatException e)
    					{
    						System.out.println("error processing: " + input);
    						continue;
    					}    
        			}
        		}
        		else if(input.equals("clear"))
        			Critter.clearWorld();
        		else if(input.equals("show"))
        			Critter.displayWorld();
        		
        		else if(input.contains("create"))
        		{
        			String[] splitInput = input.split(" ");
        			//if(input.equals("create"))
        			//	Critter.createCritter(splitInput[1]);
        			if(splitInput.length > 2 && splitInput.length < 4)
            		{
        				try
        				{
        					int createCount = Integer.parseInt(splitInput[2]);
        					for(int i = 0; i < createCount; i++)
        						Critter.createCritter(splitInput[1]);
        				}
        				catch(NumberFormatException e)
        				{
        					System.out.println("error processing: " + input);
        					continue;
        				}        		
            		}  
        			else if(splitInput.length == 2)
        				Critter.createCritter(splitInput[1]);
        			else
    					System.out.println("error processing: " + input);
        		}
        		
        		else if(input.contains("step"))
        		{
    				String[] splitInput = input.split(" ");
        				//single step
        				if(input.equals("step"))
        				{
        					Critter.worldTimeStep();
        					continue;
        				}
        				else if(!splitInput[0].equals("step"))
        				{
        					System.out.println("error processing: " + input);
        					continue;
        				}
        				else if(splitInput.length > 1 && splitInput.length < 3)
        				{
        					try
        					{
        						int stepCount = Integer.parseInt(splitInput[1]);
        						for(int i = 0; i < stepCount; i++)
        							Critter.worldTimeStep();
        					}
        					catch(NumberFormatException e)
        					{
        						System.out.println("error processing: " + input);
        						continue;
        					}
        				}
        				else
    						System.out.println("error processing: " + input);

        		}

        		else 
        			System.out.println("error processing: " + input);
    		}
    		//this will be implemented later, still in stage 1
    		catch(InvalidCritterException a)
    		{	
    			String[] splitInput = input.split(" ");
    			System.out.println("Invalid Critter Class: " + splitInput[1]);
    		}
    	}
    }
}