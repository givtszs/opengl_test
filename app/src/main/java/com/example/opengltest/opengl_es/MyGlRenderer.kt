package com.example.opengltest.opengl_es

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import com.example.opengltest.MainActivity
import com.example.opengltest.opengl_es.shapes.OpenGLShape
import com.example.opengltest.opengl_es.shapes.Shapes
import com.example.opengltest.opengl_es.shapes.Square
import com.example.opengltest.opengl_es.shapes.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGlRenderer(private val shapeType: String?) : GLSurfaceView.Renderer {
    private var shape: OpenGLShape? = null

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        // initialize shape
        shape = when (shapeType) {
            Shapes.TRIANGLE.shapeName -> {
                Triangle(
                    shapeCoords = floatArrayOf(
                        0.0f, 0.62200844f, 0.0f,
                        -0.5f, -0.31100425f, 0.0f,
                        0.5f, -0.31100425f, 0.0f
                    ),
                    color = floatArrayOf(0.404f, 0.314f, 0.643f, 1.0f)
                )
            }

            Shapes.SQUARE.shapeName -> {
                Square(
                    shapeCoords = floatArrayOf(
                        -0.5f, 0.5f, 0.0f,
                        -0.5f, -0.5f, 0.0f,
                        0.5f, -0.5f, 0.0f,
                        0.5f, 0.5f, 0.0f
                    ),
                    color = floatArrayOf(0.404f, 0.314f, 0.643f, 1.0f)
                )
            }

            else -> null
        }
    }

    override fun onSurfaceChanged(gL10: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gL10: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        shape?.draw()
    }
}