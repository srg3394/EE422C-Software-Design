/*
 * CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Joshua Urbank
 * jbu234
 * 76000
 * Sathvik Gujja
 * srg3394
 * 76000
 * Slip days used: <0>
 * Summer 2021
 */

package assignment5;

import java.lang.reflect.InvocationTargetException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;



public abstract class Critter {
	

    private int energy = 0;

    private int x_coord;
    private int y_coord;

    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();
    private static Map<String, int[]> critterCoordinates = new HashMap<String, int[]>();
    private static int extraKeyValue = 0;
    private String mapName;

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
    
    

/* START --- NEW FOR PROJECT 5 */
public enum CritterShape {
    CIRCLE,
    SQUARE,
    TRIANGLE,
    DIAMOND,
    STAR
}

/* the default color is white, which I hope makes critters invisible by default
 * If you change the background color of your View component, then update the default
 * color to be the same as you background
 *
 * critters must override at least one of the following three methods, it is not
 * proper for critters to remain invisible in the view
 *
 * If a critter only overrides the outline color, then it will look like a non-filled
 * shape, at least, that's the intent. You can edit these default methods however you
 * need to, but please preserve that intent as you implement them.
 */

/*
public javafx.scene.paint.Color viewColor() {
    return javafx.scene.paint.Color.RED;
}
public javafx.scene.paint.Color viewOutlineColor() {
    return viewColor();
}
public javafx.scene.paint.Color viewFillColor() {
    return viewColor();
}*/

public abstract CritterShape viewShape();

protected final String look(int direction, boolean steps) 
{
	energy -= Params.LOOK_ENERGY_COST;
	int dist;
	if(steps) {
		dist = 2; 
	}
	else {
		dist = 1;
	}
	int lookX = 0;
	int lookY = 0;
	
	switch(direction) {
	case 0:
		lookX = x_coord += dist;
		lookY = y_coord;
		break;
	case 1:
		lookX = x_coord += dist;
		lookY = y_coord -= dist;
		break;
	case 2:
		lookX = x_coord;
		lookY = y_coord -= dist;
		break;
	case 3:
		lookX = x_coord -= dist;
		lookY = y_coord -= dist;
		break;
	case 4:
		lookX = x_coord -= dist;
		lookY = y_coord;
		break;
	case 5:
		lookX = x_coord -= dist;
		lookY = y_coord += dist;
		break;
	case 6:
		lookX = x_coord;
		lookY = y_coord += dist;
		break;
	case 7:
		lookX = x_coord += dist;
		lookY = y_coord += dist;
		break;
	}
	for(Critter c: population) {
		if(c.x_coord == lookX && c.y_coord == lookY) {
			return c.toString();
		}
	}
	
    return null;
}

public static String runStats(List<Critter> critters) {
    String result = "";
    Map<String, Integer> critterCount = new HashMap<String, Integer>();
    
    for(Critter c : critters) {
    	String critter_name = c.toString();
    	Integer count = critterCount.get(critter_name);
    	if(count == null) {
    		critterCount.put(critter_name, 1);
    	}
    	else {
    		critterCount.put(critter_name, count.intValue() + 1);
    	}
    }
    
    result += "There are " + critters.size() + " critters on the board:";
    String start = "";
    for(String s: critterCount.keySet()) {
    	result += start + s + ": " + critterCount.get(s);
    	start = ", ";
    }
   
    return result;
}


public static void displayWorld(Object pane) {
	Main.world.getChildren().clear();
    for(Critter crit : population)
    {
    	float energyIndicator = 1;
    	float energy = Params.START_ENERGY- crit.energy;
    	energy = energy/Params.START_ENERGY;
		float damageIndicator = 1-energy;
		if(damageIndicator == 1)
		{
			damageIndicator = -1;
			energyIndicator = 0;
		}

    	if(crit.viewShape() == Critter.CritterShape.DIAMOND)
    	{
    		Polygon diamond = new Polygon(Main.columnWidth/2, 0.0,
    		    0.0, Main.columnHeight/2,
    		    Main.columnWidth/2,Main.columnHeight,
    		    Main.columnWidth, Main.columnHeight/2);
    		Stop[] stops = new Stop[] { 
         		     new Stop(damageIndicator, Color.WHITE),
      		         new Stop(energyIndicator, Color.BLUE)  
       		      };  
       		      LinearGradient linearGradient = 
       		         new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops); 
       		       
       		      //Setting the linear gradient to the circle and text 
       		      diamond.setFill(linearGradient);
    		Main.world.add(diamond, crit.y_coord, crit.x_coord);

    	}
    	else if(crit.viewShape() == Critter.CritterShape.CIRCLE)
    	{
    		Circle circle = new Circle(Main.columnWidth/2);
    		Stop[] stops = new Stop[] { 
      		     new Stop(damageIndicator, Color.WHITE),
   		         new Stop(energyIndicator, Color.RED)  
    		      };  
    		      LinearGradient linearGradient = 
    		         new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops); 
    		       
    		      //Setting the linear gradient to the circle and text 
    		      circle.setFill(linearGradient);
    	    Main.world.add(circle, crit.y_coord, crit.x_coord);

    	}
    	else if(crit.viewShape() == Critter.CritterShape.TRIANGLE)
    	{
    		Polygon triangle = new Polygon(Main.columnWidth/2, 0.0,
    				0.0, Main.columnHeight,
    				Main.columnWidth, Main.columnHeight);
    		Stop[] stops = new Stop[] { 
         		     new Stop(damageIndicator, Color.WHITE),
      		         new Stop(energyIndicator, Color.GREEN)  
       		      };  
       		      LinearGradient linearGradient = 
       		         new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops); 
       		       
