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
		actCollaborative(f,m,rocksCollected);
		// actSimple(f,m,rocksCollected);
	}

	/**
	 * 	An additional set of rules is utilised by the collaborative vehicle. These are given below:
	 * 
	 * if carrying a sample and at the base then drop sample (1)
	 * if carrying a sample and not at the base then travel up gradient (2)
 	 * if detect a sample then pick sample (3)
     * if true then move randomly (4)
	 * if carrying a sample and not at the base then drop two crumbs and travel up gradient (5)
	 * if sense crumbs then pick up 1 and travel down gradient (6)
	 * 
	 * The subsumption hierarchy for the collaborative vehicle is as follows:
	 * (1) ≺ (5) ≺ (3) ≺ (6) ≺ (4).
	 * 
	 * @param f Field
	 * @param m Mothership
	 * @param rocksCollected A list of Rocks that are to be removed.
	 */
	public void actCollaborative(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		// if carrying a sample and at the base then drop sample (1)
		if (this.carryingSample && f.isNeighbourTo(this.getLocation(), Mothership.class)) {
			// Drop sample
			this.carryingSample = false;
			return;
		}

		// if carrying a sample and not at the base then drop two crumbs and travel up gradient (5)
		if (this.carryingSample && !f.isNeighbourTo(this.getLocation(), Mothership.class)) {
			// Drop two crumbs
			f.dropCrumbs(this.getLocation(), 2);

			// Travel up gradient
			this.moveUpGradient(f);
			return;
		}

		// if detect a sample then pick sample (3)
		if (f.isNeighbourTo(this.getLocation(), Rock.class)) {
			// Pick up sample
			this.pickupRock(f, rocksCollected);
			return;
		}
		
		// if sense crumbs then pick up 1 and travel down gradient (6)
		if (f.getCrumbQuantityAt(this.getLocation()) > 0) {
			// Pick up crumb
			f.pickUpACrumb(this.getLocation());
			// Travel down gradient
			this.moveDownGradient(f);
		}

		// if true then move randomly (4)
		if (true) {
			// move randomly
			this.moveRandomly(f);
			return;
		}
	}

	/**
	 * Checks preconditions and performs an action.
	 * 
	 * The behaviour of the simple vehicle is represented by the following set of rules.
	 * 
	 * if carrying a sample and at the base then drop sample (1)
	 * if carrying a sample and not at the base then travel up gradient (2)
 	 * if detect a sample then pick sample (3)
     * if true then move randomly (4)
	 * 
	 * These behaviours are arranged into the following subsumption hierarchy:
 	 * (1) ≺ (2) ≺ (3) ≺ (4).
	 * 
	 * @param f Field
	 * @param m Mothership
	 * @param rocksCollected A list of Rocks that are to be removed.
	 */
	public void actSimple(Field f, Mothership m, ArrayList<Rock> rocksCollected){
		// if carrying a sample and at the base then drop sample (1)
		if (this.carryingSample && f.isNeighbourTo(this.getLocation(), Mothership.class)) {
			// Drop sample
			this.carryingSample = false;
			return;
		}
		
		// if carrying a sample and not at the base then travel up gradient (2)
		if (this.carryingSample && !f.isNeighbourTo(this.getLocation(), Mothership.class)) {
			// travel gradient
			this.moveUpGradient(f);
			return;
		}
		
		// if detect a sample then pick sample (3)
		if (f.isNeighbourTo(this.getLocation(), Rock.class)) {
			// Pick up sample
			pickupRock(f, rocksCollected);
			return;
		}
		
		// if true then move randomly (4)
		if (true) {
			// move randomly
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

		int highestSignal = f.getSignalStrength(this.getLocation());
		Location highestSignalLocation = this.getLocation();

		for (Location currentSpace : freeSpaces) {
			if (f.getSignalStrength(currentSpace) > highestSignal) {
				highestSignal = f.getSignalStrength(currentSpace);
				highestSignalLocation = currentSpace;
			}
		}
		return highestSignalLocation;
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
	 * Returns a free agacent slot which has a lower signal strength to the
	 * Mothership.
	 * 
	 * @param f Field
	 * @return A valid Location to moveto whith a higher signal strength.
	 */
	public Location getLocationDownGradient(Field f) {
		ArrayList<Location> freeSpaces = f.getAllfreeAdjacentLocations(this.getLocation());

		int lowestSignal = f.getSignalStrength(this.getLocation());
		Location lowestSignalLocation = this.getLocation();

		for (Location currentSpace : freeSpaces) {
			if (f.getSignalStrength(currentSpace) < lowestSignal) {
				lowestSignal = f.getSignalStrength(currentSpace);
				lowestSignalLocation = currentSpace;
			}
		}
		return lowestSignalLocation;
	}

	/**
	 * Moves the robot to an agacent space with lower signal strength.
	 * 
	 * @param f Field
	 */
	public void moveDownGradient(Field f) {
		Location nextLocation = this.getLocationDownGradient(f);
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