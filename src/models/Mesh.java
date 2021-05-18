package models;

import org.lwjgl.BufferUtils;
import shaders.Shader;
import textures.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {
    private ArrayList<Vertex> vertices;
    private ArrayList<Integer> indices;
    private ArrayList<Texture> textures;
    private int vao;
    private int vbo;
    private int ebo;

    public Mesh(ArrayList<Vertex> vertices, ArrayList<Integer> indices, ArrayList<Texture> textures) {
        this.vertices = vertices;
        this.indices = indices;
        this.textures = textures;

        setBuffers();
    }

    public void render(Shader shader) {
        int diffuseNum = 1;
        int specularNum = 1;

        for (int i = 0; i < textures.size(); ++i) {
            glActiveTexture(GL_TEXTURE0 + i);
            String name = textures.get(i).getType();
            int number = 0;
            if (name.equals("textureDiffuse"))
                number = diffuseNum++;
            else if (name.equals("textureSpecular"))
                number = specularNum++;
            shader.setFloat("material." + name + number, i);
            glBindTexture(GL_TEXTURE_2D, textures.get(i).getId());
        }
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void setBuffers() {
        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();
        this.ebo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.size() * Vertex.NUM_FLOATS);
        for (Vertex vertex : vertices)
            buffer.put(vertex.toFloatArray());
        buffer.flip();                                          // To prepare the buffer to be read from
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        IntBuffer intBuffer = BufferUtils.createIntBuffer(indices.size());
        for (int i : indices)
            intBuffer.put(i);
        intBuffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.SIZE, Vertex.NORMALS_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, Vertex.SIZE, Vertex.TEXTURE_OFFSET);
        glEnableVertexAttribArray(2);

        glBindVertexArray(0);
    }
}
