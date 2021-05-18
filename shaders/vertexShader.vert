#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 textureCoordinates;

out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector[2];

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[2];

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textureCoordinates = textureCoordinates;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0f)).xyz;
    for (int i = 0; i < 2; i++) {
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    }
}
