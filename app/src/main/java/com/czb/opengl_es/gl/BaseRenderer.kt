package com.czb.opengl_es.gl

import android.content.Context
import android.opengl.GLES32
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import glm_.vec3.Vec3

abstract class BaseRenderer(protected var context: Context) : GLSurfaceView.Renderer {
  private var camera: Camera = Camera(Vec3(0.0f, 0.0f, 3.0f))
  protected fun createAndBindVAO(size: Int): IntArray {
    val vao = IntArray(size)
    GLES32.glGenVertexArrays(vao.size, vao, 0)
    GLES32.glBindVertexArray(vao[0])
    return vao
  }

  protected fun unBindVAO() {
    GLES32.glBindVertexArray(GLES32.GL_NONE)
  }

  protected fun createAndBindVBO(size: Int): IntArray {
    val vbo = IntArray(size)
    GLES32.glGenBuffers(vbo.size, vbo, 0)
    GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vbo[0])
    return vbo
  }

  protected fun unBindVBO() {
    GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, GLES32.GL_NONE)
  }

  protected fun createAndBindEBO(size: Int): IntArray {
    val ebo = IntArray(size)
    GLES32.glGenBuffers(ebo.size, ebo, 0)
    GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, ebo[0])
    return ebo
  }

  protected fun unBindEBO() {
    GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, GLES32.GL_NONE)
  }

  protected fun createAndBindTexture2D(): IntArray {
    val texture = IntArray(1)
    GLES32.glGenTextures(1, texture, 0)
    GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texture[0])
    return texture
  }

  fun move(d: Direction?, cameraSpeed: Float) {
    camera.move(d, cameraSpeed)
  }

  fun setYaw(yaw: Float) {
    camera.setYaw(yaw)
  }

  fun setPitch(pitch: Float) {
    camera.setPitch(pitch)
  }

  fun setFov(fov: Float) {
    camera.setFov(fov)
  }

  fun handleZoom(event: MotionEvent, fingersMaxDistance: Int): Boolean {
    return camera.handleZoom(event, fingersMaxDistance)
  }

  companion object {
    const val BYTES_PER_INT = 4
    const val BYTES_PER_FLOAT = 4
  }
}