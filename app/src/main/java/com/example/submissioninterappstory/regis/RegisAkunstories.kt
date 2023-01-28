package com.example.submissioninterappstory.regis

import android.animation.AnimatorSet
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.submissioninterappstory.R
import com.example.submissioninterappstory.databinding.ActivityLogInBinding
import com.example.submissioninterappstory.databinding.ActivityRegisAkunstoriesBinding
import com.example.submissioninterappstory.login.LogInActivity
import com.example.submissioninterappstory.model.ModelFactory
import com.example.submissioninterappstory.model.UserPref
import com.example.submissioninterappstory.story.utils.Helper

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisAkunstories : AppCompatActivity() {

    private lateinit var binding: ActivityRegisAkunstoriesBinding
    private lateinit var regisModel: RegisModelStory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisAkunstoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewstory()
        setupViewModelstory()
        setActionstory()
        plyAnimations()
    }

    private fun plyAnimations(){
        ObjectAnimator.ofFloat(binding.imgRegsinakun, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.descregisTextView, View.ALPHA, 1f).setDuration(500)
        val nama = ObjectAnimator.ofFloat(binding.nameregisTv, View.ALPHA, 1f).setDuration(500)
        val namaedt = ObjectAnimator.ofFloat(binding.nameregisakunEdit, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailregisakunTv, View.ALPHA, 1f).setDuration(500)
        val emailedt = ObjectAnimator.ofFloat(binding.emailregisakunEdit, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.passwordRrgisTv, View.ALPHA, 1f).setDuration(500)
        val passedt = ObjectAnimator.ofFloat(binding.passwordregisakumEdit, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(title,nama,namaedt,email,emailedt,pass,passedt)
            start()
        }
    }

    private fun setupViewModelstory(){
        regisModel = ViewModelProvider(
            this, ModelFactory(UserPref.getInstance(dataStore))
        )[RegisModelStory::class.java]
    }

    private fun setupViewstory(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setActionstory(){
        binding.ReggisbuttonAkun.setOnClickListener {
            val name = binding.nameregisakunEdit.text.toString()
            val email = binding.emailregisakunEdit.text.toString()
            val password = binding.passwordregisakumEdit.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameregisakunEdit.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.emailregisakunEdit.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordregisakumEdit.error = "Masukkan password"
                }
                else -> {
                    binding.progressLogInakun.visibility = View.VISIBLE
                    regisModel.register(name, email, password, object : Helper.ApiCallbackString {
                        override fun onResponse(success: Boolean, message: String) {
                            showAlert(success, message)
                        }
                    })
                }
            }
        }

    }

    private fun showAlert(boolean: Boolean, message: String) {
        if (boolean) {
            AlertDialog.Builder(this).apply {
                binding.progressLogInakun.visibility = View.GONE
                setTitle(getString(R.string.Yes))
                setMessage(getString(R.string.createsukses))
                setPositiveButton(getString(R.string.next)) { _, _ ->

                    startActivity(Intent(this@RegisAkunstories, LogInActivity::class.java))
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                binding.progressLogInakun.visibility = View.GONE
                setTitle(getString(R.string.no))
                setMessage(getString(R.string.createfailed) + "$message")
                setPositiveButton(getString(R.string.againregis)) { _, _ ->
                }
                create()
                show()
            }
        }
    }
}