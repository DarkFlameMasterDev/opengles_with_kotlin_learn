package com.czb.opengl_es.gl.sample

import android.content.Context
import android.util.AttributeSet
import com.czb.opengl_es.gl.BaseGLSurfaceView

class MixTextureGLSurfaceView : BaseGLSurfaceView {

  var mixValue = 0.5f
    set(value) {
      field = value
      changeRendererMixValue()
    }
  private val mixTextureRenderer = MixTextureRenderer(context)

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  init {
    setRendererConfig(mixTextureRenderer, RENDERMODE_CONTINUOUSLY)
  }

  private fun changeRendererMixValue() {
    mixTextureRenderer.mixValue = mixValue
  }
}