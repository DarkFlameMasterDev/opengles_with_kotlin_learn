package com.czb.opengl_es.gl.sample

import android.content.Context
import android.util.AttributeSet
import com.czb.opengl_es.gl.BaseGLSurfaceView

class TransformGLSurfaceView : BaseGLSurfaceView {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  init {
    setRendererConfig(TransformRenderer(context), RENDERMODE_CONTINUOUSLY)
  }

}