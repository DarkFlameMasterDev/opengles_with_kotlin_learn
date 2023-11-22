package com.czb.opengl_es.gl.utils

import android.content.Context
import android.opengl.GLES32
import android.util.Log
import androidx.annotation.RawRes
import java.io.ByteArrayOutputStream

fun loadShaderCode(context: Context, @RawRes rawId: Int): String? {
  var result: String? = null
  try {
    val shaderCode = context.resources.openRawResource(rawId)
    var temp: Int
    val byteArrayOutputStream = ByteArrayOutputStream()

    // -1 是终止符
    while (shaderCode.read().also { temp = it } != -1) {
      byteArrayOutputStream.write(temp)
    }
    byteArrayOutputStream.close()
    shaderCode.close()
    result = byteArrayOutputStream.toString("UTF-8")

    // "\r\n"（Windows风格的换行符）
    result = result.replace("\\r\\n".toRegex(), "\n")
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return result
}
