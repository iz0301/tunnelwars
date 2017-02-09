package game;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import engine.graph.Mesh;
import engine.graph.OBJLoader;
import engine.graph.Texture;

public class DummyGame implements IGameLogic {

	private static final float MOUSE_SENSITIVITY = 0.2f;

	private final Vector3f cameraInc;

	private final Renderer renderer;

	private final Camera camera;

	private GameItem[] gameItems;

	private static float CAMERA_POS_STEP = 0.25f;

	public ArrayList<GameItem> gameItemArray = new ArrayList<GameItem>();

	public DummyGame() {
		// Initialize everything
		renderer = new Renderer();
		camera = new Camera();
		cameraInc = new Vector3f(0, 0, 0);
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		Mesh mesh = OBJLoader.loadMesh("/bunny.obj");
		GameItem gameItem = new GameItem(mesh);
		gameItem.setScale(1.5f);
		gameItem.setPosition(0, 0, -2);

		GameItem gameItem1 = new GameItem(mesh);
		gameItem1.setScale(1.0f);
		gameItem1.setPosition(0, 10, -5);

		Mesh mesh2 = OBJLoader.loadMesh("/Mesh.obj");
		GameItem gameItem2 = new GameItem(mesh2);
		gameItem2.setScale(1.0f);
		gameItem2.setPosition(5, 5, -5);

		Texture texture = new Texture("/cube.png");
		Mesh mesh3 = OBJLoader.loadMesh("/TexturedCube.obj");
		GameItem gameItem3 = new GameItem(mesh3);
		gameItem3.setPosition(0, 0, -10);
		mesh3.setTexture(texture);
		gameItem3.getMesh().isTextured();

		Mesh torus = OBJLoader.loadMesh("/torus.obj");
		GameItem torusItem = new GameItem(torus);
		Vector3f color = new Vector3f(1.0f, 0.0f, 0.0f);
		torus.setColour(color);
		torusItem.setPosition(3, 4, -5);
		
		

		gameItems = new GameItem[] { gameItem, gameItem1, gameItem2, gameItem3, torusItem };
		
	}

	/**
	 * When a button is pressed change how much the camera is supposed to move
	 * in that direction
	 */
	@Override
	public void input(Window window, MouseInput mouseInput) {
		cameraInc.set(0, 0, 0);
		if (window.isKeyPressed(GLFW_KEY_W)) {
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			cameraInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			cameraInc.y = -1;
		} else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
			cameraInc.y = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_P)) {
			CAMERA_POS_STEP += 0.01f;
		} else if (window.isKeyPressed(GLFW_KEY_O)) {
			CAMERA_POS_STEP -= 0.01f;
		}
	}

	/**
	 * Update the coordinates on the SCREEN depending on what keys have been
	 * pressed and by what direction the mouse is pointing
	 * 
	 * @param interval
	 *            not used
	 * @param mouseInput
	 *            the value for the current mouse input
	 */
	@Override
	public void update(float interval, MouseInput mouseInput) {
		// Update camera position
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP,
				cameraInc.z * CAMERA_POS_STEP);

		// Update camera's direction based on mouse
		if (mouseInput.isRightButtonPressed()) {
			Vector2f rotVec = mouseInput.getDisplVec();
			camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		}
	}

	/**
	 * @param window
	 *            value for windowHandler to be supplied to Renderer class
	 */
	@Override
	public void render(Window window) {
		renderer.render(window, camera, gameItems);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		for (GameItem gameItem : gameItems) {
			gameItem.getMesh().cleanUp();
		}
	}

}