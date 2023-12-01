package com.czb.opengl_es.gl.sample

import android.content.Context
import android.util.AttributeSet
import com.czb.opengl_es.gl.BaseGLSurfaceView
import com.czb.opengl_es.gl.Direction

class CameraGLSurfaceView : BaseGLSurfaceView {

  private val cameraRenderer = CameraRenderer(context)

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  init {
    setRendererConfig(cameraRenderer, RENDERMODE_CONTINUOUSLY)
  }

  fun processIn(direction: Direction) {
    cameraRenderer.processInput(direction)
  }
}