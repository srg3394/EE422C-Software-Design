package assignment4;

import java.util.ArrayList;
import java.util.List;

/**
 * Wise critter, fight only when it has energy
 */
public class SmartCritter extends Critter {

	private int direction;
	private boolean hasMoved;

	public SmartCritter()
	{
    	direction = Critter.getRandomInt(7);

	}
	/*
	 * this critter to move in some specified direction
	 */
    @Override
    public void doTimeStep() 
    {
    	//change back to 10
    	if(getEnergy()>100)
    	{
    		MyCritter1 baby = new MyCritter1();
    		reproduce(baby, this.direction);
    		hasMoved = false;
    	}
    	else if(getEnergy()> 10)
    	{
    		walk(direction);
        	direction = Critter.getRandomInt(7);
    		hasMoved = true; 
    	}
    	else
    	{
    		//no movement
    		//walk(8);
    		hasMoved = false;
    	}
    }

    @Override
    public boolean fight(String opponent) {
        if (getEnergy() > 10) 
        	return true;
        else if(!hasMoved)
        {
        	walk(-direction-1);
        	return false;
        }
        	
        return false;
    }

    public String toString() {
        return "S";
    }

    public void test(List<Critter> l) {
    }

}
