package physics;

import geometry.Point;

/**
 * A class to put a box around a physics body. Used for collision detection.
 * @author Isaac Zachmann
 *
 */
public class BodyBox {
	/**
	 * the original body
	 */
	private PhysicsBody body;
	
	/**
	 * the minimum point of the box; smallest x, y, and z
	 */
	public Point min = null;
	
	/**
	 * the max point of the box; largest x, y, and z
	 */
	public Point max = null;
	
	/**
	 * Creates a new box around the physics body
	 * @param body the body to create the box around
	 */
	public BodyBox(PhysicsBody body){
		this.body = body;
		for(Object f : body.faces){
			for(Object o : ((PhysicsFace) f).getPoints()){
				Point p = (Point) o;
				
				if(min == null){
					min = p;
				}
				if(max == null){
					max = p;
				}
				
				if(p.x < min.x){
					min.x = p.x;
				}
				if(p.y < min.y){
					min.y = p.y;
				}
				if(p.z < min.z){
					min.z = p.z;
				}
				
				if(p.x > max.x){
					max.x = p.x;
				}
				if(p.y > max.y){
					max.y = p.y;
				}
				if(p.z > max.z){
					max.z = p.z;
				}
			}
		}
	}
	
	/**
	 * Gets the original physics body
	 * @return the original physics body
	 */
	public PhysicsBody getBody(){
		return body;
	}
	
	/**
	 * Checks to see if two BodyBoxes are intersecting
	 * @param b1 the first BodyBox
	 * @param b2 the second BodyBox
	 * @return true if the bodies are intersecting, false if otherwise
	 */
	public static boolean areBoxesIntersecting(BodyBox b1, BodyBox b2){
		if((b1.min.x <= b2.max.x && b1.max.x >= b2.min.x) &&
				(b1.min.y <= b2.max.y && b1.max.y >= b2.min.y) && 
				(b1.min.z <= b2.max.z && b1.max.z >= b2.min.z)){
			return true;
		}
		return false;
	}
}
