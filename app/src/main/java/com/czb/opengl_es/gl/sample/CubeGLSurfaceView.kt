package com.czb.opengl_es.gl.sample

import android.content.Context
import android.util.AttributeSet
import com.czb.opengl_es.gl.BaseGLSurfaceView
import com.czb.opengl_es.log

class CubeGLSurfaceView : BaseGLSurfaceView {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  init {
    setRendererConfig(CubeRenderer(context), RENDERMODE_CONTINUOUSLY)
  }
}