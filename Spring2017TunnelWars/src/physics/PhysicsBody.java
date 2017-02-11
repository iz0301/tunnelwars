package physics;

import java.util.ArrayList;

import geometry.Face;
import geometry.ObjectImporter;
import geometry.Point;
import math.Ray;
import math.Vector;

/**
 * Defines a class for creating 3D rigid structures that will physically interact.
 * Currently only works for completely symmetrical non-jointed structures
 * @author Isaac Zachmann
 *
 */
public class PhysicsBody {
	/**
	 * The number that defines how accurate the center of mass is for physics bodies. 
	 * A higher the number, the more accurate center of mass.
	 */
	public static final int CENTROID_ACCURACY = 100000;

	/**
	 * All the faces that make up this physics body
	 */
	ArrayList<PhysicsFace> faces = new ArrayList<PhysicsFace>();

	/**
	 * An array list to store the remaining time for each torque/force
	 */
	ArrayList<Float> times = new ArrayList<Float>();
	
	/**
	 * All the forces acting upon this body
	 */
	ArrayList<Vector> forces = new ArrayList<Vector>();

	/**
	 * All the torques acting upon this body
	 */
	ArrayList<Vector> torques = new ArrayList<Vector>();
	
	/**
	 * The moment of inertia around the local x, y, and z axes in kg*m^2
	 * Calculated when getMomentOfInertia() is called
	 * NOTE: When doing math with momentOfInerta, you will usually want to use element by element multiplication.
	 */
	public Vector momentOfInertia;

	/**
	 * The mass in kilograms of the physics body
	 */
	public float mass;

	/**
	 * The density in kilograms/m^3 of the physics body
	 */
	public float density = 10;

	/**
	 * Defines if this physics body is a "floor" if it is it will not be affected by gravity or move due to other forces
	 */
	public boolean floor;

	/**
	 * The current position of the (center of mass?) of the body relative to the origin
	 */
	public Point position;

	/**
	 * The current orientation of the physics body relative to the origin axes. Represented as a point because somehow???
	 */
	public Point rotation;

	/**
	 * The current linear velocity of the physics body
	 */
	Vector linearVelocity = Vector.ZERO_VECTOR;

	/**
	 * The current angular velocity of the physics body
	 */
	Vector angularVelocity = Vector.ZERO_VECTOR;

	/**
	 * The acceleration of the physics body. Updated every engine update.
	 */
	Vector acceleration = Vector.ZERO_VECTOR;

	/**
	 * The angular acceleration of the physics body. Updated every engine update.
	 */
	Vector angularAcceleration = Vector.ZERO_VECTOR;

	/**
	 * The center of mass of this object. All objects have uniform density. This should be equal to the position.?
	 **/
	public Point centroid;

	/**
	 * Creates a physics body using the specified faces
	 * @param faces All the faces to add to the physics body
	 */
	public PhysicsBody(PhysicsFace faces[]){
		for(PhysicsFace face : faces){
			this.faces.add(face);
		}
		mass = 0;
		density = 0;
		recalculateCentroidAndMomentOfInertia();		
		floor = false;
	}

	/**
	 * Creates a physics body from the .obj file loaded from ObjectImporter
	 * @param object the object to create a physics body from
	 */
	public PhysicsBody(ObjectImporter object){
		this(PhysicsFace.generateFromFaces(object.faces.toArray(new Face[object.faces.size()])));
	}

	/**
	 * Applies a constant force to the of mass of the body. Force is then translated
	 * into linear and angular motion (Torques and Forces).
	 * @param newForce the force (in newtons) to be applied to the body
	 */
	public void applyForce(Force newForce){
		forces.add(newForce.force);
		//T=R x F (wikipidia)
		torques.add(Vector.corssMultiply(new Vector(centroid, newForce.position), newForce.force));//center to vector in local coords
		times.add(newForce.time);
	}

