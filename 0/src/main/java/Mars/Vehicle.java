package Mars;

import java.util.ArrayList;
import java.util.Random;

class Vehicle extends Entity{
	public boolean carryingSample;
	
	public Vehicle(Location l){
		super(l);	
		this.carryingSample = false;
	}

	public void act(Field f, Mothership m, ArrayList<Rock> rocksCollected)
	{
		// actCollaborative(f,m,rocksCollected);
		actSimple(f,m,rocksCollected);
	}
	
	
	public void actCollaborative(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		//complete this method
	}

	public void actSimple(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		// if carrying a sample and at the base then drop sample (1)
		if (this.carryingSample && f.isNeighbourTo(this.getLocation(), Mothership.class)) {
			// Drop sample
			System.out.println("At mothership");
			this.carryingSample = false;
			return;
		}
		
		// if carrying a sample and not at the base then travel up gradient (2)
		if (this.carryingSample && !f.isNeighbourTo(this.getLocation(), Mothership.class)) {
			// travel gradient
			System.out.println("Move up gradient");
			this.moveUpGradient(f);
			return;
		}
		
		// if detect a sample then pick sample (3)
		if (f.isNeighbourTo(this.getLocation(), Rock.class)) {
			// Pick up sample
			System.out.println("Pickup Sample");
			pickupRock(f, rocksCollected);
			return;
		}
		
		// if true then move randomly (4)
		if (true) {
			// move randomly
			System.out.println("Move Randomly");
			this.moveRandomly(f);
			return;
		}
	}

	/**
	 * Returns a free agacent slot which has a higher signal strength to the
	 * Mothership.
	 * 
	 * @param f Field
	 * @return A valid Location to moveto whith a higher signal strength.
	 * 	If no location is avalible null is returned.
	 */
	public Location getLocationUpGradient(Field f) {
		ArrayList<Location> freeSpaces = f.getAllfreeAdjacentLocations(this.getLocation());

		for (Location currentSpace : freeSpaces) {
			if (f.getSignalStrength(currentSpace) > f.getSignalStrength(this.getLocation())) {
				return currentSpace;
			}
		}
		return null;
	}

	/**
	 * Moves the robot to an agacent space with higher signal strength.
	 * 
	 * @param f Field
	 */
	public void moveUpGradient(Field f) {
		Location nextLocation = this.getLocationUpGradient(f);
			if (nextLocation != null) {
				this.updateLocation(f, nextLocation);;
			}
	}

	/**
	 * Picks up a rock that is agent to the robot.
	 * 
	 * @param f Field
	 * @param rocksCollected ArrayList of rocks to be removed by the simulation.
	 */
	public void pickupRock(Field f, ArrayList<Rock> rocksCollected) {
		Location rockLoc = f.getNeighbour(this.getLocation(), Rock.class);
		if (rockLoc != null) {
			Rock newRock = new Rock(rockLoc);
			rocksCollected.add(newRock);
		}
		this.carryingSample = true;
	}

	/**
	 * Moves the robot to a random free agacent space.
	 * 
	 * @param f Field
	 */
	public void moveRandomly(Field f) {
		ArrayList<Location> freeSpaces = f.getAllfreeAdjacentLocations(this.getLocation());
		Random rand = new Random();
		int n = rand.nextInt(freeSpaces.size());
		this.updateLocation(f, freeSpaces.get(n));
	}

	/**
	 * Updates the robot's location.
	 * 
	 * @param f Field
	 * @param newLocation The new Location to move the robot to.
	 */
	public void updateLocation(Field f, Location newLocation) {
		f.clearLocation(this.getLocation());
		this.setLocation(newLocation);
		f.place(this, newLocation);
	}
}