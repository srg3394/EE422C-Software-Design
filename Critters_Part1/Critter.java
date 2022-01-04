
/*
 * CRITTERS Critter.java
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

import java.lang.reflect.InvocationTargetException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

/* 
 * See the PDF for descriptions of the methods and fields in this
 * class. 
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {
	

    private int energy = 0;

    private int x_coord;
    private int y_coord;

    //xhanged to protected
    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
    	Class<?> newCritterClass;
        
        try {
        	newCritterClass = Class.forName(myPackage + "." + critter_class_name);       	
        } catch(ClassNotFoundException e) {
        	throw new InvalidCritterException(critter_class_name);
        } catch(NoClassDefFoundError e) {
        	throw new InvalidCritterException(critter_class_name);
        }
        
        try {
        	Critter newCritter = (Critter) newCritterClass.getConstructor().newInstance();
        	newCritter.energy = Params.START_ENERGY;
        	//switched to -1 for printing purposes
        	newCritter.x_coord = getRandomInt(Params.WORLD_WIDTH);
        	newCritter.y_coord = getRandomInt(Params.WORLD_WIDTH);

        	population.add(newCritter);
        	
        } catch(InstantiationException e) {
        	throw new InvalidCritterException(critter_class_name);
        } catch(InvocationTargetException e) {
        	throw new InvalidCritterException(critter_class_name);
        } catch (IllegalAccessException e) {
        	throw new InvalidCritterException(critter_class_name);
		} catch (IllegalArgumentException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (NoSuchMethodException e) {
			throw new InvalidCritterException(critter_class_name);
		} catch (SecurityException e) {
			throw new InvalidCritterException(critter_class_name);
		}
             
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *        Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {
        // TODO: Complete this method
    	List<Critter> instances = new ArrayList<Critter>();
    	
    	for(Critter c : population) {
    		Class<?> critter_class = null;
    		try {
    			critter_class = Class.forName(myPackage + "." + critter_class_name);
    		}
    		catch (IllegalArgumentException e) {
    			throw new InvalidCritterException(critter_class_name);
    		} catch (SecurityException e) {
    			throw new InvalidCritterException(critter_class_name);
    		} catch (ClassNotFoundException e) {
				throw new InvalidCritterException(critter_class_name);
			}
    		if(critter_class.isInstance(c)) {
    			instances.add(c);
    		}
    	}
    	return instances;
    	
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        // TODO: Complete this method
    	population.clear();
    }
    
    
    /*
     * This method should invokedoTimeStep() on every living critter
     * If at the end of this some critters occupy the same space an 
     * encounter must occur. 
     */
    public static void worldTimeStep() 
    {
    	for(Critter crit: population)
    	{
    		crit.doTimeStep();
    	}
    	doEncounters();
    	updateRestEnergy();
    	photosynthesizeClovers();
    	addClovers();
    	population.addAll(babies);
    	babies.clear();
    }
    private static void updateRestEnergy()
    {
    	for(Critter crit: population)
    		crit.energy -= Params.REST_ENERGY_COST;
    }
    private static void photosynthesizeClovers()
    {
    	for(Critter crit: population)
    	{
    		if(crit instanceof Clover)
    		{
    			crit.energy+= Params.PHOTOSYNTHESIS_ENERGY_AMOUNT;
        		crit.energy -= Params.REST_ENERGY_COST;
    		}
    	}
    }
    private static void addClovers()
    {
    	//adding clovers at the end of the timestep
    	for(int i = 0; i < Params.REFRESH_CLOVER_COUNT; i++)
    	{
    		Critter clover = new Clover();
        	clover.energy = Params.START_ENERGY;
        	clover.x_coord = getRandomInt(Params.WORLD_WIDTH);
        	clover.y_coord = getRandomInt(Params.WORLD_WIDTH);
        	population.add(clover);
    	}
    }

    //stage 1 creating the world
    //This will eventually need to account for the critters in the world
    public static void displayWorld() 

    {
        //print the world size based on the params we have
    	//String [][] world1 = new String [Params.WORLD_HEIGHT+2][Params.WORLD_WIDTH+2];
    	ArrayList<ArrayList<String>> world = new ArrayList<>();
    	for(int i = 0; i < Params.WORLD_HEIGHT+2; i++)
    	{
    		world.add(new ArrayList<>());
    	}

    	for(int i = 0; i < Params.WORLD_HEIGHT+2/*world.length*/; i++) {
    		for(int j = 0; j < Params.WORLD_WIDTH+2/*world[0].length*/; j++) {
    			if((i == 0 && j == 0) || (i == 0 && j == Params.WORLD_HEIGHT+1) || (i == Params.WORLD_WIDTH+1 && j == 0) || (i == Params.WORLD_WIDTH+1 && j == Params.WORLD_HEIGHT+1)) {
    				world.get(i).add(j, "+");
    			}
    			else if(i == 0 || i == Params.WORLD_WIDTH+1){
    				//world[i][j] = "-";
    				world.get(i).add(j, "-");
    			}
    			else if(j == 0 || j == Params.WORLD_HEIGHT+1) {
    				//world[i][j] = "|";
    				world.get(i).add(j, "|");

    			}
    			else {
    				//world[i][j] = " ";
    				world.get(i).add(j, " ");

    			}
    		}
    	}
    	
    	for(Critter crit:population)
    	{
    		//world[crit.y_coord+1][crit.x_coord+1] = crit.toString();		
			world.get(crit.x_coord+1).remove(crit.y_coord+1);
			world.get(crit.x_coord+1).add(crit.y_coord+1, crit.toString());

    	}
    	for(int i = 0; i < Params.WORLD_HEIGHT+2; i++) {
    		for(int j = 0; j < Params.WORLD_WIDTH+2; j++) {
    			System.out.print(world.get(i).get(j));
    		}
    		System.out.println("");
    	}
    }

    /**
     * Prints out how many Critters of each type there are on the
     * board.
     *
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }
    
  //Added encounter methodd
    private static void doEncounters()
    {
    	for(Critter crit1 : population)
    	{
    		if(crit1 == population.get(population.size()-1))
    		{
    			break;
    		}
    		for(Critter crit2 : population)
    		{
    			int index1 = population.indexOf(crit1);
    			int index2 = population.indexOf(crit2);
    			if(index2 <= index1)
    				continue;
    			if((crit1.x_coord == crit2.x_coord) && (crit1.y_coord == crit2.y_coord) && (crit1.energy > 0 && crit2.energy > 0))
    			{
       				boolean crit1Fight = crit1.fight(crit2.toString());
    				boolean crit2Fight = crit2.fight(crit1.toString());
    				//case where one of the critters calls walk() or run() in fight()
    				if((crit1.x_coord != crit2.x_coord) || (crit1.y_coord != crit2.y_coord))
						continue;
    				int crit1Roll = 0;
    				int crit2Roll = 0;
    				//int crit2Roll = 0;
    				//need to roll die if they chose to fight
    				if(crit1Fight)
    					crit1Roll = getRandomInt(crit1.getEnergy());
    				if(crit2Fight)
    					crit2Roll = getRandomInt(crit2.getEnergy());
    				//crit1> crit 2 he wins
    				if(crit1Roll > crit2Roll)
    				{
    					crit1.energy += Math.floor(.5*crit2.energy);
    					crit2.energy = 0;
    				}
    				else if(crit1Roll < crit2Roll)
    				{
    					crit2.energy += Math.floor(.5*crit1.energy);
    					crit1.energy = 0;
    				}    			
    				else
    				{
    					int randomInt = getRandomInt(1);
    					if(randomInt == 0)
    					{
        					crit1.energy += Math.floor(.5*crit2.energy);
        					crit2.energy = 0;
        				}
    					else
    					{
        					crit2.energy += Math.floor(.5*crit1.energy);
        					crit1.energy = 0;
        				}   
    				}    				
    			}
    		}		
    	}
    	clearDeadCritters(); 	
    	return;
    }

    //Added to clear dead critters
    private static void clearDeadCritters()
    {
    	Iterator<Critter> crit = population.iterator();
    	while(crit.hasNext())
    	{
    		Critter currentCrit = crit.next();
    		if(currentCrit.getEnergy() <= 0)
    			crit.remove();
    	}
    }

    protected int getEnergy() {
        return energy;
    }

    protected final void walk(int direction) {
        // TODO: Complete this method
    	if(direction == 8)
    		return;
    	else if(direction < 0)
    		runFromFight(direction);
    	else
    	{
        	energy -= Params.WALK_ENERGY_COST;
        	movement(1, direction);
    	}
    }

    protected final void run(int direction) {
        // TODO: Complete this method
    	energy -= Params.RUN_ENERGY_COST;
    	movement(2, direction);

    }
    //new method to run from a fight
    private void runFromFight(int direction)
    {
    	int prevX = x_coord;
    	int prevY = y_coord;
    	movement(1, -(direction+1));
    	for(Critter crit :population)
    	{
    		//if we move and land on another critter, go to prev
    		//x and y coordinates
    		if(crit.x_coord == x_coord && crit.y_coord == y_coord && crit!= this)
    		{
    			x_coord = prevX;
    			y_coord = prevY;
    			return;
    		}
    	}
    	energy-= Params.WALK_ENERGY_COST;
    }
    private final void movement(int distance, int direction) {
    	if(direction == 0) {
    		x_coord += distance;
    	}
    	else if(direction == 1) {
    		x_coord += distance;
    		y_coord -= distance;
    	}
    	else if(direction == 2) {
    		y_coord -= distance;
    	}
    	else if(direction == 3) {
    		x_coord -= distance;
    		y_coord -= distance;
    	}
    	else if(direction == 4) {
    		x_coord -= distance;
    	}
    	else if(direction == 5) {
    		x_coord -= distance;
    		y_coord += distance;
    	}
    	else if(direction == 6) {
    		y_coord += distance;
    	}
    	else if(direction == 7) {
    		x_coord += distance;
    		y_coord += distance;
    	}
    	
    	if(x_coord < 0) {
    		x_coord = x_coord + Params.WORLD_WIDTH;
    	}
       	if(y_coord < 0) {
    		y_coord = y_coord + Params.WORLD_HEIGHT;
    	}
    	if(x_coord >= Params.WORLD_WIDTH) {
    		x_coord = 0;
    	}
    	if(y_coord >= Params.WORLD_HEIGHT) {
    		y_coord = 0;
    	}
    }

    protected final void reproduce(Critter offspring, int direction) {
        // TODO: Complete this method
    	if(this.energy < Params.MIN_REPRODUCE_ENERGY) {
    		return;
    	}
    	
    	offspring.energy= (int) Math.floor(this.energy / 2);
    	this.energy = (int) Math.ceil(this.energy / 2);
    	offspring.x_coord = this.x_coord;
    	offspring.y_coord = this.y_coord;
    	
    	switch(direction) {
    	case 1:{
    		offspring.movement(1, 1);	
    	}
    	case 2:{
    		offspring.movement(1, 2);
    	}
    	case 3:{
    		offspring.movement(1, 3);
    	}
    	case 4:{
    		offspring.movement(1, 4);
    	}
    	case 5:{
    		offspring.movement(1, 5);
    	}
    	case 6:{
    		offspring.movement(1, 6);
    	}
    	case 7:{
    		offspring.movement(1, 7);
    	}
    	}
    	
    	babies.add(offspring);
    	
    }
    
    

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
