package engine;

import org.joml.Vector3f;

import engine.graph.Mesh;

public class GameItem {

	private final Mesh mesh;

	private final Vector3f position;

	private float scale;

	private final Vector3f rotation;

	public GameItem(Mesh mesh) {
		// Initialize the mesh with coordinates at original position of supplied
		// coordinates in the mesh with no scalar or rotation
		this.mesh = mesh;
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * Supplies the rotation of the mesh in relation to the origin
	 * 
	 * @return Vector3f rotation
	 */
	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public Mesh getMesh() {
		return mesh;
	}
}