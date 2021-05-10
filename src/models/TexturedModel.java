package models;

import org.joml.Vector2f;
import org.joml.Vector3f;
import textures.Texture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class TexturedModel extends Model {
    private final Texture texture;

    public TexturedModel(float[] coordinates, int[] indices, float[] textureCoordinates, float[] normals, Texture texture) {
        super(coordinates, indices);
        this.texture = texture;
        this.bind();
        storeDataInAttributeList(1, 2, textureCoordinates);
        storeDataInAttributeList(2, 3, normals);
        this.unbind();
    }

    public static TexturedModel fromObjFile(String objFile, Texture texture) {
        FileReader fr = null;
        try {
            fr = new FileReader(new File(objFile));
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file " + objFile);
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(fr);
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray = null;
        float[] textureArray = null;
        float[] normalsArray = null;
        int[] indicesArray = null;

        var reading = true;
        String line = null;
        String[] currentLine;
        try {
            while (reading) {
                line = reader.readLine();
                currentLine = line.split(" ");

                switch (currentLine[0]) {
                    case "v" -> {
                        Vector3f v = new Vector3f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]),
                                Float.parseFloat(currentLine[3]));
                        vertices.add(v);
                    }
                    case "vt" -> {
                        Vector2f vt = new Vector2f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]));
                        textures.add(vt);
                    }
                    case "vn" -> {
                        Vector3f vn = new Vector3f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]),
                                Float.parseFloat(currentLine[3]));
                        normals.add(vn);
                    }
                    case "f" -> {
                        textureArray = new float[vertices.size() * 2];
                        normalsArray = new float[vertices.size() * 3];
                        reading = false;
                    }
                }
            }

            while (line != null) {
                currentLine = line.split(" ");
                if (!currentLine[0].equals("f")) {
                    line = reader.readLine();
                    continue;
                }

                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);

                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        for (int i = 0; i < vertices.size(); i++) {
            verticesArray[i*3] = vertices.get(i).x;
            verticesArray[i*3 + 1] = vertices.get(i).y;
            verticesArray[i*3 + 2] = vertices.get(i).z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return new TexturedModel(verticesArray, indicesArray, textureArray, normalsArray, texture);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        int vertexIndex = Integer.parseInt(vertexData[0]) - 1;
        indices.add(vertexIndex);

        Vector2f textureIndex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[vertexIndex*2] = textureIndex.x;
        textureArray[vertexIndex*2 + 1] = 1 - textureIndex.y;

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[vertexIndex * 3] = currentNorm.x;
        normalsArray[vertexIndex * 3 + 1] = currentNorm.y;
        normalsArray[vertexIndex * 3 + 2] = currentNorm.z;
    }

    @Override
    protected void preRender() {
        super.preRender();
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.texture.getId());
    }

    @Override
    protected void postRender() {
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        super.postRender();
    }

    /**
     * @post only the model is deleted, the texture remains available
     */
    public void delete() {
        super.delete();
    }
}
