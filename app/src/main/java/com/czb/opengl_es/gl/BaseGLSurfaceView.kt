package com.czb.opengl_es.gl

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent

class BaseGLSurfaceView<T : BaseRender> : GLSurfaceView {
  private val TAG = javaClass.simpleName
  private var mRender: T? = null
  private val render: T
    get() = mRender!!

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

  fun setRender(render: T, renderMode: Int) {
    mRender = render
    setEGLContextClientVersion(3)
    setRenderer(render)
    setRenderMode(renderMode)
  }

  fun move(d: Direction?) {
    render.move(d, 0.05f)
    requestRender()
  }

  fun move(d: Direction?, cameraSpeed: Float) {
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
    val max = Math.min(width, height)
    val zoom: Boolean = render.handleZoom(event, max)
    if (zoom) requestRender()
    return true
  }
}