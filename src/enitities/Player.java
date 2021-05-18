package enitities;

import models.Model;
import org.joml.Vector3f;
import shaders.StaticShader;
import window.Keyboard;
import window.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Player {
    private final Camera camera;
    public static final float SENSITIVITY = 0.05f;
    public static final float MOVEMENT = 0.05f;
    private final Vector3f gunPosition;

    private double prevX = (double) Window.WIDTH / 2;
    private double prevY = (double) Window.HEIGHT / 2;
    private boolean firstMove = true;
    private final Entity gun;

    public Player() {
        this.camera = new Camera();

        var gunModel = new Model("res/gun/Gun.dae");
        this.gunPosition = new Vector3f(.4f, 1.5f, -1f);
        this.gun = new Entity(gunModel, gunPosition, new Vector3f(270, 0, 270), 1);
    }

    public void changeDirection(double xpos, double ypos) {
        if (firstMove) {
            prevX = xpos;
            prevY = ypos;
            firstMove = false;
        }

        float xOffset = (float) (xpos - prevX);
        float yOffset = (float) (ypos - prevY);
        prevX = xpos;
        prevY = ypos;

        xOffset *= SENSITIVITY;
        yOffset *= SENSITIVITY;

        this.camera.setYaw(this.camera.getYaw() + xOffset);
        this.camera.setPitch(this.camera.getPitch() + yOffset);
        this.gun.setRotation(this.gun.getRotation().add(new Vector3f(0, 0, -xOffset)));

        var cameraYaw = this.camera.getYaw();
        var x = (float)(this.gunPosition.x * Math.cos(Math.toRadians(cameraYaw)) + this.gunPosition.z * Math.sin(Math.toRadians(-cameraYaw)));
        var z = (float)(this.gunPosition.x * Math.sin(Math.toRadians(cameraYaw)) + this.gunPosition.z * Math.cos(Math.toRadians(cameraYaw)));

        this.gun.setPosition(new Vector3f(x, this.gunPosition.y, z));
    }

    public void move() {
        var cameraPosition = this.camera.getPosition();
        var gunPosition = this.gun.getPosition();
        var cameraYaw = this.camera.getYaw();

        double dx1 = MOVEMENT * Math.sin(Math.toRadians(cameraYaw));
        double dz1 = MOVEMENT * Math.cos(Math.toRadians(cameraYaw));
        double dx2 = MOVEMENT * Math.cos(Math.toRadians(-cameraYaw));
        double dz2 = MOVEMENT * Math.sin(Math.toRadians(-cameraYaw));

        if (Keyboard.get().isKeyDown(GLFW_KEY_W)) {
            cameraPosition.x += dx1;
            cameraPosition.z -= dz1;
            gunPosition.x += dx1;
            gunPosition.z -= dz1;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_S)) {
            cameraPosition.x -= dx1;
            cameraPosition.z += dz1;
            gunPosition.x -= dx1;
            gunPosition.z += dz1;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_A)) {
            cameraPosition.z += dz2;
            cameraPosition.x -= dx2;
            gunPosition.z += dz2;
            gunPosition.x -= dx2;
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_D)) {
            cameraPosition.z -= dz2;
            cameraPosition.x += dx2;
            gunPosition.z -= dz2;
            gunPosition.x += dx2;
        }

        this.gun.setPosition(gunPosition);
        this.camera.setPosition(cameraPosition);
    }

    public void loadTo(StaticShader shader) {
        shader.loadViewMatrix(camera);
    }

    public void render(StaticShader shader) {
        this.gun.render(shader);
    }
}
