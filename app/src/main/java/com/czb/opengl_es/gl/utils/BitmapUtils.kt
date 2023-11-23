package com.czb.opengl_es.gl.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Matrix

enum class Orientation {
  Horizontal, Vertical
}

fun Bitmap.reverse(orientation: Orientation): Bitmap {

  val matrix = Matrix()
  when (orientation) {
    Orientation.Horizontal -> matrix.setScale(-1f, 1f)
    Orientation.Vertical -> matrix.setScale(1f, -1f)
  }
  return createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}