import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;


/**
 * @author Cédric Meukens
 */
public class Model {
    private final int vaoId;
    private final int vboId;
    private final int vertexCount;

    /**
     * Load a model from vertex positions
     * @param positions The positions of the vertices in the model
     * @return The model loaded from the positions
     */
    public static Model fromPositions(float[] positions) {
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        int vboId = storeDataInAttributeList(0, positions);
        glBindVertexArray(0);
        return new Model(vaoId, vboId, positions.length / 3);
    }

    /**
     * Store data in a new VBO
     * @param attributeIndex Index in the VBO to put the data
     * @param data The data to insert
     * @return The id of the VBO containing the data
     */
    private static int storeDataInAttributeList(int attributeIndex, float[] data) {
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeIndex, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboId;
    }

    private Model(int vaoId, int vboId, int vertexCount) {
        this.vaoId = vaoId;
        this.vboId = vboId;
        this.vertexCount = vertexCount;
    }

    public void delete() {
        glDeleteVertexArrays(this.getVaoId());
        glDeleteBuffers(this.getVboId());
    }

    public int getVboId() {
        return this.vboId;
    }

    public int getVaoId() {
        return this.vaoId;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }

    public void render() {
        glBindVertexArray(this.getVaoId());
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, this.vertexCount);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
