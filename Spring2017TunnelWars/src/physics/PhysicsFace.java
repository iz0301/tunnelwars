package physics;

import geometry.Face;
import geometry.Point;

/**
 * A class that defines faces for physical interaction
 * @author Isaac Zachmann
 *
 */
public class PhysicsFace extends Face{
	/**
	 * The friction of this face
	 */
	public float friction = .2f;

	/**
	 * The restitution or bounciness of the face. Expressed as a float between 0 and 1. 
	 * Represents the fraction of kinetic energy conserved during a collision. 
	 */
	public float restitution = .2f;

	/**
	 * The strength of the face in pascals (N/m^2). If this pressure is reached the face will break.
	 * Or should this just be Newtons instead?
	 */
	public double strength = 100000;

	/**
	 * Creates a new face with the specified points 
	 * @param ptA The first point
	 * @param ptB The second point
	 * @param ptC The third point
	 */
	public PhysicsFace(Point ptA, Point ptB, Point ptC){
		super(ptA, ptB, ptC);
	}

	/**
	 * Creates a physics face from the face
	 * NOTE: the way i do this could be better i think
	 * @param f the geometric face to turn into a physics face
	 */
	public PhysicsFace(Face f) {
		super(f.point1, f.point2, f.point3);
		if(f instanceof PhysicsFace){
			this.friction = ((PhysicsFace)f).friction;
			this.restitution = ((PhysicsFace)f).restitution;
			this.strength = ((PhysicsFace)f).strength;
		}
	}

	/**
	 * Changes the faces into physics faces
	 * @param array
	 * @return the array of PhysicsFaces from the faces
	 */
	public static PhysicsFace[] generateFromFaces(Face[] array) {
		PhysicsFace[] physicsFaces = new PhysicsFace[array.length];
		for(int i = 0; i < array.length; i++){
			physicsFaces[i] = new PhysicsFace(array[i]);
		}
		return physicsFaces;
	}


	/*
	/**
	 * Creates a physics face from the specified geometric face
	 * @param face the geometric face to create a physics face from
	 */
	/*public PhysicsFace(Face face){
		super(face);
	}*/
}
