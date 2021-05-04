#version 330 core

in vec3 position;
out vec3 colour;

void main() {
    gl_Position = vec4(position.x, position.y, position.z, 1.0);
    colour = vec3(position.x + 0.5, position.y + 0.5, position.z);
}
