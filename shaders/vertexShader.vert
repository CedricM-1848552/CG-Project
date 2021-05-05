#version 330 core

in vec3 position;
in vec2 textureCoordinates;

out vec2 pass_textureCoordinates;

void main() {
    gl_Position = vec4(position, 1.0);
    pass_textureCoordinates = textureCoordinates;
}
