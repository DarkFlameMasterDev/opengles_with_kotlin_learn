package com.czb.opengl_es.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.czb.opengl_es.R
import com.czb.opengl_es.databinding.ActivityMixTextureBinding

class MixTextureActivity : AppCompatActivity() {

  private lateinit var mixTextureBinding: ActivityMixTextureBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mixTextureBinding = ActivityMixTextureBinding.inflate(layoutInflater)
    setContentView(mixTextureBinding.root)
    initView()
  }


  private fun initView() {
    mixTextureBinding.upMix.setOnClickListener {
      upMix()
    }
    mixTextureBinding.downMix.setOnClickListener {
      downMix()
    }
  }

  private fun upMix() {
    mixTextureBinding.textureGLSurfaceView.mixValue += 0.05f
    if (mixTextureBinding.textureGLSurfaceView.mixValue >= 1) {
      mixTextureBinding.textureGLSurfaceView.mixValue = 1f
    }
  }

  private fun downMix() {
    mixTextureBinding.textureGLSurfaceView.mixValue -= 0.05f
    if (mixTextureBinding.textureGLSurfaceView.mixValue <= 0) {
      mixTextureBinding.textureGLSurfaceView.mixValue = 0f
    }
  }
}