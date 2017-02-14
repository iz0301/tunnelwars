package physics;

import geometry.FaceIntersection;
import geometry.Point;
import math.Vector;


/**
 * Class for collision detection. Holds information once a collision is verified such as point of collision and bodies colliding.
 * @author Isaac
 *
 */
public class BodyIntersection {
	/**
	 * The points at which the bodies are intersecting
	 */
	public PhysicsFaceIntersection[] intersections;
	
	/**
	 * The first intersecting body
	 */
	public PhysicsBody body1;
	
	/**
	 * The second intersecting body
	 */
	public PhysicsBody body2;
	
	/**
	 * Creates an intersection from a point and two bodies
	 * @param faces the faces at which the bodies are intersecting
	 * @param body1 the first body intersecting
	 * @param body2 the second body intersecting
	 */
	public BodyIntersection(PhysicsFaceIntersection[] intersections, PhysicsBody body1, PhysicsBody body2){
		this.intersections = intersections;
		this.body1 = body1;
		this.body2 = body2;
	}
	
	/**
	 * Calculates and returns the average bounciness of the collision by taking the average restitution of the intersecting faces
	 * @return the restitution of the collision, taking in to account all colliding faces of both bodies
	 */
	public float getAverageRestitution(){
		float totalRestitution = 0;
		int totalFaces = 0;
		for(PhysicsFaceIntersection intersection : intersections){
			totalRestitution += intersection.face1.restitution + intersection.face2.restitution;
			totalFaces += 2;
		}
		return totalRestitution/totalFaces;
	}
	
	/**
	 * Calculates and returns the average point of impact of the collision. This averages out all the points where the two bodies
	 * are intersecting and returns that point.
	 * @return the point of intersection for the two bodies
	 */
	public Point getAverageImpactPoint(){
		Vector totalPoint = Vector.ZERO_VECTOR;
		int numOfPoints = 0;
		for(FaceIntersection intersection : intersections){
			for(Point p : intersection.points){
				totalPoint = Vector.addVectors(totalPoint, p.toVector());
				numOfPoints++;
			}
		}
		return new Point(Point.ORIGIN, Vector.multiplyVectorByScalar(totalPoint, 1f/numOfPoints));
	}
}
