package com.example.opengltest

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGlRenderer : GLSurfaceView.Renderer {
    private lateinit var triangle: Triangle
    private lateinit var square: Square

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        triangle = Triangle()
        square = Square()
    }

    override fun onSurfaceChanged(gL10: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gL10: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
//        triangle.draw()
        square.draw()
    }
}