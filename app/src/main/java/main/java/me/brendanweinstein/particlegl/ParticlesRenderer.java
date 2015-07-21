package main.java.me.brendanweinstein.particlegl;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import main.java.me.brendanweinstein.particlegl.objects.ParticleSystem;
import main.java.me.brendanweinstein.particlegl.program.ParticleShaderProgram;

public class ParticlesRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = ParticlesRenderer.class.getSimpleName();

	private final Context context;

	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];
	private final float[] viewProjectionMatrix = new float[16];

	private ParticleShaderProgram particleProgram;
	private ParticleSystem particleSystem;
	private long globalStartTime;

	public ParticlesRenderer(Context context) {
		this.context = context;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		glEnable(GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		//GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);

		particleProgram = new ParticleShaderProgram(context);
		globalStartTime = System.nanoTime();

		float time = globalStartTime / 1000000000f;
		particleSystem = new ParticleSystem(context, time);
	}
	
	public void handleTouchPress(float normalizedX, float normalizedY) {
		
	}
	
	public void handleTouchDrag(float normalizedX, float normalizedY) {
		
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		glViewport(0, 0, width, height);

		float mRatio = (float) width / height;
		int offset = 0;
		float left = -mRatio;
		float right = mRatio;
		float bottom = -1f;
		float top = 1f;
		float near = 3f;
		float far = 7f;
		Matrix.frustumM(projectionMatrix, offset, left, right, bottom, top,
				near, far);

		// Set the camera position (View matrix)
		int rmOffset = 0;
		float eyeX = 0.0f;
		float eyeY = 0.0f;
		float eyeZ = 3f;
		float centerX = 0f;
		float centerY = 0f;
		float centerZ = 0f;
		float upX = 0f;
		float upY = 1.0f;
		float upZ = 0.0f;
		Matrix.setLookAtM(viewMatrix, rmOffset, eyeX, eyeY, eyeZ, centerX,
				centerY, centerZ, upX, upY, upZ);

		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
	}
	
	int frameCount = 0;

	@Override
	public void onDrawFrame(GL10 gl) {
		glClear(GL_COLOR_BUFFER_BIT);

		float currentTime = SystemClock.uptimeMillis();

		particleProgram.useProgram();
		particleProgram.setUniforms(viewProjectionMatrix, currentTime);

		particleSystem.bindData(particleProgram);
		particleSystem.draw();
		
		/*float[] vertexData = new float[] { 0.1f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, currentTime };
		FloatBuffer floatBuffer = ByteBuffer
				.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
		floatBuffer.position(0);
		glVertexAttribPointer(particleProgram.getPositionAttributeLocation(), 3, GL_FLOAT,
				false, 7 * 4, floatBuffer);
		glEnableVertexAttribArray(particleProgram.getPositionAttributeLocation());
		floatBuffer.position(0);
		floatBuffer.position(3);

		glVertexAttribPointer(particleProgram.getColorAttributeLocation(), 3, GL_FLOAT,
				false, 7 * 4, floatBuffer);
		glEnableVertexAttribArray(particleProgram.getColorAttributeLocation());
		floatBuffer.position(0);
		floatBuffer.position(6);
		
		glVertexAttribPointer(particleProgram.getParticleStartTimeAttributeLocation(), 3, GL_FLOAT,
				false, 7 * 4, floatBuffer);
		glEnableVertexAttribArray(particleProgram.getParticleStartTimeAttributeLocation());
		
		floatBuffer.position(0);
		glDrawArrays(GL_POINTS, 0, 1);*/
	}

}