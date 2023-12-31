package com.czb.opengl_es.gl.sample

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES32.GL_ARRAY_BUFFER
import android.opengl.GLES32.GL_CLAMP_TO_BORDER
import android.opengl.GLES32.GL_COLOR_BUFFER_BIT
import android.opengl.GLES32.GL_ELEMENT_ARRAY_BUFFER
import android.opengl.GLES32.GL_FLOAT
import android.opengl.GLES32.GL_LINEAR
import android.opengl.GLES32.GL_LINEAR_MIPMAP_LINEAR
import android.opengl.GLES32.GL_STATIC_DRAW
import android.opengl.GLES32.GL_TEXTURE0
import android.opengl.GLES32.GL_TEXTURE_2D
import android.opengl.GLES32.GL_TEXTURE_BORDER_COLOR
import android.opengl.GLES32.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES32.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES32.GL_TEXTURE_WRAP_S
import android.opengl.GLES32.GL_TEXTURE_WRAP_T
import android.opengl.GLES32.GL_TRIANGLES
import android.opengl.GLES32.GL_UNSIGNED_INT
import android.opengl.GLES32.glActiveTexture
import android.opengl.GLES32.glBindTexture
import android.opengl.GLES32.glBindVertexArray
import android.opengl.GLES32.glBufferData
import android.opengl.GLES32.glClear
import android.opengl.GLES32.glClearColor
import android.opengl.GLES32.glDrawElements
import android.opengl.GLES32.glEnableVertexAttribArray
import android.opengl.GLES32.glGenerateMipmap
import android.opengl.GLES32.glTexParameterfv
import android.opengl.GLES32.glTexParameteri
import android.opengl.GLES32.glVertexAttribPointer
import android.opengl.GLES32.glViewport
import android.opengl.GLUtils
import com.czb.opengl_es.R
import com.czb.opengl_es.gl.BaseRenderer
import com.czb.opengl_es.gl.Shader
import com.czb.opengl_es.gl.utils.Orientation
import com.czb.opengl_es.gl.utils.reverse
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class TextureRenderer(context: Context) : BaseRenderer(context) {

  private lateinit var vao: IntArray
  private lateinit var shader: Shader
  private lateinit var texture: IntArray

  override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    // 1. shader & program
    shader = Shader.Builder(context)
      .setVertexShader(R.raw.texture_vertex)
      .setFragmentShader(R.raw.texture_fragment)
      .buildProgram()

    val vertices = floatArrayOf(
      // 顶点坐标 // 纹理坐标
      0.5f, 0.5f, 0.0f,
      1.2f, 1.2f, // 右上

      0.5f, -0.5f, 0.0f,
      1.2f, 0.0f, // 右下

      -0.5f, -0.5f, 0.0f,
      0.0f, 0.0f, // 左下

      -0.5f, 0.5f, 0.0f,
      0.0f, 1.2f // 左上
    )

    val indices = intArrayOf(
      // 注意索引从0开始!
      0, 1, 3,  // 第一个三角形
      1, 2, 3 // 第二个三角形
    )

    // 2.先绑 VAO
    // 3.再绑 VBO
    vao = createAndBindVAO(1)
    createAndBindVBO(1)

    // 4.VBO Buffer 填充
    val verticesBuffer = ByteBuffer
      .allocateDirect(vertices.size * BYTES_PER_FLOAT)
      .order(ByteOrder.nativeOrder())
      .asFloatBuffer()
      .put(vertices)
    verticesBuffer.position(0)
    glBufferData(GL_ARRAY_BUFFER, vertices.size * BYTES_PER_FLOAT, verticesBuffer, GL_STATIC_DRAW)

    // 5.设置 Attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * BYTES_PER_FLOAT, 0)
    glEnableVertexAttribArray(0)
    glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * BYTES_PER_FLOAT, 3 * BYTES_PER_FLOAT)
    glEnableVertexAttribArray(1)

    // 6.EBO
    createAndBindEBO(1)
    val indicesBuffer = ByteBuffer
      .allocateDirect(indices.size * BYTES_PER_INT)
      .order(ByteOrder.nativeOrder())
      .asIntBuffer()
      .put(indices)
    indicesBuffer.position(0)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size * 4, indicesBuffer, GL_STATIC_DRAW)

    unBindVBO()
    unBindVAO()
    unBindEBO()

    // texture
    val borderColor = floatArrayOf(0.3f, 0.2f, 1.0f, 1.0f)
    texture = createAndBindTexture2D()
    // 环绕方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER)
    glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor, 0)
    // 采样方式
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    // bitmap 解码
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.awesomeface)
    GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap.reverse(Orientation.Vertical), 0)
    glGenerateMipmap(GL_TEXTURE_2D)
    bitmap.recycle()
  }

  override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    glViewport(0, 0, width, height)
  }

  override fun onDrawFrame(gl: GL10?) {
    glClearColor(0.2f, 0.8f, 0.8f, 1.0f)
    glClear(GL_COLOR_BUFFER_BIT)

    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, texture[0])

    shader.use()
    glBindVertexArray(vao[0])
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0)
    unBindVAO()
  }

}
