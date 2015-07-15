package com.particlegl.program;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniformMatrix4fv;

import com.particlegl.R;

import android.content.Context;

public class ParticleShaderProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrixLocation;
    private final int uTimeLocation;
    
    // Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aParticleStartTimeLocation;
    private final int aSize;
    
    public ParticleShaderProgram(Context context) {
        super(context, R.raw.particle_vertex_shader,
            R.raw.particle_fragment_shader);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTimeLocation = glGetUniformLocation(program, U_TIME);
        
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aParticleStartTimeLocation = 
            glGetAttribLocation(program, A_PARTICLE_START_TIME);
        aSize = glGetAttribLocation(program, A_SIZE);
    }
	
    /*
    public void setUniforms(float[] matrix, float elapsedTime) {
     */ 
    public void setUniforms(float[] matrix, float elapsedTime) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1f(uTimeLocation, elapsedTime);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getColorAttributeLocation() {
        return aColorLocation;
    }  
    public int getParticleStartTimeAttributeLocation() {
        return aParticleStartTimeLocation;
    }
    
    public int getPartizleSizeAttributeLocation() {
    	return aSize;
    }
}
