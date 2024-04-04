#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform float Time;
uniform vec2 Frequency;
uniform vec2 WobbleAmount;
uniform vec2 resolution;

const vec4 Zero = vec4(0.0);
const vec4 Half = vec4(0.5);
const vec4 One = vec4(1.0);
const vec4 Two = vec4(2.0);

const float Pi = 3.1415926535;
const float PincushionAmount = 0.02;
const float CurvatureAmount = 0;
const float ScanlineAmount = 0.8;
const float ScanlineScale = 1;
const float ScanlineHeight = 5;
const float ScanlineBrightScale = 1.0;
const float ScanlineBrightOffset = 0.0;
const float ScanlineOffset = 0.0;
const vec3 Floor = vec3(0.05, 0.05, 0.05);
const vec3 Power = vec3(0.8, 0.8, 0.8);

void main()
{
	float xOffset = sin(texCoord.y * Frequency.x + Time * 3.1415926535 * 2.0) * WobbleAmount.x;
    float yOffset = cos(texCoord.x * Frequency.y + Time * 3.1415926535 * 2.0) * WobbleAmount.y;
    vec2 offset = vec2(xOffset, 0);
	vec4 color = texture2D(DiffuseSampler, texCoord+(offset/4));

	float dis = sqrt((texCoord.x - 0.5)*(texCoord.x - 0.5)+(texCoord.y - 0.5)*(texCoord.y - 0.5));
	color.rgb *= 1.0-dis;

	
	const vec3 proportions = vec3(0.30, 0.79, 0.11);
	float intentisy = dot(color.rgb, proportions);
 
	// adjust contrast
	intentisy = clamp(1.4 * (intentisy - 0.5) + 0.5, 0.0, 1.0);

		float green = clamp(intentisy / 0.8, 0.0, 1.0);
    vec3 OutColor = vec3(0.03, green*1.25+0.1, 0.03);
	    
	
	
	 vec2 PinUnitCoord = texCoord * Two.xy - One.xy;
    float PincushionR2 = pow(length(PinUnitCoord), 2.0);
    vec2 PincushionCurve = PinUnitCoord * PincushionAmount * PincushionR2;
    vec2 ScanCoord = texCoord;

    ScanCoord *= One.xy - PincushionAmount * 0.2;
    ScanCoord += PincushionAmount * 0.1;
    ScanCoord += PincushionCurve;

    vec2 CurvatureClipCurve = PinUnitCoord * CurvatureAmount * PincushionR2;
    vec2 ScreenClipCoord = texCoord;
    ScreenClipCoord -= Half.xy;
    ScreenClipCoord *= One.xy - CurvatureAmount * 0.2;
    ScreenClipCoord += Half.xy;
    ScreenClipCoord += CurvatureClipCurve;

    // -- Alpha Clipping --
   
   //if (ScanCoord.x < 0.0) OutColor.rgb-=-0.5*(ScanCoord.x-1);
   //if (ScanCoord.y < 0.0) OutColor.rgb-=-0.5*(ScanCoord.y-1);
   //if (ScanCoord.x > 1.0) OutColor.rgb-=0.5*(ScanCoord.x);
   //if (ScanCoord.y > 1.0) OutColor.rgb-=0.5*(ScanCoord.y);
    // -- Scanline Simulation --
    float InnerSine = ScanCoord.y * InSize.y * ScanlineScale * 0.25;
    float ScanBrightMod = sin(InnerSine * Pi + ScanlineOffset * InSize.y * 0.25);
    float ScanBrightness = mix(1.0, (pow(ScanBrightMod * ScanBrightMod, ScanlineHeight) * ScanlineBrightScale + 1.0) * 0.5, ScanlineAmount);
    vec3 ScanlineTexel = OutColor.rgb * ScanBrightness;

    // -- Color Compression (increasing the floor of the signal without affecting the ceiling) --
    ScanlineTexel = Floor + (One.xyz - Floor) * ScanlineTexel;

    ScanlineTexel.rgb = pow(ScanlineTexel.rgb, Power);
	
	
    gl_FragColor = vec4(ScanlineTexel.rgb, 1.0);
	
}