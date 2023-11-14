package com.lazzy.stories.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lazzy.stories.R
import com.lazzy.stories.adapter.ListStoriesAdapter
import com.lazzy.stories.adapter.LoadingStateAdapter
import com.lazzy.stories.data.remote.response.ListStoryItem
import com.lazzy.stories.databinding.ActivityMainBinding
import com.lazzy.stories.tools.ViewModelFactory
import com.lazzy.stories.ui.detail.DetailActivity
import com.lazzy.stories.ui.login.LoginActivity
import com.lazzy.stories.ui.maps.MapsActivity
import com.lazzy.stories.ui.preference.UserPreference
import com.lazzy.stories.ui.stories.CreateStoryActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope.launch {
            val userPreference = UserPreference.getInstance(datastore)
            userPreference.getToken().collect { token ->
                if (token.isEmpty()){
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }else{
                    setupViewModel()
                    getAllStories()

                    binding.fabAdd.setOnClickListener {view ->
                        if(view.id == R.id.fabAdd){
                            val intent = Intent(this@MainActivity, CreateStoryActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    binding.fabMaps.setOnClickListener{ view ->
                        if(view.id == R.id.fabMaps){
                            MapsActivity.start(this@MainActivity)
                        }
                    }
                }
            }
        }

        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.layoutManager = LinearLayoutManager(this)


    }

    override fun onResume() {
        super.onResume()
        if (intent != null) {
            // Retrieve the extra, providing a default value of 0 if it's not present
            val resultUpload = intent.getIntExtra(RESULT_UPLOAD, 0)

            // Perform null check on intent and handle the resultUpload
            if (resultUpload == 1) {
                getAllStories()

                // Set the extra back to 0 to avoid repeated processing
                intent.putExtra(RESULT_UPLOAD, 0)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionRefresh -> {
                getAllStories()
            }
            R.id.actionAccount -> {

            }
            R.id.actionLogOut -> {
                mainViewModel.logout()
            }
            R.id.actionSetting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel(){
        ViewModelProvider(
            this, ViewModelFactory(this, UserPreference.getInstance(datastore))
        )[MainViewModel::class.java]
    }

    private fun getAllStories(){
        val listStoriesAdapter = ListStoriesAdapter(getString(R.string.upload_date))

        binding.rvStory.adapter = listStoriesAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoriesAdapter.retry()
            }
        )
        mainViewModel.stories.observe(this) { listStories ->
            listStoriesAdapter.submitData(lifecycle, listStories)
            listStoriesAdapter.onItemClick = {_, story: ListStoryItem ->
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, story.id)
                intent.putExtra(DetailActivity.EXTRA_NAME, story.name)
                startActivity(intent)
            }
        }
    }

    companion object{
        const val RESULT_UPLOAD = "result_upload"

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}