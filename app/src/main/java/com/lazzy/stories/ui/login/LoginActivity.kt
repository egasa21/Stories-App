package com.lazzy.stories.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.lazzy.stories.R
import com.lazzy.stories.data.remote.LoginBody
import com.lazzy.stories.databinding.ActivityLoginBinding
import com.lazzy.stories.tools.ViewModelFactory
import com.lazzy.stories.ui.home.MainActivity
import com.lazzy.stories.ui.preference.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        loginViewModel.isLoading.observe(this){
            it.getContentIfNotHandled()?.let { isLoading ->
                showLoading(isLoading)
            }
        }

        binding.btnLogin.setOnClickListener { setupAction() }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    private fun showSnackBar(){
        loginViewModel.snackbar.observe(this){
            it.getContentIfNotHandled()?.let { snackbar ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackbar,
                    Snackbar.LENGTH_SHORT
                ).show()
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
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel(){
        ViewModelProvider(
            this, ViewModelFactory(this, UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupAction(){
        binding.apply {
            if (etEmail.text.toString().isNotEmpty() and etPassword.text.toString().isNotEmpty()){
                val loginBody = LoginBody(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
                loginViewModel.loginUser(loginBody)
                showSnackBar()

                loginViewModel.isLogin.observe(this@LoginActivity){
                    it.getContentIfNotHandled()?.let { isLogin ->
                        if (isLogin){
                            MainActivity.start(this@LoginActivity)
                            finish()
                        }
                    }
                }
            }else{
                Toast.makeText(
                    this@LoginActivity, getString(R.string.error_email_password), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

}