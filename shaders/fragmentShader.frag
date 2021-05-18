#version 330 core
#define lightCount 6

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[lightCount];

out vec4 colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour[lightCount];
uniform vec3 attenuation[lightCount];

void main() {
    vec3 unitNormal = normalize(surfaceNormal);

    vec3 totalDiffuse = vec3(0.0);

    for (int i = 0; i < lightCount; i++) {
        float distance = length(toLightVector[i]);
        float attenuationFactor = attenuation[i].x + attenuation[i].y * distance + attenuation[i].z * distance * distance;
        vec3 unitLightVector = normalize(toLightVector[i]);
        float normalDotLight = dot(unitNormal, unitLightVector);
        float brightness = normalDotLight;
        totalDiffuse = totalDiffuse + (brightness * lightColour[i])/attenuationFactor;
    }

    colour = vec4(totalDiffuse, 1.0) * texture(textureSampler, pass_textureCoordinates);
}
