package com.czb.opengl_es.gl

import android.view.MotionEvent
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3

/**
 * @author wuzhanqiao
 * @date 2022/6/17.
 */
class Camera {
  //cameras属性
  var position = Vec3(0.0f, 0.0f, 0.0f)
    private set
  var front = Vec3(0.0f, 0.0f, -1.0f)
    private set
  private var up: Vec3? = null
  private var right: Vec3? = null
  private var worldUp = Vec3(0.0f, 1.0f, 0.0f)

  //欧拉角
  private var yaw = -90.0f
  private var pitch = 0.0f

  //camera选项
  private var fov = 45.0f
  private var lastDist = 0.0f

  constructor(position: Vec3) {
    this.position = position
    updateCameraVectors()
  }

  constructor(position: Vec3, worldUp: Vec3) {
    this.position = position
    this.worldUp = worldUp
    updateCameraVectors()
  }

  constructor(yaw: Float, pitch: Float) {
    this.yaw = yaw
    this.pitch = pitch
    updateCameraVectors()
  }

  constructor(position: Vec3, worldUp: Vec3, yaw: Float, pitch: Float) {
    this.position = position
    this.worldUp = worldUp
    this.yaw = yaw
    this.pitch = pitch
    updateCameraVectors()
  }

  val viewMatrix: Mat4?
    /**
     * 获取使用欧拉角和LookAt矩阵计算的view矩阵
     */
    get() = up?.let { glm.lookAt(position, position.plus(front), it) }

  fun move(d: Direction?, cameraSpeed: Float) {
    when (d) {
      Direction.UP ->                 //即position += front * cameraSpeed
        position = position.plus(front.times(cameraSpeed))

      Direction.DOWN ->                 //即position -= front * cameraSpeed;
        position = position.minus(front.times(cameraSpeed))

      Direction.LEFT ->                 //即position -= right * cameraSpeed
        position = position.minus(right!!.times(cameraSpeed))

      Direction.RIGHT ->                 //即position += right * cameraSpeed;
        position = position.plus(right!!.times(cameraSpeed))

      else -> {}
    }
  }

  fun setYaw(yaw: Float) {
    this.yaw = yaw
    updateCameraVectors()
  }

  fun setPitch(pitch: Float) {
    setPitch(pitch, true)
  }

  fun setPitch(pitch: Float, constrainPitch: Boolean) {
    var pitch = pitch
    if (constrainPitch) {
      if (pitch > MAX_PITCH) {
        pitch = MAX_PITCH
      } else if (pitch < MIN_PITCH) {
        pitch = MIN_PITCH
      }
    }
    this.pitch = pitch
    updateCameraVectors()
  }

  fun setFov(fov: Float) {
    this.fov = adjustFov(fov)
  }

  private fun adjustFov(fov: Float): Float {
    var fov = fov
    if (fov > MAX_FOV) {
      fov = MAX_FOV
    } else if (fov < MIN_FOV) {
      fov = MIN_FOV
    }
    return fov
  }

  fun getFov(): Float {
    return fov
  }

  /**
   * 根据摄像机更新的欧拉角，重新计算front等向量
   */
  private fun updateCameraVectors() {
    //计算front向量
    val direction = Vec3()
    direction.x =
      Math.cos(glm.radians(yaw).toDouble()).toFloat() * Math.cos(glm.radians(pitch).toDouble())
        .toFloat()
    direction.y = Math.sin(glm.radians(pitch).toDouble()).toFloat()
    direction.z =
      Math.sin(glm.radians(yaw).toDouble()).toFloat() * Math.cos(glm.radians(pitch).toDouble())
        .toFloat()
    front = glm.normalize(direction, Vec3())
    //重新计算right和up向量
    //将向量标准化，因为你向上或向下看的次数越多，它们的长度就越接近0，从而导致运动变慢。
    right = glm.normalize(glm.cross(front, worldUp, Vec3()), Vec3())
    up = glm.normalize(glm.cross(right!!, front, Vec3()), Vec3())
  }

  fun handleZoom(event: MotionEvent, fingersMaximumDistance: Int): Boolean {
    when (event.actionMasked) {
      MotionEvent.ACTION_POINTER_DOWN -> {

        //刚好双指
        if (event.pointerCount == 2) {
          lastDist = getTwoFingersDistance(event)
        }
      }

      MotionEvent.ACTION_MOVE -> {
        if (event.pointerCount == 2) {
          val dist = getTwoFingersDistance(event)
          val offset = getFovOffset(lastDist - dist, fingersMaximumDistance)
          lastDist = dist
          val newFov = adjustFov(fov + offset)
          if (newFov != fov) {
            setFov(newFov)
            return true
          }
        }
      }
    }
    return false
  }

  private fun getTwoFingersDistance(event: MotionEvent): Float {
    val x0 = event.getX(0)
    val y0 = event.getY(0)
    val x1 = event.getX(1)
    val y1 = event.getY(1)
    return Math.sqrt(Math.pow((x0 - x1).toDouble(), 2.0) + Math.pow((y0 - y1).toDouble(), 2.0))
      .toFloat()
  }

  private fun getFovOffset(d: Float, fingersMaximumDistance: Int): Float {
    val unit = (MAX_FOV - MIN_FOV) / fingersMaximumDistance
    return d * unit
  }

  companion object {
    private const val MIN_FOV = 1.0f
    private const val MAX_FOV = 45.0f
    private const val MIN_PITCH = -89.0f
    private const val MAX_PITCH = 89.0f
  }
}