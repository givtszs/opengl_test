package com.example.opengltest.opengl_es.shapes

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import androidx.annotation.DrawableRes
import com.example.opengltest.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

const val COORDS_PER_VERTEX = 3
const val TEXTURE_COORDS = 2

abstract class OpenGLShape(
    val coords: FloatArray,
    val textureCoords: FloatArray
) {
    protected var vertexBuffer: FloatBuffer
    protected var textureBuffer: FloatBuffer
    var color: FloatArray = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
    protected var program: Int = 0
    protected var vertexCount: Int = 0
    protected val vertexStride: Int = COORDS_PER_VERTEX * 4

    private val vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "attribute vec2 aTextureCoordinate;" + // Texture coordinates
        "varying vec2 vTextureCoordinate;" + // Passed to fragment shader
        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        // Note that the uMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        "   gl_Position = uMVPMatrix * vPosition;" +
        "   vTextureCoordinate = aTextureCoordinate;" + // Pass texture coordinate
        "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "uniform sampler2D uTexture;" + // Texture sampler
        "varying vec2 vTextureCoordinate;" + // Received from vertex shader
        "void main() {" +
        "  gl_FragColor = texture2D(uTexture, vTextureCoordinate);" +
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

        vertexBuffer = ByteBuffer.allocateDirect(coords.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(coords)
                // set the buffer to read the first coordinate
                position(0)
            }
        }

        textureBuffer = ByteBuffer.allocateDirect(textureCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(textureCoords)
                position(0)
            }
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

    abstract fun draw(mvpMatrix: FloatArray? = null, context: Context)

    protected fun loadGlTexture(context: Context, @DrawableRes imageId: Int): Int {
        val textures = IntArray(1)
        // Generate OpenGL texture IDs and store them in the 'textures' array
        GLES20.glGenTextures(textures.size, textures, 0)

        if (textures[0] != 0) {
            // Activate texture unit 0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

            // Bind the texture ID to the GL_TEXTURE_2D target
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

            // Set texture filtering parameters for minification and magnification
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST)

            // Set texture wrapping parameters for the S (horizontal) and T (vertical) directions
            // to repeat texture if it does not fit in the whole face
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)
            // use Android GLUtils to specify a two-dimensional texture image from our bitmap
            val bitmap = BitmapFactory.decodeResource(
                context.resources,
                imageId,
                BitmapFactory.Options().apply {
                    inScaled = false
                }
            )
            Log.d("Bitmap", "Width: ${bitmap.width}, Height: ${bitmap.height}, Format: ${bitmap.config}")

            // Load the bitmap data into the OpenGL texture at mipmap level 0 with RGBA format
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0)
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

            // clean up
            bitmap.recycle()
        }

        if (textures[0] == 0) {
            throw RuntimeException("Error loading texture.")
        }

        return textures[0]
    }

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