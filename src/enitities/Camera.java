package enitities;

import org.joml.Vector3f;
import window.Keyboard;
import window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    public static final float SENSITIVITY = 0.05f;
    private double prevX = (double) Window.WIDTH / 2;
    private double prevY = (double) Window.HEIGHT / 2;

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
            position.z -= 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_S)) {
            position.z += 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_A)) {
            position.x -= 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_D)) {
            position.x += 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            position.y -= 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_SPACE)) {
            position.y += 0.05f;
        }
        // Rotate camera (temporary)
        if (Keyboard.get().isKeyDown(GLFW_KEY_UP)) {
            pitch -= 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_DOWN)) {
            pitch += 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_LEFT)) {
            yaw -= 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_RIGHT)) {
            yaw += 0.05f;
        }
    }

    public void changeDirection(double xpos, double ypos) {
        float xOffset = (float) (xpos - prevX);
        float yOffset = (float) (ypos - prevY);
        prevX = xpos;
        prevY = ypos;

        xOffset *= SENSITIVITY;
        yOffset *= SENSITIVITY;

        setYaw(yaw + xOffset);
        setPitch(pitch + yOffset);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;

        if (this.pitch > 89.0f)
            this.pitch = 89.0f;
        else if (this.pitch < -89.0f)
            this.pitch = -89.0f;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}
