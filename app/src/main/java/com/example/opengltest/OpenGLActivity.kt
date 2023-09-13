package com.example.opengltest

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.example.opengltest.opengl_es.MyGLSurfaceView
import com.example.opengltest.opengl_es.MyGlRenderer


class OpenGLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shape = intent.getStringExtra(MainActivity.EXTRA_SHAPE)
        supportActionBar?.title = shape
        setContentView(
            MyGLSurfaceView(
                context = this,
                renderer = MyGlRenderer(shape)
            )
        )
    }
}