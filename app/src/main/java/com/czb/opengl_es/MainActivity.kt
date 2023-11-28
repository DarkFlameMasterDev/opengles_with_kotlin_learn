package com.czb.opengl_es

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.czb.opengl_es.activities.CubeActivity
import com.czb.opengl_es.activities.MixTextureActivity
import com.czb.opengl_es.activities.TextureActivity
import com.czb.opengl_es.activities.TransformActivity
import com.czb.opengl_es.activities.TriangleActivity
import com.czb.opengl_es.activities.WindowActivity
import com.czb.opengl_es.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  private lateinit var mainBinding: ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mainBinding.root)
    init()
  }

  private fun init() {
    mainBinding.btnWindowActivity.setOnClickListener {
      startActivityByIntent(WindowActivity::class.java)
    }
    mainBinding.btnTriangleActivity.setOnClickListener {
      startActivityByIntent(TriangleActivity::class.java)
    }
    mainBinding.btnTextureActivity.setOnClickListener {
      startActivityByIntent(TextureActivity::class.java)
    }
    mainBinding.btnMixTextureActivity.setOnClickListener {
      startActivityByIntent(MixTextureActivity::class.java)
    }
    mainBinding.btnTransformActivity.setOnClickListener {
      startActivityByIntent(TransformActivity::class.java)
    }
    mainBinding.btnCubeActivity.setOnClickListener {
      startActivityByIntent(CubeActivity::class.java)
    }
  }

  private fun startActivityByIntent(activity: Class<out AppCompatActivity>) {
    val intent = Intent(this, activity)
    startActivity(intent)
  }


}