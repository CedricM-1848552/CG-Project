package models;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    public static final int NUM_FLOATS = 8;
    public static final int SIZE = NUM_FLOATS * Float.BYTES;
    public static final int NORMALS_OFFSET = 12;                // 3 * 4 bytes
    public static final int TEXTURE_OFFSET = 24;                // 6 * 4 bytes

    private Vector3f coordinates;
    private Vector3f normal;
    private Vector2f texture;

    public Vertex(Vector3f coordinates, Vector3f normal, Vector2f texture) {
        this.coordinates = coordinates;
        this.normal = normal;
        this.texture = texture;
    }

    public float[] toFloatArray() {
        return new float[]{coordinates.x, coordinates.y, coordinates.z, normal.x, normal.y, normal.z, texture.x, texture.y};
    }

    public void setCoordinates(Vector3f coordinates) {
        this.coordinates = coordinates;
    }

    public Vector3f getCoordinates() {
        return coordinates;
    }

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public void setTexture(Vector2f texture) {
        this.texture = texture;
    }

    public Vector2f getTexture() {
        return texture;
    }
}
