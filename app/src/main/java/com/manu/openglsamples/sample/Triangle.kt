package com.manu.openglsamples.sample

import android.content.Context
import android.opengl.GLES20
import com.manu.openglsamples.R
import com.manu.openglsamples.common.Config
import com.manu.openglsamples.util.GLUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @Desc:Triangle
 * @Author: jzman
 * @Date: 2021/7/3 18:49.
 */
class Triangle(context: Context) {
    companion object {
        // 坐标数组中每个顶点的坐标数
        private const val COORDINATE_PER_VERTEX = 3
    }

    private var programHandle: Int = 0
    private var positionHandle: Int = 0
    private var colorHandler: Int = 0
    private var vPMatrixHandle: Int = 0
    // 顶点之间的偏移量，每个顶点四个字节
    private var vertexStride = COORDINATE_PER_VERTEX * 4

    // 三角形的三条边
    private var triangleCoordinate = floatArrayOf(     // 逆时针的顺序的三条边
        0.0f, 0.5f, 0.0f,      // top
        -0.5f, -0.5f, 0.0f,    // bottom left
        0.5f, -0.5f, 0.0f      // bottom right
    )

    // 顶点个数
    private val vertexCount: Int = triangleCoordinate.size / COORDINATE_PER_VERTEX

    // 颜色数组
    private val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(triangleCoordinate.size * 4).run {
            // ByteBuffer使用本机字节序
            this.order(ByteOrder.nativeOrder())
            // ByteBuffer to FloatBuffer
            this.asFloatBuffer().apply {
                put(triangleCoordinate)
                position(0)
            }
        }

    init {
        val vertexShaderCode:String? = if (Config.DEFAULT){
            GLUtil.readShaderSourceCodeFromRaw(context, R.raw.vertex_shader_triangle_default)
        }else{
            GLUtil.readShaderSourceCodeFromRaw(context, R.raw.vertex_shader_triangle)
        }

        val fragmentShaderCode =
            GLUtil.readShaderSourceCodeFromRaw(context, R.raw.fragment_shader_triangle)
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

        // draw triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}
