package com.czb.opengl_es

import android.content.res.Resources
import android.util.Log

fun Float.toPx(): Float {
  val density: Float = Resources.getSystem().displayMetrics.density
  return (this * density)
}

fun Float.toDp(): Float {
  val density: Float = Resources.getSystem().displayMetrics.density
  return (this * density)
}

private const val LogTag = "lucas-kotlin-opengles"

fun log(msg: String, level: Int = Log.DEBUG) {
  when (level) {
    Log.INFO -> Log.i(LogTag, msg)
    Log.DEBUG -> Log.d(LogTag, msg)
    Log.WARN -> Log.w(LogTag, msg)
    Log.ERROR -> Log.e(LogTag, msg)
    Log.ASSERT -> Log.wtf(LogTag, msg)
    Log.VERBOSE -> Log.v(LogTag, msg)
    else -> Log.v(LogTag, msg)
  }
}