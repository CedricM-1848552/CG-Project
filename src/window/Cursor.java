package window;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class Cursor implements GLFWCursorPosCallbackI {

    private static Cursor instance;

    private double xpos;
    private double ypos;

    private Cursor() {
        this.xpos = 0;
        this.ypos = 0;
    }

    public static Cursor get() {
        if (instance == null) {
            instance = new Cursor();
        }

        return instance;
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public double getXpos() {
        return xpos;
    }

    public double getYpos() {
        return ypos;
    }
}
