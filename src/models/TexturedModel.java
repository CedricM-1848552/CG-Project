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

    public void render() {
        bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.texture.getId());
        glDrawElements(GL_TRIANGLES, this.getVertexCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        unbind();
    }

    /**
     * @post only the model is deleted, the texture remains available
     */
    public void delete() {
        super.delete();
    }
}
