package models;

import static org.lwjgl.opengl.GL33.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import shaders.Shader;
import textures.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * @author Cédric Meukens
 */
public class Model {
    private String directory;
    private ArrayList<Mesh> meshes = new ArrayList<>();
    private ArrayList<Texture> loadedTextures = new ArrayList<>();

    public void delete() {
        for (var texture : this.loadedTextures) {
            texture.delete();
        }
    }

    /**
     * Constructor: creates a model from a file
     * @param path the path of the 3D model file
     */
    public Model(String path) {
         AIScene scene = Assimp.aiImportFile(path, Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenNormals);

        // Checks if the scene and root node of the scene exist and if the returned data is complete
        if ((scene == null) || (scene.mRootNode() == null)  || ((scene.mFlags() & Assimp.AI_SCENE_FLAGS_INCOMPLETE) == 1)) {
            System.err.println("Import error: " + Assimp.aiGetErrorString());
            return;
        }
        this.directory = path.substring(0, path.lastIndexOf('/'));
        processMeshes(scene);
    }

    private void processMeshes(AIScene scene) {
        int numMeshes = scene.mNumMeshes();
        PointerBuffer mMeshes = scene.mMeshes();

        for (int i = 0; i < numMeshes; ++i) {
            AIMesh mesh = AIMesh.create(mMeshes.get(i));
            meshes.add(processMesh(mesh, scene));
        }
    }

    private Mesh processMesh(AIMesh mesh, AIScene scene) {
        ArrayList<Vertex> vertices = processVertices(mesh);
        ArrayList<Integer> indices = processIndices(mesh);
        ArrayList<Texture> textures = processTextures(mesh, scene);

        return new Mesh(vertices, indices, textures);
    }

    private ArrayList<Vertex> processVertices(AIMesh mesh) {
        ArrayList<Vertex> vertices = new ArrayList<>();
        int numVertices = mesh.mNumVertices();

        for (int i = 0; i < numVertices; ++i) {
            Vector3f coordinates = new Vector3f(
                    mesh.mVertices().get(i).x(),
                    mesh.mVertices().get(i).y(),
                    mesh.mVertices().get(i).z());

            Vector3f normal = new Vector3f(
                    mesh.mNormals().get(i).x(),
                    mesh.mNormals().get(i).y(),
                    mesh.mNormals().get(i).z());

            Vector2f texture;

            if (mesh.mTextureCoords(0) != null) {
                texture = new Vector2f(
                        mesh.mTextureCoords(0).get(i).x(),
                        1 - mesh.mTextureCoords(0).get(i).y());
            } else {
                texture = new Vector2f(0, 0);
                System.out.println("No texture coordinates");
            }

            vertices.add(new Vertex(coordinates, normal, texture));
        }
        return vertices;
    }

    private ArrayList<Integer> processIndices(AIMesh mesh) {
        ArrayList<Integer> indices = new ArrayList<>();
        int numFaces = mesh.mNumFaces();

        for (int i = 0; i < numFaces; ++i) {
            AIFace face = mesh.mFaces().get(i);

            for (int j = 0; j < face.mNumIndices(); ++j)
                indices.add(face.mIndices().get(j));
        }
        return indices;
    }

    private ArrayList<Texture> processTextures(AIMesh mesh, AIScene scene) {
        ArrayList<Texture> textures = new ArrayList<>();

        if (mesh.mMaterialIndex() >= 0) {
            AIMaterial material = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));

            ArrayList<Texture> diffuses = loadTextures(material, Assimp.aiTextureType_DIFFUSE, "textureDiffuse");
            textures.addAll(diffuses);

            ArrayList<Texture> speculars = loadTextures(material, Assimp.aiTextureType_SPECULAR, "textureSpecular");
            textures.addAll(speculars);
        }
        return textures;
    }

    private ArrayList<Texture> loadTextures(AIMaterial material, int textureType, String type) {
        ArrayList<Texture> textures = new ArrayList<>();
        int textureCount = Assimp.aiGetMaterialTextureCount(material, textureType);

        for (int i = 0; i < textureCount; ++i) {
            AIString path = AIString.calloc();
            Assimp.aiGetMaterialTexture(material, textureType, i, path, null, null, null, null, null, (int[]) null);
            boolean alreadyLoaded = false;
            for (Texture loadedTexture : loadedTextures) {
                if (loadedTexture.getPath().equals(directory + "/" + path.dataString())) {
                    textures.add(loadedTexture);
                    alreadyLoaded = true;
                    break;
                }
            }
            if (!alreadyLoaded) {
                Texture texture = new Texture(directory + "/" + path.dataString());
                texture.setType(type);
                textures.add(texture);
                loadedTextures.add(texture);
            }
        }
        return textures;
    }

    public void render(Shader shader) {
        for (Mesh mesh : meshes) mesh.render(shader);
    }
}
