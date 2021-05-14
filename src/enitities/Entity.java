package enitities;

import models.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import shaders.StaticShader;

public class Entity {
    private final Model model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public Entity(Model model, Vector3f position, Vector3f rotation, float scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void increasePosition(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    public void increaseRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    private Matrix4f createTransformationMatrix() {
        var transformation = new Matrix4f();
        transformation.identity();
        transformation.translate(this.position);
        transformation.rotate((float) Math.toRadians(this.rotation.x), new Vector3f(1,0,0));
        transformation.rotate((float) Math.toRadians(this.rotation.y), new Vector3f(0,1,0));
        transformation.rotate((float) Math.toRadians(this.rotation.z), new Vector3f(0,0,1));
        transformation.scale(this.scale);
        return transformation;
    }

    public void render(StaticShader shader) {
        var transformation = createTransformationMatrix();
        shader.loadTransformationMatrix(transformation);
        this.model.render(shader);
    }
}
