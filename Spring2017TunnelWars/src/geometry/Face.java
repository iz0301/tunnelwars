package geometry;

import math.Ray;
import math.Vector;

/**
 * Not really sure if this is right, but this class makes a triangular face based on three points
 * @author Isaac
 *
 */
public class Face {
	/**
	 * The first point
	 */
	public Point point1;

	/**
	 * The second point
	 */
	public Point point2;

	/**
	 * The third point
	 */
	public Point point3;

	/**
	 * Creates a Face from three points
	 * @param p1 the first point
	 * @param p2 the second point
	 * @param p3 the third point
	 */
	public Face(Point p1, Point p2, Point p3){
		point1 = p1;
		point2 = p2;
		point3 = p3;
	}

	/**
	 * Creates a face from an array of three points
	 * @param points an array of three points
	 */
	public Face(Point[] points){
		point1 = points[0];
		point2 = points[1];
		point3 = points[2];
	}

	/**
	 * Returns the points of the face
	 * @return the three points that make the face
	 */
	public Point[] getPoints(){
		return new Point[]{point1, point2, point3};
	}

	/**
	 * Tests to see if the specified point is on this face
	 * If the point is on the edge of the face or vertex, it still counts
	 * @param point The point to test 
	 * @return true if the point is on the face and false if it's not. Really it is not that complicated
	 */
	public boolean isPointOnFace(Point point) {
		//Step one
		Vector side1 = new Vector(point1, point2);
		Vector side2 = new Vector(point1, point3);
		Vector pointToPoint = new Vector(point1, point);
		//Step two
		float dot1to1 = Vector.dotMultiply(side1, side1);
		float dot1to2 = Vector.dotMultiply(side1, side2);
		float dot1toPoint = Vector.dotMultiply(side1, pointToPoint);
		float dot2to2 = Vector.dotMultiply(side2, side2);
		float dot2toPoint = Vector.dotMultiply(side2, pointToPoint);
		//Step 3 (compute)
		float denominator = dot1to1 * dot2to2 - dot1to2 * dot1to2;
		float u = (dot2to2 * dot1toPoint - dot1to2 * dot2toPoint)/denominator;
		float v = (dot1to1 * dot2toPoint - dot1to2 * dot1toPoint)/denominator;
		return (v >= 0) && (u >= 0) && (u + v <= 1);
	}

	/**
	 * Tests to see if the two specified faces intersect and returns the points at which they intersect.
	 * It returns points where the edge of one face intersects the face, edge, or vertex of the other.
	 * Does NOT return the line on which the intersection occurs, but this line could be calculated if two points are returned.
	 * @param face1 the first face to test
	 * @param face2 the second face to test
	 * @return an array of Points where the faces intersect, null if the faces do not intersect
	 */
	public static Point[] doFacesIntersect(Face face1, Face face2){
		//Point[] result = new Point[3];
		Ray side1a = new Ray(face1.point1, new Vector(face1.point1, face1.point2));
		Ray side2a = new Ray(face1.point2, new Vector(face1.point2, face1.point3));
		Ray side3a = new Ray(face1.point3, new Vector(face1.point3, face1.point1));

		Ray side1b = new Ray(face2.point1, new Vector(face2.point1, face2.point2));
		Ray side2b = new Ray(face2.point2, new Vector(face2.point2, face2.point3));
		Ray side3b = new Ray(face2.point3, new Vector(face2.point3, face2.point1));

		Point[] intersections = new Point[0];
		Point testPoint = null;
		if((testPoint = side1a.intersectsFace(face2)) != null ){	
			if(Point.isPointBetween(face1.point1, face1.point2, testPoint)){
				//Probably better way to add to end of array
				Point[] old = intersections;
				intersections = new Point[intersections.length + 1];
				for(int i = 0; i < old.length; i++){
					intersections[i] = old[i];
				}
				intersections[intersections.length - 1] = testPoint;
			}
		}
		if((testPoint = side2a.intersectsFace(face2)) != null ){
			if(Point.isPointBetween(face1.point2, face1.point3, testPoint)){
				Point[] old = intersections;
				intersections = new Point[intersections.length + 1];
				for(int i = 0; i < old.length; i++){
					intersections[i] = old[i];
				}
				intersections[intersections.length - 1] = testPoint;
			}
		}
		if((testPoint = side3a.intersectsFace(face2)) != null ){
			if(Point.isPointBetween(face1.point3, face1.point1, testPoint)){
				Point[] old = intersections;
				intersections = new Point[intersections.length + 1];
				for(int i = 0; i < old.length; i++){
					intersections[i] = old[i];
				}
				intersections[intersections.length - 1] = testPoint;			
			}
		}

		if((testPoint = side1b.intersectsFace(face1)) != null ){
			if(Point.isPointBetween(face2.point1, face2.point2, testPoint)){
				Point[] old = intersections;
				intersections = new Point[intersections.length + 1];
				for(int i = 0; i < old.length; i++){
					intersections[i] = old[i];
				}
				intersections[intersections.length - 1] = testPoint;
			}
		}
		if((testPoint = side2b.intersectsFace(face1)) != null ){
			if(Point.isPointBetween(face2.point2, face2.point3, testPoint)){
				Point[] old = intersections;
				intersections = new Point[intersections.length + 1];
				for(int i = 0; i < old.length; i++){
					intersections[i] = old[i];
				}
				intersections[intersections.length - 1] = testPoint;
			}
		}
		if((testPoint = side3b.intersectsFace(face1)) != null ){
			if(Point.isPointBetween(face2.point3, face2.point1, testPoint)){
				Point[] old = intersections;
				intersections = new Point[intersections.length + 1];
				for(int i = 0; i < old.length; i++){
					intersections[i] = old[i];
				}
				intersections[intersections.length - 1] = testPoint;
			}
		}
		if(intersections.length > 0){
			//Remove repeats... Also probably a lot better way to do this. ArrayLists would be easier but should run a speed comparison b4 implementing
			for(int i = 0; i < intersections.length; i++){
				for(int i2 = 0; i2 < intersections.length; i2++){
					if(((Math.abs(intersections[i].x - intersections[i2].x))<Point.ERROR) && 
							(Math.abs(intersections[i].y - intersections[i2].y)<Point.ERROR) && 
							(Math.abs(intersections[i].z - intersections[i2].z)<Point.ERROR) && 
							i != i2){
						Point[] old = intersections;
						intersections = new Point[intersections.length-1];
						int oc = 0;
						int nc = 0;
						while(oc < old.length){
							if(oc == i2){
								oc++;
							} else {
								intersections[nc] = old[oc];
								oc++;
								nc++;
							}
						}
					}
				}
			}
			return intersections;
		} else {
			return null;
		}
	}
}
