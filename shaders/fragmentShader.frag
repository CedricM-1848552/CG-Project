#version 330 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    float normalDotLight = dot(unitNormal, unitLightVector);
    float brightness = max(normalDotLight, 0.0f);
    vec3 diffuse = brightness * lightColour;

    colour = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoordinates);
}
