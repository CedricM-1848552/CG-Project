#version 330 core

in vec2 pass_textureCoordinates;

out vec4 colour;

uniform sampler2D textureSampler;

void main() {
    colour = texture(textureSampler, pass_textureCoordinates);
}
