package enitities;

import org.joml.Vector3f;
import physics.BoundingBox;
import shaders.StaticShader;
import window.Keyboard;
import window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    public static final float SENSITIVITY = 0.05f;
    public static final float MOVEMENT = 0.05f;

    private double prevX = (double) Window.WIDTH / 2;
    private double prevY = (double) Window.HEIGHT / 2;
    private boolean firstMove = true;

    private final Vector3f position;
    private float pitch;
    private float yaw;

    private BoundingBox boundingBox;

    public Camera() {
        this.position = new Vector3f(0, 2, 0);
        this.pitch = 0;
        this.yaw = 0;
//        this.boundingBox = new BoundingBox(0, 0, 0, 0.5f, 2, 0.5f);
    }

    public void move() {
        if (Keyboard.get().isKeyDown(GLFW_KEY_W)) {
            position.x += MOVEMENT * Math.sin(Math.toRadians(yaw));
            position.z -= MOVEMENT * Math.cos(Math.toRadians(yaw));
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_S)) {
            position.x -= MOVEMENT * Math.sin(Math.toRadians(yaw));
            position.z += MOVEMENT * Math.cos(Math.toRadians(yaw));
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_A)) {
            position.z += MOVEMENT * Math.sin(Math.toRadians(-yaw));
            position.x -= MOVEMENT * Math.cos(Math.toRadians(-yaw));
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_D)) {
            position.z -= MOVEMENT * Math.sin(Math.toRadians(-yaw));
            position.x += MOVEMENT * Math.cos(Math.toRadians(-yaw));
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            position.y -= 0.05f;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_SPACE)) {
            position.y += 0.05f;
        }
//        boundingBox.move(position.x, position.z);
    }

    public void changeDirection(double xpos, double ypos) {
        // Prevents the camera from quickly moving away on the first mouse movement
        if (firstMove) {
            prevX = xpos;
            prevY = ypos;
            firstMove = false;
        }

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

//    public void render(StaticShader shader) {
//        boundingBox.render(shader);
//    }
}
