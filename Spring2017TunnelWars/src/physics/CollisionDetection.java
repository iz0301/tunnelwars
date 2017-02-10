package physics;

import geometry.Face;
import geometry.FaceIntersection;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to contain static methods for collision detection
 * NOTE: uses ArrayLists which are slow, should change to just arrays.
 * @author Isaac Zachmann
 *
 */
public class CollisionDetection {

	/**
	 * The size to make the regions for the first stage of collision detection
	 */
	public static final int GLOBAL_REGION = 1000;



	/**
	 * Runs the first and stage of collision detection. In this stages groups of bodies are returned if they are in the same global region of
	 * GLOBAL_REGION by GLOBAL_REGION by GLOBAL_REGION units. 
	 * NOTE: right now, no global regions
	 * @param bodies bodies to test the intersection
	 * @return bodies that are in the same global region
	 */
	public static List<BodyBox[]> stage1(List<PhysicsBody> bodies){
		ArrayList<BodyBox> bodyBoxes = new ArrayList<BodyBox>();
		for(PhysicsBody b : bodies){
			bodyBoxes.add(new BodyBox(b));
		}
		ArrayList<BodyBox[]> returnVal = new ArrayList<BodyBox[]>();
		returnVal.add(bodyBoxes.toArray(new BodyBox[bodyBoxes.size()]));
		return returnVal;
	}

	/**
	 * Runs second stage of collision detection.
	 * Note: needs improvement
	 * @param bodies the physics bodies to test (result from stage1)
	 * @return the body boxes of objects that could be colliding. Returns an array of arrays, each has 2 boxes that are intersecting
	 */
	public static List<BodyBox[]> stage2(List<BodyBox[]> boxes){
		List<BodyBox[]> intersecting = new ArrayList<BodyBox[]>();
		for(BodyBox[] tests : boxes){
			for(BodyBox b1 : tests){
				for(BodyBox b2 : tests){
					if(BodyBox.areBoxesIntersecting(b1, b2) && b1 != b2){
						intersecting.add(new BodyBox[]{b1,b2});
					}
				}
			}
		}
		return intersecting;
	}

	/**
	 * The final stage of collision detection to verify two bodies are or are not intersecting
	 * @param intersecting The bodies to test. Each array of physics bodies should be two bodies to test(result from stage2)
	 * @return a list of all the intersections of the input bodies
	 */
	public static List<BodyIntersection> stage3(List<BodyBox[]> intersecting){
		List<BodyIntersection> bodyIntersections = new ArrayList<BodyIntersection>();
		for(BodyBox[] b : intersecting){
			List<PhysicsFaceIntersection> faceIntersections = new ArrayList<PhysicsFaceIntersection>();
			FaceIntersection newFaceIntersection;
			for(PhysicsFace face1 : b[0].getBody().faces){
				for(PhysicsFace face2 : b[1].getBody().faces){
					if((newFaceIntersection = Face.doFacesIntersect(face1, face2)) != null){
						faceIntersections.add(new PhysicsFaceIntersection(newFaceIntersection));
					}
				}
			}
			bodyIntersections.add(new BodyIntersection(faceIntersections.toArray(new PhysicsFaceIntersection[0]), b[0].getBody(), b[1].getBody()));
		}
		return bodyIntersections;
	}
	/*Below is old way for 3, not really sure how its supposed to work but dont think it does
	//look for intersection by all three sides on both bodies
	Point intersection = new Ray(face2.point1, new Vector(face2.point1, face2.point2)).lineIntersectsFace(face1);
	if(intersection != null){
		intersections.add(new Intersection(intersection, b[0].getBody(), b[1].getBody()));
	}
	intersection = new Ray(face2.point1, new Vector(face2.point1, face2.point3)).lineIntersectsFace(face1);
	if(intersection != null){
		intersections.add(new Intersection(intersection, b[0].getBody(), b[1].getBody()));
	}
	intersection = new Ray(face2.point2, new Vector(face2.point2, face2.point3)).lineIntersectsFace(face1);
	if(intersection != null){
		intersections.add(new Intersection(intersection, b[0].getBody(), b[1].getBody()));
	}

	intersection = new Ray(face1.point1, new Vector(face1.point1, face1.point2)).lineIntersectsFace(face2);
	if(intersection != null){
		intersections.add(new Intersection(intersection, b[0].getBody(), b[1].getBody()));
	}
	intersection = new Ray(face1.point1, new Vector(face1.point1, face1.point3)).lineIntersectsFace(face2);
	if(intersection != null){
		intersections.add(new Intersection(intersection, b[0].getBody(), b[1].getBody()));
	}
	intersection = new Ray(face1.point2, new Vector(face1.point2, face1.point3)).lineIntersectsFace(face2);
	if(intersection != null){
		intersections.add(new Intersection(intersection, b[0].getBody(), b[1].getBody()));
	}*/
}
