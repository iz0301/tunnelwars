package math;

import geometry.Face;
import geometry.Point;

/**
 * A class to define a ray
 * a ray consists of a starting point and a direction in wich it goes forever
 * used with intersections of planes
 * to be used in physics
 * @author Isaac
 *
 */
public class Ray {

	/**
	 * Direction of the ray
	 */
	public Vector direction;

	/**
	 * The initial point of the ray
	 */
	public Point initalPoint;

	/**
	 * Creates a ray with the specified starting point and direction
	 * @param point the initial point
	 * @param dir the direction of the ray as a vector
	 */
	public Ray(Point point, Vector dir){
		this.initalPoint = point;
		this.direction = dir;
	}

	/**
	 * Determines where this ray intersects the specified face
	 * @param face the face to test an intersection with
	 * @return the point of the intersection, null if the face and ray do not intersect
	 */
	public Point intersectsFace(Face face){
		//help: http://www.cs.princeton.edu/courses/archive/fall00/cs426/lectures/raycast/sld017.htm
		//https://www.siggraph.org/education/materials/HyperGraph/raytrace/rtinter0.htm
		//Step 1: face to plane: (equation for plane is ax+by+cz=d where vec (a,b,c) is normal vec
		Vector v1 = new Vector(face.point1, face.point2);
		Vector v2 = new Vector(face.point2, face.point3);
		Vector normal = Vector.corssMultiply(v1, v2);
		float a = normal.xComponent;
		float b = normal.yComponent;
		float c = normal.zComponent;
		float d = a*face.point1.x + b*face.point1.y + c*face.point1.z;
		//Step 2: plug ray (R(t)=Ro+t*Rd (which means x=xo+t*xd) into plane and solve for t
		Vector Rd = direction.getUnitVector();
		float t = (-a*initalPoint.x-b*initalPoint.y-c*initalPoint.z+d)/(a*Rd.xComponent+b*Rd.yComponent+c*Rd.zComponent);
		if(t <= 0){
			return null; //no intersect bc face is behind ray
		}
		//Step 3: now that we have t, solve for x, y, and z
		Point result = new Point(initalPoint.x+t*Rd.xComponent, 
				initalPoint.y+t*Rd.yComponent, 
				initalPoint.z+t*Rd.zComponent);
		//Step 4: make sure intersection is on face
		if(face.isPointOnFace(result)){
			return result;
		}else{
			return null;
		}
	}
	
	/**
	 * Calculates the distance between the specified point and the line that this ray lies on
	 * @param point the point to find the distance to the line
	 * @return the distance between the point and this line
	 */
	public float distanceFromPointToLine(Point point){
		//http://onlinemschool.com/math/library/analytic_geometry/p_line/
		//ignore "/" bc using unit vector
		return (Vector.corssMultiply(new Vector(point, initalPoint), direction.getUnitVector())).getMagnitude();
	}
	
	/**
	 * Tests to see if the line that this Ray lies on intersects the specified face. Useful for collision detection
	 * NOTE: may error if line is tangent to face?
	 * NOTE: This code is similar to the ray intersects face, perhaps simplification can be done?
	 * @param face the face to test intersection of
	 * @return the point at the intersection
	 */
	public Point lineIntersectsFace(Face face){
		//Just like ray, execept we ignore t
		Vector v1 = new Vector(face.point1, face.point2);
		Vector v2 = new Vector(face.point2, face.point3);
		Vector normal = Vector.corssMultiply(v1, v2);
		float a = normal.xComponent;
		float b = normal.yComponent;
		float c = normal.zComponent;
		float d = a*face.point1.x + b*face.point1.y + c*face.point1.z;
		//Step 2: plug ray (R(t)=Ro+t*Rd (wich means x=xo+t*xd) into plane and solve for t
		Vector Rd = direction.getUnitVector();
		float t = (-a*initalPoint.x-b*initalPoint.y-c*initalPoint.z+d)/(a*Rd.xComponent+b*Rd.yComponent+c*Rd.zComponent);
		//Step 3: now that we have t, solve for x, y, and z
		Point result = new Point(initalPoint.x+t*Rd.xComponent, 
				initalPoint.y+t*Rd.yComponent, 
				initalPoint.z+t*Rd.zComponent);
		//Step 4: make sure intersection is on face
		if(face.isPointOnFace(result)){
			return result;
		}else{
			return null;
		}
	}
}
