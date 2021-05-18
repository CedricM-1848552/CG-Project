package enitities;

import org.joml.Vector3f;

public class Camera {

    private Vector3f position;
    private float pitch;
    private float yaw;

    public Camera() {
        this.position = new Vector3f(0, 0, 0);
        this.pitch = 0;
        this.yaw = 0;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
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
}
