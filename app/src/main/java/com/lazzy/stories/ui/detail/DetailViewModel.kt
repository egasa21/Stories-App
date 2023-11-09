package com.lazzy.stories.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lazzy.stories.data.remote.api.ApiConfig
import com.lazzy.stories.data.remote.response.DetailStoriesResponse
import com.lazzy.stories.data.remote.response.Story
import com.lazzy.stories.tools.Event
import com.lazzy.stories.ui.preference.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val userPreference: UserPreference) : ViewModel() {

    private val _stories = MutableLiveData<Story>()
    val stories : LiveData<Story> = _stories

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading : LiveData<Event<Boolean>>  = _isLoading

    private val _snackbar = MutableLiveData<Event<String>>()
    val snackbar : LiveData<Event<String>> = _snackbar

    fun getDetailStories(id: String) {
        _isLoading.postValue(Event(true))
        val client = ApiConfig.getApiServices(userPreference).getDetailStories(id)
        client.enqueue(object : Callback<DetailStoriesResponse>{
            override fun onResponse(
                call: Call<DetailStoriesResponse>,
                response: Response<DetailStoriesResponse>
            ) {
                _isLoading.postValue(Event(false))
                if (response.isSuccessful){
                    _stories.postValue(response.body()?.story)
                }else{
                    Log.d(TAG, "Response onFailure: ${response.message()}")
                    _snackbar.postValue(Event("Response onFailure: ${response.message()}}"))
                }
            }

            override fun onFailure(call: Call<DetailStoriesResponse>, t: Throwable) {
                _isLoading.postValue(Event(false))
                Log.d(TAG, "onFailure: ${t.message}")
                _snackbar.postValue(Event("onFailure: ${t.message}"))
            }
        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}