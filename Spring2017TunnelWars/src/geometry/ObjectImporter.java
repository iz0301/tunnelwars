package geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class to read wavefront obj files (.obj) and store them as their points faces, etc.
 * NOTE: needs work, currently only creates faces and vertices!
 * @author Isaac
 *
 */
public class ObjectImporter {
	
	public static ArrayList<Point> verticies = new ArrayList<Point>();
	public ArrayList<Face> faces = new ArrayList<Face>();
	public ArrayList<Integer> arrayIndicies = new ArrayList<Integer>();
	public static float[] floatVerticies = null;
	public static int[] indicies = null;
	/**
	 * 
	 * @param ArrayList<Integers>
	 * @return int[]
	 */
	public static int[] listToArray(ArrayList<Integer> integers) {
		int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
		return ret;
	}
	
	/**
	 * Creates a new object from the specified .obj path
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public ObjectImporter(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line = null;
			//read until end of file
			while((line = in.readLine()) != null){
				String[] split1 = line.split(" ");
				switch(split1[0]){
				case "v":
				{
					verticies.add(new Point(
							Float.parseFloat(split1[1]),
							Float.parseFloat(split1[2]),
							Float.parseFloat(split1[3])
							));
					break;
				}case "f":
				{
					Point[] pts = new Point[3]; //get ready to make 3 points
					for(int i = 1; i < 4; i++){ //go through the line starting after "f" every space
						String[] split2 = split1[i].split("/"); //split every point into v, vt, and vn. only v is used now
						pts[i-1] = verticies.get(Integer.parseInt(split2[0])-1); //the first one of the "/" split is a v for the face. i-1 bc i is shifted to skip f
						arrayIndicies.add(Integer.parseInt(split2[0]));
					}
					faces.add(new Face(pts));
					break;
				}case "vn":
				{
					break;
				}case "s":
				{
					break;
				}case "o":
				{
					break;
				}case "g":
				{
					break;
				}case "usemtl":
				{
					break;
				}case "mtlib":
				{
					break;
				}default:
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		floatVerticies = Point.pointFloatBuffer(verticies);
		indicies = listToArray(arrayIndicies);
		//System.out.println(floatVerticies.length);
	}
}
