package com.czb.opengl_es.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.czb.opengl_es.R
import com.czb.opengl_es.databinding.ActivityCubeBinding

class CubeActivity : AppCompatActivity() {

  private lateinit var cubeBinding: ActivityCubeBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    cubeBinding = ActivityCubeBinding.inflate(layoutInflater)
    setContentView(cubeBinding.root)
  }
}