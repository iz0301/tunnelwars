package geometry;

import java.util.ArrayList;

import math.Vector;

/**
 * A class for simple 3D points and point math
 * @author Isaac Zachmann
 *
 */
public class Point {
	
	/**
	 * Just a point at the origin, (0,0,0)
	 */
	public static final Point ORIGIN = new Point(0,0,0);
	
	/**
	 * The x-coordinate of the point
	 */
	public float x;

	/**
	 * The y-coordinate of the point
	 */
	public float y;

	/**
	 * The z-coordinate of the point
	 */
	public float z;

	/**
	 * Creates a point at the specified coordinates
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param z the z-coordinate
	 */
	public Point(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a new point at the end of the vector. The initial point argument is for the start of the vector.
	 * @param point the start point
	 * @param vec the vector to go from the start point.
	 */
	public Point(Point point, Vector vec){
		x = point.x + vec.getX();
		y = point.y + vec.getY();
		z = point.z + vec.getZ();
	}

	/**
	 * Finds and returns the midpoint between two points
	 * @param pointA the first point 
	 * @param pointB the second point
	 * @return the midpoint
	 */
	public static Point midpoint(Point pointA, Point pointB){
		return new Point((pointA.x+pointB.x)/2, (pointA.y+pointB.y)/2, (pointA.z+pointB.z)/2);
	}

	/**
	 * Finds and returns the distance between two points
	 * @param pointA the first point
	 * @param pointB the second point
	 * @return the distance
	 */
	public static double distance(Point pointA, Point pointB){
		return Math.sqrt(
				Math.pow(pointA.x-pointB.x, 2)+
				Math.pow(pointA.y-pointB.y, 2)+
				Math.pow(pointA.z-pointB.z, 2));
	}

	/**
	 * Returns the point array list as a float buffer: an array of floats
	 * where every 3 items is one point
	 * Effectively the opposite of floatBufferToPoints.
	 * @param pointArrayList the array list 
	 * @return
	 */
	public static float[] pointFloatBuffer(ArrayList<Point> pointArrayList) {
		ArrayList<Float> arrayVerticiesF = new ArrayList<Float>();
		for (int i = 0; i < pointArrayList.size(); i++) {
			arrayVerticiesF.add(pointArrayList.get(i).x);
			arrayVerticiesF.add(pointArrayList.get(i).y);
			arrayVerticiesF.add(pointArrayList.get(i).z);
		}
		Float[] verticiesF = arrayVerticiesF.toArray(new Float[arrayVerticiesF.size()]);
		float[] lowerCaseFloat = new float[verticiesF.length];
		for(int i = 0; i < verticiesF.length; i++){
			lowerCaseFloat[i] = verticiesF[i];
		}
		return lowerCaseFloat;
	}

	/**
	 * Takes the array of floats where every 3 values is 1 point and converts it to an array of points.
	 * Effectively the opposite of pointFloatBuffer.
	 * @param floatPointValues the array of floats to convert to points where every 3 floats is 1 point
	 * @return The array of points converted from the float array
	 */
	public static Point[] floatBufferToPoints(float[] floatPointValues){
		Point[] points = new Point[floatPointValues.length/3];
		for(int i = 0; i < floatPointValues.length; i += 3){
			points[i/3] = new Point(floatPointValues[i], floatPointValues[i+1], floatPointValues[i+2]);
		}
		return points;
	}

	@Override
	public String toString(){
		return "("+Float.toString(x)+", "+Float.toString(y)+", "+Float.toString(z)+")";
	}

	@Override
	/**
	 * Returns true if this point is at the same location as Point o
	 */
	public boolean equals(Object o) {
		if(((Point)(o)).x == this.x &&
				((Point)(o)).y == this.y &&
				((Point)(o)).z == this.z){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Creates a vector with an x, y, and z component the same as the x, y, and z components of this point.
	 * Useful for doing math with points.
	 * Another way to think about it is creating a vector from the origin to this point.
	 * @return the vector with the same x, y, z of this point
	 */
	public Vector toVector(){
		return new Vector(x, y, z);
	}
	
	/**
	 * Fraction of error to assume a point is still collinear with the other two in the isPointBetween function.
	 */
	private static float ERROR = 0.000001f;

	/**
	 * Calculates if p3 is collinear and lies on the segment created by p1 and p2. If it is, return true, if not return false.
	 * Returns true if testPoint is the same as either p1 or p2.
	 * NOTE: Rounding problems made it so that instead of testing exactly it will still return true even if the point is a little bit off
	 * this error amount of error is percent off for each of the components of vectors generated. 
	 * NOTE: new way of doing face intersections may not need this rounding.
	 * @param p1 The first point of the line segment
	 * @param p2 The second point of the line segment
	 * @param testPoint to point to test if it lies on the line segment
	 * @return True if testPoint is between p1 and p2, false if otherwise.
	 */
	public static boolean isPointBetween(Point p1, Point p2, Point testPoint){
		Vector segment = new Vector(p1, p2);
		Vector test = new Vector(p1, testPoint);
		Vector segmentAtLength = Vector.multiplyVectorByScalar(segment, test.getMagnitude()/segment.getMagnitude());/*The reason for this: originally looked 
				at unit vectors of both segment and test to check if they are the same direction, BUT ran into rounding errors when test 
				was significantly small, this caused the rounding errors when expanding it to unit vector size. Instead, shrink the segment 
				vector to test vector size. This should work because segment vector will always be longer than test vector if the point 
				is between. <--- Also that didn't fix it completely because there are still small rounding errors so this ERROR thing is a work
				around that would be good to fix*/
		if ((Math.abs(segmentAtLength.getX()-test.getX())<ERROR) &&
				(Math.abs(segmentAtLength.getY()-test.getY())<ERROR) &&
				(Math.abs(segmentAtLength.getZ()-test.getZ())<ERROR) &&
				(test.getMagnitude() <= segment.getMagnitude())){
			return true;
		} else if (p1.equals(testPoint) || p2.equals(testPoint)){
			return true;
		} else {
			return false;
		}
	}

}
