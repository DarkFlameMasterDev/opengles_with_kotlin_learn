package com.czb.opengl_es.gl.sample

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class TriangleGLSurfaceView : GLSurfaceView {

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    setEGLContextClientVersion(3)
    setEGLConfigChooser(8, 8, 8, 8, 16, 8)
    setRenderer(TriangleRenderer(context))
    renderMode = RENDERMODE_WHEN_DIRTY
  }
}