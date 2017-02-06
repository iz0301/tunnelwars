package physics;

import geometry.Point;
import math.Vector;

/**
 * A very simple class to make a force. Force is a vector and a time unit
 * @author Isaac Zachmann
 *
 */
public class Force {
	
	/**
	 * The force vector
	 */
	Vector force;
	
	/**
	 * The time in seconds the force should be applied for
	 */
	float time;
	
	/**
	 * The position relative to the center of the body to apply the force
	 */
	Point position;
	
	/**
	 * Creates a new force 
	 * @param force the vector of the force to be applied
	 * @param time the time, in seconds, for the force to be applied for
	 * @param the position relative to the center of the body for the force to be applied
	 */
	public Force(Vector force, float time, Point position){
		this.force = force;
		this.time = time;
		this.position = position;
	}
}
