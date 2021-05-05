package textures;

import models.Model;
import org.lwjgl.system.MemoryStack;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final int texture;

    /**
     * Load a texture
     * @param textureFile name of the texture
     * @param wrapping The wrapping protocol, one of <code>GL_REPEAT</code>, <code>GL_MIRRORED_REPEAT</code>, <code>GL_CLAMP_TO_EDGE</code> or <code>GL_CLAMP_TO_BORDER</code>
     * @param filtering The filtering protocol, one of <code>GL_NEAREST</code> or <code>GL_LINEAR</code>
     * @pre the texture must be situated in the <code>res/</code> folder and must be of the <code>png</code> format
     */
    public Texture(String textureFile, int wrapping, int filtering) {
        this.texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.texture);

        // Set wrapping protocol
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapping);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapping);

        // Set filtering protocol
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filtering);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filtering);

        // Generate MipMap
        glGenerateMipmap(GL_TEXTURE_2D);

        // read texture file
        MemoryStack stack = MemoryStack.stackGet();
        IntBuffer w = stack.mallocInt(1);
        IntBuffer h = stack.mallocInt(1);
        IntBuffer comp = stack.mallocInt(1);
        ByteBuffer image = stbi_load(textureFile, w, h, comp, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load a texture file!"
                    + System.lineSeparator() + stbi_failure_reason());
        }

        int width = w.get();
        int height = h.get();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
    }

    public void delete() {
        glDeleteTextures(this.texture);
    }

    public int getId() {
        return this.texture;
    }
}
