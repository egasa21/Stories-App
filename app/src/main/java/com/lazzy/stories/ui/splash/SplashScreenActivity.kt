package com.lazzy.stories.ui.splash

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.lazzy.stories.databinding.ActivitySplashScreenBinding
import com.lazzy.stories.tools.ViewModelFactory
import com.lazzy.stories.ui.home.MainActivity
import com.lazzy.stories.ui.login.LoginViewModel
import com.lazzy.stories.ui.preference.UserPreference

private val Context.datastore : DataStore<Preferences> by preferencesDataStore(name ="settings")

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, activityOptions.toBundle())

        Handler(Looper.getMainLooper()).postDelayed({
            setupSession()
        }, 2000)
    }

    private fun setupSession() {
        ViewModelProvider(
            this,
            ViewModelFactory(this, UserPreference.getInstance(datastore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            if(!user.isLogin){
                WelcomeActivity.start(this@SplashScreenActivity)
                finish()
            }else{
                MainActivity.start(this@SplashScreenActivity)
            }
        }
    }

    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}