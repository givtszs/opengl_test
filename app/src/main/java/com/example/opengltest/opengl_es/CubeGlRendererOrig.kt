package com.example.opengltest.opengl_es

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import com.example.opengltest.R
import com.example.opengltest.opengl_es.shapes.COORDS_PER_VERTEX
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CubeGlRendererOrig(private val context: Context) : GLSurfaceView.Renderer {
    // used to move objects from model space
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)

    // store cube data in a float buffer
    private var cubePositions: FloatBuffer
    private var cubeTextureCoordinates: FloatBuffer

    private var programHandle: Int = 0
    private var positionHandle: Int = 0
    private var textureCoordinateHandle: Int = 0
    private var textureUniformHandle: Int = 0
    private var mvpMatrixHandle: Int = 0
    private var colorHandle: Int = 0

    private val vertexShaderCode =
        "uniform mat4 u_MVPMatrix;" + // represents the combined model, view and projection matrices
        "attribute vec4 a_Position;" + // per-vertex position info
        "attribute vec2 a_TexCoordinate;" + // per-vertex texture coordinate info
        "varying vec2 v_TexCoordinate;" +
        "varying vec2 v_TexCoordinate;" +
        "void main() {" +
        "   v_TexCoordinate = a_TexCoordinate;" +
        "   gl_Position = u_MVPMatrix * a_Position;" +
        "}"

    private val fragmentShaderCode =
        "uniform sampler2D u_Texture;" + // the input texture
        "uniform vec4 v_Color;" + // the input texture
        "precision mediump float;" +
        "varying vec2 v_TexCoordinate;" +
        "void main() {" +
        "  gl_FragColor = v_Color;" +
        "}"

    private val cubeCoordinates = floatArrayOf(
        -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
        0.2f, -0.2f,  0.2f,  // 1. right-bottom-front
        -0.2f,  0.2f,  0.2f,  // 2. left-top-front
        0.2f,  0.2f,  0.2f,  // 3. right-top-front
// BACK
        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
        0.2f,  0.2f, -0.2f,  // 7. right-top-back
        -0.2f,  0.2f, -0.2f,  // 5. left-top-back
// LEFT
        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
        -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
        -0.2f,  0.2f, -0.2f,  // 5. left-top-back
        -0.2f,  0.2f,  0.2f,  // 2. left-top-front
// RIGHT
        0.2f, -0.2f,  0.2f,  // 1. right-bottom-front
        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
        0.2f,  0.2f,  0.2f,  // 3. right-top-front
        0.2f,  0.2f, -0.2f,  // 7. right-top-back
// TOP
        -0.2f,  0.2f,  0.2f,  // 2. left-top-front
        0.2f,  0.2f,  0.2f,  // 3. right-top-front
        -0.2f,  0.2f, -0.2f,  // 5. left-top-back
        0.2f,  0.2f, -0.2f,  // 7. right-top-back
// BOTTOM
        -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
        0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
        -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
        0.2f, -0.2f,  0.2f   // 1. right-bottom-front
    )

    private val colors = arrayOf(
        floatArrayOf(1.0f, 0.5f, 0.0f, 1.0f),
        floatArrayOf(1.0f, 0.0f, 1.0f, 1.0f),
        floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f),
        floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f),
        floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f),
        floatArrayOf(1.0f, 1.0f, 0.0f, 1.0f)
    )

    private val textureCoordinates = floatArrayOf(
        // Front face
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,

        // Right face
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,

        // Back face
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,

        // Left face
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,

        // Top face
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,

        // Bottom face
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    )

    init {
        cubePositions = ByteBuffer.allocateDirect(cubeCoordinates.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(cubeCoordinates)
                position(0)
            }

        cubeTextureCoordinates = ByteBuffer.allocateDirect(textureCoordinates.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(textureCoordinates)
                position(0)
            }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // set the background frame color to white
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        GLES20.glEnable(GLES20.GL_CULL_FACE)  // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_DEPTH_TEST) // Enable depth testing
        GLES20.glFrontFace(GLES20.GL_CCW) // front face in counter-clockwise orientation

        // position the eye in front of the origin
        val eyeX = 0.0f
        val eyeY = 0.0f
        val eyeZ = -0.5f

        // we are looking toward the distance
        val lookX = 0.0f
        val lookY = 0.0f
        val lookZ = -5.0f

        // set our up vector. this is where our head would be pointing when we holding the camera
        val upX = 0.0f
        val upY = 1.0f
        val upZ = 0.0f

        // set the view matrix
//        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

        Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        val vertexShaderHandle =
            ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShaderHandle =
            ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        programHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio
        val ratio = width.toFloat() / height.toFloat()
        val left = -ratio
        val right = ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 1.0f
        val far = 10.0f

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // do a complete rotation every 10s
        val time = SystemClock.uptimeMillis() % 10000L
        val angleInDegrees = (360.0f / 10000.0f) * time.toInt()

        GLES20.glUseProgram(programHandle)

        // set program handles for cube drawing
        mvpMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix")
//        textureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture")
        positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position")
//        textureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate")
        colorHandle = GLES20.glGetUniformLocation(programHandle, "v_Color")

//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, getGlSurfaceTexture())
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
//        GLES20.glUniform1i(textureUniformHandle, 0)

        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -3.3f)
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1.0f, 1.0f, 0.0f)

        drawCube()
    }

    private fun drawCube() {
        cubePositions.position(0)
        with (positionHandle) {
            GLES20.glVertexAttribPointer(
                this,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                0,
                cubePositions
            )
            GLES20.glEnableVertexAttribArray(this)
        }

//        GLES20.glUniform4fv(colorHandle, 1, color, 0)

//        cubeTextureCoordinates.position(0)
//        with (textureCoordinateHandle) {
//            GLES20.glVertexAttribPointer(
//                this,
//                TEXTURE_COORDS_SIZE,
//                GLES20.GL_FLOAT,
//                false,
//                0,
//                cubeTextureCoordinates
//            )
//            GLES20.glEnableVertexAttribArray(this)
//        }

        // multiply the view matrix by the model matrix, and store the result in MVP matrix
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // multiply the modelview matrix by the projection matrix, and store the result in the mvp matrix
//        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, modelMatrix, 0)
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        // draw the cube
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cubeCoordinates.size / COORDS_PER_VERTEX)
        GLES20.glDisableVertexAttribArray(positionHandle)
//        GLES20.glDisableVertexAttribArray(textureUniformHandle)
    }

    private fun getGlSurfaceTexture(): Int {
        val textures = IntArray(1)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glGenTextures(1, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)

        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.tex_1,
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

        return textures[0]
    }
}