	/**
	 * Sets the velocity in m/s (meters per second) of the physics body to the velocity represented by the vector.
	 * @param vel The vector that represents the velocity
	 */
	public void setLinearVelocity(Vector vel){
		linearVelocity = vel;
	}

	/**
	 * Sets the angular velocity in rad/s (radians per second) of the physics body.
	 * @param vel The vector that represent angular velocity. 
	 */
	public void setAngularVelocity(Vector vel){
		angularVelocity = vel;
	}

	/**
	 * Sets the position of the center of mass of the physics body
	 * @param pos The new middle position
	 */
	public void setPosition(Point pos){
		move(new Vector(centroid, pos));
	}

	/**
	 * Moves the body by the specified vector direction and magnitude
	 * @param change the vector to move the body
	 */
	public void move(Vector change){
		position = new Point(position, change);
		centroid = new Point(centroid, change);
		for(PhysicsFace f : faces){
			for(Point p : f.getPoints()){
				p = new Point(p, change);
			}
		}
	}

	/**
	 * Rotates the body by the specified amount in rad around each axis
	 * @param change
	 */
	public void rotate(Vector change){
		rotation = new Point(rotation, change);
		for(PhysicsFace f : faces){
			for(Point p : f.getPoints()){
				//Rotate around z, then x, then y axis: https://www.siggraph.org/education/materials/HyperGraph/modeling/mod_tran/3drota.htm
				p.x = (float) (p.x*Math.cos(change.getZ())-p.y*Math.sin(change.getZ()));
				p.y = (float) (p.x*Math.sin(change.getZ())-p.y*Math.cos(change.getZ()));
				p.z = p.z;
				
				p.y = (float) (p.y*Math.cos(change.getX())-p.z*Math.sin(change.getX()));
				p.z = (float) (p.y*Math.sin(change.getX())+p.z*Math.cos(change.getX()));
				p.x = p.x;
				
				p.z = (float) (p.z*Math.cos(change.getY())-p.x*Math.sin(change.getY()));
				p.x = (float) (p.x*Math.sin(change.getY())+p.x*Math.cos(change.getY()));
				p.y = p.y;
			}
		}
	}

