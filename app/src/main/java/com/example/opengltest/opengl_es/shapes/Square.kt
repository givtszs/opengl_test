package com.example.opengltest.opengl_es.shapes

import android.content.Context
import android.opengl.GLES20
import com.example.opengltest.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer

class Square : OpenGLShape(
    coords = floatArrayOf(
        -0.3f, 0.3f, 0.0f,    // top left
        -0.3f, -0.3f, 0.0f,   // bottom left
        0.3f, -0.3f, 0.0f,    // bottom right
        0.3f, 0.3f, 0.0f      // top right
    ),
    textureCoords = floatArrayOf(
        //x,    y
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,
    )
) {
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

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

    override fun draw(mvpMatrix: FloatArray, context: Context) {
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

        // get handle to fragment shader's vColor member
//        GLES20.glGetUniformLocation(program, "vColor").also { colorHandle ->
//
//            // Set color for drawing the triangle
//            GLES20.glUniform4fv(colorHandle, 1, color, 0)
//        }

        val texCoordinateHandle = GLES20.glGetAttribLocation(program, "aTextureCoordinate").also {
            // pass texture position to shader
            GLES20.glVertexAttribPointer(
                it,
                TEXTURE_COORDS_SIZE,
                GLES20.GL_FLOAT,
                false,
                0, // auto stride because it's tightly packed
                textureBuffer
            )

            GLES20.glEnableVertexAttribArray(it)
        }

        val textureUniformHandle = GLES20.glGetUniformLocation(program, "uTexture")

        val textureId = loadGlTexture(context, R.drawable.tex_1)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        // Bind the texture and set the uniform sample2D in the shader program
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glUniform1i(textureUniformHandle, 0)

        // get handle to shape's transformation matrix
        val vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        // Draw the square
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawListBuffer
        )

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vertPositionHandle)
        GLES20.glDisableVertexAttribArray(texCoordinateHandle)
    }
}