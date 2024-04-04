#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;
uniform float Radius;
uniform float Health;
uniform float MaxHealth;
const vec3 SEPIA = vec3(1.2, 1.0, 0.8); 
void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;
	float rad = Radius+(((Health-MaxHealth)/(-MaxHealth))*5);
    for(float r = -rad; r <= rad; r += 1.0) {
        vec4 sample = texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir);

		// Accumulate average alpha
        totalAlpha = totalAlpha + sample.a;
        totalSamples = totalSamples + 1.0;

		// Accumulate smoothed blur
        float strength = 1.0 - abs(r / rad);
        totalStrength = totalStrength + strength;
        blurred = blurred + sample;
    }
	
	 float gray = dot(blurred.rgb, vec3(0.299, 0.587, 0.114));
	 vec3 grayBlur = vec3(mix(blurred.rgb, vec3(gray), (Health-MaxHealth)/(-MaxHealth)));
    gl_FragColor = vec4(grayBlur.rgb / (rad * 2.0 + 1.0), totalAlpha);
}

