package com.example.opengltest.opengl_es.shapes

import android.opengl.GLES20
import com.example.opengltest.opengl_es.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube {
//    private val vertices = floatArrayOf(
//        -0.2f, -0.2f, 0.2f,  // 0. left-bottom-front
//        0.2f, -0.2f, 0.2f,  // 1. right-bottom-front
//        -0.2f, 0.2f, 0.2f,  // 2. left-top-front
//        0.2f, 0.2f, 0.2f,  // 3. right-top-front
//        // BACK
//        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
//        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
//        0.2f, 0.2f, -0.2f,  // 7. right-top-back
//        -0.2f, 0.2f, -0.2f,  // 5. left-top-back
//        // LEFT
//        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
//        -0.2f, -0.2f, 0.2f,  // 0. left-bottom-front
//        -0.2f, 0.2f, -0.2f,  // 5. left-top-back
//        -0.2f, 0.2f, 0.2f,  // 2. left-top-front
//        // RIGHT
//        0.2f, -0.2f, 0.2f,  // 1. right-bottom-front
//        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
//        0.2f, 0.2f, 0.2f,  // 3. right-top-front
//        0.2f, 0.2f, -0.2f,  // 7. right-top-back
//        // TOP
//        -0.2f, 0.2f, 0.2f,  // 2. left-top-front
//        0.2f, 0.2f, 0.2f,  // 3. right-top-front
//        -0.2f, 0.2f, -0.2f,  // 5. left-top-back
//        0.2f, 0.2f, -0.2f,  // 7. right-top-back
//        // BOTTOM
//        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
//        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
//        -0.2f, -0.2f, 0.2f,  // 0. left-bottom-front
//        0.2f, -0.2f, 0.2f // 1. right-bottom-front
//    )
//
//    private val indices =
//        shortArrayOf(
//            0,
//            1,
//            2,
//            2,
//            1,
//            3,
//            5,
//            4,
//            7,
//            7,
//            4,
//            6,
//            8,
//            9,
//            10,
//            10,
//            9,
//            11,
//            12,
//            13,
//            14,
//            14,
//            13,
//            15,
//            16,
//            17,
//            18,
//            18,
//            17,
//            19,
//            22,
//            23,
//            20,
//            20,
//            23,
//            21
//        )
//
//    private val colors = arrayOf(
//        floatArrayOf(1.0f, 0.5f, 0.0f, 1.0f),
//        floatArrayOf(1.0f, 0.0f, 1.0f, 1.0f),
//        floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f),
//        floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f),
//        floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f),
//        floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f)
//    )

//    private val vertices = floatArrayOf(
//        -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
//        0.2f, -0.2f,  0.2f,  // 1. right-bottom-front
//        -0.2f,  0.2f,  0.2f,  // 2. left-top-front
//        0.2f,  0.2f,  0.2f,  // 3. right-top-front
//// BACK
//        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
//        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
//        0.2f,  0.2f, -0.2f,  // 7. right-top-back
//        -0.2f,  0.2f, -0.2f,  // 5. left-top-back
//// LEFT
//        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
//        -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
//        -0.2f,  0.2f, -0.2f,  // 5. left-top-back
//        -0.2f,  0.2f,  0.2f,  // 2. left-top-front
//// RIGHT
//        0.2f, -0.2f,  0.2f,  // 1. right-bottom-front
//        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
//        0.2f,  0.2f,  0.2f,  // 3. right-top-front
//        0.2f,  0.2f, -0.2f,  // 7. right-top-back
//// TOP
//        -0.2f,  0.2f,  0.2f,  // 2. left-top-front
//        0.2f,  0.2f,  0.2f,  // 3. right-top-front
//        -0.2f,  0.2f, -0.2f,  // 5. left-top-back
//        0.2f,  0.2f, -0.2f,  // 7. right-top-back
//// BOTTOM
//        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
//        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
//        -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
//        0.2f, -0.2f,  0.2f   // 1. right-bottom-front
//    )

    private val vertices = floatArrayOf( // Vertices of the 6 faces
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
    )

    private val colors = arrayOf(
        floatArrayOf(1.0f, 0.5f, 0.0f, 1.0f),
        floatArrayOf(1.0f, 0.0f, 1.0f, 1.0f),
        floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f),
        floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f),
        floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f),
        floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f)
    )

    var indices = shortArrayOf(
        0,
        1,
        2,
        2,
        1,
        3,
        5,
        4,
        7,
        7,
        4,
        6,
        8,
        9,
        10,
        10,
        9,
        11,
        12,
        13,
        14,
        14,
        13,
        15,
        16,
        17,
        18,
        18,
        17,
        19,
        22,
        23,
        20,
        20,
        23,
        21
    )

    private var vertexBuffer: FloatBuffer
    private var indexBuffer: ShortBuffer

    private val vertexShaderCode =  // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        "  gl_Position = uMVPMatrix * vPosition;" +
        "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    private var program: Int

    private var positionHandle = 0
    private var colorHandle = 0
    private var vPMatrixHandle = 0

    private val vertexCount: Int = vertices.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4

    init {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }

        indexBuffer = ByteBuffer.allocateDirect(indices.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(indices)
                position(0)
            }
        }

        val vertexShader = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader =
            ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        program = ShaderHelper.createAndLinkProgram(vertexShader, fragmentShader)
    }


    fun draw(mvpMatrix: FloatArray) {
        // Adding program to OpenGL ES environment
        GLES20.glUseProgram(program)
        GLES20.glFrontFace(GLES20.GL_CCW);    // Front face in counter-clockwise orientation
//        GLES20.glEnable(GLES20.GL_CULL_FACE); // Enable cull face
//        GLES20.glCullFace(GLES20.GL_BACK);    // Cull the back face (don't display)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition").also {

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

        vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        colorHandle = GLES20.glGetUniformLocation(program, "vColor").also { colorH ->
            repeat(6) { face ->
                // Set the color for each of the faces
                GLES20.glUniform4fv(colorH, 1, colors[face], 0)
                GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
                // Draw the primitive from the vertex-array directly
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, face * 4, 4)
            }
        }




//        colorHandle = GLES20.glGetUniformLocation(program, "vColor").also { colorH ->
//            repeat(6) { face ->
//                GLES20.glUniform4fv(colorH, 1, colors[face], 0)
//                GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
//                indexBuffer.position(face * 6)
//                GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer)
//            }
//        }

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}