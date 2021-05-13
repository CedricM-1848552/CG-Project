package window;

import enitities.Camera;
import enitities.Entity;
import enitities.Light;
import models.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import shaders.StaticShader;
import textures.Texture;

import java.nio.*;
import java.util.Arrays;
import java.util.Collections;
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

        glfwSetKeyCallback(window, Keyboard.get());

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

        var texture = new Texture("res/textures/stallTexture.png", GL_MIRRORED_REPEAT, GL_NEAREST);
        var model = TexturedModel.fromObjFile("res/models/stall.obj", texture);

        var entity = new Entity(model, new Vector3f(0, -10, -25), new Vector3f(0, 180, 0), 1);
        var light1 = new Light(new Vector3f(0, 5, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0.01f, 0.002f));
        var light2 = new Light(new Vector3f(0, 0, -5), new Vector3f(1, 0, 0), new Vector3f(1, 0.01f, 0.002f));

        var shader = new StaticShader();

        var camera = new Camera();
        var showingWireframe = false;

        glClearColor(0.3f, 0.4f, 0.7f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        while ( !glfwWindowShouldClose(window) ) {
            // Clear buffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Update
            Keyboard.get().update();
            camera.move();

            // Toggle wireframe mode
            if (Keyboard.get().isKeyReleased(GLFW_KEY_F)) {
                showingWireframe = !showingWireframe;

                if (showingWireframe) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                } else {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                }
            }

            // Render
            shader.start();
            shader.loadViewMatrix(camera);
            shader.loadLights(Arrays.asList(light1, light2));
            entity.render(shader);
            shader.stop();

            // Swap buffers
            glfwSwapBuffers(window);
        }

        model.delete();
        texture.delete();
        shader.delete();
    }

    public static void main(String[] args) {
        new Window().run();
    }

}