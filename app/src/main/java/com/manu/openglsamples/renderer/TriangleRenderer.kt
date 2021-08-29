package com.manu.openglsamples.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.manu.openglsamples.common.Config
import com.manu.openglsamples.sample.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleRenderer(private var context: Context) : GLSurfaceView.Renderer {
    private val tag = TriangleRenderer::class.java.simpleName
    private lateinit var triangle: Triangle
    private val vPMatrix = FloatArray(16) // 模型视图投影矩阵
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 创建Surface时调用，在渲染开始时调用，用来创建渲染开始时需要的资源
        Log.d(tag, "onSurfaceCreated")
        triangle = Triangle(context)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Surface改变大小时调用，设置视口
        Log.d(tag, "onSurfaceChanged")
        GLES20.glViewport(0, 0, width, height)
        if (!Config.DEFAULT){
            // 投影矩阵应用于对象的坐标
            val ratio: Float = width.toFloat() / height.toFloat()
            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 8f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        // 绘制当前frame，用于渲染处理具体的内容
        Log.d(tag, "onDrawFrame")
        if (!Config.DEFAULT){
            // 设置相机位置（视图矩阵）
            Matrix.setLookAtM(viewMatrix,0,
                0.0f,0.0f,5.0f, // 相机位置
                0.0f,0.0f,0.0f, // 目标位置
                0.0f,1.0f,0.0f) // 相机正上方向量
            // 计算投影跟视图变换
            Matrix.multiplyMM(vPMatrix,0,projectionMatrix,0,viewMatrix,0)
        }
        triangle.draw(vPMatrix)
    }
}