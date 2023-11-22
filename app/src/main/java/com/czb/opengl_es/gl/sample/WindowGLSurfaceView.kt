package com.czb.opengl_es.gl.sample

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class WindowGLSurfaceView : GLSurfaceView {

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    setEGLContextClientVersion(3)
    //前4个参数分别对应RGBA占用的bit数。后2个参数是深度值和模板(Stencil)值，后面会讲。
    setEGLConfigChooser(8, 8, 8, 8, 16, 8)
    //为当前View设置渲染器，并且启动渲染线程，调用渲染器进行渲染。
    //渲染器负责调用OpenGL来渲染帧。
    //该方法在GLSurfaceView的生命周期中只能调用一次。

    //以下的GLSurfaceView的方法只能在setRender之前调用：
    //setEGLConfigChooser(boolean)
    //setEGLConfigChooser(GLSurfaceView.EGLConfigChooser)
    //setEGLConfigChooser(int, int, int, int, int, int)

    //以下的GLSurfaceView的方法只能在setRender之后调用：
    //getRenderMode()
    //onPause()
    //onResume()
    //queueEvent(Runnable)
    //requestRender()
    //setRenderMode(int)
    setRenderer(WindowRenderer(context))

    //设置渲染模式，默认是RENDERMODE_CONTINUOUSLY。
    //两种渲染模式：
    //RENDERMODE_CONTINUOUSLY：渲染器会被定时重复用于渲染，不管数据是否更新
    //RENDERMODE_WHEN_DIRTY：渲染器只会在surface被创建以及requestRender方法被调用时，才会进行渲染。
    //即有新数据才会进行渲染。按需渲染，更省电量、CPU、GPU
    renderMode = RENDERMODE_WHEN_DIRTY
  }
}