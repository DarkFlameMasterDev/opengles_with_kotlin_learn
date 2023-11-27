package com.czb.opengl_es.gl

import android.content.Context
import android.opengl.GLES32
import androidx.annotation.RawRes
import com.czb.opengl_es.gl.utils.loadShaderCode
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Shader private constructor(builder: Builder) {
  private val context: Context
  private val vertexShaderRawId: Int
  private val fragShaderRawId: Int
  private val program: Int
  private var geometryShaderRawId = -1

  init {
    context = builder.context
    vertexShaderRawId = builder.vertexShaderRawId
    fragShaderRawId = builder.fragShaderRawId
    geometryShaderRawId = builder.geometryShaderRawId
    program = createShaderProgram()
  }

  fun use() {
    GLES32.glUseProgram(program)
  }

  fun setBoolean(name: String, value: Boolean) {
    val location = GLES32.glGetUniformLocation(program, name)
    checkLocation(location, name)
    GLES32.glUniform1i(location, if (value) 1 else 0)
  }

  fun setInt(name: String, value: Int) {
    val location = GLES32.glGetUniformLocation(program, name)
    checkLocation(location, name)
    GLES32.glUniform1i(location, value)
  }

  fun setFloat(name: String, value: Float) {
    val location = GLES32.glGetUniformLocation(program, name)
    checkLocation(location, name)
    GLES32.glUniform1f(location, value)
  }

  fun setVec3(name: String, x: Float, y: Float, z: Float) {
    val location = GLES32.glGetUniformLocation(program, name)
    checkLocation(location, name)
    GLES32.glUniform3f(location, x, y, z)
  }

  fun setVec3(name: String, vec3: Vec3) {
    val location = GLES32.glGetUniformLocation(program, name)
    checkLocation(location, name)
    GLES32.glUniform3f(location, vec3.x, vec3.y, vec3.z)
  }

  fun setMatrix(name: String, mat4: Mat4) {
    val location = GLES32.glGetUniformLocation(program, name)
    checkLocation(location, name)
    val floatBuffer = ByteBuffer //矩阵4x4
      .allocateDirect(4 * 4 * BYTES_PER_FLOAT)
      .order(ByteOrder.nativeOrder())
      .asFloatBuffer()
    GLES32.glUniformMatrix4fv(location, 1, false, mat4 to floatBuffer)
  }

  fun setMatrix(name: String, matrix: FloatArray?) {
    val location = GLES32.glGetUniformLocation(program, name)
    checkLocation(location, name)
    GLES32.glUniformMatrix4fv(location, 1, false, matrix, 0)
  }

  private fun checkLocation(location: Int, name: String) {
    if (location == -1) throw RuntimeException("The uniform variable named '$name' cannot be found in your program.")
  }

  private fun createShader(type: Int, @RawRes rawId: Int): Int {
    val shader = GLES32.glCreateShader(type)
    val shaderCode: String? = loadShaderCode(context, rawId)
    GLES32.glShaderSource(shader, shaderCode)
    GLES32.glCompileShader(shader)
    val result = IntArray(1)
    GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, result, 0)
    if (result[0] != GLES32.GL_TRUE) {
      val errorMsg = GLES32.glGetShaderInfoLog(shader)
      throw RuntimeException(getShaderName(type) + " compile failed : " + errorMsg)
    }
    return shader
  }

  private fun getShaderName(type: Int): String {
    var name = ""
    when (type) {
      GLES32.GL_VERTEX_SHADER -> name = "GL_VERTEX_SHADER"
      GLES32.GL_FRAGMENT_SHADER -> name = "GL_FRAGMENT_SHADER"
      GLES32.GL_GEOMETRY_SHADER -> name = "GL_GEOMETRY_SHADER"
    }
    return name
  }

  private fun createShaderProgram(): Int {
    val shaderProgram = GLES32.glCreateProgram()
    val vertexShader = createShader(GLES32.GL_VERTEX_SHADER, vertexShaderRawId)
    val fragShader = createShader(GLES32.GL_FRAGMENT_SHADER, fragShaderRawId)
    GLES32.glAttachShader(shaderProgram, vertexShader)
    GLES32.glAttachShader(shaderProgram, fragShader)
    var geometryShader = -1
    if (geometryShaderRawId != -1) {
      geometryShader = createShader(GLES32.GL_GEOMETRY_SHADER, geometryShaderRawId)
      GLES32.glAttachShader(shaderProgram, geometryShader)
    }
    GLES32.glLinkProgram(shaderProgram)
    val result = IntArray(1)
    GLES32.glGetProgramiv(shaderProgram, GLES32.GL_LINK_STATUS, result, 0)
    if (result[0] != GLES32.GL_TRUE) {
      val errorMsg = GLES32.glGetProgramInfoLog(shaderProgram)
      throw RuntimeException("ShaderProgram Link failed : $errorMsg")
    }
    GLES32.glDeleteShader(vertexShader)
    GLES32.glDeleteShader(fragShader)
    if (geometryShader != -1) GLES32.glDeleteShader(geometryShader)
    return shaderProgram
  }

  class Builder(val context: Context) {
    var vertexShaderRawId = -1
    var fragShaderRawId = -1
    var geometryShaderRawId = -1
    fun setVertexShader(@RawRes id: Int): Builder {
      vertexShaderRawId = id
      return this
    }

    fun setFragmentShader(@RawRes id: Int): Builder {
      fragShaderRawId = id
      return this
    }

    fun setGeometryShader(@RawRes id: Int): Builder {
      geometryShaderRawId = id
      return this
    }

    fun buildProgram(): Shader {
      require(vertexShaderRawId != -1) { "Vertex Shader must be set!" }
      require(fragShaderRawId != -1) { "Fragment Shader must be set!" }
      return Shader(this)
    }
  }

  companion object {
    private const val TAG = "Shader"
    private const val BYTES_PER_INT = 4
    private const val BYTES_PER_FLOAT = 4
  }
}