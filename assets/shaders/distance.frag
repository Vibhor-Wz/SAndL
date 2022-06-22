#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoord;

uniform float u_smoothing;

uniform float outlineDistance; // Between 0 and 0.5, 0 = thick outline, 0.5 = no outline
uniform vec4 outlineColor;

void main() {
    if(u_smoothing>0.0){
        float distance = texture2D(u_texture, v_texCoord).a;
        float alpha = smoothstep(0.5 - u_smoothing, 0.5 + u_smoothing, distance);
        gl_FragColor = vec4(v_color.rgb, v_color.a * alpha);
    }else{
        gl_FragColor = v_color * texture2D(u_texture, v_texCoord);
    }

    if(outlineDistance>0.0){
        float distance = texture2D(u_texture, v_texCoord).a;
            float outlineFactor = smoothstep(0.5 - u_smoothing, 0.5 + u_smoothing, distance);
            vec4 color = mix(outlineColor, v_color, outlineFactor);
            float alpha = smoothstep(outlineDistance - u_smoothing, outlineDistance + u_smoothing, distance);
            gl_FragColor = vec4(color.rgb, color.a * alpha);
    }
}