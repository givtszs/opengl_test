package com.example.opengltest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.opengltest.opengl_es.renderers.CubeGlRenderer
import com.example.opengltest.opengl_es.MyGLSurfaceView
import com.example.opengltest.opengl_es.renderers.MyGlRenderer
import com.example.opengltest.opengl_es.shapes.Shapes


class OpenGLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shape = intent.getStringExtra(MainActivity.EXTRA_SHAPE)
        Log.d("OpenGLActivity", "shape: $shape")
        supportActionBar?.title = shape
        setContentView(
            MyGLSurfaceView(
                context = this@OpenGLActivity,
                renderer = if (shape != Shapes.CUBE.shapeName) {
                    MyGlRenderer(shape,this@OpenGLActivity)
                } else {
                    CubeGlRenderer(this@OpenGLActivity)
                }
            )
        )
    }
}