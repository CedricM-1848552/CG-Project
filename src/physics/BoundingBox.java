package physics;

import org.joml.Vector3f;

public class BoundingBox {
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;
    private float minZ;
    private float maxZ;

    public BoundingBox(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public BoundingBox(BoundingBox other) {
        this.minX = other.minX;
        this.maxX = other.maxX;
        this.minY = other.minY;
        this.maxY = other.maxY;
        this.minZ = other.minZ;
        this.maxZ = other.maxZ;
    }

    /**
     * Returns the bottom middle position
     * @return a vector containing the bottom middle position of the bounding box
     */
    public Vector3f getPosition() {
        float x = minX + (maxX - minX) / 2f;
        float z = minZ + (maxZ - minZ) / 2f;

        return new Vector3f(x, minY, z);
    }

    public void scaleCoords(float scale) {
        float xDiff = ((maxX - minX) / 2f) * (1f - scale);
        float zDiff = ((maxZ - minZ) / 2f) * (1f - scale);

        minX += xDiff;
        maxX -= xDiff;
        maxY -= (maxY - minY) * (1f - scale);
        minZ += zDiff;
        maxZ -= zDiff;
    }

    /**
     * Rotates 90 degrees clock-wise
     */
    public void rotate90() {
        var x1 = this.maxZ;
        var x2 = this.minZ;
        var z1 = this.maxX;
        var z2 = this.minX;
        this.minX = Math.min(x1, x2);
        this.maxX = Math.max(x1, x2);
        this.minZ = Math.min(z1, z2);
        this.maxZ = Math.max(z1, z2);;
    }

    /**
     * Checks if the given bounding box collides with the current instance
     * @param boundingBox the second bounding box
     * @return true if it collides (has to collide on all 3 axes to really collide)
     */
    public boolean collides(BoundingBox boundingBox) {

        var xCollision = (this.minX >= boundingBox.minX && this.minX <= boundingBox.maxX) || (this.maxX >= boundingBox.minX && this.maxX <= boundingBox.maxX);
        var yCollision = (this.minY >= boundingBox.minY && this.minY <= boundingBox.maxY) || (this.maxY >= boundingBox.minY && this.maxY <= boundingBox.maxY);
        var zCollision = (this.minZ >= boundingBox.minZ && this.minZ <= boundingBox.maxZ) || (this.maxZ >= boundingBox.minZ && this.maxZ <= boundingBox.maxZ);

        return (xCollision && yCollision && zCollision);
    }

    public void increasePosition(float x, float y, float z) {
        this.minX += x;
        this.maxX += x;
        this.minY += y;
        this.maxY += y;
        this.minZ += z;
        this.maxZ += z;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", minZ=" + minZ +
                ", maxZ=" + maxZ +
                '}';
    }
}
