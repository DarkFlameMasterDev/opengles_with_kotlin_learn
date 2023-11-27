package com.czb.opengl_es.gl.sample

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES32
import android.opengl.GLUtils
import com.czb.opengl_es.R
import com.czb.opengl_es.gl.BaseRender
import com.czb.opengl_es.gl.Shader
import com.czb.opengl_es.gl.utils.Orientation
import com.czb.opengl_es.gl.utils.reverse
import com.czb.opengl_es.log
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TransformRenderer(context: Context) : BaseRender(context) {

  private lateinit var vao: IntArray
  private lateinit var shader: Shader
  private lateinit var texture: IntArray
  override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    // 1. shader & program
    shader = Shader.Builder(context)
      .setVertexShader(R.raw.transform_vertex)
      .setFragmentShader(R.raw.texture_fragment)
      .buildProgram()

    val vertices = floatArrayOf(
      // 顶点坐标 // 纹理坐标
      0.5f, 0.5f, 0.0f,
      1.0f, 1.0f, // 右上

      0.5f, -0.5f, 0.0f,
      1.0f, 0.0f, // 右下

      -0.5f, -0.5f, 0.0f,
      0.0f, 0.0f, // 左下

      -0.5f, 0.5f, 0.0f,
      0.0f, 1.0f // 左上
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
    GLES32.glBufferData(
      GLES32.GL_ARRAY_BUFFER,
      vertices.size * BYTES_PER_FLOAT,
      verticesBuffer,
      GLES32.GL_STATIC_DRAW
    )

    // 5.设置 Attribute
    GLES32.glVertexAttribPointer(0, 3, GLES32.GL_FLOAT, false, 5 * BYTES_PER_FLOAT, 0)
    GLES32.glEnableVertexAttribArray(0)
    GLES32.glVertexAttribPointer(
      1,
      2,
      GLES32.GL_FLOAT,
      false,
      5 * BYTES_PER_FLOAT,
      3 * BYTES_PER_FLOAT
    )
    GLES32.glEnableVertexAttribArray(1)

    // 6.EBO
    createAndBindEBO(1)
    val indicesBuffer = ByteBuffer
      .allocateDirect(indices.size * BYTES_PER_INT)
      .order(ByteOrder.nativeOrder())
      .asIntBuffer()
      .put(indices)
    indicesBuffer.position(0)
    GLES32.glBufferData(
      GLES32.GL_ELEMENT_ARRAY_BUFFER,
      indices.size * 4,
      indicesBuffer,
      GLES32.GL_STATIC_DRAW
    )

    unBindVBO()
    unBindVAO()
    unBindEBO()

    // texture
    val borderColor = floatArrayOf(0.3f, 0.2f, 1.0f, 1.0f)
    texture = createAndBindTexture2D()
    // 环绕方式
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D,
      GLES32.GL_TEXTURE_WRAP_S,
      GLES32.GL_CLAMP_TO_BORDER
    )
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D,
      GLES32.GL_TEXTURE_WRAP_T,
      GLES32.GL_CLAMP_TO_BORDER
    )
    GLES32.glTexParameterfv(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_BORDER_COLOR, borderColor, 0)
    // 采样方式
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D,
      GLES32.GL_TEXTURE_MIN_FILTER,
      GLES32.GL_LINEAR_MIPMAP_LINEAR
    )
    GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR)
    // bitmap 解码
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.awesomeface)
    GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap.reverse(Orientation.Vertical), 0)
    GLES32.glGenerateMipmap(GLES32.GL_TEXTURE_2D)
    bitmap.recycle()
  }

  override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    GLES32.glViewport(0, 0, width, height)
  }

  private var translateX = 0f
  private var translateY = 0f
  private var translateZ = 0f
  private var transform = Mat4(1.0f)
  private var angle = 0f
  override fun onDrawFrame(gl: GL10?) {
    GLES32.glClearColor(0.2f, 0.8f, 0.8f, 1.0f)
    GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT)

    GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
    GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texture[0])

    if (translateX < 0.5) {
      translateX += 0.001f
    } else {
      translateX = 0.5f
    }

    if (translateY > -0.5) {
      translateY -= 0.001f
    } else {
      translateY = -0.5f
    }
    transform = Mat4(1.0f)
    transform = glm.translate(transform, Vec3(translateX, translateY, translateZ))
    angle = (System.currentTimeMillis() % 6283) / 1000f
    log("System.currentTimeMillis() = ${System.currentTimeMillis()} angle =  $angle")
    transform = glm.rotate(transform, angle, Vec3(0, 0, 1))

    shader.use()
    shader.setMatrix("transform", transform)

    GLES32.glBindVertexArray(vao[0])
    GLES32.glDrawElements(GLES32.GL_TRIANGLES, 6, GLES32.GL_UNSIGNED_INT, 0)
    unBindVAO()
  }
}