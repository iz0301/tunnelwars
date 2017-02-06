package physics;

import geometry.Face;
import geometry.Point;

import java.util.ArrayList;

/**
 * A class that defines faces for physical interaction
 * @author Isaac Zachmann
 *
 */
public class PhysicsFace extends Face{
	/**
	 * The friction of this face
	 */
	public double friction = .2;
	
	/**
	 * The restitution or bounciness of the face. Expressed as a double between 0 and 1.
	 */
	public double restitution = .2;
	
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
	}

	/**
	 * Changes the faces into physics faces
	 * @param array
	 * @return
	 */
	public static PhysicsFace[] generateFromFaces(Face[] array) {
		ArrayList<PhysicsFace> physicsFaces = new ArrayList<PhysicsFace>();
		for(Face f : array){
			physicsFaces.add(new PhysicsFace(f));
		}
		return physicsFaces.toArray(new PhysicsFace[physicsFaces.size()]);
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
