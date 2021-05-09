package models;

import static org.lwjgl.opengl.GL33.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Cédric Meukens
 */
public class Model {
    private final int vao;
    private final int vboVertices;
    private int vboIndices;
    private final int vertexCount;

    /**
     * Load a model from vertex coordinates
     * @param coordinates The coordinates of the vertices in the model
     */
    public  Model(float[] coordinates, int[] indices) {
        this.vao = glGenVertexArrays();
        this.bind();
        this.bindIndicesBuffer(indices);
        this.vboVertices = storeDataInAttributeList(0, 3, coordinates);
        this.vertexCount = indices.length;
        this.unbind();
    }

    /**
     * Store data in a new VBO
     * @param attributeIndex Index in the VBO to put the data
     * @param attributeSize The size of each attribute
     * @param data The data to insert
     * @return The id of the VBO containing the data
     */
    protected static int storeDataInAttributeList(int attributeIndex, int attributeSize, float[] data) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeIndex, attributeSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    private void bindIndicesBuffer(int[] indices) {
        this.vboIndices = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vboIndices);
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    protected void bind() {
        glBindVertexArray(this.vao);
    }

    protected void unbind() {
        glBindVertexArray(0);
    }

    public void delete() {
        glDeleteVertexArrays(this.vao);
        glDeleteBuffers(vboVertices);
        glDeleteBuffers(vboIndices);
    }

    public final void render() {
        bind();
        preRender();
        glDrawElements(GL_TRIANGLES, this.getVertexCount(), GL_UNSIGNED_INT, 0);
        postRender();
        unbind();
    }

    protected void preRender() {
        glEnableVertexAttribArray(0);
    }

    protected void postRender() {
        glDisableVertexAttribArray(0);
    }

    protected int getVertexCount() {
        return this.vertexCount;
    }
}
