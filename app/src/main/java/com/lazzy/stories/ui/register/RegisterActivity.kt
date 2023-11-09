package com.lazzy.stories.ui.register

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.lazzy.stories.R
import com.lazzy.stories.data.remote.RegisterBody
import com.lazzy.stories.databinding.ActivityRegisterBinding
import com.lazzy.stories.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    private lateinit var registerBody: RegisterBody
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        registerViewModel.isLoading.observe(this){
            it.getContentIfNotHandled()?.let { isLoading ->
                showLoading(isLoading)
            }
        }

        binding.btnRegister.setOnClickListener { setupAction() }
    }

    private fun setupView(){
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showSnackBar() {
        registerViewModel.snackBar.observe(this) {
            it.getContentIfNotHandled()?.let { sncakBar ->
                Snackbar.make(
                    window.decorView.rootView,
                    sncakBar,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
        private fun setupAction() {

            registerBody = RegisterBody(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )

            registerViewModel.registerUser(registerBody)
            showSnackBar()

            registerViewModel.result.observe(this) {
                it.getContentIfNotHandled()?.let { result ->
                    if (result) {
                        LoginActivity.start(this@RegisterActivity)
                        finish()
                    }
                }
            }
        }
}