       		      //Setting the linear gradient to the circle and text 
       		      triangle.setFill(linearGradient);
          		Main.world.add(triangle, crit.y_coord, crit.x_coord);

    	}
    	else if(crit.viewShape() == Critter.CritterShape.STAR)
    	{
    		Polygon star = new Polygon(Main.columnWidth/2, 0.0,
    				Main.columnWidth/4, Main.columnHeight/4,
    				0.0, Main.columnHeight/4,
    				Main.columnWidth/4, Main.columnHeight/2,
    				0.0, Main.columnHeight,
    				Main.columnWidth/2, 3*Main.columnHeight/4,
    				Main.columnWidth, Main.columnHeight,
    				3*Main.columnWidth/4, Main.columnHeight/2,
    				Main.columnWidth, Main.columnHeight/4,
    				3*Main.columnWidth/4, Main.columnHeight/4,
    				Main.columnWidth/2, 0.0);
    		Stop[] stops = new Stop[] { 
         		     new Stop(damageIndicator, Color.WHITE),
      		         new Stop(energyIndicator, Color.YELLOW)  
       		      };  
       		      LinearGradient linearGradient = 
       		         new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops); 
       		       
       		      //Setting the linear gradient to the circle and text 
       		      star.setFill(linearGradient);
          		Main.world.add(star, crit.y_coord, crit.x_coord);
    	}
    	else
    	{
    		Rectangle rectangle = new Rectangle(5,5);
    		Stop[] stops = new Stop[] { 
        		     new Stop(damageIndicator, Color.WHITE),
     		         new Stop(energyIndicator, Color.PURPLE)  
      		      };  
      		      LinearGradient linearGradient = 
      		         new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops); 
      		       
      		      //Setting the linear gradient to the circle and text 
      		      rectangle.setFill(linearGradient);
         		Main.world.add(rectangle, crit.y_coord, crit.x_coord);
    	}
    }
   }

