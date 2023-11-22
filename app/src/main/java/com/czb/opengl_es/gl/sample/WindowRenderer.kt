package com.czb.opengl_es.gl.sample

import android.content.Context
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import com.czb.opengl_es.gl.BaseRender
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class WindowRenderer(context: Context) : BaseRender(context) {
  override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

  }

  override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    GLES32.glViewport(0, 0, width, height)
  }

  override fun onDrawFrame(gl: GL10?) {
    GLES32.glClearColor(0.1f, 0.8f, 0.8f, 1.0f)
    GLES32.glClear(GL10.GL_COLOR_BUFFER_BIT)
  }
}