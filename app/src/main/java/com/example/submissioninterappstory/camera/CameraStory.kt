package com.example.submissioninterappstory.camera

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.submissioninterappstory.R
import com.example.submissioninterappstory.databinding.ActivityAddStoryAppsBinding
import com.example.submissioninterappstory.databinding.ActivityCameraStoryBinding
import com.example.submissioninterappstory.story.AddStoryApps
import com.example.submissioninterappstory.story.utils.Helper

class CameraStory : AppCompatActivity() {

    private lateinit var binding: ActivityCameraStoryBinding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraStoryBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_camera_story)
        setContentView(binding.root)

        binding.cameraImage.setOnClickListener {
            takePhotoStory()
        }

        binding.flipCameraimg.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            staetCamera()
        }

    }

    public override fun onResume() {
        super.onResume()
        hideSystemUIstories()
        staetCamera()
    }

    private fun takePhotoStory(){
        val imageCapture = imageCapture?:return
        val photoFile = Helper.createFile(application)
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraStory, "Foto gagal diambil", Toast.LENGTH_SHORT).show()
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val intent = Intent()
                    intent.putExtra("picture", photoFile)
                    intent.putExtra("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    setResult(AddStoryApps.CAMERA_X_RESULT, intent)
                    finish()
                }
            }
        )

    }

    private fun staetCamera(){
        //camera
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraVieww.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,preview, imageCapture
                )
            } catch (exc: Exception){
                Toast.makeText(this@CameraStory, "Kamera gagal", Toast.LENGTH_SHORT).show()

            }
        }, ContextCompat.getMainExecutor(this))

    }

    private fun hideSystemUIstories(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}