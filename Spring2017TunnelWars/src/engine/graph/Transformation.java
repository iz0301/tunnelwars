package engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.GameItem;

public class Transformation {

	private final Matrix4f projectionMatrix;

	private final Matrix4f modelViewMatrix;

	private final Matrix4f viewMatrix;

	public Transformation() {
		projectionMatrix = new Matrix4f();
		modelViewMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
	}

	/**
	 * Set the projection matrix that everything will be projected into
	 * 
	 * @param fov
	 *            FOV in radians
	 * @param width
	 *            Physical window width in pixels
	 * @param height
	 *            Same as width just for height
	 * @param zNear
	 *            How close to the origin do you want to begin loading into
	 *            memory
	 * @param zFar
	 *            How far from....
	 * @return Matrix4f projectionMatrix
	 */
	public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height;
		// Returns an empty 4x4 matrix
		projectionMatrix.identity();
		// Sets the 4x4 matrix
		projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
		return projectionMatrix;
	}

	/**
	 * Given the camera's rotation values. Apply those values to the viewMatrix
	 * 
	 * @param camera
	 *            Camera Object
	 * @return Matrix4f viewMatrix
	 */
	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f cameraPos = camera.getPosition();
		Vector3f rotation = camera.getRotation();

		// Returns an empty 4x4 matrix
		viewMatrix.identity();
		// First do the rotation so camera rotates over its position
		viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
				.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
		// Then do the translation. This is due to OpenGL moving the world and
		// not the actual camera.
		viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		return viewMatrix;
	}

	/**
	 * Translate to the position of the mesh. Rotate along X, Y, and Z axis.
	 * Scale the item based upon scalar. Take the models matrix and multiply it
	 * by the viewMatrix to get the current View
	 * 
	 * @param gameItem
	 *            gameItem Object
	 * @param viewMatrix
	 *            the matrix in which stuffed is moved by(I think)
	 * @return Matrix4f Current view which includes all objects within the view
	 */
	public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
		Vector3f rotation = gameItem.getRotation();
		modelViewMatrix.identity().translate(gameItem.getPosition()).rotateX((float) Math.toRadians(-rotation.x))
				.rotateY((float) Math.toRadians(-rotation.y)).rotateZ((float) Math.toRadians(-rotation.z))
				.scale(gameItem.getScale());
		Matrix4f viewCurr = new Matrix4f(viewMatrix);
		return viewCurr.mul(modelViewMatrix);
	}
}