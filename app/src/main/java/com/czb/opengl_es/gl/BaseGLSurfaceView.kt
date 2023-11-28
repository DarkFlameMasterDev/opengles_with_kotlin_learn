package com.czb.opengl_es.gl

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.math.min

open class BaseGLSurfaceView : GLSurfaceView {
  private var mRender: BaseRenderer? = null
  private val render: BaseRenderer
    get() = mRender!!

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


  fun setRendererConfig(render: BaseRenderer, renderMode: Int) {
    mRender = render
    setEGLContextClientVersion(3)
    setEGLConfigChooser(8, 8, 8, 8, 16, 8)
    setRenderer(render)
    setRenderMode(renderMode)
  }

  fun move(d: Direction) {
    render.move(d, 0.05f)
    requestRender()
  }

  fun move(d: Direction, cameraSpeed: Float) {
    render.move(d, cameraSpeed)
    requestRender()
  }

  fun setYaw(yaw: Float) {
    render.setYaw(yaw)
    requestRender()
  }

  fun setPitch(pitch: Float) {
    render.setPitch(pitch)
    requestRender()
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    val max = min(width, height)
    val zoom: Boolean = render.handleZoom(event, max)
    if (zoom) requestRender()
    return true
  }
}