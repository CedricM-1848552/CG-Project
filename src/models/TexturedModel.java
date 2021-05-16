package models;

import textures.Texture;

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
