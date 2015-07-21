precision mediump float;
varying vec4 v_Color;
varying float v_ElapsedTime;

void main()
{
	float xDistance = 0.5 - gl_PointCoord.x;
	float yDistance = 0.5 - gl_PointCoord.y;
	float distanceFromCenter = sqrt(xDistance * xDistance + yDistance * yDistance);
	if(distanceFromCenter > 0.5) {
		discard;
	} else {
		gl_FragColor = v_Color;
	}
}