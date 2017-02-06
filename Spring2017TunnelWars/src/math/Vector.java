package math;

import geometry.Point;

/**
 * A class for Vectors and vector math
 * @author Isaac Zachmann
 *
 */
public class Vector {
	
	/**
	 * Defines a vector with zero as all components
	 */
	public static final Vector ZERO_VECTOR = new Vector(0,0,0);
	
	/**
	 * The x-component of the vector
	 */
	float xComponent;
	
	/**
	 * The y-component of the vector
	 */
	float yComponent;
	
	/**
	 * The z-component of the vector
	 */
	float zComponent;
	
	/**
	 * Creates a vector with the specified components.
	 * @param x the x-component of the vector
	 * @param y the y-component of the vector
	 * @param z the z-component of the vector
	 */
	public Vector(float x, float y, float z){
		xComponent = x;
		yComponent = y;
		zComponent = z;
	}
	
	/**
	 * Creates a vector going from pointA to pointB.
	 * Note: Vectors do not actually have location, so this vector will have the direction and magnitude, but not the location
	 * @param pointA The start position of the vector
	 * @param pointB The ending position of the vector
	 */
	public Vector(Point pointA, Point pointB){
		xComponent = pointB.x - pointA.x;
		yComponent = pointB.y - pointA.y;
		zComponent = pointB.z - pointA.z;
	}
	
	/**
	 * Creates a vector with a direction identical to dir, but with the specified magnitude.
	 * @param dir the direction vector of the new vector
	 * @param magnitude the magnitude of the new vector
	 */
	public Vector(Vector dir, float magnitude){
		 xComponent = dir.xComponent * (magnitude/dir.getMagnitude());
		 yComponent = dir.yComponent * (magnitude/dir.getMagnitude());
		 zComponent = dir.zComponent * (magnitude/dir.getMagnitude());
	}
	
