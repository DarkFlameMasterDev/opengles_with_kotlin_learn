package com.czb.opengl_es.gl.sample

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES32.glDrawArrays
import android.opengl.GLES32.GL_DEPTH_TEST
import android.opengl.GLES32.glEnable
import android.opengl.GLES32
import android.opengl.GLES32.GL_TRIANGLES
import android.opengl.GLUtils
import com.czb.opengl_es.R
import com.czb.opengl_es.gl.BaseRenderer
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
import kotlin.random.Random

class CubeRenderer(context: Context) : BaseRenderer(context) {
  private lateinit var vao: IntArray
  private lateinit var shader: Shader
  private lateinit var texture1: IntArray
  private lateinit var texture2: IntArray
  private var mixValue = 0.5f

  var width = 0
  var height = 0

  private lateinit var cubePositions: Array<Vec3>
  private lateinit var rotateVec3: Array<Vec3>

  override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    val vertices = floatArrayOf(
      // 顶点坐标 // 纹理坐标
      -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
      0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
      0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
      0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
      -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
      -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

      -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
      0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
      0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
      0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
      -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
      -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

      -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
      -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
      -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
      -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
      -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
      -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

      0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
      0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
      0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
      0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
      0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
      0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

      -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
      0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
      0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
      0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
      -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
      -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

      -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
      0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
      0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
      0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
      -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
      -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
    )

    cubePositions = arrayOf(
      Vec3(0.0f, 0.0f, 0.0f),
      Vec3(2.0f, 5.0f, -15.0f),
      Vec3(-1.5f, -2.2f, -2.5f),
      Vec3(-3.8f, -2.0f, -12.3f),
      Vec3(2.4f, -0.4f, -3.5f),
      Vec3(-1.7f, 3.0f, -7.5f),
      Vec3(1.3f, -2.0f, -2.5f),
      Vec3(1.5f, 2.0f, -2.5f),
      Vec3(1.5f, 0.2f, -1.5f),
      Vec3(-1.3f, 1.0f, -1.5f)
    )

    rotateVec3 = arrayOf(
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()),
      Vec3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
    )

    glEnable(GL_DEPTH_TEST)

    shader = Shader.Builder(context).setVertexShader(R.raw.cube_vertex)
      .setFragmentShader(R.raw.mix_fragment).buildProgram()

    // VAO
    // VBO
    vao = createAndBindVAO(1)
    createAndBindVBO(1)

    val verticesBuffer =
      ByteBuffer.allocateDirect(vertices.size * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder())
        .asFloatBuffer().put(vertices)
    verticesBuffer.position(0)
    GLES32.glBufferData(
      GLES32.GL_ARRAY_BUFFER, vertices.size * 4, verticesBuffer, GLES32.GL_STATIC_DRAW
    )

    GLES32.glVertexAttribPointer(0, 3, GLES32.GL_FLOAT, false, 5 * BYTES_PER_FLOAT, 0)
    GLES32.glEnableVertexAttribArray(0)
    GLES32.glVertexAttribPointer(
      1, 2, GLES32.GL_FLOAT, false, 5 * BYTES_PER_FLOAT, 3 * BYTES_PER_FLOAT
    )
    GLES32.glEnableVertexAttribArray(1)

    unBindVBO()
    unBindVAO()

    // texture
    val borderColor = floatArrayOf(0.3f, 0.2f, 1.0f, 1.0f)
    texture1 = createAndBindTexture2D()
    // 环绕方式
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_CLAMP_TO_BORDER
    )
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_CLAMP_TO_BORDER
    )
    GLES32.glTexParameterfv(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_BORDER_COLOR, borderColor, 0)
    // 采样方式
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_LINEAR_MIPMAP_LINEAR
    )
    GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR)
    // bitmap 解码
    val bitmap1 = BitmapFactory.decodeResource(context.resources, R.drawable.wooden_container)
    GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap1.reverse(Orientation.Vertical), 0)
    GLES32.glGenerateMipmap(GLES32.GL_TEXTURE_2D)
    bitmap1.recycle()

    texture2 = createAndBindTexture2D()
    // 环绕方式
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_CLAMP_TO_BORDER
    )
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_CLAMP_TO_BORDER
    )
    GLES32.glTexParameterfv(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_BORDER_COLOR, borderColor, 0)
    // 采样方式
    GLES32.glTexParameteri(
      GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_LINEAR_MIPMAP_LINEAR
    )
    GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR)
    // bitmap 解码
    val bitmap2 = BitmapFactory.decodeResource(context.resources, R.drawable.awesomeface)
    GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap2.reverse(Orientation.Vertical), 0)
    GLES32.glGenerateMipmap(GLES32.GL_TEXTURE_2D)
    bitmap2.recycle()

    shader.use()
    shader.setInt("texture1", 0)
    shader.setInt("texture2", 1)
  }

  override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    this.width = width
    this.height = height
    GLES32.glViewport(0, 0, width, height)
  }

  override fun onDrawFrame(gl: GL10?) {
    GLES32.glClearColor(0.2f, 0.8f, 0.8f, 1.0f)
    GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

    GLES32.glActiveTexture(GLES32.GL_TEXTURE0)
    GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texture1[0])
    GLES32.glActiveTexture(GLES32.GL_TEXTURE1)
    GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texture2[0])

    // 注意，我们将矩阵向我们要进行移动场景的反方向移动。
    var view = Mat4(1.0f)
    view = glm.translate(view, Vec3(0.0f, 0.0f, -3.0f))
    val projection = glm.perspective(glm.radians(45.0f), (width / height).toFloat(), 0.1f, 100.0f)

    shader.use()
    shader.setFloat("mixValue", mixValue)
    log("mixValue = $mixValue")

    shader.setMatrix("view", view)
    shader.setMatrix("projection", projection)

    GLES32.glBindVertexArray(vao[0])
    for (i in 0..<10) {
      var model = Mat4(1.0f)
      model = glm.translate(model, cubePositions[i])
      model = glm.rotate(model, (System.currentTimeMillis() % 6283) / 1000f, rotateVec3[i])
      shader.setMatrix("model", model)
      glDrawArrays(GL_TRIANGLES, 0, 36)
    }
    unBindVAO()
  }
}