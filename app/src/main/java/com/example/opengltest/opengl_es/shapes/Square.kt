package com.example.opengltest.opengl_es.shapes

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLU
import android.opengl.GLUtils
import com.example.opengltest.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer

class Square(
    shapeCoords: FloatArray,
    textureCoords: FloatArray,
    color: FloatArray? = null
) : OpenGLShape(shapeCoords, textureCoords) {
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
    private val textures = IntArray(1)

    // byte buffer for the draw list
    private val drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    init {
        color?.let {
            this.color = it
        }
    }

    override fun draw(mvpMatrix: FloatArray?, context: Context) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(program)

        // get handle to vertex shader's vPosition member
        val vertPositionHandle = GLES20.glGetAttribLocation(program, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }

//        // get handle to fragment shader's vColor member
//        GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
//
//            // Set color for drawing the triangle
//            GLES20.glUniform4fv(colorHandle, 1, color, 0)
//        }

        // texture position handler
        val texPositionHandle = GLES20.glGetAttribLocation(program, "a_TexCoordinate").also {
            // pass texture position to shader
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                textureBuffer
            )

            GLES20.glEnableVertexAttribArray(it)
        }

        // texture uniform handler
        val textureUniformHandle = GLES20.glGetUniformLocation(program, "u_Texture")

        // get handle to shape's transformation matrix
        val vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        // load texture
//        loadGlTexture(context)

        // attach the object texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glUniform1i(textureUniformHandle, 0)

        // Draw the square
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vertPositionHandle)
        GLES20.glDisableVertexAttribArray(texPositionHandle)
    }

    override fun loadGlTexture(context: Context) {
//        GLES20.glEnable(GLES20.GL_BLEND)
//        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        // loading texture
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.polygonal_texture)

        // generate one texture pointer and bind it to the textures array
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glGenTextures(textures.size, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat());
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST.toFloat());

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        // clean up
        bitmap.recycle()
    }
}