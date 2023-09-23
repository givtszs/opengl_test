package com.example.opengltest.opengl_es.renderers

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import com.example.opengltest.opengl_es.shapes.Cube
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class CubeGlRenderer(private val context: Context) : BaseRenderer() {
    private lateinit var cube: Cube

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)

        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
//        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
//        GLES20.glClearDepthf(1.0f)

        // Position the eye in front of the origin.
        val eyeX = 0.0f
        val eyeY = 0.5f
        val eyeZ = -1.5f

        // We are looking toward the distance
        val lookX = 0.0f
        val lookY = 0.0f
        val lookZ = 0.0f

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        val upX = 0.0f
        val upY = 1.0f
        val upZ = 0.0f

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)
        cube = Cube()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        val ratio = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1f, 10.0f)
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)

        // Calculate the projection and view transformation
//        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Apply rotation to the cube
        Matrix.setRotateM(rotationMatrix, 0, angle, 0.0f, 1.0f, 0.0f)
//        rotationAngle += 1.0f // Rotate by 1 degree per frame

        // Combine the rotation matrix with the projection and camera view
        val scratch = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0)

        cube.draw(scratch, context)
    }
}