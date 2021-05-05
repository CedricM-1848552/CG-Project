package shaders;

import static org.lwjgl.opengl.GL33.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Shader {
    private int program;
    private int vertexShader;
    private int fragmentShader;

    public Shader(String vertexFile, String fragmentFile) {
        vertexShader = loadShader(vertexFile, GL_VERTEX_SHADER);
        fragmentShader = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
        program = glCreateProgram();
        bindAttributes();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        glValidateProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            System.out.println(glGetProgramInfoLog(program, 512));
            System.err.println("Could not link program");
            System.exit(-1);
        }
    }

    public void start() {
        glUseProgram(program);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void delete() {
        stop();
        glDetachShader(program, vertexShader);
        glDetachShader(program, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(program);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(program, attribute, variableName);
    }

    private static int loadShader(String file, int type) {
        StringBuilder source = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read shader source file");
            e.printStackTrace();
            System.exit(-1);
        }

        int shader = glCreateShader(type);

        glShaderSource(shader, source);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shader, 512));
            System.err.println("Could not compile shader");
            System.exit(-1);
        }

        return shader;
    }
}
