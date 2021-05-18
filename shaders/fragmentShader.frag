#version 330 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[2];

out vec4 colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour[2];
uniform vec3 attenuation[2];

void main() {
    vec3 unitNormal = normalize(surfaceNormal);

    vec3 totalDiffuse = vec3(0.0);

    for (int i = 0; i < 2; i++) {
        float distance = length(toLightVector[i]);
        float attenuationFactor = attenuation[i].x + attenuation[i].y * distance + attenuation[i].z * distance * distance;
        vec3 unitLightVector = normalize(toLightVector[i]);
        float normalDotLight = dot(unitNormal, unitLightVector);
        float brightness = max(normalDotLight, 0.0f);
        totalDiffuse = totalDiffuse + (brightness * lightColour[i])/attenuationFactor;
    }

    totalDiffuse = max(totalDiffuse, 0.2f);

    colour = vec4(totalDiffuse, 1.0) * texture(textureSampler, pass_textureCoordinates);
}
