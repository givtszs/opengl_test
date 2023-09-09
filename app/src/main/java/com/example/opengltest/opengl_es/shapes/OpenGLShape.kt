package com.example.opengltest.opengl_es.shapes

import android.opengl.GLES20
import java.nio.FloatBuffer

const val COORDS_PER_VERTEX = 3

abstract class OpenGLShape(
    val coords: FloatArray
) {
    protected abstract var vertexBuffer: FloatBuffer
    var color: FloatArray = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
    protected var program: Int = 0
    protected var vertexCount: Int = 0
    protected val vertexStride: Int = COORDS_PER_VERTEX * 4

    private val vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        // Note that the uMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        "  gl_Position = uMVPMatrix * vPosition;" +
        "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}"

    init {
        vertexCount = coords.size / COORDS_PER_VERTEX
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // Create empty OpenGL ES Program
        program = GLES20.glCreateProgram().also { program ->
            // add the vertex shader to program
            GLES20.glAttachShader(program, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(program, fragmentShader)

            // create OpenGL ES program executables
            GLES20.glLinkProgram(program)
        }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->
            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    abstract fun draw(mvpMatrix: FloatArray? = null)

    override fun toString(): String {
        return """
            coords: $coords;
            color: $color;
            program: $program;
            vertexCount: $vertexCount,
            vertexBuffer: $vertexBuffer
        """.trimIndent()
    }
}