/* END --- NEW FOR PROJECT 5
		rest is unchanged from Project 4 */


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

        	//new
        	int[] gridSpot = {newCritter.x_coord,newCritter.y_coord};
        	//newCritter.setMapName(extraKeyValue);
        	critterCoordinates.put(newCritter.mapName + extraKeyValue, gridSpot);
        	extraKeyValue++;
        	
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
    	critterCoordinates.clear();
    	extraKeyValue = 0;
    	ObservableList<Node> childrens = Main.world.getChildren();
    	for(Node node : childrens) {
    	    if(node instanceof Label) 
    	    {
    	        Label label =(Label)(node); // use what you want to remove
    	        Main.world.getChildren().remove(label);
    	    }
    	  } 
    	population.clear();

    }
    

    
    /**
     * worldTimeStep- driver of the step functionality of the code,
     * handles individual doTimeSteps, which implement various combinations
     * of fight, reproduce, walk, run. Handles data structures responsible
     * for holding living critters and babies 
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
    	updateCritterCoordinates();
    	population.addAll(babies);
    	babies.clear();
    }
    /**
     * keep track of where all coordinates are after the world timestep has
     * completed. critterCoordinates used solely for look() funciton
     */
    private static void updateCritterCoordinates()
    {
    	critterCoordinates.clear();
    	for(Critter crit: population)
    	{
    		int[] newLocations = {crit.x_coord, crit.y_coord};
    		critterCoordinates.put(crit.toString() + extraKeyValue, newLocations);
    		extraKeyValue++;
    	}
    }
    /**
     * updateRestEnergy- subtracts reste energy from all critters
     * in population
     */
    private static void updateRestEnergy()
    {
    	for(Critter crit: population)
    		crit.energy -= Params.REST_ENERGY_COST;
    }
    /**
     * Adds energy to all clovers in population through
     * photsynthesis
     */
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
    /**
     * adds clovers to population based on Params.REFRESH_CLOVER_COUNT
     */
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


    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }
    
    protected static List<Critter> getPopulation() {
        return population;
    }
   /**
    * goes though every combination of critters in population to fight
    * as needed. Critter culled and end of both foreach loops
    */
    private static void doEncounters()
    {
    	for(Critter crit1 : population)
    	{
    		int crit1X = crit1.x_coord;
    		int crit1Y = crit1.y_coord;
    		int crit1Energy = crit1.energy;
    		if(crit1 == population.get(population.size()-1))
    		{
    			break;
    		}
    		if(crit1Energy<=0)
    			continue;
    		for(Critter crit2 : population)
    		{
    			if(population.indexOf(crit1) <= population.indexOf(crit2) ||crit2.energy <= 0)
    				continue;
    			else if((crit1X == crit2.x_coord) && (crit1Y == crit2.y_coord))
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
    				if(crit1Fight)//crit1Fight
    					crit1Roll = getRandomInt(crit1.getEnergy());
    				if(crit2Fight)//crit2Fight
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

        					crit1.energy += Math.floor(.5*crit2.energy);
        					crit2.energy = 0;

    				}    				
    			}
    		}		
    	}
    	clearDeadCritters(); 	
    	return;
    }

    /**
     * removes the dead critters from the population arrayList
     */
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
    /**
     * moves a critter in specified direction, takes away walk
     * energy
     * @param direction
     */
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
    /**
     * runs a critter in a specified direction, takes away 
     * run energy
     * @param direction
     */
    protected final void run(int direction) {
        // TODO: Complete this method
    	energy -= Params.RUN_ENERGY_COST;
    	movement(2, direction);

    }
    /**
     * handles cases in which a critter wants to run from a fight,
     * Note that the critter actually WALKS away, hence walk energy 
     * deducted
     * @param direction
     */
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
    /**
     * movement method changes the actual coordinates of a critter
     * Handles torus style world we have, wrapping critters around
     * @param distance
     * @param direction
     */
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

    /**
     * critter will reproduce if it has certain amount of energy,
     * moves in specified direction
     * 
     * @param offspring
     * @param direction
     */
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