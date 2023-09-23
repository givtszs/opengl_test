package com.example.opengltest.opengl_es.renderers

import android.content.Context
import android.opengl.Matrix
import android.util.Log
import com.example.opengltest.opengl_es.shapes.OpenGLShape
import com.example.opengltest.opengl_es.shapes.Shapes
import com.example.opengltest.opengl_es.shapes.Square
import com.example.opengltest.opengl_es.shapes.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGlRenderer(
    private val shapeType: String?,
    private val context: Context,
) : BaseRenderer() {
    private var shape: OpenGLShape? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        // initialize shape
        shape = when (shapeType) {
            Shapes.TRIANGLE.shapeName -> Triangle()
            Shapes.SQUARE.shapeName -> Square()
            else -> null
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        Log.d("MyGlRenderer", "onDrawFrame is called")

//        Matrix.setIdentityM(rotationMatrix, 0)
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and viZew transformation
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Create a rotation transformation for the shape
//        val time = SystemClock.uptimeMillis() % 4000L
//        println("time: $time")
//        angle = 0.090f * time.toInt()
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)

        // this matrix will hold the combination of rotation, view and projection matrices.
        val combinedMatrix = FloatArray(16)

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(combinedMatrix, 0, mvpMatrix, 0, rotationMatrix, 0)

        // Draw shape
        shape?.draw(combinedMatrix, context)
    }
}