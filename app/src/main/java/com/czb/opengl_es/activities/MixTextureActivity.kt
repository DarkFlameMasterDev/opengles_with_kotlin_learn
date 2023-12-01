package com.czb.opengl_es.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import com.czb.opengl_es.R
import com.czb.opengl_es.databinding.ActivityMixTextureBinding
import com.czb.opengl_es.gl.Direction
import com.czb.opengl_es.log
import java.util.concurrent.atomic.AtomicBoolean

class MixTextureActivity : AppCompatActivity() {

  private lateinit var mixTextureBinding: ActivityMixTextureBinding
  private lateinit var handler: Handler
  private lateinit var btnActionMap: Map<Int, () -> Unit>
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mixTextureBinding = ActivityMixTextureBinding.inflate(layoutInflater)
    setContentView(mixTextureBinding.root)
    handler = Handler(mainLooper)
    initView()
  }


  @SuppressLint("ClickableViewAccessibility")
  private fun initView() {
    btnActionMap = mapOf(
      mixTextureBinding.upMix.id to {
        val tempMixValue = mixTextureBinding.mixTextureGLSurfaceView.mixValue + 0.05f
        if (tempMixValue > 1.0f) {
          mixTextureBinding.mixTextureGLSurfaceView.mixValue = 1.0f
        } else {
          mixTextureBinding.mixTextureGLSurfaceView.mixValue = tempMixValue
        }
      },
      mixTextureBinding.downMix.id to {
        val tempMixValue = mixTextureBinding.mixTextureGLSurfaceView.mixValue - 0.05f
        if (tempMixValue < 0.0f) {
          mixTextureBinding.mixTextureGLSurfaceView.mixValue = 0.0f
        } else {
          mixTextureBinding.mixTextureGLSurfaceView.mixValue = tempMixValue
        }
      }
    )
    mixTextureBinding.upMix.setOnTouchListener { v, event ->
      longTouch(event, v as Button)
      true
    }
    mixTextureBinding.downMix.setOnTouchListener { v, event ->
      longTouch(event, v as Button)
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
    pressStateSet(btn, true)
    handler.post(object : Runnable {
      override fun run() {
        log("isPressing = ${pressStateGet(btn)} time second = ${System.currentTimeMillis() / 1000 % 3600}")
        if (pressStateGet(btn)) {
          btnActionMap[btn.id]?.invoke()
          handler.postDelayed(this, 500)
        }
      }
    })
  }

  private fun stopPress(btn: Button) {
    pressStateSet(btn, false)
  }

  @Volatile
  private var isUpPressing = AtomicBoolean(false)

  @Volatile
  private var isDownPressing = AtomicBoolean(false)

  private fun pressStateGet(btn: Button) = when (btn.id) {
    mixTextureBinding.upMix.id -> isUpPressing.get()
    mixTextureBinding.downMix.id -> isDownPressing.get()
    else -> false
  }

  private fun pressStateSet(btn: Button, state: Boolean) {
    when (btn.id) {
      mixTextureBinding.upMix.id -> isUpPressing.set(state)
      mixTextureBinding.downMix.id -> isDownPressing.set(state)
    }
  }
}