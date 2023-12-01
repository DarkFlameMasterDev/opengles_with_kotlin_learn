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
import java.util.concurrent.atomic.AtomicBoolean

class CameraActivity : AppCompatActivity() {

  private lateinit var cameraBinding: ActivityCameraBinding
  private lateinit var btnActionMap: Map<Int, Direction>
  private lateinit var handler: Handler
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    cameraBinding = ActivityCameraBinding.inflate(layoutInflater)
    setContentView(cameraBinding.root)
    handler = Handler(mainLooper)
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
        startPrint(v as Button)
      }

      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        stopPrinting(v as Button)
      }
    }
  }


  @Volatile
  private var isUpPressing = AtomicBoolean(false)

  @Volatile
  private var isDownPressing = AtomicBoolean(false)

  @Volatile
  private var isLeftPressing = AtomicBoolean(false)

  @Volatile
  private var isRightPressing = AtomicBoolean(false)

  private fun pressStateSet(btn: Button, state: Boolean) {
    when (btn.id) {
      cameraBinding.up.id -> {
        isUpPressing.set(state)
      }

      cameraBinding.down.id -> {
        isDownPressing.set(state)
      }

      cameraBinding.left.id -> {
        isLeftPressing.set(state)
      }

      cameraBinding.right.id -> {
        isRightPressing.set(state)
      }
    }
  }

  private fun pressStateGet(btn: Button) = when (btn.id) {
    cameraBinding.up.id -> isUpPressing.get()
    cameraBinding.down.id -> isDownPressing.get()
    cameraBinding.left.id -> isLeftPressing.get()
    cameraBinding.right.id -> isRightPressing.get()
    else -> false
  }

  private fun startPrint(btn: Button) {
    pressStateSet(btn, true)
    handler.post(object : Runnable {
      override fun run() {
        log("isPressing = ${pressStateGet(btn)} time second = ${System.currentTimeMillis() / 1000 % 3600}")
        if (btn.id in btnActionMap.keys) {
          cameraBinding.cameraGLSurfaceView.processIn(btnActionMap[btn.id]!!)
        }
        if (pressStateGet(btn)) {
          handler.postDelayed(this, 500)
        }
      }
    })
  }

  private fun stopPrinting(btn: Button) {
    pressStateSet(btn, false)
  }
}