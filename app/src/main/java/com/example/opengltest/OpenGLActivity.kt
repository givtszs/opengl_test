package com.example.opengltest

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.example.opengltest.opengl_es.CubeGlRenderer
import com.example.opengltest.opengl_es.MyGLSurfaceView
import com.example.opengltest.opengl_es.MyGlRenderer
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
                    CubeGlRenderer()
                }
            )
        )
    }
}