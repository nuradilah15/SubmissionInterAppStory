package com.example.submissioninterappstory.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.submissioninterappstory.R
import com.example.submissioninterappstory.databinding.ActivityWelcomeScreenBinding
import com.example.submissioninterappstory.login.LogInActivity
import com.example.submissioninterappstory.regis.RegisAkunstories

class WelcomeScreen : AppCompatActivity() {

    private  lateinit var binding: ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewakun()
        plyAnimation()
        setUpSettingsakun()

        binding.loginBtnAkunstories.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        binding.registerBtnAkunstories.setOnClickListener {
            val intent =Intent(this, RegisAkunstories::class.java)
            startActivity(intent)
        }
    }

    private fun setUpSettingsakun(){
        binding.settingakunStories.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun plyAnimation(){
        ObjectAnimator.ofFloat(binding.logoAkunstories, View.TRANSLATION_X, -30f, 30f).
        apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcome = ObjectAnimator.ofFloat(binding.welcomeTv, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.desctextTv2, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginBtnAkunstories, View.ALPHA, 1f).setDuration(500)
        val regis = ObjectAnimator.ofFloat(binding.registerBtnAkunstories, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(welcome, desc, login, regis)
            start()
        }
    }

    private fun setupViewakun(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}