package geometry;

import math.Vector;
import physics.PhysicsBody;
import physics.PhysicsFace;

/**
 * A class that creates a simple test cube
 * @author Isaac Zachmann
 */
public class TestCube {

	/**
	 * The physical body of the cube.
	 */
	public PhysicsBody physicsBody;

	/**
	 * The location of the center point of the cube
	 */
	public Point location;
	
	/**
	 * Creates a test cube
	 * @param position the center of the cube
	 * @param sideLength the length of one side of the cube
	 */
	public TestCube(Point position, float sideLength){
		//Vector radius = new Vector(sideLength, sideLength, sideLength);
		PhysicsFace face1 = new PhysicsFace(new Point(position, new Vector(sideLength, sideLength, sideLength)),
				new Point(position, new Vector(sideLength, sideLength, -sideLength)), 
				new Point(position, new Vector(sideLength, -sideLength, -sideLength)));
		PhysicsFace face2 = new PhysicsFace(new Point(position, new Vector(sideLength, -sideLength, -sideLength)),
				new Point(position, new Vector(sideLength, -sideLength, sideLength)), 
				new Point(position, new Vector(sideLength, sideLength, sideLength)));
		
		PhysicsFace face3 = new PhysicsFace(new Point(position, new Vector(-sideLength, sideLength, sideLength)),
				new Point(position, new Vector(-sideLength, sideLength, -sideLength)), 
				new Point(position, new Vector(-sideLength, -sideLength, -sideLength)));
		PhysicsFace face4 = new PhysicsFace(new Point(position, new Vector(-sideLength, -sideLength, -sideLength)),
				new Point(position, new Vector(-sideLength, -sideLength, sideLength)), 
				new Point(position, new Vector(-sideLength, sideLength, sideLength)));
		
		PhysicsFace face5 = new PhysicsFace(new Point(position, new Vector(sideLength, sideLength, sideLength)),
				new Point(position, new Vector(-sideLength, sideLength, sideLength)), 
				new Point(position, new Vector(-sideLength, sideLength, -sideLength)));
		PhysicsFace face6 = new PhysicsFace(new Point(position, new Vector(-sideLength, sideLength, -sideLength)),
				new Point(position, new Vector(sideLength, sideLength, -sideLength)), 
				new Point(position, new Vector(sideLength, sideLength, sideLength)));
		
		PhysicsFace face7 = new PhysicsFace(new Point(position, new Vector(sideLength, -sideLength, sideLength)),
				new Point(position, new Vector(-sideLength, -sideLength, sideLength)), 
				new Point(position, new Vector(-sideLength, -sideLength, -sideLength)));
		PhysicsFace face8 = new PhysicsFace(new Point(position, new Vector(-sideLength, -sideLength, -sideLength)),
				new Point(position, new Vector(sideLength, -sideLength, -sideLength)), 
				new Point(position, new Vector(sideLength, -sideLength, sideLength)));
		
		PhysicsFace face9 = new PhysicsFace(new Point(position, new Vector(sideLength, sideLength, sideLength)),
				new Point(position, new Vector(sideLength, -sideLength, sideLength)), 
				new Point(position, new Vector(-sideLength, -sideLength, sideLength)));
		PhysicsFace face10 = new PhysicsFace(new Point(position, new Vector(-sideLength, -sideLength, sideLength)),
				new Point(position, new Vector(-sideLength, sideLength, sideLength)), 
				new Point(position, new Vector(sideLength, sideLength, sideLength)));
		
		PhysicsFace face11 = new PhysicsFace(new Point(position, new Vector(sideLength, sideLength, -sideLength)),
				new Point(position, new Vector(sideLength, -sideLength, -sideLength)), 
				new Point(position, new Vector(-sideLength, -sideLength, -sideLength)));
		PhysicsFace face12 = new PhysicsFace(new Point(position, new Vector(-sideLength, -sideLength, -sideLength)),
				new Point(position, new Vector(-sideLength, sideLength, -sideLength)), 
				new Point(position, new Vector(sideLength, sideLength, -sideLength)));
		
		physicsBody = new PhysicsBody(new PhysicsFace[]{face1,face2,face3,face4,face5,face6,face7,face8,face9,face10,face11,face12}); 
	}

}
