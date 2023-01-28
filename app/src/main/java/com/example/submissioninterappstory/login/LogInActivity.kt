package com.example.submissioninterappstory.login

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.submissioninterappstory.R
import com.example.submissioninterappstory.databinding.ActivityLogInBinding
import com.example.submissioninterappstory.main.MainActivity
import com.example.submissioninterappstory.model.ModelFactory
import com.example.submissioninterappstory.model.UserModelStory
import com.example.submissioninterappstory.model.UserPref
import com.example.submissioninterappstory.story.utils.Helper

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var userakun: UserModelStory
    private lateinit var loginModel: LogInModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewstory()
        setupViewModelstory()
        setActionstory()
        plyAnimations()
    }

    private fun plyAnimations() {
        ObjectAnimator.ofFloat(binding.imgLoginakun, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupViewModelstory() {
        loginModel = ViewModelProvider(
            this, ModelFactory(UserPref.getInstance(dataStore))
        )[LogInModel::class.java]

        loginModel.getUser().observe(this) { user ->
            this.userakun = user
        }
    }

    private fun setupViewstory(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setActionstory(){
        binding.LogInbuttonAkun.setOnClickListener {
            val email= binding.LoginemailEdit.text.toString()
            val password = binding.LoginpasswordEdit.text.toString()
            when {
                email.isEmpty() -> {
                    binding.LoginemailEdit.error = "Masukkan email anda di sini"
                }
                password.isEmpty() -> {
                    binding.LoginpasswordEdit.error = "Masukkan password anda di sini"
                }
                else -> {
                    binding.progressLogInakun.visibility = View.VISIBLE
                    loginModel.loginUserstory(email, password, object : Helper.ApiCallbackString{
                        override fun onResponse(success: Boolean,message: String) {
                        showAlertDialog(success, message)
                        }
                    })
                }
            }
        }
    }

    private fun showAlertDialog(success: Boolean, message: String) {
        if (success){
            AlertDialog.Builder(this).apply {
                binding.progressLogInakun.visibility = View.GONE
                        setTitle(getString(R.string.Yes))
                        setMessage(getString(R.string.successlogin))
                        setPositiveButton(getString(R.string.next)) { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                binding.progressLogInakun.visibility = View.GONE
                setTitle(getString(R.string.no))
                setMessage(getString(R.string.failedlogin) + " $message")
                setPositiveButton(getString(R.string.again)) { _, _ ->

                }
                create()
                show()
            }
        }
    }

    companion object{
        var token = ""
    }

}