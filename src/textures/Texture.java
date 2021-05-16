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
    private String type;
    private String path;

    /**
     * Load a texture
     * @param textureFile name of the texture
     * @param wrapping The wrapping protocol, one of <code>GL_REPEAT</code>, <code>GL_MIRRORED_REPEAT</code>, <code>GL_CLAMP_TO_EDGE</code> or <code>GL_CLAMP_TO_BORDER</code>
     * @param filtering The filtering protocol, one of <code>GL_NEAREST</code> or <code>GL_LINEAR</code>
     * @pre the texture must be situated in the <code>res/</code> folder and must be of the <code>png</code> format
     */
    public Texture(String textureFile, int wrapping, int filtering) {
        this.texture = glGenTextures();
        this.path = textureFile;
        glBindTexture(GL_TEXTURE_2D, this.texture);

        // Set wrapping protocol
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapping);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapping);

        // Set filtering protocol
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filtering);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filtering);

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

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        // Generate MipMap
        glGenerateMipmap(GL_TEXTURE_2D);

        // Cleanup memory
        stbi_image_free(image);
    }

    public Texture(String textureFile) {
        this.texture = glGenTextures();
        this.path = textureFile;

        MemoryStack stack = MemoryStack.stackGet();
        IntBuffer width = stack.mallocInt(1);
        IntBuffer height = stack.mallocInt(1);
        IntBuffer numComponents = stack.mallocInt(1);;
        ByteBuffer data = stbi_load(textureFile, width, height, numComponents, 0);

        if (data != null) {
            int format = 0;
            if (numComponents.get(0) == 1)
                format = GL_RED;
            else if (numComponents.get(0) == 2)
                format = GL_RG;
            else if (numComponents.get(0) == 3)
                format = GL_RGB;
            else if (numComponents.get(0) == 4)
                format = GL_RGBA;

            glBindTexture(GL_TEXTURE_2D, texture);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, format, width.get(0), height.get(0), 0, format, GL_UNSIGNED_BYTE, data);
            glGenerateMipmap(GL_TEXTURE_2D);

            stbi_image_free(data);
        }
        else {
            System.out.println("Failed to load texture at path: " + path);
        }
    }

    public void delete() {
        glDeleteTextures(this.texture);
    }

    public int getId() {
        return this.texture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
