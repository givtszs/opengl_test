package com.example.opengltest.opengl_es

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.example.opengltest.opengl_es.renderers.BaseRenderer

const val TOUCH_SCALE_FACTOR = 180.0f / 320

@SuppressLint("ViewConstructor")
class MyGLSurfaceView(
    context: Context,
    private var renderer: BaseRenderer,
) : GLSurfaceView(context) {

    private var previousX = 0f
    private var previousY = 0f

    init {
        // create an OpenGL     ES 2.0 context
        setEGLContextClientVersion(2)

        // set the renderer for drawing on the GLSurface View
        setRenderer(renderer)

        // draw the view on a change to the drawing data
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls.

        val x = event?.x ?: 0f
        val y = event?.y ?: 0f

        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx = x - previousX
                var dy = y - previousY

                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy *= -1
                }

                renderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
                requestRender()
            }
        }

        previousX = x
        previousY = y
        return true
    }
}