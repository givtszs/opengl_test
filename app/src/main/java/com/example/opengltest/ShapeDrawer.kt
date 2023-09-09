package com.example.opengltest

interface ShapeDrawer {
    /**
     *  Sets the position and color values to the shape’s vertex shader and fragment shader,
     *  and then executes the drawing function.
     */
    fun draw()
}