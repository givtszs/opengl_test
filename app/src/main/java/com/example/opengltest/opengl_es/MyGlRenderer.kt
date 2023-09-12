package com.example.opengltest.opengl_es

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import com.example.opengltest.opengl_es.shapes.OpenGLShape
import com.example.opengltest.opengl_es.shapes.Shapes
import com.example.opengltest.opengl_es.shapes.Square
import com.example.opengltest.opengl_es.shapes.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGlRenderer(private val shapeType: String?) : GLSurfaceView.Renderer {
    private var shape: OpenGLShape? = null
    private val modelViewProjectionMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        // initialize shape
        shape = when (shapeType) {
            Shapes.TRIANGLE.shapeName -> {
                Triangle(
                    shapeCoords = floatArrayOf(
                        0.0f, 0.31100425f, 0.0f,
                        -0.3f, -0.31100425f, 0.0f,
                        0.3f, -0.31100425f, 0.0f
                    ),
                    color = floatArrayOf(0.404f, 0.314f, 0.643f, 1.0f)
                )
            }

            Shapes.SQUARE.shapeName -> {
                Square(
                    shapeCoords = floatArrayOf(
                        -0.3f, 0.3f, 0.0f,
                        -0.3f, -0.3f, 0.0f,
                        0.3f, -0.3f, 0.0f,
                        0.3f, 0.3f, 0.0f
                    ),
                    color = floatArrayOf(0.404f, 0.314f, 0.643f, 1.0f)
                )
            }

            else -> null
        }
    }

    override fun onSurfaceChanged(gL10: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(gL10: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Create a rotation transformation for the shape
        val time = SystemClock.uptimeMillis() % 4000L
        println("time: $time")
        val angle = 0.090f * time.toInt()
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)

        // this matrix will hold the combination of rotation, view and projection matrices.
        val combinedMatrix = FloatArray(16)

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(combinedMatrix, 0, modelViewProjectionMatrix, 0, rotationMatrix, 0)

        // Draw shape
        shape?.draw(combinedMatrix)
    }
}