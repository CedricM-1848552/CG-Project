package shaders;

public class StaticShader extends Shader {
    private static final String VERTEX_FILE = "shaders/vertexShader.vert";
    private static final String FRAGMENT_FILE = "shaders/fragmentShader.frag";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
