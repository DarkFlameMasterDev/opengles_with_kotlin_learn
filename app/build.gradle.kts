plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.czb.opengl_es"
  compileSdk = 34

  packaging {
    resources.excludes.add("META-INF/INDEX.LIST")
    resources.excludes.add("META-INF/linux/x64/org/lwjgl/liblwjgl.so.sha1")
    resources.excludes.add("META-INF/linux/x64/org/lwjgl/jemalloc/libjemalloc.so.sha1")
  }

  defaultConfig {
    applicationId = "com.czb.opengl_es"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }


  viewBinding {
    enable = true
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.10.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  implementation("io.github.kotlin-graphics:glm:0.9.9.1-12")
  implementation("io.github.kotlin-graphics:kool:0.9.79")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
}