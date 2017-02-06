package physics;

import java.util.ArrayList;

import math.Vector;

/**
 * A class for a physics engine/world 
 * @author Isaac Zachmann
 *
 */
public class PhysicsEngine {

	/**
	 * All the physics bodies in this physics world
	 */
	ArrayList<PhysicsBody> bodies = new ArrayList<PhysicsBody>();

	/**
	 * The time of the last physics update
	 */
	private long lastTime;

	/**
	 * The time multiplier of the physics world.
	 * a higher number will speed time up and make physics less stable and less accurate, 
	 * a lower number will slow time down and make physics more stable and more accurate.
	 */
	float timeMultiplier;

	/**
	 * Creates a new physics engine/world
	 * @param timeDelay the time multiplier for the physics world, 
	 * a higher number will speed time up and make physics less stable and less actuate, 
	 * a lower number will slow time down and make physics more stable and more accurate.
	 */
	public PhysicsEngine(float speed){
		timeMultiplier = speed;
	}

	/**
	 * Adds the specified physics body to this physics world
	 * @param body
	 */
	public void addBody(PhysicsBody body){
		bodies.add(body);
	}

	/**
	 * Updates all physics for this engine. Should be called every update to move all physics bodies, calculate forces, etc.
	 * Note: should probably change from Euler Integration to Velocity Verlet
	 * Note: Perhaps best to run this in a new thread
	 * @return the time, in milliseconds, between this update and the last update
	 */
	public float physicsUpdate(){
		float timeBetween = (float) timeMultiplier*((System.currentTimeMillis() - lastTime)/1000);
		if(timeBetween > 0){
			for(PhysicsBody body : bodies){
				if (!body.floor){
					//F=ma...a = F/m = F*1/m
					Vector new_acceleration = Vector.multiplyVectorByScalar(body.getNetForce(), 1/body.mass); //calc new accel
					Vector acceleration = Vector.multiplyVectorByScalar(  //use average accel
							Vector.addVectors(body.acceleration, new_acceleration), 
							1/2);
					body.acceleration = new_acceleration; //set new accel
					body.linearVelocity = Vector.addVectors(body.linearVelocity, 
							Vector.multiplyVectorByScalar(acceleration, timeBetween));//velocity = oldVelocity + acceleration * time
					body.move(					//position = 1/2*a*t^2
							Vector.multiplyVectorByScalar(
									acceleration, 
									(float)(.5*Math.pow(timeBetween,2)
											)));


					Vector new_angularAccl = Vector.elementDivide(body.getNetTorque(), body.momentOfInertia);
					Vector angularAccl = Vector.multiplyVectorByScalar(Vector.addVectors(body.angularAcceleration, new_angularAccl), 1/2);

					body.angularAcceleration = new_angularAccl;
					body.angularVelocity = Vector.addVectors(body.angularVelocity, Vector.multiplyVectorByScalar(angularAccl, timeBetween));
					body.rotate(Vector.multiplyVectorByScalar(angularAccl, (float)(.5*Math.pow(timeBetween, 2))));

					for(int i = 0; i < body.times.size(); i++){
						if(body.times.get(i) <= 0){
							body.forces.remove(i);
							body.torques.remove(i);
						}
					}
				}
			}

			for(Intersection intersection : CollisionDetection.stage3(CollisionDetection.stage2(CollisionDetection.stage1(bodies)))){
				//APPLY FORCES FROM INTERSECTION
			}
		}
		lastTime = System.currentTimeMillis();
		return timeBetween;
	}
}
