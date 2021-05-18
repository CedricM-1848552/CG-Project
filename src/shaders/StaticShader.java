package shaders;

import enitities.Camera;
import enitities.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import window.Window;

import java.util.List;

public class StaticShader extends Shader {
    private static final int MAX_LIGHTS = 6;

    private static final String VERTEX_FILE = "shaders/vertexShader.vert";
    private static final String FRAGMENT_FILE = "shaders/fragmentShader.frag";

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int[] location_lightPosition;
    private int[] location_lightColour;
    private int[] location_attenuation;

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
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        this.location_viewMatrix =super.getUniformLocation("viewMatrix");

        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
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

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
                super.loadVector(location_lightColour[i], lights.get(i).getColour());
                super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
                super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }
}
