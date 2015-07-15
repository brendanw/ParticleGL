package com.particlegl.objects;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.particlegl.Constants;
import com.particlegl.R;
import com.particlegl.data.VertexArray;
import com.particlegl.program.ParticleShaderProgram;
import com.particlegl.util.Geometry.Point;

public class ParticleSystem {

	private static final String TAG = ParticleSystem.class.getSimpleName();

	private static final int POSITION_COMPONENT_COUNT = 3;
	private static final int COLOR_COMPONENT_COUNT = 4;
	private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;
	private static final int SIZE_COMPONENT_COUNT = 1;

	private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT
			+ COLOR_COMPONENT_COUNT
			+ PARTICLE_START_TIME_COMPONENT_COUNT
			+ SIZE_COMPONENT_COUNT;

	private static final int STRIDE = TOTAL_COMPONENT_COUNT
			* Constants.BYTES_PER_FLOAT;

	private final float[] particles;
	private final VertexArray vertexArray;
	private final int maxParticleCount;

	private int nextParticle = 0;

	public ParticleSystem(Context context, float startTime) {
		BitmapDrawable draw = (BitmapDrawable) context.getResources()
				.getDrawable(R.drawable.brendanw_grayscale);
		Bitmap bitmap = draw.getBitmap();
		maxParticleCount = bitmap.getWidth() * bitmap.getHeight();
		particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
		vertexArray = new VertexArray(particles);
		setupParticles(bitmap, context, startTime);
	}

	private static final float X_POSITION_OFFSET = 0.6f;
	private static final float Y_POSITION_OFFSET = 0.5f;
	
	/**
	 * Reads in bitmap. Creates corresponding particle for each pixel
	 */
	public void setupParticles(Bitmap bitmap, Context context, float startTime) {
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				float xPos = x / (float) (bitmap.getWidth() - 1);
				xPos -= X_POSITION_OFFSET;
				float yPos = y / (float) (bitmap.getHeight() - 1);
				yPos = (1.0f - yPos);
				yPos -= Y_POSITION_OFFSET;
				int color = bitmap.getPixel(x, y);
				if(isTransparent(bitmap, x, y)) {
					color = Color.WHITE;
				}
				Point p = new Point(xPos, yPos, 0.0f);
				float size = (255-Color.red(color)) / 14f;
				Log.d(TAG, "size: "+size);
				addParticle(p, color, startTime, size);
			}
		}
	}
	
	boolean isTransparent(Bitmap bitmap, int x, int y) {
		int pixel = bitmap.getPixel(x, y);
		if ((pixel >> 24) == 0x00) {
			return true;
		}
		return false;
	}

	public void addParticle(Point position, int color, float particleStartTime, float size) {
		final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;

		int currentOffset = particleOffset;
		nextParticle++;

		particles[currentOffset++] = position.x;
		particles[currentOffset++] = position.y;
		particles[currentOffset++] = position.z;
		
		particles[currentOffset++] = Color.red(color) / 255f;
		particles[currentOffset++] = Color.green(color) / 255f;
		particles[currentOffset++] = Color.blue(color) / 255f;
		particles[currentOffset++] = 0.6f;

		particles[currentOffset++] = particleStartTime;
		
		particles[currentOffset++] = size;

		vertexArray.updateBuffer(particles, particleOffset,
				TOTAL_COMPONENT_COUNT);
	}

	public void bindData(ParticleShaderProgram particleProgram) {
		int dataOffset = 0;
		vertexArray.setVertexAttribPointer(dataOffset,
				particleProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT, STRIDE);
		dataOffset += POSITION_COMPONENT_COUNT;

		vertexArray.setVertexAttribPointer(dataOffset,
				particleProgram.getColorAttributeLocation(),
				COLOR_COMPONENT_COUNT, STRIDE);
		dataOffset += COLOR_COMPONENT_COUNT;

		vertexArray.setVertexAttribPointer(dataOffset,
				particleProgram.getParticleStartTimeAttributeLocation(),
				PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
		dataOffset += PARTICLE_START_TIME_COMPONENT_COUNT;
		
		vertexArray.setVertexAttribPointer(dataOffset,
				particleProgram.getPartizleSizeAttributeLocation(),
				SIZE_COMPONENT_COUNT, STRIDE);
	}

	public void draw() {
		glDrawArrays(GL_POINTS, 0, maxParticleCount);
	}

}
