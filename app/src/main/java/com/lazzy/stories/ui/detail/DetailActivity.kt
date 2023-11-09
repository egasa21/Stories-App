package com.lazzy.stories.ui.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.lazzy.stories.R
import com.lazzy.stories.databinding.ActivityDetailBinding
import com.lazzy.stories.tools.ViewModelFactory
import com.lazzy.stories.tools.withDateFormat
import com.lazzy.stories.ui.preference.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        val id = intent.getStringExtra(EXTRA_ID)
        getStories(id.toString())

        setupView()

        detailViewModel.isLoading.observe(this){
            it.getContentIfNotHandled()?.let { isLoading ->
                showLoading(isLoading)
            }
        }

        showSnackBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel(){
        ViewModelProvider(
            this, ViewModelFactory(this, UserPreference.getInstance(dataStore))
        )[DetailViewModel::class.java]
    }

    private fun setupView(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(EXTRA_NAME)
    }

    private fun getStories(id: String){
        detailViewModel.getDetailStories(id)
        detailViewModel.stories.observe(this) { stories ->
            binding.apply {
                Glide.with(applicationContext)
                    .load(stories.photoUrl)
                    .into(ivPhotoDetail)

                val uploaded = StringBuilder(resources.getString(R.string.uploaded)).append(" ").append(stories.createdAt.withDateFormat())

                tvNameDetail.text = stories.name
                tvDescDetail.text = stories.description
                tvDate.text = uploaded
            }
        }
    }

    private fun showSnackBar(){
        detailViewModel.snackbar.observe(this){
            it.getContentIfNotHandled()?.let { snackbar ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackbar,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(isLoading : Boolean) {
        binding.progressBar.visibility = if (isLoading){
            View.VISIBLE
        }else{
            View.GONE
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
    }
}