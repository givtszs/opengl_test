package com.example.opengltest.opengl_es

import android.opengl.GLES20

class ShaderHelper {
    companion object {
        fun compileShader(shaderType: Int, shaderResource: String): Int {
            return GLES20.glCreateShader(shaderType).also { shader ->
                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderResource)
                GLES20.glCompileShader(shader)
            }
        }

        fun createAndLinkProgram(vertexShader: Int, fragmentShader: Int): Int {
            return GLES20.glCreateProgram().also { program ->
                // add the vertex shader to program
                GLES20.glAttachShader(program, vertexShader)

                // add the fragment shader to program
                GLES20.glAttachShader(program, fragmentShader)

                // create OpenGL ES program executables
                GLES20.glLinkProgram(program)
            }
        }
    }
}