	/**
	 * Determines if the point is on the inside of the physics body
	 * @param point the point to test
	 * @return true if the point is on the inside of the body, false if otherwise
	 */
	public boolean isPointInBody(Point point){
		int numberOfFaceIntersections = 0;
		Ray testRay = new Ray(point, new Vector(1,0,0));
		for(PhysicsFace face : faces){
			if(testRay.intersectsFace(face) != null){ //Direction dont matter, can be any direction for a closed object
				numberOfFaceIntersections++;
			}
		}
		if(numberOfFaceIntersections % 2 == 1){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Finds and returns the total net force on the body
	 * @return the net force acting upon the physics body
	 */
	public Vector getNetForce(){
		Vector totalForce = Vector.ZERO_VECTOR;
		for(Vector force : forces){
			totalForce = Vector.addVectors(force, totalForce);
		}
		return totalForce;
	}

	/**
	 * Finds and returns the total net torque on the body
	 * @return the net torque acting upon the physics body
	 */
	public Vector getNetTorque(){
		Vector totalTorque = Vector.ZERO_VECTOR;
		for(Vector torque : torques){
			totalTorque = Vector.addVectors(torque, totalTorque);
		}
		return totalTorque;
	}
	
	/**
	 * Recalculates center of mass and moments of inertia for this physics body. Do this after changing mass, or density.
	 */
	public void recalculateCentroidAndMomentOfInertia(){
		float maxX = 0, maxY = 0, maxZ = 0, minX = 0, minY = 0, minZ = 0;
		for(PhysicsFace face : this.faces){
			for(Point point : face.getPoints()){
				if(point.x < minX){
					minX = point.x;
				} else if (point.x > maxX){
					maxX = point.x;
				}

				if(point.y < minY){
					minY = point.y;
				} else if (point.y > maxY){
					maxY = point.y;
				}

				if(point.z < minZ){
					minZ = point.z;
				} else if (point.z > maxZ){
					maxZ = point.z;
				}
			}
		}


		//Set default mass
		if(density == 0){
			density = 10;
		}
		if(mass == 0){
			mass = density*(maxX-minX)*(maxY-minY)*(maxZ-minZ);
		}
		//calc centroid;
		ArrayList<Point> inBody = new ArrayList<Point>();
		for(int i = 0; i < CENTROID_ACCURACY; i++){
			Point randomPoint = new Point(
					(float)(minX+Math.random()*(maxX-minX)),
					(float)(minY+Math.random()*(maxY-minY)),
					(float)(minZ+Math.random()*(maxZ-minZ)));
			while (!isPointInBody(randomPoint)) {
				randomPoint = new Point(
						(float)(minX+Math.random()*(maxX-minX)),
						(float)(minY+Math.random()*(maxY-minY)),
						(float)(minZ+Math.random()*(maxZ-minZ)));
			}
			inBody.add(randomPoint);
		}
		float totalX = 0, totalY = 0, totalZ = 0;
		for(Point p : inBody){
			totalX += p.x;
			totalY += p.y;
			totalZ += p.z;
		}
		centroid = new Point(totalX/CENTROID_ACCURACY, 
				totalY/CENTROID_ACCURACY, 
				totalZ/CENTROID_ACCURACY);
		//position = centroid;
		//calc Moment of inertias for x, y, and z axis
		float massOfOnePoint = mass/CENTROID_ACCURACY;
		Ray xAxis = new Ray(centroid, new Vector(1,0,0));
		Ray yAxis = new Ray(centroid, new Vector(0,1,0));
		Ray zAxis = new Ray(centroid, new Vector(0,0,1));
		for(Point p : inBody){//mr^2
			momentOfInertia = Vector.addVectors(momentOfInertia, new Vector(
					(float)(massOfOnePoint*Math.pow(xAxis.distanceFromPointToLine(p), 2)),
					(float)(massOfOnePoint*Math.pow(yAxis.distanceFromPointToLine(p), 2)),
					(float)(massOfOnePoint*Math.pow(zAxis.distanceFromPointToLine(p), 2))));
		}
	}

	//no longer needed: calculate center of mass in constructor
	/*/**
	 * Should calculate and return the center of mass if the object has consistent density, but doesn't.
	 * Should work for symmetrical objects (cubes)
	 * Note: Probably doesn't work to well because all this does is takes the average of the x's, y's and z's. 
	 * We should fix this using TRIPLE INTEGRATION!
	 * @return The center of mass
	 *//*
	public Point getCenterOfMass(){
		float xTotal = 0, 
				yTotal = 0,
				zTotal = 0;
		int totalPoints = 0;
		for(PhysicsFace face : faces){
			for(Point point : face.getPoints()){
				totalPoints++;
				xTotal += point.x;
				yTotal += point.y;
				zTotal += point.z;
			}
		}
		return new Point(xTotal/totalPoints, yTotal/totalPoints, zTotal/totalPoints);
	}*/

	/**
	 * Gets the middle point of the physics body. Calculated by the middle of the largest and smallest coordinates.
	 * @return The middle of this physics body
	 * @deprecated should do all calculations on center of mass instead
	 */
	public Point getMiddlePoint(){
		float maxX = 0, maxY = 0, maxZ = 0, minX = 0, minY = 0, minZ = 0;
		for(PhysicsFace face : this.faces){
			for(Point point : face.getPoints()){
				if(point.x < minX){
					minX = point.x;
				} else if (point.x > maxX){
					maxX = point.x;
				}

				if(point.y < minY){
					minY = point.y;
				} else if (point.y > maxY){
					maxY = point.y;
				}

				if(point.z < minZ){
					minZ = point.z;
				} else if (point.z > maxZ){
					maxZ = point.z;
				}
			}
		}
		return Point.midpoint(new Point(maxX, maxY, maxZ), new Point(minX, minY, minZ));
	}
}
