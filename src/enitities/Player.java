package enitities;

import models.Model;
import org.joml.Vector3f;
import physics.BoundingBox;
import shaders.StaticShader;
import window.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private final Camera camera;
    public static final float SENSITIVITY = 0.05f;
    public static final float MOVEMENT = 0.08f;
    private final Vector3f fixedGunPosition;
    private final Vector3f gunPositionRelativeToCamera;

    private double prevX = (double) Window.WIDTH / 2;
    private double prevY = (double) Window.HEIGHT / 2;
    private boolean firstMove = true;
    private final Entity gun;
    private final Sound pew;
    private BoundingBox boundingBox;

    public Player() {
        this.camera = new Camera();

        var gunModel = new Model("res/gun/Gun.dae");
        this.fixedGunPosition = new Vector3f(.4f, -0.5f, -1f);
        this.gunPositionRelativeToCamera = new Vector3f(fixedGunPosition);
        this.gun = new Entity(gunModel, fixedGunPosition, new Vector3f(270, 0, 270), 1);
        this.camera.setPosition(new Vector3f(0, 2, -2));
        this.pew = new Sound("res/pew/pew.wav");
        this.boundingBox = new BoundingBox(-.5f + camera.getPosition().x, .5f + camera.getPosition().x,
                camera.getPosition().y - 2, camera.getPosition().y + .25f,
                -.5f + camera.getPosition().z, .5f + camera.getPosition().z);
    }

    public void changeDirection() {
        var xpos = Cursor.get().getXpos();
        var ypos = Cursor.get().getYpos();

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

        gunPositionRelativeToCamera.x = (float)(this.fixedGunPosition.x * Math.cos(Math.toRadians(cameraYaw))
                + this.fixedGunPosition.z * Math.sin(Math.toRadians(-cameraYaw)));
        gunPositionRelativeToCamera.z = (float)(this.fixedGunPosition.x * Math.sin(Math.toRadians(cameraYaw))
                + this.fixedGunPosition.z * Math.cos(Math.toRadians(cameraYaw)));

        this.gun.setPosition(this.camera.getPosition().add(gunPositionRelativeToCamera));
    }

    public void update(ArrayList<Entity> entities) {
        this.changeDirection();
        var cameraPosition = new Vector3f(this.camera.getPosition());
        var gunPosition = new Vector3f(this.gun.getPosition());
        var cameraYaw = this.camera.getYaw();
        var oldBoundingBox = new BoundingBox(this.boundingBox);

        double dx1 = MOVEMENT * Math.sin(Math.toRadians(cameraYaw));
        double dz1 = MOVEMENT * Math.cos(Math.toRadians(cameraYaw));
        double dx2 = MOVEMENT * Math.cos(Math.toRadians(-cameraYaw));
        double dz2 = MOVEMENT * Math.sin(Math.toRadians(-cameraYaw));

        if (Keyboard.get().isKeyDown(GLFW_KEY_W)) {
            cameraPosition.x += dx1;
            cameraPosition.z -= dz1;
            gunPosition.x += dx1;
            gunPosition.z -= dz1;
            boundingBox.increasePosition((float) dx1, 0, (float) -dz1);
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_S)) {
            cameraPosition.x -= dx1;
            cameraPosition.z += dz1;
            gunPosition.x -= dx1;
            gunPosition.z += dz1;
            boundingBox.increasePosition((float) -dx1, 0, (float) dz1);
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_A)) {
            cameraPosition.z += dz2;
            cameraPosition.x -= dx2;
            gunPosition.z += dz2;
            gunPosition.x -= dx2;
            boundingBox.increasePosition((float) -dx2, 0, (float) dz2);
        }
        if (Keyboard.get().isKeyDown(GLFW_KEY_D)) {
            cameraPosition.z -= dz2;
            cameraPosition.x += dx2;
            gunPosition.z -= dz2;
            gunPosition.x += dx2;
            boundingBox.increasePosition((float) dx2, 0, (float) -dz2);
        }
        if (Mouse.get().isButtonPressed(GLFW_MOUSE_BUTTON_1)) {
            this.pew.play();
        }

        if (!playerCollides(entities)) {
            this.gun.setPosition(gunPosition);
            this.camera.setPosition(cameraPosition);
        }
        else {
            this.boundingBox = oldBoundingBox;
        }
    }

    public void loadTo(StaticShader shader) {
        shader.loadViewMatrix(camera);
    }

    public void render(StaticShader shader) {
        this.gun.render(shader);
    }

    public boolean playerCollides(ArrayList<Entity> entities) {
        for (Entity entity : entities) {
            if (boundingBox.collides(entity.getBoundingBox()))
                return true;
        }
        return false;
    }
}
