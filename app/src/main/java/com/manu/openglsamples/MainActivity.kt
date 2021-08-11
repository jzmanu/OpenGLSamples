package com.manu.openglsamples

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.manu.openglsamples.renderer.MRenderer
import com.manu.openglsamples.triangle.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


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
        glSurfaceView.setRenderer(MRenderer(this))
//        glSurfaceView.setRenderer(MRenderer2(this))
//        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        setContentView(glSurfaceView)
    }
}

class MRenderer2(private var context: Context) : GLSurfaceView.Renderer {
    private val tag = MRenderer::class.java.simpleName
    private lateinit var triangle: Triangle
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 创建Surface时调用，在渲染开始时调用，用来创建渲染开始时需要的资源
        Log.d(tag, "onSurfaceCreated")
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Surface改变大小时调用，设置视口
        Log.d(tag, "onSurfaceChanged")
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 绘制当前frame，用于渲染处理具体的内容
        Log.d(tag, "onDrawFrame")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }
}