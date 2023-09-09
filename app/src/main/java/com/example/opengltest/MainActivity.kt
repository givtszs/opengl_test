package com.example.opengltest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.opengltest.databinding.ActivityMainBinding
import com.example.opengltest.opengl_es.shapes.Shapes

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDrawTriangle.setOnClickListener {
            Intent(this@MainActivity, OpenGLActivity::class.java).also {
                it.putExtra(EXTRA_SHAPE, Shapes.TRIANGLE.shapeName)
                startActivity(it)
            }
        }

        binding.btnDrawSquare.setOnClickListener {
            Intent(this@MainActivity, OpenGLActivity::class.java).also {
                it.putExtra(EXTRA_SHAPE, Shapes.SQUARE.shapeName)
                startActivity(it)
            }
        }
    }

    companion object {
        const val EXTRA_SHAPE = "shape"
    }
}