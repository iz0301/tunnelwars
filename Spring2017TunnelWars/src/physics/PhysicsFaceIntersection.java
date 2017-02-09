package physics;

import geometry.FaceIntersection;
import geometry.Point;

/**
 * A class for face intersections. Contains information such as the faces intersecting and the points of intersection.
 * Useful to find properties of intersection such as bounciness and friction. Specifically for PhysicsFaces, unlike FaceIntersection 
 * @author isaaczachmann
 *
 */
public class PhysicsFaceIntersection extends FaceIntersection {
	/**
	 * The first face of the intersection
	 */
	public PhysicsFace face1;
	
	/**
	 * The second face of the intersection
	 */
	public PhysicsFace face2;
	
	/**
	 * Creates a new face intersection
	 * @param points The points at which the two faces intersect
	 * @param f1 the first face of intersection
	 * @param f2 the second face of intersection
	 */
	public PhysicsFaceIntersection(Point[] points, PhysicsFace f1, PhysicsFace f2){
		super(points, f1, f2);
		this.face1 = f1;
		this.face2 = f2;
	}
}