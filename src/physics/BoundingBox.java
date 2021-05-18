package physics;

import models.Model;

public class BoundingBox {
    private float x;
    private float y;
    private float z;
    private float width;
    private float length;
    private float height;

//    private Model model;

    /**
     * Constructor
     * @param x x coordinate of the front bottom left point of the cube
     * @param y y coordinate of the front bottom left point of the cube
     * @param z z coordinate of the front bottom left point of the cube
     * @param width width of the cube
     * @param height height of the cube
     * @param length length of the cube
     */
    public BoundingBox(float x, float y, float z, float width, float height, float length) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;

//        this.model = new Model(new float[] {
//                3, 3, 0,
//                3, 6, 0,
//                6, 6, 0,
//                6, 3, 0,
//                3, 3, 3,
//                3, 6, 3,
//                6, 6, 3,
//                6, 3, 3
//        }, new int[] {
//                0, 1, 3,
//                1, 2, 3,
//                2, 3, 6,
//                3, 6, 7,
//                4, 6, 7,
//                4, 5, 6,
//                1, 4, 5,
//                0, 1, 4,
//                1, 2, 5,
//                2, 5, 6,
//                0, 3, 4,
//                3, 4, 7
//        });
    }

    /**
     * Checks if the given bounding box collides with the current instance
     * @param boundingBox the second bounding box
     * @return true if it collides (has to collide on all 3 axes to really collide)
     */
    public boolean collides(BoundingBox boundingBox) {
        boolean xCollision = this.x + this.width >= boundingBox.x;
        boolean yCollision = this.y + this.height >= boundingBox.y;
        boolean zCollision = this.z + this.length >= boundingBox.z;

        return (xCollision && yCollision && zCollision);
    }

    public void move(float x, float z) {
        this.x = x;
        this.z = z;
    }

//    public void render(StaticShader shader) {
//        model.render();
//    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
