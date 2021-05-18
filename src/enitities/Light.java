package enitities;

import org.joml.Vector3f;

public class Light {
    private Vector3f position;
    private Vector3f colour;
    private Vector3f attenuation;

    public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
        this.position = position;
        this.colour = colour;
        this.attenuation = attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getColour() {
        return colour;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }
}
