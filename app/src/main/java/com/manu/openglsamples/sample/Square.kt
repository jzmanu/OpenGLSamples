package com.manu.openglsamples.sample

import android.content.Context
import android.opengl.GLES20
import com.manu.openglsamples.R
import com.manu.openglsamples.common.Config
import com.manu.openglsamples.util.GLUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
/**
 * @Desc: 矩形
 * @Author: jzman
 * @Date: 2021/8/1.
 */
class Square(context: Context) {
    companion object {
        // 坐标数组中每个顶点的坐标数
        private const val COORDINATE_PER_VERTEX = 3
    }

    private var programHandle: Int = 0
    private var positionHandle: Int = 0
    private var colorHandler: Int = 0
    private var vPMatrixHandle: Int = 0
    private var vertexStride = COORDINATE_PER_VERTEX * 4

    private var squareCoordinates = floatArrayOf(
        -0.5f, 0.5f, 0.0f,      // top left
        -0.5f, -0.5f, 0.0f,     // bottom left
        0.5f, -0.5f, 0.0f,      // bottom right
        0.5f, 0.5f, 0.0f        // top right
    )

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

    // 颜色数组
    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    // initialize vertex byte buffer for shape coordinates
    private val vertexBuffer: FloatBuffer =
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoordinates.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoordinates)
                position(0)
            }
        }

    // initialize byte buffer for the draw list
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
        val vertexShaderCode:String? = if (Config.DEFAULT){
            GLUtil.readShaderSourceCodeFromRaw(context, R.raw.vertex_shader_square_default)
        }else{
            GLUtil.readShaderSourceCodeFromRaw(context, R.raw.vertex_shader_square)
        }

        val fragmentShaderCode =
            GLUtil.readShaderSourceCodeFromRaw(context, R.raw.fragment_shader_square)
        if (vertexShaderCode.isNullOrEmpty() || fragmentShaderCode.isNullOrEmpty()) {
            throw RuntimeException("vertexShaderCode or fragmentShaderCode is null or empty")
        }
        val vertexShaderHandler = GLUtil.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShaderHandler =
            GLUtil.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        programHandle = GLUtil.createAndLinkProgram(vertexShaderHandler, fragmentShaderHandler)
        GLES20.glUseProgram(programHandle)
    }

    fun draw(mvpMatrix: FloatArray) {
        // 获取attribute变量的地址索引
        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(programHandle, "vPosition").also {
            // enable vertex attribute，默认是disable
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                COORDINATE_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }
        // get handle to fragment shader's vColor member
        colorHandler = GLES20.glGetUniformLocation(programHandle, "vColor").also {
            GLES20.glUniform4fv(it, 1, color, 0)
        }
        if (!Config.DEFAULT){
            // get handle to shape's transformation matrix
            vPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uMVPMatrix")
            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)
        }

        // draw square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,drawOrder.size, GLES20.GL_UNSIGNED_SHORT,drawListBuffer)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}
    