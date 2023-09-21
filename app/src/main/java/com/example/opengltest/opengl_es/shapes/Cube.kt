package com.example.opengltest.opengl_es.shapes

import android.content.Context
import android.opengl.GLES20
import com.example.opengltest.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10


class Cube : OpenGLShape(
    coords = floatArrayOf( // Vertices of the 6 faces
        // FRONT
        -0.2f, -0.2f, 0.2f,  // 0. left-bottom-front
        0.2f, -0.2f, 0.2f,  // 1. right-bottom-front
        -0.2f, 0.2f, 0.2f,  // 2. left-top-front
        0.2f, 0.2f, 0.2f,  // 3. right-top-front
        // BACK
        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
        0.2f, 0.2f, -0.2f,  // 7. right-top-back
        -0.2f, 0.2f, -0.2f,  // 5. left-top-back
        // LEFT
        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
        -0.2f, -0.2f, 0.2f,  // 0. left-bottom-front
        -0.2f, 0.2f, -0.2f,  // 5. left-top-back
        -0.2f, 0.2f, 0.2f,  // 2. left-top-front
        // RIGHT
        0.2f, -0.2f, 0.2f,  // 1. right-bottom-front
        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
        0.2f, 0.2f, 0.2f,  // 3. right-top-front
        0.2f, 0.2f, -0.2f,  // 7. right-top-back
        // TOP
        -0.2f, 0.2f, 0.2f,  // 2. left-top-front
        0.2f, 0.2f, 0.2f,  // 3. right-top-front
        -0.2f, 0.2f, -0.2f,  // 5. left-top-back
        0.2f, 0.2f, -0.2f,  // 7. right-top-back
        // BOTTOM
        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
        -0.2f, -0.2f, 0.2f,  // 0. left-bottom-front
        0.2f, -0.2f, 0.2f // 1. right-bottom-front
    ),

    textureCoords = floatArrayOf(
        0.0f, 1.0f,  // A. left-bottom (NEW)
        1.0f, 1.0f,  // B. right-bottom (NEW)
        0.0f, 0.0f,  // C. left-top (NEW)
        1.0f, 0.0f   // D. right-top (NEW)
    )
) {


    init {
        textureBuffer = ByteBuffer.allocateDirect(textureCoords.size * 4 * 6).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                repeat(6) {
                    put(textureCoords)
                }
                position(0)
            }
        }
    }

    override fun draw(mvpMatrix: FloatArray, context: Context) {
        // Adding program to OpenGL ES environment
        GLES20.glUseProgram(program)
//        GLES20.glFrontFace(GLES20.GL_CCW)    // Front face in counter-clockwise orientation
//        GLES20.glEnable(GLES20.GL_CULL_FACE); // Enable cull face
//        GLES20.glCullFace(GLES20.GL_BACK);    // Cull the back face (don't display)

        // get handle to vertex shader's vPosition member
        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition").also {
            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)
        }


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
        // Bind the texture and set the uniform sample2D in the shader program
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, loadGlTexture(context, R.drawable.tex_1))
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glUniform1i(textureUniformHandle, 0)

        val vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        repeat(6) { face ->
            // Render each face in TRIANGLE_STRIP using 4 vertices
            GLES20.glDrawArrays(GL10.GL_TRIANGLE_STRIP, face * 4, 4)
        }

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordinateHandle)
    }
}