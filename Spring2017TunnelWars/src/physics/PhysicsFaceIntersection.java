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
	
	/**
	 * Creates the PhysicsFaceIntersection from the more generic type FaceIntersection.
	 * If the faces in faceIntersection are already physics faces, then face1 and face2 of the PhysicsFaceIntersection will be these
	 * faces. If the faces in faceIntersection are generic faces and cannot be casted to PhysicsFace, then the PhysicsFaces will be
	 * created from the faces in faceIntersection with default physics properties. 
	 * @param faceIntersection the generic FaceIntersection to create a PhysicsFaceIntersection out of.
	 */
	public PhysicsFaceIntersection(FaceIntersection faceIntersection) {
		
	}
	
	/**
	 * See the constructor. Converts FaceIntersections to PhysicsFaceIntersections. 
	 * @param faceIntersections the array of FaceIntersections to convert to PhysicsFaceIntersections.
	 * @return the array of PhysicsFaceIntersections created forom the FaceIntersections
	 * @see {@link #PhysicsFaceIntersection(FaceIntersection)}
	 */
	public static PhysicsFaceIntersection[] constructPhysicFaces(FaceIntersection[] faceIntersections){
		for()
	}
}