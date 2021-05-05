package models;

import textures.Texture;

import static org.lwjgl.opengl.GL33.*;

public class TexturedModel extends Model {
    private final Texture texture;

    public TexturedModel(float[] coordinates, int[] indices, float[] textureCoordinates, Texture texture) {
        super(coordinates, indices);
        this.texture = texture;
        this.bind();
        storeDataInAttributeList(1, 2, textureCoordinates);
        this.unbind();
    }

    @Override
    protected void preRender() {
        super.preRender();
        glEnableVertexAttribArray(1);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.texture.getId());
    }

    @Override
    protected void postRender() {
        glDisableVertexAttribArray(1);
        super.postRender();
    }

    /**
     * @post only the model is deleted, the texture remains available
     */
    public void delete() {
        super.delete();
    }
}
