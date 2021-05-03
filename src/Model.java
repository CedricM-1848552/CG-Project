import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;


/**
 * @author Cédric Meukens
 */
public class Model {
    private final int vaoId;
    private final int vboIdVertices;
    private final int vboIdIndices;
    private final int vertexCount;

    /**
     * Load a model from vertex positions
     * @param positions The positions of the vertices in the model
     * @return The model loaded from the positions
     */
    public static Model fromPositions(float[] positions, int[] indices) {
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        int vboIdIndices = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIdIndices);
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        int vboIdVertices = storeDataInAttributeList(0, positions);
        glBindVertexArray(0);
        return new Model(vaoId, vboIdVertices, vboIdIndices, indices.length);
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

//    private IntBuffer createIntBuffer(int[] data) {
//        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
//        buffer.put(data);
//        buffer.flip();
//        return buffer;
//    }

//    private FloatBuffer createFloatBuffer(float[] data) {
//        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
//        buffer.put(data);
//        buffer.flip();
//        return buffer;
//    }

    private Model(int vaoId, int vboIdVertices, int vboIdIndices, int vertexCount) {
        this.vaoId = vaoId;
        this.vboIdVertices = vboIdVertices;
        this.vboIdIndices = vboIdIndices;
        this.vertexCount = vertexCount;
    }

    public void delete() {
        glDeleteVertexArrays(this.vaoId);
        glDeleteBuffers(vboIdVertices);
        glDeleteBuffers(vboIdIndices);
    }

    public void render() {
        glBindVertexArray(this.vaoId);
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, this.vertexCount, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
