package shaders;

import enitities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import window.Window;

public class StaticShader extends Shader {
    private static final String VERTEX_FILE = "shaders/vertexShader.vert";
    private static final String FRAGMENT_FILE = "shaders/fragmentShader.frag";

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
        this.start();
        var projection = createProjectionMatrix();
        this.loadProjectionMatrix(projection);
        this.stop();
    }

    private static Matrix4f createProjectionMatrix() {
        float ratio = (float)(Window.WIDTH / Window.HEIGHT);
        float y_scale = (float)((1f / Math.tan(Math.toRadians(FOV / 2f))) * ratio);
        float x_scale = y_scale / ratio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        Matrix4f projection = new Matrix4f();
        projection.m00(x_scale);
        projection.m11(y_scale);
        projection.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projection.m23(-1);
        projection.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projection.m33(0);
        return projection;
    }

    private static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f view = new Matrix4f();
        view.identity();
        view.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        view.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));

        try {
            Vector3f pos = (Vector3f) camera.getPosition().clone();
            pos.mul(-1);
            view.translate(pos);
        } catch (CloneNotSupportedException e) { /* I like living on the edge */ }

        return view;
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
    }

    @Override
    protected void getAllUniformLocations() {
        this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        this.location_viewMatrix =super.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f transformation) {
        loadMatrix(this.location_transformationMatrix, transformation);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(this.location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera camera) {
        var view = createViewMatrix(camera);
        loadMatrix(this.location_viewMatrix, view);
    }
}
