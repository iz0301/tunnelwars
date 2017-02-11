package physics;

import geometry.Point;

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

				Vector m1in = Vector.multiplyVectorByScalar(new Vector(pB1.position, intersection.body1.position), 1/timeBetween);//average velocity of body 1 into the collision
				Vector m2in = Vector.multiplyVectorByScalar(new Vector(pB2.position, intersection.body2.position), 1/timeBetween);
				//p=mv
				Vector p1in = Vector.multiplyVectorByScalar(m1in, pB1.mass);
				Vector p2in = Vector.multiplyVectorByScalar(m2in, pB2.mass);
				Vector tP = Vector.addVectors(p1in, p2in);//tP = total momentum
				float R = intersection.getAverageRestitution();
				Point poi = intersection.getAverageImpactPoint();
				//Math is hard... I think i found: http://physics.tutorvista.com/momentum/inelastic-collision.html
				Vector v1o = 
						Vector.multiplyVectorByScalar(
								Vector.addVectors(
										Vector.multiplyVectorByScalar(
												Vector.subtractVectors(
														p2in, 
														Vector.multiplyVectorByScalar(m1in, pB2.mass)), 
														R), 
														tP), 
														1/(pB1.mass+pB2.mass));
				Vector v2o = 
						Vector.multiplyVectorByScalar(
								Vector.addVectors(
										Vector.multiplyVectorByScalar(
												Vector.subtractVectors(
														Vector.multiplyVectorByScalar(m1in, pB2.mass), 
														p2in), 
														R), 
														tP), 
														1/(pB1.mass+pB2.mass));
				pB1.applyForce(new Force(Vector.multiplyVectorByScalar(Vector.subtractVectors(v1o, m1in), pB1.mass/timeBetween), timeBetween, poi));
				pB2.applyForce(new Force(Vector.multiplyVectorByScalar(Vector.subtractVectors(v2o, m2in), pB2.mass/timeBetween), timeBetween, poi));//F=ma a=v/t F=mv/t F=vm/t
				
				
				//Do we actually need to calculate this rotation stuff? IDK
				Vector r1in = Vector.multiplyVectorByScalar(new Vector(pB1.rotation, intersection.body1.rotation), 1/timeBetween);//rotation of body 1 into the collision
				Vector r2in = Vector.multiplyVectorByScalar(new Vector(pB2.rotation, intersection.body2.rotation), 1/timeBetween);
				Vector L1in = Vector.elementMultiply(r1in, pB1.momentOfInertia);
				Vector L2in = Vector.elementMultiply(r2in, pB2.momentOfInertia);
				Vector tL = Vector.addVectors(L1in, L2in);
				Vector w1o = 
						Vector.elementDivide(
								Vector.addVectors(
										Vector.multiplyVectorByScalar(
												Vector.subtractVectors(
														L2in, 
														Vector.elementMultiply(r1in, pB2.momentOfInertia)), 
														R), 
														tL), 
														Vector.addVectors(pB1.momentOfInertia, pB2.momentOfInertia));
				Vector w2o = 
						Vector.elementDivide(
								Vector.addVectors(
										Vector.multiplyVectorByScalar(
												Vector.subtractVectors(
														Vector.elementMultiply(r1in, pB2.momentOfInertia),
														L2in), 
														R), 
														tL), 
														Vector.addVectors(pB1.momentOfInertia, pB2.momentOfInertia));
				//THIS ISNT quite RIGHT!!!!:
				//pB1.applyForce(new Force(Vector.elementMultiply(Vector.subtractVectors(w1o, r1in), Vector.multiplyVectorByScalar(pB1.momentOfInertia,1/timeBetween)), timeBetween, poi));
				//pB2.applyForce(new Force(Vector.elementMultiply(Vector.subtractVectors(w2o, r2in), Vector.multiplyVectorByScalar(pB2.momentOfInertia,1/timeBetween)), timeBetween, poi));//F=ma a=v/t F=mv/t F=vm/t

			}
		}
		lastTime = System.currentTimeMillis();
		return timeBetween;
	}
}
