package com.manu.openglsamples

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.manu.openglsamples.renderer.SquareRenderer
import com.manu.openglsamples.renderer.TriangleRenderer

/**
 * 渲染模式：
 *    RENDERMODE_CONTINUOUSLY:不间断调用进行渲染
 *    RENDERMODE_WHEN_DIRTY:Surface被创建后渲染一次，只调用了requestRender()才会继续渲染
 */
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var glSurfaceView:GLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(TriangleRenderer(this))
//        glSurfaceView.setRenderer(SquareRenderer(this))
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        setContentView(glSurfaceView)
    }
}