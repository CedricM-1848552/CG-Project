#version 330 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[2];

out vec4 colour;

uniform sampler2D textureSampler;
uniform vec3 lightColour[2];

void main() {
    vec3 unitNormal = normalize(surfaceNormal);

    vec3 totalDiffuse = vec3(0.0);

    for (int i = 0; i < 2; i++) {
        vec3 unitLightVector = normalize(toLightVector[i]);
        float normalDotLight = dot(unitNormal, unitLightVector);
        float brightness = max(normalDotLight, 0.0f);
        totalDiffuse = totalDiffuse + brightness * lightColour[i];
    }

    totalDiffuse = max(totalDiffuse, 0.4f);

    colour = vec4(totalDiffuse, 1.0) * texture(textureSampler, pass_textureCoordinates);
}
