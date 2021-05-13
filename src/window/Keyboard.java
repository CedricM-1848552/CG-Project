package window;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard implements GLFWKeyCallbackI {

    private static Keyboard instance;

    private final ArrayList<Integer> pressedKeys;
    private final ArrayList<Integer> releasedKeys;
    private int activeMods;

    private Keyboard() {
        this.pressedKeys = new ArrayList<>();
        this.releasedKeys = new ArrayList<>();
        this.activeMods = 0;
    }

    public static Keyboard get() {
        if (instance == null) {
            instance = new Keyboard();
        }

        return instance;
    }

    public void update() {
        this.releasedKeys.clear();
        glfwPollEvents();
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        this.activeMods = mods;
        if (action == GLFW_PRESS) {
            pressedKeys.add(key);
        } else if (action == GLFW_RELEASE) {
            pressedKeys.remove(Integer.valueOf(key));
            releasedKeys.add(key);
        }
    }

    public boolean isKeyPressed(int key) {
        return pressedKeys.contains(key);
    }

    public boolean isKeyReleased(int key) {
        return releasedKeys.contains(key);
    }

    public boolean isKeyDown(int key) {
        return isKeyPressed(key);
    }

    public boolean isKeyUp(int key) {
        return !isKeyPressed(key);
    }

    public boolean isModifierActive(int modifier) {
        return (activeMods & modifier) == modifier;
    }
}