	/**
	 * Gets the magnitude of the vector.
	 * @return the magnitude of the vector
	 */
	public float getMagnitude() {
		//sqrt(x^2 + y^2 + z^2) distance formula
		return (float) Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2) + Math.pow(zComponent, 2));
	}
	
	/**
	 * Returns the x-component of the vector
	 * @return the x-component
	 */
	public float getX(){
		return xComponent;
	}
	
	/**
	 * Returns the y-component of the vector
	 * @return the y-component
	 */
	public float getY(){
		return yComponent;
	}

	/**
	 * Returns the z-component of the vector
	 * @return the z-component
	 */
	public float getZ(){
		return zComponent;
	}
	
	/**
	 * Returns a new vector with the specified components.
	 * @param x the x-component of the vector
	 * @param y the y-component of the vector
	 * @param z the z-component of the vector
	 * @return a new Vector
	 * @deprecated use vector constructor instead
	 */
	@Deprecated
	public static Vector makeVector(float x, float y, float z){
		return new Vector(x, y, z);
	}

	/**
	 * Adds the two specified vectors and returns the result.
	 * @param vecA the fist vector to add
	 * @param vecB the second vector to add
	 * @return the result vector (vecA+vecB)
	 */
	public static Vector addVectors(Vector vecA, Vector vecB){
		return new Vector(vecA.xComponent+vecB.xComponent, vecA.yComponent+vecB.yComponent, vecA.zComponent+vecB.zComponent);
	}

	/**
	 * Subtracts vecB from vecA and returns the result.
	 * @param vecA the minuend vector
	 * @param vecB the subtrahend vector
	 * @return the result vector (vecA-vecB)
	 */
	public static Vector subtractVectors(Vector vecA, Vector vecB){
		return new Vector(vecA.xComponent-vecB.xComponent, vecA.yComponent-vecB.yComponent, vecA.zComponent-vecB.zComponent);
	}

	/**
	 * Multiplies vecA by scalar and returns the outcome.
	 * @param vecA the vector to be multiplied by scalar
	 * @param scalar the quantity to multiply vecA by
	 * @return the result vector (vecA*scalar)
	 */
	public static Vector multiplyVectorByScalar(Vector vecA, float scalar){
		return new Vector(vecA.xComponent*scalar, vecA.yComponent*scalar, vecA.zComponent*scalar);
	}
	
	/**
	 * Element multiplies (vector product) vecA by vecB and returns the product. Elemental multiplication simply
	 * multiplies each element by the corresponding element in the other vector. It multiplies both the first elements
	 * to get the resulting first element, then does thing same thing for the other two elements.
	 * @param vecA The first vector to multiply elements of
	 * @param vecB The second vector to multiply elements of
	 * @return the resulting vector (vecA.x*vecB.x, vecA.y*vecB.y, vecA.z*vecB.z)
	 */
	public static Vector elementMultiply(Vector vecA, Vector vecB){
		return new Vector(vecA.xComponent*vecB.xComponent, vecA.yComponent*vecB.yComponent, vecA.zComponent*vecB.zComponent);
	}

	/**
	 * Element divides (vector out) vecA by vecB and returns the quotient. Elemental division simply
	 * divides each element by the corresponding element in the other vector. It divides both the first elements
	 * to get the resulting first element, then does thing same thing for the other two elements.
	 * @param vecA The first vector to divide elements of
	 * @param vecB The second vector to divide elements of
	 * @return the resulting vector (vecA.x/vecB.x, vecA.y/vecB.y, vecA.z/vecB.z)
	 */
	public static Vector elementDivide(Vector vecA, Vector vecB){
		return new Vector(vecA.xComponent/vecB.xComponent, vecA.yComponent/vecB.yComponent, vecA.zComponent/vecB.zComponent);
	}
	
	/**
	 * Cross multiplies (vector product) vecA by vecB and returns the product. Cross vector multiplication returns the
	 * vector perpendicular to the plane created by the two specified vectors with a magnitude of the 
	 * area of the parallelogram created by the two vectors. Look up cross product multiplication for more info.
	 * @param vecA the first vector to multiply
	 * @param vecB the second vector to multiply
	 * @return the result vector (vecA x vecB)
	 */
	public static Vector corssMultiply(Vector vecA, Vector vecB){
		/* C = A x B
		 * Cx = AyBz - AzBy 
		 * Cy = AzBx - AxBz 
		 * Cz = AxBy - AyBx
		 */
		return new Vector(
				vecA.yComponent*vecB.zComponent - vecA.zComponent*vecB.yComponent, 
				vecA.zComponent*vecB.xComponent - vecA.xComponent*vecB.zComponent,
				vecA.xComponent*vecB.yComponent - vecA.yComponent*vecB.xComponent);
	}
	
	/**
	 * Dot multiplies (scalar product) vecA by vecB and returns the product. Dot vector multiplication returns a scalar
	 * quantity. You can look up dot product multiplication for more info. 
	 * @param vecA the first vector to multiply
	 * @param vecB the second vector to multiply
	 * @return the result scalar (vecA * vecB)
	 */
	public static float dotMultiply(Vector vecA, Vector vecB){
		//a*b = ax * bx + ay * by + az *bz
		return vecA.xComponent*vecB.xComponent + vecA.yComponent*vecB.yComponent + vecA.zComponent*vecB.zComponent;
	}
	
	/**
	 * Checks if the two vectors are equal. Vectors are equal if they have the same direction and magnitude.
	 * @param vecA the first vector to check
	 * @param vecB the second vector to check
	 * @return true if the vectors are equal; false if otherwise
	 */
	public static boolean areVectorsEqual(Vector vecA, Vector vecB){
		if(vecA.xComponent == vecB.xComponent && vecA.yComponent == vecB.yComponent && vecA.zComponent == vecB.zComponent){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Creates and returns a vector with the same direction, but a magnitude of one
	 * @return a unit vector
	 */
	public Vector getUnitVector() {
		return Vector.multiplyVectorByScalar(this, 1/this.getMagnitude());
	}
	
	@Override
	public String toString(){
		return "<"+xComponent+", "+yComponent+", "+zComponent+">";
	}
}
