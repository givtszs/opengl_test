package com.example.opengltest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.opengltest.databinding.ActivityMainBinding
import com.example.opengltest.opengl_es.shapes.Shapes

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDrawTriangle.setOnClickListener {
            onClickListener(Shapes.TRIANGLE.shapeName)
        }

        binding.btnDrawSquare.setOnClickListener {
            onClickListener(Shapes.SQUARE.shapeName)
        }

        binding.btnDrawCube.setOnClickListener {
           onClickListener(Shapes.CUBE.shapeName)
        }
    }

    private fun onClickListener(shape: String) {
        Intent(this@MainActivity, OpenGLActivity::class.java).also {
            it.putExtra(EXTRA_SHAPE, shape)
            startActivity(it)
        }
    }

    companion object {
        const val EXTRA_SHAPE = "shape"
    }
}