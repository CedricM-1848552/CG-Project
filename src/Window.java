import models.Model;
import models.TexturedModel;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import shaders.Shader;
import shaders.StaticShader;
import textures.Texture;

import java.nio.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = WIDTH/16*9;

    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "Mood", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videomode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert videomode != null;
            glfwSetWindowPos(
                    window,
                    (videomode.width() - pWidth.get(0)) / 2,
                    (videomode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();

        Texture texture = new Texture("res/texture02.png", GL_MIRRORED_REPEAT, GL_NEAREST);
        Model texturedModel = new TexturedModel(new float[]{
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
        }, new int[]{
                0, 1, 3,
                3, 1, 2,
        }, new float[]{
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        }, texture);

        Shader shader = new StaticShader();

        // Uncomment for wireframe
        // glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        while ( !glfwWindowShouldClose(window) ) {
            // Clear buffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Update
            glfwPollEvents();

            // Render
            shader.start();
            texturedModel.render();
            shader.stop();

            // Swap buffers
            glfwSwapBuffers(window);
        }

        texturedModel.delete();
        texture.delete();
        shader.delete();
    }

    public static void main(String[] args) {
        new Window().run();
    }

}