package geometry;


/**
 * A class for face intersections. Contains information such as the faces intersecting and the points of intersection.
 * Useful to find properties of intersection such as bounciness and friction. 
 * @author isaaczachmann
 *
 */
public class FaceIntersection {
	/**
	 * The points at which the faces intersect
	 */
	public Point[] points;
	
	/**
	 * The first face of the intersection
	 */
	public Face face1;
	
	/**
	 * The second face of the intersection
	 */
	public Face face2;
	
	/**
	 * Creates a new face intersection
	 * @param points The points at which the two faces intersect
	 * @param f1 the first face of intersection
	 * @param f2 the second face of intersection
	 */
	public FaceIntersection(Point[] points, Face f1, Face f2){
		this.points = points;
		this.face1 = f1;
		this.face2 = f2;
	}
}
