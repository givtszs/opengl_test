package com.example.opengltest

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MyGLSurfaceView(this, MyGlRenderer()))
//        setContentView(R.layout.activity_main)
    }
}