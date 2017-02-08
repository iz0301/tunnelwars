package physics;

import geometry.Point;

/**
 * Class for collision detection. Holds information once a collision is verified such as point of collision and bodies colliding.
 * @author Isaac
 *
 */
public class BodyIntersection {
	/**
	 * The points at which the bodies are intersecting
	 */
	public Point[] intersections;
	
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
	public BodyIntersection(PhysicsFaceIntersection[] faces, PhysicsBody body1, PhysicsBody body2){
		intersections = faces;
		this.body1 = body1;
		this.body2 = body2;
	}
}
