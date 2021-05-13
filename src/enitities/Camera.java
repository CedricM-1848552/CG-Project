package enitities;

import org.joml.Vector3f;
import window.Keyboard;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private final Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {
        this.position = new Vector3f(0, 0, 0);
        this.pitch = 0;
        this.yaw = 0;
        this.roll = 0;
    }

    public void move() {
        if (Keyboard.get().isKeyDown(GLFW_KEY_W)) {
            position.z -= 0.02f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_S)) {
            position.z += 0.02f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_A)) {
            position.x -= 0.02f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_D)) {
            position.x += 0.02f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            position.y -= 0.02f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_SPACE)) {
            position.y += 0.02f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
