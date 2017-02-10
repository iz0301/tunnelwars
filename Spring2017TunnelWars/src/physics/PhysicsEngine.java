package physics;

import java.util.ArrayList;
import java.util.List;

import math.Vector;

/**
 * A class for a physics engine/world 
 * @author Isaac Zachmann
 *
 */
public class PhysicsEngine {

	/**
	 * All the physics bodies in this physics world, except one tick behind actual time.
	 */
	List<PhysicsBody> bodies = new ArrayList<PhysicsBody>();

	/**
	 * All the physics bodies in this physics world
	 */
	private List<PhysicsBody> pastBodies = new ArrayList<PhysicsBody>();

	/**
	 * All the physics bodies in this physics world, except one tick ahead of the actual time.
	 */
	private List<PhysicsBody> futureBodies = new ArrayList<PhysicsBody>();

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
		pastBodies.add(body);
		futureBodies.add(body);
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
			int index = 0;
			for(PhysicsBody body : bodies){
				if (!body.floor){
					//Linear motion
					{
						//F=ma...a = F/m = F*1/m
						Vector new_acceleration = Vector.multiplyVectorByScalar(body.getNetForce(), 1/body.mass); //calc new accel
						Vector acceleration = Vector.multiplyVectorByScalar(  //use average accel
								Vector.addVectors(body.acceleration, new_acceleration), 
								1/2);

						//set for the past
						pastBodies.get(index).acceleration = body.acceleration;
						pastBodies.get(index).linearVelocity = body.linearVelocity;
						pastBodies.get(index).position = body.position;

						//Calculate
						Vector dVel = Vector.multiplyVectorByScalar(acceleration, timeBetween);//dVel for change in velocity
						Vector dPos = Vector.multiplyVectorByScalar(
								acceleration, 
								(float)(.5*Math.pow(timeBetween,2)
										));
						
						//set for present
						body.acceleration = new_acceleration; //set new accel
						body.linearVelocity = Vector.addVectors(body.linearVelocity, dVel);//velocity = oldVelocity + acceleration * time
						body.move(dPos);

						//set for future (assuming no forces change, hence no acceleration change)
						futureBodies.get(index).acceleration = body.acceleration;
						futureBodies.get(index).linearVelocity = body.linearVelocity;
						futureBodies.get(index).position = body.position;
						
							//accleration is constant so not here
						futureBodies.get(index).linearVelocity = Vector.addVectors(body.linearVelocity, dVel);//adding dVel again, that way future is ahead
						futureBodies.get(index).move(dPos);//move again, same amount.
					}



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
				index++;
			}

			for(BodyIntersection intersection : CollisionDetection.stage3(CollisionDetection.stage2(CollisionDetection.stage1(futureBodies)))){
				//APPLY FORCES FROM INTERSECTION
				PhysicsBody pB1 = bodies.get(futureBodies.indexOf(intersection.body1));//pB1 for presentBody1. pB1 and intersection.body1 are the same, but intersection.body1 is one tick ahead
				PhysicsBody pB2 = bodies.get(futureBodies.indexOf(intersection.body2));
				Vector m1in = new Vector(pB1.position, intersection.body1.position);//motion of body 1 into the collision
				Vector m2in = new Vector(pB2.position, intersection.body2.position);
				//p=mv
				Vector p1in = Vector.multiplyVectorByScalar(m1in, pB1.mass);
				Vector p2in = Vector.multiplyVectorByScalar(m2in, pB2.mass);
				Vector tP = Vector.addVectors(p1in, p2in);//tP = total momentum
				float KEin = (.5f*pB1.mass*pB1.linearVelocity.getMagnitude()*pB1.linearVelocity.getMagnitude()) +
						(.5f*pB2.mass*pB2.linearVelocity.getMagnitude()*pB2.linearVelocity.getMagnitude()); 
				float KEout = intersection.getAverageRestitution()*KEin;
				//Math is hard... I think i found that: 
				//0=(m2*m1+m1^2)*v1o^2+(-2pt*m1)*v1o+(pt^2-m2*2*KEout)
				//where v1o is out velocity of object 1, pt is total momentum, m1 and m2 are masses
				//so use quad formula where v1o is x
				float a = pB2.mass*pB1.mass+pB1.mass*pB1.mass;//(m2*m1+m1^2)
				Vector b = Vector.multiplyVectorByScalar(tP, -2*pB1.mass);//(-2pt*m1)
				float c = tP.getMagnitude()*tP.getMagnitude() - pB2.mass*2*KEout;//(pt^2-m2*2*KEout)
				Vector v1o1 = Vector.multiplyVectorByScalar(Vector.addVectors(Vector.multiplyVectorByScalar(b, -1), ,2*a);//(-b+-sqrt(b^2-4ac))/(2a)    two possibilities
				Vector v1o2
				
				
				Vector r1in = new Vector(pB1.rotation, intersection.body1.rotation);//rotation of body 1 into the collision
				Vector r2in = new Vector(pB2.rotation, intersection.body2.rotation);
			}
		}
		lastTime = System.currentTimeMillis();
		return timeBetween;
	}
}
