package window;

import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse implements GLFWMouseButtonCallbackI {

    private static Mouse instance;

    private final ArrayList<Integer> downButtons;
    private final ArrayList<Integer> pressedButtons;
    private final ArrayList<Integer> releasedButtons;

    private Mouse() {
        this.downButtons = new ArrayList<>();
        this.pressedButtons = new ArrayList<>();
        this.releasedButtons = new ArrayList<>();
    }

    public static Mouse get() {
        if (instance == null) {
            instance = new Mouse();
        }

        return instance;
    }

    public void update() {
        this.pressedButtons.clear();
        this.releasedButtons.clear();
    }

    // Mouse button handler
    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            this.downButtons.add(button);
            this.pressedButtons.add(button);
        } else if (action == GLFW_RELEASE) {
            this.downButtons.remove(button);
            this.releasedButtons.add(button);
        }
    }

    public boolean isButtonPressed(int button) {
        return this.pressedButtons.contains(button);
    }

    public boolean isButtonReleased(int button) {
        return this.releasedButtons.contains(button);
    }

    public boolean isButtonDown(int button) {
        return this.downButtons.contains(button);
    }

    public boolean isKeyUp(int button) {
        return !isButtonDown(button);
    }
}
