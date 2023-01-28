package com.example.submissioninterappstory.story

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.ViewModelProvider
import com.example.submissioninterappstory.R
import com.example.submissioninterappstory.api.ApiAkunConfig
import com.example.submissioninterappstory.api.ApiInterResponse
import com.example.submissioninterappstory.camera.CameraStory
import com.example.submissioninterappstory.databinding.ActivityAddStoryAppsBinding
import com.example.submissioninterappstory.main.MainViewModel
import com.example.submissioninterappstory.model.ModelFactory
import com.example.submissioninterappstory.model.UserPref
import com.example.submissioninterappstory.story.utils.Helper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryApps : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryAppsBinding
    private lateinit var modelMain: MainViewModel
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
            )
        }

        setModel()

        binding.addCameraxBtn.setOnClickListener { startCameraX() }
        binding.addGallBtn.setOnClickListener { startGallery() }
        binding.addUploadphotoBtn.setOnClickListener { uploadPhoto() }
    }

    private fun setModel() {
        modelMain = ViewModelProvider(
            this,
            ModelFactory(UserPref.getInstance(dataStore))
        )[MainViewModel::class.java]
    }

    private fun uploadPhoto(){
        if (getFile != null){
            val file = Helper.reduceFileImage(getFile as File)
            val description = binding.desEditText.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            modelMain.getUserakun().observe(this){user ->
                val service = ApiAkunConfig().getApiUserInter().uploadImageakunstory(imageMultipart, description, "Bearer ${user.token}")
                service.enqueue(object : Callback<ApiInterResponse>{
                    override fun onResponse(
                        call: Call<ApiInterResponse>,
                        response: Response<ApiInterResponse>
                    ) {
                        if (response.isSuccessful){
                            val responseBody = response.body()
                            if(responseBody != null && !responseBody.error){
                                Toast.makeText(this@AddStoryApps, "SUKSER", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else{
                            Toast.makeText(this@AddStoryApps,response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiInterResponse>, t: Throwable) {
                        Toast.makeText(this@AddStoryApps, "failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Helper.showToast(this@AddStoryApps, "Silahkan masukan foto")
        }
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle("Upload Image..")
                setMessage("Suksess")
                setPositiveButton("Oke") { _, _ ->
                }
                finish()
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Upload Image..")
                setMessage("Failed" +  ", $message")
                setPositiveButton("Oke") { _, _ ->
                }
                create()
                show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    private fun startCameraX() {
        val intent = Intent(this, CameraStory::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {

            val myFile = it.data?.getSerializableExtra("picture") as File
            getFile = myFile
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = Helper.rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.addStoryImgview.setImageBitmap(result)
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = Helper.uriToFile(selectedImg, this@AddStoryApps)
            getFile = myFile
            binding.addStoryImgview.setImageURI(selectedImg)
        }
    }

    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}