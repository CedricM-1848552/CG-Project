package window;

import enitities.Entity;
import enitities.Light;
import enitities.Player;
import models.Model;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import shaders.StaticShader;

import java.nio.*;
import java.util.ArrayList;
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
        glfwSetMouseButtonCallback(window, Mouse.get());

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        glfwSetCursorPosCallback(window, Cursor.get());

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

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
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // Resizes viewport when the window size changes
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> glViewport(0, 0, width, height));
    }

    private void loop() {
        GL.createCapabilities();

        // Walls
        var wallModel = new Model("res/wall/Wall.obj");
        var walls = new Entity[]{
                // Front
                new Entity(wallModel, new Vector3f(4, 0, -4.9f), new Vector3f(0, 0, 0), .1f),
                new Entity(wallModel, new Vector3f(2, 0, -4.9f), new Vector3f(0, 0, 0), .1f),
                new Entity(wallModel, new Vector3f(-0, 0, -4.9f), new Vector3f(0, 0, 0), .1f),
                new Entity(wallModel, new Vector3f(-2, 0, -4.9f), new Vector3f(0, 0, 0), .1f),
                new Entity(wallModel, new Vector3f(-4, 0, -4.9f), new Vector3f(0, 0, 0), .1f),

                // Back
                new Entity(wallModel, new Vector3f(4.2f, 0, 5.1f), new Vector3f(0, 180, 0), .1f),
                new Entity(wallModel, new Vector3f(2.2f, 0, 5.1f), new Vector3f(0, 180, 0), .1f),
                new Entity(wallModel, new Vector3f(0.2f, 0, 5.1f), new Vector3f(0, 180, 0), .1f),
                new Entity(wallModel, new Vector3f(-1.8f, 0, 5.1f), new Vector3f(0, 180, 0), .1f),
                new Entity(wallModel, new Vector3f(-3.8f, 0, 5.1f), new Vector3f(0, 180, 0), .1f),

                // Left
                new Entity(wallModel, new Vector3f(-4.9f, 0, 4.2f), new Vector3f(0, 90, 0), .1f),
                new Entity(wallModel, new Vector3f(-4.9f, 0, 2.2f), new Vector3f(0, 90, 0), .1f),
                new Entity(wallModel, new Vector3f(-4.9f, 0, 0.2f), new Vector3f(0, 90, 0), .1f),
                new Entity(wallModel, new Vector3f(-4.9f, 0, -1.8f), new Vector3f(0, 90, 0), .1f),
                new Entity(wallModel, new Vector3f(-4.9f, 0, -3.8f), new Vector3f(0, 90, 0), .1f),

                // Right
                new Entity(wallModel, new Vector3f(5.1f, 0, 4), new Vector3f(0, 270, 0), .1f),
                new Entity(wallModel, new Vector3f(5.1f, 0, 2), new Vector3f(0, 270, 0), .1f),
                new Entity(wallModel, new Vector3f(5.1f, 0, 0), new Vector3f(0, 270, 0), .1f),
                new Entity(wallModel, new Vector3f(5.1f, 0, -2), new Vector3f(0, 270, 0), .1f),
                new Entity(wallModel, new Vector3f(5.1f, 0, -4), new Vector3f(0, 270, 0), .1f),
        };

        // Floor
        var floorModel = new Model("res/grass/grass_floor.obj");
        var floorSize = 25;
        var floorTiles = new Entity[floorSize*floorSize];
        for (int z = 0; z < floorSize; z++) {
            for (int x = 0; x < floorSize; x++) {
                floorTiles[z *floorSize+x] = new Entity(floorModel, new Vector3f(x*2 - floorSize, 0, z*2 - floorSize), new Vector3f(0, 0, 0), 1f);
            }
        }

        // Target
        var targetModel = new Model("res/target/shtfrtr.obj");
        var target = new Entity(targetModel, new Vector3f(0, 0, 2), new Vector3f(0, 0, 0), 0.3f);

        // Center campfire
        var campfireModel = new Model("res/campfire/Campfire_clean.OBJ");
        var campfires = new Entity[] {
                new Entity(campfireModel, new Vector3f(0, 0, 0), new Vector3f(0 , 0, 0), 0.01f),
                new Entity(campfireModel, new Vector3f(15, 0, 15), new Vector3f(0 , 0, 0), 0.01f),
                new Entity(campfireModel, new Vector3f(-12, 0, -13), new Vector3f(0 , 0, 0), 0.01f),
                new Entity(campfireModel, new Vector3f(-14, 0, 12), new Vector3f(0 , 0, 0), 0.01f),
                new Entity(campfireModel, new Vector3f(11, 0, -13), new Vector3f(0 , 0, 0), 0.01f),
        };

        // Lights
        var lights = new ArrayList<>(
                Collections.singletonList(
                        new Light(new Vector3f(0, 100, 0), new Vector3f(.2f, .2f, .2f), new Vector3f(1, 0, 0))
                ));

        for (var campfire : campfires) {
            lights.add(new Light(campfire.getPosition().add(new Vector3f(0, 1, 0)), new Vector3f(.7f, .45f, 0), new Vector3f(1, 0.01f, 0.002f)));
        }

        var shader = new StaticShader();

        var player = new Player();

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
            Mouse.get().update();
            glfwPollEvents();
            player.update();

            // Toggle wireframe mode
            if (Keyboard.get().isKeyReleased(GLFW_KEY_F)) {
                showingWireframe = !showingWireframe;

                if (showingWireframe) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                } else {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                }
            }

            // Close the window when escape gets pressed (closes on release)
            if (Keyboard.get().isKeyReleased(GLFW_KEY_ESCAPE)) {
                glfwSetWindowShouldClose(window, true);
            }

            // Render
            shader.start();
            player.loadTo(shader);
            shader.loadLights(lights);
            player.render(shader);
            target.render(shader);
            for (var campfire : campfires) {
                campfire.render(shader);
            }
            for (var tile : floorTiles) {
                tile.render(shader);
            }
            for (var wall : walls) {
                wall.render(shader);
            }
            shader.stop();

            // Swap buffers
            glfwSwapBuffers(window);
        }

        shader.delete();
    }

    public static void main(String[] args) {
        new Window().run();
    }
}