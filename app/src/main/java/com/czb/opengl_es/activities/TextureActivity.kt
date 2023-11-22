package com.czb.opengl_es.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.czb.opengl_es.R
import com.czb.opengl_es.databinding.ActivityTextureBinding

class TextureActivity : AppCompatActivity() {

  private lateinit var textureBinding: ActivityTextureBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    textureBinding = ActivityTextureBinding.inflate(layoutInflater)
    setContentView(textureBinding.root)
  }

}