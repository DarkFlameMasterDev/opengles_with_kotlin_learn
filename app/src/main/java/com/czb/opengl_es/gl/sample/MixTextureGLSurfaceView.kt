package com.czb.opengl_es.gl.sample

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MixTextureGLSurfaceView : GLSurfaceView {

  var mixValue = 0.5f
    set(value) {
      field = value
      changeRendererMixValue()
    }
  private val mixTextureRenderer = MixTextureRenderer(context)

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    setEGLContextClientVersion(3)
    setEGLConfigChooser(8, 8, 8, 8, 16, 8)
    setRenderer(mixTextureRenderer)
    renderMode = RENDERMODE_CONTINUOUSLY
  }

  private fun changeRendererMixValue() {
    mixTextureRenderer.mixValue = mixValue
  }
}