package com.czb.opengl_es.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.czb.opengl_es.databinding.ActivityCameraBinding
import com.czb.opengl_es.gl.Direction
import com.czb.opengl_es.log

class CameraActivity : AppCompatActivity() {

  private lateinit var cameraBinding: ActivityCameraBinding
  private lateinit var btnActionMap: Map<Int, Direction>
  private lateinit var childThread: ChildThread
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    cameraBinding = ActivityCameraBinding.inflate(layoutInflater)
    setContentView(cameraBinding.root)
    childThread = ChildThread()
    childThread.start()
    initView()
  }


  @SuppressLint("ClickableViewAccessibility")
  private fun initView() {
    btnActionMap = mapOf(
      cameraBinding.up.id to Direction.UP,
      cameraBinding.down.id to Direction.DOWN,
      cameraBinding.left.id to Direction.LEFT,
      cameraBinding.right.id to Direction.RIGHT
    )
    cameraBinding.up.setOnTouchListener { v, event ->
      longTouch(event, v)
      true
    }
    cameraBinding.down.setOnTouchListener { v, event ->
      longTouch(event, v)
      true
    }
    cameraBinding.left.setOnTouchListener { v, event ->
      longTouch(event, v)
      true
    }
    cameraBinding.right.setOnTouchListener { v, event ->
      longTouch(event, v)
      true
    }
  }

  private fun longTouch(event: MotionEvent, v: View?) {
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        startPress(v as Button)
      }

      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        stopPress(v as Button)
      }
    }
  }

  private fun startPress(btn: Button) {
    when (btn) {
      cameraBinding.up -> {
        childThread.isUpPressing = true
      }

      cameraBinding.down -> {
        childThread.isDownPressing = true
      }

      cameraBinding.left -> {
        childThread.isLeftPressing = true
      }

      cameraBinding.right -> {
        childThread.isRightPressing = true
      }
    }
  }

  private fun stopPress(btn: Button) {
    when (btn) {
      cameraBinding.up -> {
        childThread.isUpPressing = false
      }

      cameraBinding.down -> {
        childThread.isDownPressing = false
      }

      cameraBinding.left -> {
        childThread.isLeftPressing = false
      }

      cameraBinding.right -> {
        childThread.isRightPressing = false
      }
    }
  }

  inner class ChildThread : Thread() {

    @Volatile
    var isUpPressing = false

    @Volatile
    var isDownPressing = false

    @Volatile
    var isLeftPressing = false

    @Volatile
    var isRightPressing = false
    override fun run() {
      while (true) {
        if (isUpPressing || isDownPressing || isLeftPressing || isRightPressing) {
          when {
            isUpPressing -> cameraBinding.cameraGLSurfaceView.processIn(Direction.UP)
            isDownPressing -> cameraBinding.cameraGLSurfaceView.processIn(Direction.DOWN)
            isLeftPressing -> cameraBinding.cameraGLSurfaceView.processIn(Direction.LEFT)
            isRightPressing -> cameraBinding.cameraGLSurfaceView.processIn(Direction.RIGHT)
          }
          sleep(500)
        }
      }
    }
  }
}