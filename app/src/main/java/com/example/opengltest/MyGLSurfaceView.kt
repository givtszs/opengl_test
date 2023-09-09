package com.example.opengltest

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView

class MyGLSurfaceView(
    context: Context,
    renderer: MyGlRenderer
) : GLSurfaceView(context) {

    init {
        // create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        // set the renderer for drawing on the GLSurface View
        setRenderer(renderer)

//        // draw the view on a change to the drawing data
//        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}