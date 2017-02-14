package physics;

import geometry.Point;

import java.util.ArrayList;
import java.util.List;

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
	private Point min = null;
	
	/**
	 * the max point of the box; largest x, y, and z
	 */
	private Point max = null;
	
	/**
	 * Creates a new box around the physics body
	 * @param body the body to create the box around
	 */
	public BodyBox(PhysicsBody body){
		this.body = body;
		float sX = body.faces.get(0).point1.x;
		float bX = body.faces.get(0).point1.x;//set to values that could actully exist rather than 0
		float sY = body.faces.get(0).point1.y;
		float bY = body.faces.get(0).point1.y;
		float sZ = body.faces.get(0).point1.z;
		float bZ = body.faces.get(0).point1.z;
		for(PhysicsFace f : body.faces){
			for(Point p : f.getPoints()){
				if(p.x < sX){
					sX = p.x;
				} else if(p.x > bX){
					bX = p.x;
				}
				
				if(p.y < sY){
					sY = p.y;
				} else if(p.y > bY){
					bY = p.y;
				}
				
				if(p.z < sZ){
					sZ = p.z;
				} else if(p.z > bZ){
					bZ = p.z;
				}
			}
		}
		min = new Point(sX, sY, sZ);
		max = new Point(bX, bY, bZ);
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof BodyBox){
			if(((BodyBox)(o)).min.equals(this.min) && ((BodyBox)o).max.equals(this.max)){
				return true;
			}
		}
		return false;
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
