package com.czb.opengl_es.gl.sample

import android.content.Context
import android.opengl.GLES32.GL_FLOAT
import android.opengl.GLES32
import android.opengl.GLES32.GL_ARRAY_BUFFER
import android.opengl.GLES32.GL_COLOR_BUFFER_BIT
import android.opengl.GLES32.GL_NONE
import android.opengl.GLES32.GL_STATIC_DRAW
import android.opengl.GLES32.GL_TRIANGLES
import android.opengl.GLES32.glBindVertexArray
import android.opengl.GLES32.glBufferData
import android.opengl.GLES32.glClear
import android.opengl.GLES32.glClearColor
import android.opengl.GLES32.glDrawArrays
import android.opengl.GLES32.glViewport

import com.czb.opengl_es.R
import com.czb.opengl_es.gl.BaseRender
import com.czb.opengl_es.gl.Shader
import com.czb.opengl_es.log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author wuzhanqiao
 * @date 2022/4/12.
 */
class TriangleRenderer(context: Context) : BaseRender(context) {
  private var vertices = floatArrayOf(
    -0.5f, -0.5f, 0.0f,
    0.5f, -0.5f, 0.0f,
    0.0f, 0.5f, 0.0f
  )

  private var vao = IntArray(1)
  private lateinit var shader: Shader

  override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
    log("onSurfaceCreated() called with: thread = [${Thread.currentThread()}]")

    shader = Shader.Builder(context)
      .setVertexShader(R.raw.triangle_vertex)
      .setFragmentShader(R.raw.triangle_fragment)
      .buildProgram()

    vao = createAndBindVAO(1)
    createAndBindVBO(1)

    val vertexBuffer = ByteBuffer
      .allocateDirect(vertices.size * BYTES_PER_FLOAT)
      .order(ByteOrder.nativeOrder())
      .asFloatBuffer()
      .put(vertices)
    vertexBuffer.position(0)
    glBufferData(
      GL_ARRAY_BUFFER,
      vertices.size * 4,
      vertexBuffer,
      GL_STATIC_DRAW
    )

    GLES32.glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * BYTES_PER_FLOAT, 0)
    GLES32.glEnableVertexAttribArray(0)

    unBindVAO()
    unBindVBO()
  }

  override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
    log("onSurfaceChanged() called with: gl = [$gl], width = [$width], height = [$height],thread = [${Thread.currentThread()}]")
    glViewport(0, 0, width, height)
  }

  override fun onDrawFrame(gl: GL10) {
    log("onDrawFrame() called with: gl = [$gl], thread = [${Thread.currentThread()}]")

    // 渲染
    // 清除颜色缓冲
    glClearColor(0.2f, 0.8f, 0.8f, 1.0f)
    glClear(GL_COLOR_BUFFER_BIT)

    // 激活着色器
    shader.use()

    // 绘制三角形
    glBindVertexArray(vao[0])
    glDrawArrays(GL_TRIANGLES, 0, 3)
    glBindVertexArray(GL_NONE)
  }
}